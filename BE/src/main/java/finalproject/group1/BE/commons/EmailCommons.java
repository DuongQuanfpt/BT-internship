package finalproject.group1.BE.commons;

import finalproject.group1.BE.web.exception.CustomRuntimeException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailCommons {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendMimeMessage(String[] to, String subject, String text ) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text,true);
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new CustomRuntimeException(e.getMessage());
        }
    }

    @Async
    public void sendSimpleMessage(String[] to, String subject, String text ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}