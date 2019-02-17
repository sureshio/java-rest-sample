package com.javamongo.java_mongo.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.javamongo.java_mongo.bo.StudentBo;

public class SendEmail {

	public String email(String email, String pass) {
		String resp = StudentBo.FAILED;

		// Get properties object
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		// get Session
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("sangram.bachu@gmail.com", "thospbifoldzqpkj");
			}
		});
		// compose message
		try {
			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject("Your Password");
			message.setText("Your password is : " + pass);
			// send message
			Transport.send(message);
			resp = "Email Sent";
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

		return resp;
	}

}
