package com.SEP490_G9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SEP490_G9.models.DTOS.User;
import com.SEP490_G9.services.CommonService;

@RestController
@RequestMapping(value="public/common")
public class CommonController {
	@Autowired
	CommonService commonService;
	
	@GetMapping(value="getUserInfoByUsername")
	public ResponseEntity<?> getUserInfoByUsername(@RequestParam String username) {
		User user = commonService.getUserInfoByUsername(username);
		return ResponseEntity.ok(user);
	}
}
