package com.SEP490_G9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.models.DTOS.User;
import com.SEP490_G9.services.ManageAccountInfoService;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "private/profile")
public class ManageAccountInfoController {
	@Autowired
	ManageAccountInfoService manageAccountInfoService;
	
	@PostMapping(value="getUserInfoByEmail")
	public ResponseEntity<?> getUserInfo(@RequestBody String email){
		System.out.println(email);
		User user = manageAccountInfoService.getUserInfo(email);
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok(user);
	}
}
