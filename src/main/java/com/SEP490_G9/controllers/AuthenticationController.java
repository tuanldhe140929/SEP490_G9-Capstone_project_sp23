package com.SEP490_G9.controllers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.exceptions.CustomException;
import com.SEP490_G9.models.AuthRequest;
import com.SEP490_G9.models.AuthResponse;
import com.SEP490_G9.models.EmailResponse;
import com.SEP490_G9.models.User;
import com.SEP490_G9.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

//@EnableMethodSecurity(prePostEnabled = true)
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "public/auth")
@Valid
@RestController
public class AuthenticationController {
	@Autowired
	AuthService authService;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest, HttpServletResponse response) {
		AuthResponse authResponse = authService.login(authRequest, response);
		return ResponseEntity.ok(authResponse);
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@Valid @RequestBody User user) {
		authService.register(user);
		return ResponseEntity.ok(new AuthResponse(user.getEmail(), null, null));
	}

	@RequestMapping(value = "loginWithGoogle", method = RequestMethod.POST)
	public ResponseEntity<?> loginWithGoogle(@RequestBody final String code, HttpServletRequest request)
			throws ClientProtocolException, IOException {
		AuthResponse authResponse = authService.loginWithGoogle(code, request);
		return ResponseEntity.ok(authResponse);
	}

	@RequestMapping(value = "refreshToken", method = RequestMethod.POST)
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		AuthResponse auth = null;
		Cookie refreshTokenCookie = null;
		if (request.getCookies() !=null) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("refreshToken")) {
					refreshTokenCookie = cookie;
				}
			}
			auth = authService.validate(refreshTokenCookie);
		}else {
			throw new CustomException("Not found refresh token");
		}
		
		return ResponseEntity.ok(auth);
	}
	
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
			
		    Cookie cookie = new Cookie("refreshToken", null); 
		    cookie.setPath("/");
		    cookie.setDomain("localhost");
		    cookie.setHttpOnly(true);
		    cookie.setSecure(true);
		    cookie.setMaxAge(0);
		    response.addCookie(cookie);
		    session.invalidate();
		    request.getSession().invalidate();
		    SecurityContextHolder.getContext().setAuthentication(null);
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "forgotAndResetPassword", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword(HttpServletRequest request, @RequestParam String email) {
			EmailResponse response = authService.sendResetPasswordMail(request,email);
			
		return ResponseEntity.ok(response);
	}
	
	@RequestMapping(value = "forgotAndResetPasswordConfirm", method = RequestMethod.POST)
	public ResponseEntity<?> confirmRequestResetPassword(HttpServletRequest request, @RequestParam String captcha,
			@RequestParam String email, @RequestParam String newPassword) {
			boolean ret = authService.confirmRequestResetPassword(request,captcha,email, newPassword);
		return ResponseEntity.ok(ret);
	}
}
