//package com.SEP490_G9.service.authService;
//
//import java.io.IOException;
//
//import org.apache.http.client.ClientProtocolException;
//
//import com.SEP490_G9.dto.AuthRequest;
//import com.SEP490_G9.dto.AuthResponse;
//import com.SEP490_G9.entity.User;
//
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public interface AuthService {
//	public AuthResponse login(AuthRequest authRequest, HttpServletResponse response);
//
//	public AuthResponse loginWithGoogle(final String code, HttpServletResponse response)
//			throws ClientProtocolException, IOException;
//
//	public boolean register(User user);
//
//	public AuthResponse validate(Cookie refreshTokenCookie);
//
//	public boolean verifyEmail(String verifyLink,String email);
//
//	// public boolean verifyEmail(String verifyLink, String email,
//	// HttpServletRequest request);
//
//	public boolean sendVerifyEmail(String email);
//
//	public boolean sendRecoveryPasswordToEmail(String email);
//
//	public User getCurrentUser();
//
//}
