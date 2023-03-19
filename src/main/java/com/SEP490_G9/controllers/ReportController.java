package com.SEP490_G9.controllers;

import java.util.Date;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.dto.ReportDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.entities.embeddable.ReportItemKey;
import com.SEP490_G9.repository.ViolationTypeRepository;

import com.SEP490_G9.service.ReportService;

@RequestMapping("report")
@RestController
public class ReportController {

	@Autowired
	ReportService reportService;
	
	@PostMapping("/sendReport")
	public ResponseEntity<?> sendReport(@RequestParam(name = "productId") long productId, @RequestParam(name = "accountId") long accountId, @RequestParam(name = "description") String description, @RequestParam(name = "violationTypeId") long violationTypeId){
		Report report = reportService.sendReport(productId, accountId, description, violationTypeId);
		return ResponseEntity.ok(report);
	}
	
}
