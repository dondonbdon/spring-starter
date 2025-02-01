package dev.bti.starters.services.auth;

import dev.bti.starters.common.Constants;
import dev.bti.starters.common.Pair;
import dev.bti.starters.enums.CredentialType;
import dev.bti.starters.exceptions.auth.AuthException;
import dev.bti.starters.exceptions.auth.UserNotFoundException;
import dev.bti.starters.models.main.User;
import dev.bti.starters.models.req.Credential;
import dev.bti.starters.models.req.LoginRequest;
import dev.bti.starters.models.req.UserRequest;
import dev.bti.starters.services.user.UserRepository;
import dev.bti.starters.services.user.UserService;
import dev.bti.starters.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    // --- Externally Used ---

    public Pair<String, String> createUser(UserRequest userRequest) throws AuthException {
        return userService.registerUser(userRequest);
    }

    public Pair<String, String> loginUser(LoginRequest loginRequest) throws AuthException {
        if (loginRequest.getCredential().getCredentialType() == CredentialType.UID) {
            throw new UserNotFoundException();
        }

        User user = userService.getUser(loginRequest.getCredential(), User.class);

        if (user.getFailedAttempts() == 0) {
            user.setLockedMaxAttempts(true);
            user.setLockedFailedAttemptsTimestampEnd(Instant.now().plus(Duration.of(1, ChronoUnit.HOURS)));
            userRepository.save(user);
            throw Constants.Throws.MaxFailedAttempts;
        }

        if (encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            user.getLogins().add(Instant.now());
            user.setFailedAttempts(Constants.Auth.MAX_ATTEMPTS);

            String newToken = JwtUtils.createToken(user.getId(), user.getRoles().stream()
                    .map(role -> "ROLE_" + role)
                    .toArray(String[]::new));
            user.getTokens().add(newToken);
            userRepository.save(user);

            return new Pair<>(user.getId(), newToken);
        }

        user.setFailedAttempts(user.getFailedAttempts() - 1);
        userRepository.save(user);

        throw Constants.Throws.PasswordIncorrect;
    }

    public void resetPassword(Credential credential, String newPassword) throws AuthException {
        User user = userService.getUser(credential, User.class);

        if (!user.isAcceptPasswordResets()) {
            throw Constants.Throws.NotAcceptingResets;
        }

        user.setPassword(encoder.encode(newPassword));
        user.setAcceptPasswordResets(false);
        userRepository.save(user);
    }
}
