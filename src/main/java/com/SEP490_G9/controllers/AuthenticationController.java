package com.SEP490_G9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.models.AuthRequest;
import com.SEP490_G9.models.AuthResponse;
import com.SEP490_G9.models.User;
import com.SEP490_G9.services.AuthService;

import jakarta.validation.Valid;

//@EnableMethodSecurity(prePostEnabled = true)
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "public/auth")
@Valid
@RestController
public class AuthenticationController {
	@Autowired
	AuthService authService;

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@Valid @RequestBody User user) {
		authService.register(user);
		return ResponseEntity.ok(new AuthResponse(user.getEmail(), user.getPassword(), null));
	}

	
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest) {
		AuthResponse authResponse = authService.login(authRequest);
		return ResponseEntity.ok(authResponse);

	}
}
