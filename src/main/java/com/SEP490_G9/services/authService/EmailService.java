package com.SEP490_G9.services.authService;


import com.SEP490_G9.models.DTOS.EmailResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface EmailService {
	EmailResponse sendVerifyEmail(String toEmail);
	
	EmailResponse sendRecoveryPasswordToEmail(String toEmail);
}