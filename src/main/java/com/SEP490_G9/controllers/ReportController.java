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

@RequestMapping("private/manageReport")
@RestController
public class ReportController {
	@Autowired
	private ReportService reportService;

	@GetMapping("/reports")
	public List<Report> getAllEmployees() {
		return reportService.getAllReport();
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
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

//	@RequestMapping(value = "/createreport", method = RequestMethod.POST)
//	public Report addReport(@RequestBody Report report) {
//		System.out.println(report.getReportKey().getProductId());
//		System.out.println(report.getReportKey().getUserId());
//		return reportService.saveReport(report);
//	}

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
		ReportItemKey key = new ReportItemKey(userID, productID);
		reportService.deleteReport(key);
		return true;
	}
}
