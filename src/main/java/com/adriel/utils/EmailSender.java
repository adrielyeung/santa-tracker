package com.adriel.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailSender {
	
	public void sendEmail(List<String> tos, List<String> ccs, List<String> bccs, String subject, String body) throws UnsupportedEncodingException, MessagingException {
		Properties properties = new Properties();
		try (InputStream input = getClass().getResourceAsStream("/email.properties")) {
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String from = properties.getProperty("mail.from");
		String password = properties.getProperty("mail.password");
		
		Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }

        });
		 // Used to debug SMTP issues
        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from, Constants.APP_NAME));

            // Set To, Cc, Bcc: header field of the header.
            for (String to : tos) {
            	message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            }
            for (String cc : ccs) {
            	message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
            }
            for (String bcc : bccs) {
            	message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc));
            }

            message.setSubject(subject);
            message.setContent(body, "text/html");

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }
	
}
