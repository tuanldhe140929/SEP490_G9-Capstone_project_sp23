package com.SEP490_G9.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.dto.AuthResponse;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.RefreshToken;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.service.AccountService;
import com.SEP490_G9.service.authService.RefreshTokenService;
import com.SEP490_G9.common.JwtTokenUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RequestMapping(value="refreshToken")
@RestController
public class RefreshTokenController {
	@Autowired
	RefreshTokenService refreshTokenService;
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	JwtTokenUtil jwtUtil;
	
	@RequestMapping(value = "refresh", method = RequestMethod.POST)
	public ResponseEntity<?> refreshToken(HttpServletRequest request) {
		AuthResponse auth = null;
		Cookie refreshTokenCookie = null;

		boolean foundRefreshToken = false;

		if (request.getCookies() == null) {
			throw new ResourceNotFoundException("Cookie", "token", "");
		} else {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("refreshToken")) {
					refreshTokenCookie = cookie;
					foundRefreshToken = true;
					auth = validate(refreshTokenCookie);
				}
			}
			if (!foundRefreshToken) {
				throw new ResourceNotFoundException("Refresh token", "token", "");
			}
		}

		return ResponseEntity.ok(auth);

	}
	
	private AuthResponse validate(Cookie cookie) {
		String token = cookie.getValue();
		AuthResponse authResponse = refreshToken(token);
		return authResponse;
	}
	
	public AuthResponse refreshToken(String token) {
		AuthResponse authResponse = new AuthResponse();
		RefreshToken refreshToken = refreshTokenService.getByToken(token);
		if (refreshTokenService.verifyExpiration(refreshToken)) {
			throw new ResourceNotFoundException("refresh token", "token", token);
		} else {
			Account account = accountService.getByRefreshToken(refreshTokenService.getByToken(token));
			authResponse.setAccessToken(jwtUtil.generateToken(account.getEmail()));
			authResponse.setEmail(account.getEmail());
			Object[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray();
			List<String> roles = new ArrayList<>();
			for (Object authority : authorities) {
				roles.add(authority.toString());
			}
		}
		return authResponse;
	}
}
