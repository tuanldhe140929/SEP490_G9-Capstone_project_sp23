package com.SEP490_G9.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.embeddable.ReportItemKey;

public interface ReportService {
	
	Report sendReport(long productId, long accountId, String version, String description, long violationTypeId);
	
	List<Report> getAllReports();
	
	List<Report> getByProductDetails(List<Report> reportList, Product product, String version);
	
	List<Report> getByProductAllVersions(long productId, String status);
	
	List<Report> getByUser(List<Report> reportList, User user);
	
	Report getByProductUserVersion(long productId, long userId, String version);
	
	List<Report> updateReportStatus(long productId, List<String> versionList, List<Long> userIdList, List<String> statusList);
	
	List<Report> getByStatus(String status);
	
	List<Report> getProductReports(long productId);
	
	List<Report> getByStatusAndProduct(long productId, String version, String status);
	
}
