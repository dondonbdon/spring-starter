package dev.bti.starters.services.push;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;

    public void sendPasswordResetEmail(String toEmail, String resetCode) {
        String subject = "Password Reset Request";

        Context context = new Context();
        context.setVariable("resetCode", resetCode);

        try {
            Template template = freemarkerConfig.getTemplate("password-reset-email.html");
            String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, Map.of("resetCode", resetCode));

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("bac@hacachievers.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (IOException | TemplateException | MessagingException e) {
            Logger.getLogger("Email Pusher").severe(e.toString());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}


