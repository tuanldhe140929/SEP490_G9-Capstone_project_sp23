package com.SEP490_G9.services.authService;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.SEP490_G9.models.AuthRequest;
import com.SEP490_G9.models.AuthResponse;
import com.SEP490_G9.models.EmailResponse;
import com.SEP490_G9.models.Entities.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	public AuthResponse login(AuthRequest authRequest, HttpServletResponse response);

	public AuthResponse loginWithGoogle(final String code, HttpServletRequest request)
			throws ClientProtocolException, IOException;

	public boolean register(User user);

	public AuthResponse validate(Cookie refreshTokenCookie);

	public boolean verifyEmail(String verifyLink, HttpServletRequest request);

	// public boolean verifyEmail(String verifyLink, String email,
	// HttpServletRequest request);

	public EmailResponse sendVerifyEmail(HttpServletRequest request);

	public EmailResponse sendResetPasswordMail(HttpServletRequest request, String email);

	public boolean confirmRequestResetPassword(HttpServletRequest request, String captcha,String email, String newPassword);
	
}
