package dev.bti.starters.security;

import dev.bti.starters.common.Constants;
import dev.bti.starters.enums.CampaignType;
import dev.bti.starters.enums.CredentialType;
import dev.bti.starters.exceptions.auth.AuthException;
import dev.bti.starters.models.main.Campaign;
import dev.bti.starters.models.main.User;
import dev.bti.starters.models.req.Credential;
import dev.bti.starters.services.push.PushService;
import dev.bti.starters.services.user.UserRepository;
import dev.bti.starters.services.user.UserService;
import dev.bti.starters.util.GeneratorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PushService pushService;

    public Credential requestVerificationCodeFor(Credential credential) throws AuthException {
        User user = userService.getUser(credential, User.class);

        if (credential.getCredentialType() != CredentialType.EMAIL) {
            credential.transform(CredentialType.EMAIL, user.getEmail());
        }

        Campaign campaign = new Campaign.Builder()
                .type(CampaignType.EMAIL_NOTIFICATION)
                .single()
                .target(credential.getCredential())
                .build();

        String newOtp = GeneratorUtils.generateResetCode();
        pushService.push(campaign, newOtp);

        user.getValidOTPs().add(newOtp);
        user.getValidOTPsTimestampEnd().put(newOtp, Instant.now().plus(15, ChronoUnit.MINUTES));

        userRepository.save(user);

        return credential;
    }

    public void validateVerificationCodeFor(Credential credential, String resetCode) throws AuthException {
        User user = userService.getUser(credential, User.class);

        if (user.getValidOTPsTimestampEnd().get(resetCode).isBefore(Instant.now())) {
            throw Constants.Throws.ResetCodeExpired;
        } else if (!user.getValidOTPs().contains(resetCode)) {
            throw Constants.Throws.ResetCodeIncorrect;
        }

        user.setValidOTPs(new ArrayList<>());
        user.setValidOTPsTimestampEnd(new HashMap<>());
        user.setAcceptPasswordResets(true);
        user.setIsVerified(true);

        userRepository.save(user);
    }
}
