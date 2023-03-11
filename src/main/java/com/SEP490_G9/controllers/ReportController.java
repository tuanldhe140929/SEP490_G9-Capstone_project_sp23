package com.SEP490_G9.controllers;

<<<<<<< Updated upstream
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
=======
import java.util.Date;
>>>>>>> Stashed changes
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
<<<<<<< Updated upstream
import org.springframework.stereotype.Controller;
=======
import org.springframework.security.core.context.SecurityContextHolder;
>>>>>>> Stashed changes
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
<<<<<<< Updated upstream
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.entity.Report;
import com.SEP490_G9.entity.embeddable.ReportItemKey;
=======

import com.SEP490_G9.dto.ReportDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.entities.embeddable.ReportItemKey;
import com.SEP490_G9.repository.ViolationTypeRepository;
>>>>>>> Stashed changes
import com.SEP490_G9.service.ReportService;

@RequestMapping("private/manageReport")
@RestController
public class ReportController {
	 @Autowired private ReportService reportService;  

	@Autowired
	ManageProductService manageProductService;

	@Autowired
	FileStorageService fileStorageService;

	@GetMapping(value = "getCurrentUserInfo")
	public ResponseEntity<?> getCurrentUserInfo() {
		User user = manageProductService.getCurrentUserInfo();
		User user2 = new User();
		user2.setId(user.getId());
		user2.setEnabled(user.isEnabled());
		user2.setEmail(user.getEmail());
		user2.setPassword(null);
		user2.setUsername(user.getUsername());
		return ResponseEntity.ok(user2);
	}

	@GetMapping(value = "getProductsByUser")
	public ResponseEntity<?> getProductsByUser() {
		List<Product> products = manageProductService.getProductsByUser();
		return ResponseEntity.ok(products);
	}

<<<<<<< Updated upstream
	@PostMapping(value = "addProduct")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		Product ret = manageProductService.addProduct(product);
		return ResponseEntity.ok(ret);
=======
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public boolean addReport(@RequestBody ReportDTO report) {
//		System.out.println(report.getReportKey().getProductId());
//		System.out.println(report.getReportKey().getUserId());
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		report.setUserId(account.getId());
		Report reboost = new Report(report);
		reboost.setCreated_date(new Date());
		reboost.setStatus("Pending");
		reportService.saveReport(reboost);
		
		return true;
>>>>>>> Stashed changes
	}
=======
	  @RequestMapping(value= "/", method = RequestMethod.GET)  
	  public ResponseEntity<?> index(Model model) {  
	    List<Report> reports = reportService.getAllReport();  
	    return ResponseEntity.ok(reports);  
	  }  

	  @RequestMapping(value = "/createreport", method = RequestMethod.POST)  
	  public Report addReport(@RequestBody Report report) {  
		  System.out.println(report.getReportKey().getProductId());
		  System.out.println(report.getReportKey().getUserId());
	    return reportService.saveReport(report);  
	  }  
	  
	  @GetMapping("/reports")
	    public List<Report> getAllEmployees() {
	        return reportService.getAllReport();
	    }
      
	  @RequestMapping(value= "/", method = RequestMethod.GET)  
	  public ResponseEntity<?> index(Model model) {  
	    List<Report> reports = reportService.getAllReport();  
	    return ResponseEntity.ok(reports);  
	  }  

	  @RequestMapping(value = "/add", method = RequestMethod.POST)  
	  public boolean addReport(@RequestBody Report report) {  
		  System.out.println(report.getReportKey().getProductId());
		  System.out.println(report.getReportKey().getUserId());

	    reportService.saveReport(report);
	    return true;  
	  }  
	  @RequestMapping(value = "/edit", method = RequestMethod.POST)  
	  public Report editUser(@RequestBody Report report) {  
	    Report reportEdit = reportService.findReportById(report.getReportKey()).get();
	    reportEdit.setDescription(reportEdit.getDescription());
	    reportEdit.setStatus(report.getStatus());
	    reportService.saveEditedReport(reportEdit);
	    
	    return report;  
	  }  

	  @RequestMapping(value = "save", method = RequestMethod.POST)  
	  public String save(Report report) {  
	    reportService.saveReport(report);  
	    return "redirect:/";  
	  }  

	  @RequestMapping(value = "/delete", method = RequestMethod.GET)  
	  public boolean deleteUser(@RequestParam("userId") Long userID, @RequestParam("productID") Long productID) {
		  ReportItemKey key = new ReportItemKey(userID,productID);
	    reportService.deleteReport(key);  
	    return true; 
	  }  
}
