package com.SEP490_G9.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.dto.LicenseDTO;
import com.SEP490_G9.entity.License;
import com.SEP490_G9.service.LicenseService;

@RestController
@RequestMapping(value="license")
public class LicenseController {
	
	@Autowired
	LicenseService licenseService;
	
	@GetMapping("getAll")
	public ResponseEntity<?> getAllLicense(){
		List<License> licenses = licenseService.getAllLicense();
		
		List<LicenseDTO> dtos = new ArrayList<>();
		for(License license:licenses) {
			dtos.add(new LicenseDTO(license));
		}
		return ResponseEntity.ok(dtos);
	}
}
