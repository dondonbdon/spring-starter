package dev.bti.starters.push;

import dev.bti.starters.models.main.Campaign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushService {

    private final EmailService emailService;

    public void push(Campaign campaign, String passwordResetCode) {
        emailService.sendPasswordResetEmail(campaign.getTarget(), passwordResetCode);
    }
}
