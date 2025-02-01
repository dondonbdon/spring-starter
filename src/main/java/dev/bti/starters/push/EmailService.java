package dev.bti.starters.push;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendPasswordResetEmail(String toEmail, String resetCode) {
        String subject = "Password Reset Request";

        Context context = new Context();
        context.setVariable("resetCode", resetCode);

        String htmlContent = templateEngine.process("password-reset-email", context);
        saveEmailToFile(toEmail, subject, htmlContent);

//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            helper.setTo(toEmail);
//            helper.setSubject(subject);
//            helper.setText(htmlContent, true);
//
//            mailSender.send(message);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Failed to send email", e);
//        }
    }

    private void saveEmailToFile(String toEmail, String subject, String content) {
        try {
            Path filePath = Path.of("emails", "email-log.html");
            Files.createDirectories(filePath.getParent());
            String logEntry = String.format(
                    "[%s] To: %s%nSubject: %s%n%n%s%n%n---------------------------------%n",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    toEmail,
                    subject,
                    content
            );

            Files.writeString(filePath, logEntry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write email to file", e);
        }
    }
}


