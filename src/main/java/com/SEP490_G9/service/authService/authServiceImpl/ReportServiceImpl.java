package com.SEP490_G9.service.authService.authServiceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.embeddable.ReportItemKey;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.ReportRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private ReportRepository reportRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public boolean addReport(Report report) {
		reportRepository.save(report);
		return true;
	}



}
