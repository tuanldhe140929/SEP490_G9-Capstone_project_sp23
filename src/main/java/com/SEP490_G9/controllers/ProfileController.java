package com.SEP490_G9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.models.User;
import com.SEP490_G9.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "private/profile")
public class ProfileController {
	@Autowired
	UserService userService;
	
	@PostMapping(value="getUserInfo")
	public ResponseEntity<?> getUserInfo(@RequestBody String email){
		System.out.println(email);
		User user = userService.getUserInfo(email);
		return ResponseEntity.ok(user);
	}
}
