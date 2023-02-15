package com.SEP490_G9.services.authService;

import jakarta.servlet.http.HttpServletRequest;

public interface EmailService {
	boolean sendVerifyEmail(String toEmail);
	
	boolean sendRecoveryPasswordToEmail(String toEmail, String password);
}