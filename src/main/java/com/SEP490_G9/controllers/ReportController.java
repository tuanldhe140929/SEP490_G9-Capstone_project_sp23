package com.SEP490_G9.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.Report;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.models.embeddables.ReportItemKey;
import com.SEP490_G9.services.FileStorageService;
import com.SEP490_G9.services.ManageProductService;
import com.SEP490_G9.services.ReportService;
@RequestMapping("private/manageReport")
@RestController
public class ReportController {
	 @Autowired private ReportService reportService;  

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
