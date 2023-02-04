package com.SEP490_G9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.services.ManageAccountInfoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@Validated
@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "private/profile")
public class ManageAccountInfoController {
	@Autowired
	ManageAccountInfoService manageAccountInfoService;

	@GetMapping(value = "getUserInfo")
	public ResponseEntity<?> getUserInfo() {
		User user;
		user = manageAccountInfoService.getAccountInfo();
		return ResponseEntity.ok(user);
	}
	
	@PostMapping(value = "changeAccountPassword")
	public ResponseEntity<?> changeAccountPassword(@Valid @RequestParam(name = "newPassword", required = true)  @Size(min = 8, max = 30) String newPassword,
		@Valid @RequestParam(name = "oldPassword", required = true)  @Size(min = 8, max = 30) String oldPassword){
		User user;
		boolean ret = manageAccountInfoService.changeAccountPassword(newPassword, oldPassword);
		return ResponseEntity.ok(ret);
	}
	@PostMapping(value = "changeAccountName")
	public ResponseEntity<?> changeAccountName(@Valid @RequestParam(name = "newName", required = true) @Size(min = 3, max = 30) String newName){
		User user;
		user = manageAccountInfoService.changeAccountName(newName);
		return ResponseEntity.ok(user);
	}

}
