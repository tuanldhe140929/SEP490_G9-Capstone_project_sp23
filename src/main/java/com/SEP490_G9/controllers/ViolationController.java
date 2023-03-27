package com.SEP490_G9.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.repository.ViolationRepository;
import com.SEP490_G9.repository.ViolationTypeRepository;
import com.SEP490_G9.service.ViolationService;


@RequestMapping(value = "violation")

@RestController
public class ViolationController {
	@Autowired
	ViolationService violationService;
	
	@PostMapping("addviolation")
	public ResponseEntity<?> addTag(@RequestBody Violation vio) {
		Violation vionew = new Violation();
		vionew.setAccount(null);
		vionew.setCreated_date(null);
		vionew.setDescription(null);
		
		boolean canAdd = violationService.addVio(vio);
		return ResponseEntity.ok(canAdd);
	}
	
	@GetMapping("getAllVioTypes")
	public ResponseEntity<?> getAllViolationType(){
		List<ViolationType> vioTypeList = violationService.getVioTypeList();
		return ResponseEntity.ok(vioTypeList);
	}
}
