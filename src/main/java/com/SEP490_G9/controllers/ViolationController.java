package com.SEP490_G9.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.ViolationRepository;
import com.SEP490_G9.repository.ViolationTypeRepository;
import com.SEP490_G9.service.ViolationService;


@RequestMapping(value = "violation")

@RestController
public class ViolationController {
	@Autowired
	ViolationService violationService;
	
	@Autowired
	AccountRepository accountRepo;
	
	@PostMapping("addviolation")
	public ResponseEntity<?> addViolation(@RequestParam(name="violation_id") long violation_id,@RequestParam(name="createdDate") Date createdDate,@RequestParam(name="description") String description,@RequestParam(name="account_id") long account_id) {
		Violation vionew = new Violation();
		Account account = accountRepo.findById(account_id).get();
		
		vionew.setId(violation_id);
		vionew.setCreated_date(createdDate);
		vionew.setDescription(description);
		vionew.setAccount(account);
		
		boolean canAdd = violationService.addViolation(vionew);
		return ResponseEntity.ok(canAdd);
	}
	
	@GetMapping("getAllVioTypes")
	public ResponseEntity<?> getAllViolationType(){
		List<ViolationType> vioTypeList = violationService.getVioTypeList();
		return ResponseEntity.ok(vioTypeList);
	}
}
