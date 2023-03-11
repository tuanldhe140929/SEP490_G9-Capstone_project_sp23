package com.SEP490_G9.service.authService.authServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.SEP490_G9.entity.Account;
import com.SEP490_G9.exception.EmailServiceException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.RefreshTokenRepository;
import com.SEP490_G9.service.authService.EmailService;
import com.SEP490_G9.util.VerifyLinkGenerator;

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
			String html = "Click here to verify your email \n" + "<a href='http:localhost:4200/users/verifyEmail/"
					+ verifyLink + "'>Verify link</a>";
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

	@Override
	public boolean sendRecoveryPasswordToEmail(String toEmail, String newPassword) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper;

		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(toEmail);

			message.setSubject("DPM System mail reset password");
			String html = "Your new Password is \n" + "<strong>" + newPassword + "</strong>";
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
