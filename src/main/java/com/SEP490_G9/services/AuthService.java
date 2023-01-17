package com.SEP490_G9.services;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.SEP490_G9.models.AuthRequest;
import com.SEP490_G9.models.AuthResponse;
import com.SEP490_G9.models.User;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
	public AuthResponse login(AuthRequest authRequest, HttpServletResponse response);

	public AuthResponse loginWithGoogle(final String code, HttpServletRequest request)
			throws ClientProtocolException, IOException;

	public int register(User user);

	public AuthResponse validate(Cookie refreshTokenCookie);

	//public boolean verifyEmail(String verifyLink, String email, HttpServletRequest request);
	
	//public VerifyEmailResponse sendVerifyEmail(String email, HttpServletRequest request);
}
