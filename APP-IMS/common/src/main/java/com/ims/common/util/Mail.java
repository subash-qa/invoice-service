package com.ims.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import io.vertx.core.json.JsonArray;

public class Mail {
	private static final String EMAIL_FROM = "dev.swomb@gmail.com";
	private static final String USERNAME = "dev.swomb@gmail.com";
	private static final String PASSWORD = "lmncemfrlpmnyqby";
	private static JsonArray mailList = new JsonArray();

	public static void sendMail(String message, String toEmail, String attachment, String template) throws Exception {
		Properties prop = new Properties();
//		prop.put("mail.smtp.host", "smtp-mail.outlook.com");
//		prop.put("mail.smtp.host", "relay.jangosmtp.net");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.port", "587");
		
		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAIL_FROM, PASSWORD);
			}
		});

		try {

			Message msg = new MimeMessage(session);

			// from
			msg.setFrom(new InternetAddress(EMAIL_FROM, "SWOMB"));

			// to
			System.out.println(toEmail);
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));

			// subject
			msg.setSubject("SWOMB TEAM");

	
			msg.setText(message);
			if (template != null) {
				msg.setContent(template, "text/html; charset=utf-8");

			}

			Transport.send(msg);

			System.out.println("Email sent successfully...");
		} catch (AddressException e) {
			System.out.println(e.getMessage());
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	

@SuppressWarnings("unused")
	public static void sendMutipleMail(String message,JsonArray toEmail, String attachment, String template,JsonArray cc,JsonArray bcc) throws Exception {
		System.out.println("Send Mail Started");
		Properties prop = new Properties();
//		prop.put("mail.smtp.host", "smtp-mail.outlook.com");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.port", "587");

		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EMAIL_FROM, PASSWORD);
			}
		});

		try {

			
			Message msg = new MimeMessage(session);

			// from
			msg.setFrom(new InternetAddress(EMAIL_FROM, "SWOMB"));

			//  multiple to
			InternetAddress dests[];
			
			if(toEmail != null) {
				System.out.println("1");
				 dests = new InternetAddress[toEmail.size()];
				 mailList = toEmail;
			}else if(cc != null) {
				System.out.println("2");
				 dests = new InternetAddress[cc.size()];
				 mailList = cc;
			}else {
				System.out.println("3");
				 dests = new InternetAddress[bcc.size()];
				 System.out.println(bcc);
				 mailList = bcc;
			}

			AtomicInteger atomicInteger = new AtomicInteger(0);
			
			mailList.forEach(item->{
				String value = (String) item;
				try {
					dests[atomicInteger.getAndAdd(1)] = new InternetAddress(value.trim().toLowerCase());
				
				if(mailList.size() == atomicInteger.get()) {
					
					if(toEmail != null) {
						msg.setRecipients(Message.RecipientType.TO, dests);	
					}else if(cc != null) {
						msg.setRecipients(Message.RecipientType.CC, dests);
					}else {
						msg.setRecipients(Message.RecipientType.BCC, dests);
					}

					// subject
					msg.setSubject("SWOMB TEAM");

					// create Multipart object and add MimeBodyPart objects to this object
					Multipart multipart = new MimeMultipart();


					msg.setText(message);

					if (template != null) {
						msg.setContent(template, "text/html; charset=utf-8");

					}

					Transport.send(msg);

					System.out.println("Email sent successfully...");
				}
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			
		
		} catch (AddressException e) {
			System.out.println(e.getMessage());
		} catch (MessagingException e) {
			System.out.println(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
	}
}