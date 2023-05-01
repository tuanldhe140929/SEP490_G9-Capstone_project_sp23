
package com.SEP490_G9.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.dto.ViolationDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.ViolationRepository;
import com.SEP490_G9.repository.ViolationTypeRepository;
import com.SEP490_G9.security.JwtTokenUtil;
import com.SEP490_G9.service.AccountService;
import com.SEP490_G9.service.ViolationService;

@RequestMapping(value = "violation")

@RestController
public class ViolationController {
	@Autowired
	ViolationService violationService;

	@Autowired
	AccountService accountService;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@PostMapping("addviolation")
	public ResponseEntity<?> addViolation(@RequestParam("account_id") int account_id,
			@RequestParam("description") String description) {
		Violation vio = new Violation();
		vio.setDescription(description);
		Account account = accountService.getById(account_id);
		vio.setAccount(account);
		Violation vioAdd = violationService.addViolation(vio);
		System.out.println(description + account_id);
		return ResponseEntity.ok(vioAdd);
	}

	@PutMapping("updateSellerStatus")
	public ResponseEntity<?> updateSellerStatus(@RequestParam(name = "id") long id) {
		Account updateStatus = violationService.updateSellerStatus(id);
		String sellerAccessToken = jwtTokenUtil.generateToken(updateStatus.getEmail());
		jwtTokenUtil.invalidateToken(sellerAccessToken);
		return ResponseEntity.ok(updateStatus);
	}

	@GetMapping("getAllVioTypes")
	public ResponseEntity<?> getAllViolationType() {
		List<ViolationType> vioTypeList = violationService.getVioTypeList();
		return ResponseEntity.ok(vioTypeList);
	}
}
