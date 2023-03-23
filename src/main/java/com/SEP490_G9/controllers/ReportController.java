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


	@GetMapping("/reports")
	public List<Report> getAllEmployees() {
		return reportService.getAllReports();
	}
	
	@Autowired
	ProductService productService;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/sendReport")
	public ResponseEntity<?> sendReport(@RequestParam(name = "productId") long productId, @RequestParam(name = "accountId") long accountId, @RequestParam(name = "description") String description, @RequestParam(name = "violationTypeId") long violationTypeId){
		Report report = reportService.sendReport(productId, accountId, description, violationTypeId);
		return ResponseEntity.ok(report);
	}
	
	@GetMapping("/getByProductAndUser")
	public ResponseEntity<?> getByReportAndUser(@RequestParam(name = "productId") long productId, @RequestParam(name = "accountId") long accountId){
		Report report = reportService.getByProductAndUser(productId, accountId);
		return ResponseEntity.ok(report);
	}


//	@RequestMapping(value = "/createreport", method = RequestMethod.POST)
//	public Report addReport(@RequestBody Report report) {
//		System.out.println(report.getReportKey().getProductId());
//		System.out.println(report.getReportKey().getUserId());
//		return reportService.saveReport(report);
//	}

//	@RequestMapping(value = "/edit", method = RequestMethod.POST)
//	public Report editUser(@RequestBody Report report) {
//		Report reportEdit = reportService.findReportById(report.getReportKey()).get();
//		reportEdit.setDescription(reportEdit.getDescription());
//		reportEdit.setStatus(report.getStatus());
//		reportService.saveEditedReport(reportEdit);
//
//		return report;
//	}
//
//	@RequestMapping(value = "save", method = RequestMethod.POST)
//	public String save(Report report) {
//		reportService.saveReport(report);
//		return "redirect:/";
//	}
//
//	@RequestMapping(value = "/delete", method = RequestMethod.GET)
//	public boolean deleteUser(@RequestParam("userId") Long userID, @RequestParam("productID") Long productID) {
//		ReportItemKey key = new ReportItemKey(userID, productID);
//		reportService.deleteReport(key);
//		return true;
//
//	}
	
	@GetMapping("/getByStatus")
	public ResponseEntity<?> getByStatus(@RequestParam(name = "status") String status){
		List<Report> reportsByStatus = reportService.getByStatus(status);
		return ResponseEntity.ok(reportsByStatus);
	}
	
	@PutMapping("/updateReportStatus")
	public ResponseEntity<?> updateReportStatus(@RequestParam(name = "productId") long productId, @RequestParam(name = "userId") long userId, @RequestParam(name = "status") String status){
		Report report = reportService.updateReportStatus(productId, userId, status);
		return ResponseEntity.ok(report);
	}
	
	@GetMapping("/getAllReports")
	public ResponseEntity<?> getAllReports(){
		List<Report> allReports = reportService.getAllReports();
		return ResponseEntity.ok(allReports);
	}
	
	@GetMapping("/getReportsByProduct")
	public ResponseEntity<?> getReportsByProduct(@RequestParam(name = "productId")long productId){
		List<Report> reportsByProduct = reportService.getProductReports(productId);
		return ResponseEntity.ok(reportsByProduct);
	}
	
	@GetMapping("/getByProductAndStatus")
	public ResponseEntity<?> getByProductAndStatus(@RequestParam(name = "productId")long productId, @RequestParam(name = "status")String status){
		List<Report> reportsByProductAndStatus = reportService.getByStatusAndProduct(productId, status);
		return ResponseEntity.ok(reportsByProductAndStatus);
	}
}
