package com.SEP490_G9.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.embeddable.ReportItemKey;

public interface ReportService {
	
	Report sendReport(long productId, long accountId, String description, long violationTypeId);
	
	List<Report> getAllReports();
	
	List<Report> getByProduct(List<Report> reportList, Product product);
	
	List<Report> getByUser(List<Report> reportList, User user);
	
	List<Report> getByProductAndUser(long productId, long userId);
	
	List<Report> getProductReports(long productId);
}
