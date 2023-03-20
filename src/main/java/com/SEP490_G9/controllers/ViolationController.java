package com.SEP490_G9.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.repository.ViolationTypeRepository;


@RequestMapping(value = "violation")

@RestController
public class ViolationController {
	@Autowired
	ViolationTypeRepository vioTypeRepo;

	@GetMapping("allTypes")
	public ResponseEntity<?> getAllTypes() {
		List<ViolationType> vtypes = vioTypeRepo.findAll();
		return ResponseEntity.ok(vtypes);
	}
}
