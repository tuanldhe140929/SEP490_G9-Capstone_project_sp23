package com.SEP490_G9.services.authService;


import com.SEP490_G9.models.EmailResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface EmailService {
	EmailResponse sendVerifyEmail(String toEmail, HttpServletRequest request);
	
	EmailResponse sendResetPasswordEmail(String toEmail, HttpServletRequest request);
}