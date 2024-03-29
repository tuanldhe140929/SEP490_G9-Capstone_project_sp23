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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.dto.ReportDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.entities.embeddable.ReportItemKey;
import com.SEP490_G9.repository.ViolationTypeRepository;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.ReportService;
import com.SEP490_G9.service.UserService;

@RequestMapping("report")
@RestController
public class ReportController {

	@Autowired
	private ReportService reportService;
	
	@PostMapping("/sendReport")
	public ResponseEntity<?> sendReport(@RequestParam(name = "productId") long productId, @RequestParam(name = "accountId") long accountId, @RequestParam(name = "version") String version , @RequestParam(name = "description") String description, @RequestParam(name = "violationTypeId") long violationTypeId){
		Report report = reportService.sendReport(productId, accountId, version, description, violationTypeId);
		return ResponseEntity.ok(report);
	}
	
	@GetMapping("/getByProductUserVersion")
	public ResponseEntity<?> getByProductVersionAndUser(@RequestParam(name = "productId") long productId, @RequestParam(name = "accountId") long accountId, @RequestParam(name = "version") String version){
		Report report = reportService.getByProductUserVersion(productId, accountId, version);
		return ResponseEntity.ok(report);
	}
	
	@PutMapping("/updateReportStatus")
	public ResponseEntity<?> updateReportStatus(@RequestParam(name = "productId") long productId, @RequestParam(name = "version") String version , @RequestParam(name = "userIdList") List<Long> userIdList, @RequestParam(name = "statusList") List<String> statusList){
		List<Report> reportList = reportService.updateReportStatus(productId, version, userIdList, statusList);
		return ResponseEntity.ok(reportList);
	}
	
	@GetMapping("/getByProductAndStatus")
	public ResponseEntity<?> getByProductAndStatus(@RequestParam(name = "productId") long productId, @RequestParam(name = "version")String version, @RequestParam(name = "status")String status){
		List<Report> reportsByAllVersAndStatus = reportService.getByProductDetailsAndStatus(productId, version, status);
		return ResponseEntity.ok(reportsByAllVersAndStatus);
	}
}
