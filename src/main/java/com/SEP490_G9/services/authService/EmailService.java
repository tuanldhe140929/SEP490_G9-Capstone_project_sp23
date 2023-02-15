package com.SEP490_G9.services.authService;


import com.SEP490_G9.models.DTOS.EmailResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface EmailService {
	boolean sendVerifyEmail(String toEmail);
	
	boolean sendRecoveryPasswordToEmail(String toEmail, String newPassword);
}