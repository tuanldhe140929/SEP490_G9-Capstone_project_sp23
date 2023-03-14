package com.SEP490_G9.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.entities.embeddable.ReportItemKey;
import com.SEP490_G9.repository.ViolationTypeRepository;
import com.SEP490_G9.service.ReportService;

@RequestMapping("private/report")
@RestController
public class ReportController {
	@Autowired
	private ReportService reportService;

	@GetMapping("/addReport")
	public ResponseEntity<?> addReport(@RequestBody Report report){
		boolean canAdd = reportService.addReport(report);
		return ResponseEntity.ok(canAdd);
	}
}
