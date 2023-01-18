package com.SEP490_G9.controllers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.models.AuthRequest;
import com.SEP490_G9.models.AuthResponse;
import com.SEP490_G9.models.RefreshToken;
import com.SEP490_G9.models.User;
import com.SEP490_G9.services.AuthService;
import com.SEP490_G9.services.RefreshTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
		return ResponseEntity.ok(new AuthResponse(user.getEmail(), user.getPassword(), null));
	}

	@RequestMapping(value = "loginWithGoogle", method = RequestMethod.POST)
	public ResponseEntity<?> loginWithGoogle(@RequestBody final String code, HttpServletRequest request)
			throws ClientProtocolException, IOException {
		AuthResponse authResponse = authService.loginWithGoogle(code, request);
		return ResponseEntity.ok(authResponse);
	}

	@RequestMapping(value = "refreshToken", method = RequestMethod.POST)
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		Cookie refreshTokenCookie = null;
		if (request.getCookies().length > 0) {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("refreshToken")) {
					refreshTokenCookie = cookie;
				}
			}
		}
		AuthResponse auth = authService.validate(refreshTokenCookie);
		return ResponseEntity.ok(auth);
	}
}
