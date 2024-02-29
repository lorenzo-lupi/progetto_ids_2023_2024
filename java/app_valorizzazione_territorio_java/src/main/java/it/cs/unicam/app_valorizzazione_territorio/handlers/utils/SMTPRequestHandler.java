package it.cs.unicam.app_valorizzazione_territorio.handlers.utils;

import it.cs.unicam.app_valorizzazione_territorio.springConfigurations.SMTPConfigurations;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackageClasses = SMTPConfigurations.class)
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
