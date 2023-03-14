package com.SEP490_G9.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.entities.User;
import com.SEP490_G9.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@Validated
@RestController
@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(value = "private/profile")
public class ManageAccountInfoController {
	@Autowired
	UserService userService;

	@GetMapping(value = "getUserInfo")
	public ResponseEntity<?> getUserInfo() {
		User user;
		user = userService.getUserInfo();
		return ResponseEntity.ok(user);
	}

	@PostMapping(value = "changeAccountPassword")
	public ResponseEntity<?> changeAccountPassword(
			@Valid @RequestParam(name = "newPassword", required = true) @Size(min = 8, max = 30) String newPassword,
			@Valid @RequestParam(name = "oldPassword", required = true) @Size(min = 8, max = 30) String oldPassword) {
		User user;
		boolean ret = userService.changePassword(newPassword, oldPassword);
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value = "uploadProfileImage")
	public ResponseEntity<?> uploadProfileImage(@RequestParam(name = "profileImage") MultipartFile profileImage)
			throws IOException {
		String src = userService.uploadAvatar(profileImage);
		return ResponseEntity.ok(src);
	}

	@PostMapping(value = "changeAccountInfo")
	public ResponseEntity<?> changeAccountInfo(
			@Valid @RequestParam(name = "newUserName", required = true) @Size(min = 3, max = 30) String newUserName,
			@Valid @RequestParam(name = "newFirstName", required = true) @Size(min = 3, max = 30) String newFirstName,
			@Valid @RequestParam(name = "newLastName", required = true) @Size(min = 3, max = 30) String newLastName) {
		User user;
		user = userService.updateUser(newUserName, newFirstName, newLastName);
		return ResponseEntity.ok(user);
	}
}
