package com.SEP490_G9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.models.EmailResponse;
import com.SEP490_G9.services.authService.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "private/auth")
public class VerifyEmailController {
	@Autowired
	AuthService authService;

	@RequestMapping(value = "verifyEmail/{verifyLink}", method = RequestMethod.GET)
	public ResponseEntity<?> verifyEmail(@PathVariable(name = "verifyLink", required = true) String verifyLink,
			HttpServletRequest request) {
		boolean verified = authService.verifyEmail(verifyLink, request);
		return ResponseEntity.ok(verified);
	}

	@RequestMapping(value = "verifyEmailRequest", method = RequestMethod.POST)
	public ResponseEntity<?> verifyEmail(HttpServletRequest request) {
		EmailResponse response = authService.sendVerifyEmail(request);
		return ResponseEntity.ok(response);
	}
}
