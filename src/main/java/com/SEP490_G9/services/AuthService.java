package com.SEP490_G9.services;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.SEP490_G9.models.AuthRequest;
import com.SEP490_G9.models.AuthResponse;
import com.SEP490_G9.models.User;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
	public AuthResponse login(AuthRequest authRequest);

	public AuthResponse loginWithGoogle(final String code, HttpServletRequest request)
			throws ClientProtocolException, IOException;

	public int register(User user);

	//public boolean verifyEmail(String verifyLink, String email, HttpServletRequest request);
	
	//public VerifyEmailResponse sendVerifyEmail(String email, HttpServletRequest request);
}
