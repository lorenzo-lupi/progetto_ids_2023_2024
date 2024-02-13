package it.cs.unicam.app_valorizzazione_territorio.handlers.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SMTPRequestHandler {

    private static final String fromEmail = "ilcomunedelfuturo@gmail.com";
    private static final String password = "ids_2324";


    public static void sendEmail(String toEmail, String subject, String body)
            throws MessagingException, UnsupportedEncodingException{
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp-relay.gmail.com"); //host SMTP
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getInstance(props, auth);
        sendEmail(session, toEmail, subject, body);
    }

    /**
     * Utility method to send a simple HTML email
     *
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    private static void sendEmail(Session session, String toEmail, String subject, String body)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessage(session);
        //set message headers
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress("ilcomunedelfuturo@no-reply.com", "NoReply-JD"));
        msg.setReplyTo(InternetAddress.parse("ilcomunedelfuturo@no-reply.com", false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        msg.setSentDate(new Date());

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        Transport.send(msg);
    }


}
