package com.SEP490_G9.services.authService.authServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.SEP490_G9.exceptions.EmailServiceException;
import com.SEP490_G9.helpers.VerifyLinkGenerator;
import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.repositories.AccountRepository;
import com.SEP490_G9.repositories.RefreshTokenRepository;
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
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Override
	public boolean sendVerifyEmail(String toEmail) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		Account account = accountRepository.findByEmail(toEmail);
		String verifyLink = refreshTokenRepository.findByAccount(account).getToken();
		
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(toEmail);

			message.setSubject("DPM System mail verify");
			String html = "Click here to verify your email \n" + "<a href='http:localhost:4200/auth/verifyEmail/"
					+ verifyLink + "'>Verify link</a>";
			message.setText(html, "UTF-8", "html");
		} catch (MessagingException e) {
			throw new EmailServiceException("Send email failed");
		}

		try {
			javaMailSender.send(message);
		} catch (MailException ex) {
			throw new EmailServiceException("Send email failed");
		}
		return true;
	}

	@Override
	public boolean sendRecoveryPasswordToEmail(String toEmail,String password) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(toEmail);

			message.setSubject("DPM System mail reset password");
			String html = "Your new password is:\n <strong>" + password+"</strong>";
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
		return true;
	}

}
