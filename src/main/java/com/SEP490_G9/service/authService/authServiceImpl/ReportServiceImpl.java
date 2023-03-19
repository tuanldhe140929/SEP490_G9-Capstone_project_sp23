package com.SEP490_G9.service.authService.authServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.entities.embeddable.ReportItemKey;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.ReportRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.repository.ViolationTypeRepository;
import com.SEP490_G9.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ViolationTypeRepository vioTypeRepository;

	@Override
	public Report sendReport (long productId, long accountId, String description, long violationTypeId) {
		Report report = new Report();
		Product product = productRepository.findById(productId).get();
		User user = userRepository.findById(accountId).get();
		report.setProduct(product);
		report.setUser(user);
		Date date = new Date();
		report.setCreated_date(date);
		report.setDescription(description);
		report.setStatus("PENDING");
		ViolationType vt = vioTypeRepository.findById(violationTypeId);
		report.setViolation_types(vt);
		reportRepository.save(report);
		return report;
	}



}
