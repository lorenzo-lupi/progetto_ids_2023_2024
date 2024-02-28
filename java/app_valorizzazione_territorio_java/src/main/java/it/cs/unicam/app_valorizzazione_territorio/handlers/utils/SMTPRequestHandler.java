package it.cs.unicam.app_valorizzazione_territorio.handlers.utils;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
public class SMTPRequestHandler {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        MimeMessagePreparator message = newMessage -> {
            newMessage.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(toEmail)
            );
            newMessage.setFrom("noreply@ilcomunedelfuturo.it");
            newMessage.setSubject(subject);
            newMessage.setText(body);
        };

        emailSender.send(message);

    }

}
