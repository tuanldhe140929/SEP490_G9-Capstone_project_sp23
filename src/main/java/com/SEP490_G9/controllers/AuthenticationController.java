package com.SEP490_G9.controllers;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.exceptions.RefreshTokenException;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.models.DTOS.AuthRequest;
import com.SEP490_G9.models.DTOS.AuthResponse;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.services.authService.AuthService;
import com.SEP490_G9.services.authService.EmailService;

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

	@Autowired
	EmailService emailService;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest, HttpServletResponse response) {
		AuthResponse authResponse = authService.login(authRequest, response);
		return ResponseEntity.ok(authResponse);
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@Valid @RequestBody User user, HttpServletRequest request) {
		authService.register(user);
		return ResponseEntity.ok(new AuthResponse(user.getEmail(), null, null));
	}

	@RequestMapping(value = "loginWithGoogle", method = RequestMethod.POST)
	public ResponseEntity<?> loginWithGoogle(@RequestBody final String code,
			HttpServletResponse response) throws ClientProtocolException, IOException {
		AuthResponse authResponse = authService.loginWithGoogle(code, response);
		return ResponseEntity.ok(authResponse);
	}

	@RequestMapping(value = "refreshToken", method = RequestMethod.POST)
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		AuthResponse auth = null;
		Cookie refreshTokenCookie = null;

		boolean foundRefreshToken = false;

		if (request.getCookies() == null) {
			throw new RefreshTokenException(null, "Not found");
		} else {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("refreshToken")) {
					refreshTokenCookie = cookie;
					foundRefreshToken = true;
					auth = authService.validate(refreshTokenCookie);
				}
			}
			if (!foundRefreshToken) {
				throw new RefreshTokenException(null, "Not found");
			}
		}

		return ResponseEntity.ok(auth);

	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

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

	@RequestMapping(value = "resetPassword", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword(HttpServletRequest request, @RequestParam(required = true) String email) {
		boolean ret = authService.sendRecoveryPasswordToEmail(email);
		return ResponseEntity.ok(ret);
	}

//	@RequestMapping(value = "forgotAndResetPasswordConfirm", method = RequestMethod.POST)
//	public ResponseEntity<?> confirmRequestResetPassword(HttpServletRequest request,
//			@RequestParam(required = true) String captcha, @RequestParam(required = true) String email,
//			@RequestParam(required = true) String newPassword) {
//		System.out.println(email + "\n" + captcha);
//		boolean ret = authService.confirmRequestResetPassword(request, captcha, email, newPassword);
//		return ResponseEntity.ok(ret);
//	}
	
	@RequestMapping(value="sendVerifyEmail",method = RequestMethod.GET)
	public ResponseEntity<?> sendVerifyEmail(@RequestParam(name="email") String email){
		boolean ret = emailService.sendVerifyEmail(email);
		return ResponseEntity.ok(ret);
	}

	@RequestMapping(value = "verifyEmail/{verifyLink}", method = RequestMethod.GET)
	public ResponseEntity<?> verifyEmail(@PathVariable(name = "verifyLink", required = true) String verifyLink,
			@RequestParam(name = "email") String email) {
		boolean verified = authService.verifyEmail(verifyLink, email);
		return ResponseEntity.ok(verified);
	}
}
