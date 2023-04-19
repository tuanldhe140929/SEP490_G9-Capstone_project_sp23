package com.SEP490_G9.service.authService.authServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductDetails.Status;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.entities.embeddable.ReportItemKey;
import com.SEP490_G9.exception.NumberException;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.ReportRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.repository.ViolationTypeRepository;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductService;
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
	private ProductDetailsService productDetailsService;
	
	@Autowired
	private ProductDetailsRepository productDetailsRepository;
	
	@Autowired
	private ViolationTypeRepository vioTypeRepository;

	@Override
	public Report sendReport (long productId, long accountId, String version, String description, long violationTypeId) {
		if(description.length()<10||description.length()>255) {
			throw new NumberException("report length must be from 10 and 255 characters");
		}
		Report report = new Report();
		Product product = productRepository.findById(productId).get();
		User user = userRepository.findById(accountId).orElseThrow();
		report.setProduct(product);
		report.setUser(user);
		report.setVersion(version);
		Date date = new Date();
		report.setCreated_date(date);
		report.setDescription(description);
		report.setStatus("PENDING");
		ViolationType vt = vioTypeRepository.findById(violationTypeId);
		report.setViolation_types(vt);
		reportRepository.save(report);
		return report;
	}

	@Override
	public List<Report> getAllReports() {
		List<Report> allReports = reportRepository.findAll();
		return allReports;
	}
	
	@Override
	public List<Report> getByProductDetails(List<Report> reportList, Product product, String version) {
		List<Report> reportsByProduct = new ArrayList<>();
		for(Report report: reportList) {
			if(report.getProduct().equals(product)&&report.getVersion().equalsIgnoreCase(version)) {
				reportsByProduct.add(report);
			}
		}
		return reportsByProduct;
	}

	@Override
	public List<Report> getByUser(List<Report> reportList, User user) {
		List<Report> reportsByUser = new ArrayList<>();
		for(Report report: reportList) {
			if(report.getUser().equals(user)) {
				reportsByUser.add(report);
			}
		}
		return reportsByUser;
	}

	@Override
	public Report getByProductUserVersion(long productId, long userId, String version) {
		Product product = productRepository.findById(productId).get();
		User user = userRepository.findById(userId).orElseThrow();
		List<Report> allReports = getAllReports();
		List<Report> reportsByProduct = getByProductDetails(allReports, product, version);
		List<Report> reportsByUser = getByUser(reportsByProduct, user);
		if(reportsByUser.size()>0) {
			return reportsByUser.get(0);
		}else {
			return null;
		}
	}

	@Override
	public List<Report> getProductReports(long productId) {
		Product product = productRepository.findById(productId).get();
		List<Report> reportsByProduct = product.getReports();
		return reportsByProduct;
	}

	@Override
	public List<Report> updateReportStatus(long productId, String version , List<Long> userIdList, List<String> statusList) {
		List<Report> updatedList = new ArrayList<>();
		for(int i=0;i<userIdList.size();i++) {
			long userId = userIdList.get(i);
			String status = statusList.get(i);
			Report report = getByProductUserVersion(productId, userId, version);
			report.setStatus(status);
			updatedList.add(report);
			reportRepository.save(report);
		}
		Product product = productRepository.findById(productId).get();
		ProductDetails pd = productDetailsService.getByProductIdAndVersion(productId, version);
		ProductDetails latestPd = productDetailsService.getActiveVersion(productId);
		int accepted = 0;
		for(String str: statusList) {
			if(str.equalsIgnoreCase("ACCEPTED")) {
				accepted = accepted + 1;
			}
		}
		if(accepted>0) {
			pd.setFlagged(true);
			Product bannedProduct = pd.getProduct();
			bannedProduct.setEnabled(false);
			productRepository.save(bannedProduct);
		}
		return updatedList;
	}

	@Override
	public List<Report> getByStatus(String status) {
		List<Report> allReports = reportRepository.findAll();
		List<Report> statusReports = new ArrayList<>();
		for(Report report: allReports) {
			if(report.getStatus().equalsIgnoreCase(status)) {
				statusReports.add(report);
			}
		}
		return statusReports;
	}

	@Override
	public List<Report> getByStatusAndProduct(long productId, String version, String status) {
		List<Report> allReports = getAllReports();
		Product product = productRepository.findById(productId).get();
		List<Report> allByProduct = getByProductDetails(allReports, product, version);
		List<Report> allByStatus = new ArrayList<>();
		for(Report report: allByProduct) {
			if(status.equalsIgnoreCase("PENDING") && report.getStatus().equalsIgnoreCase("PENDING")) {
				allByStatus.add(report);
			}
			if(status.equalsIgnoreCase("HANDLED") && (report.getStatus().equalsIgnoreCase("ACCEPTED")||report.getStatus().equalsIgnoreCase("DENIED"))) {
				allByStatus.add(report);
			}
		}
		return allByStatus;
	}

	@Override
	public List<Report> getByProductDetailsAndStatus(long productId, String version, String status) {
		List<Report> allReports = getAllReports();
		List<Report> finalResult = new ArrayList<>();
		if(status.equalsIgnoreCase("PENDING")) {
			for(Report report: allReports) {
				if(report.getProduct().getId()==productId&& report.getVersion().equals(version) && report.getStatus().equalsIgnoreCase("PENDING")) {
					finalResult.add(report);
				}
			}
		}else {
			for(Report report: allReports) {
				if(report.getProduct().getId()==productId&& report.getVersion().equals(version) && (report.getStatus().equalsIgnoreCase("ACCEPTED")||report.getStatus().equalsIgnoreCase("DENIED"))) {
					finalResult.add(report);
				}
			}
		}
		return finalResult;
	}





}
