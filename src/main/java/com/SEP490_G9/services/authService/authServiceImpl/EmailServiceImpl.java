package com.SEP490_G9.services.authService.authServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.EmailServiceException;
import com.SEP490_G9.helpers.VerifyLinkGenerator;
import com.SEP490_G9.models.EmailResponse;
import com.SEP490_G9.services.authService.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private VerifyLinkGenerator verifyLinkGenerator;
	@Value("${spring.mail.username}")
	private String sender;


	@Override
	public EmailResponse sendVerifyEmail(String toEmail, HttpServletRequest request) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		String verifyLink = verifyLinkGenerator.generate().toString();
		
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(toEmail);
			
			message.setSubject("DPM System mail verify");
			String html = "Click here to verify your email \n" + "<a href='" + verifyLink + "'>Verify link</a>";
			message.setText(html, "UTF-8", "html");
		} catch (MessagingException e) {
			throw new EmailServiceException("Send email failed");
		}

		try {
			javaMailSender.send(message);
		} catch (MailException ex) {
			throw new EmailServiceException("Send email failed");
		}
		HttpSession session = request.getSession();
		EmailResponse response = new EmailResponse(toEmail, verifyLink);
		session.setMaxInactiveInterval(60);
		session.setAttribute(toEmail, response);
		return response;
	}


	@Override
	public EmailResponse sendResetPasswordEmail(String toEmail, HttpServletRequest request) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		String captcha = verifyLinkGenerator.generate().toString();
		
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(toEmail);
			
			message.setSubject("DPM System mail reset password");
			String html = "Enter your captcha:\n" +   captcha ;
			message.setText(html, "UTF-8", "html");
		} catch (MessagingException e) {
			System.out.println(e);
			throw new EmailServiceException("Send email failed");
		}

		try {
			javaMailSender.send(message);
		} catch (MailException ex) {
			System.out.println(ex);
			throw new EmailServiceException("Send email failed");
		}
		HttpSession session = request.getSession();
		EmailResponse response = new EmailResponse(toEmail, captcha);
		session.setMaxInactiveInterval(60);
		session.setAttribute(toEmail, response);
		return response;
	}

}
