/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import dao.EmailSettingsDAO;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.EmailSettings;

/**
 *
 * @author hrkas
 */
public class EmailUtil {

    public static void sendEmail(String to, String subject, String body) {
        EmailSettings settings = new EmailSettingsDAO().loadSettings();

        if (settings.getSenderEmail() == null || settings.getSenderPassword() == null) {
            System.out.println("Email settings not configured.");
            return;
        }

        try {
            // Decrypt the sender password before use
            String decryptedPassword = AESUtil.decrypt(settings.getSenderPassword());

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", settings.getSmtpHost() != null ? settings.getSmtpHost() : "smtp.gmail.com");
            props.put("mail.smtp.port", settings.getSmtpPort() != null ? settings.getSmtpPort() : "587");

            Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(settings.getSenderEmail(), decryptedPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(settings.getSenderEmail(), "Enrollment System"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email sent successfully to " + to);

        } catch (UnsupportedEncodingException | MessagingException ex) {
            System.err.println("Failed to send email: " + ex.getMessage());
//            ex.printStackTrace();
        }
    }

}
