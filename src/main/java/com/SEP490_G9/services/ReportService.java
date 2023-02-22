package com.SEP490_G9.services;

import java.util.List;
import java.util.Optional;

import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.Report;
import com.SEP490_G9.models.Entities.DTOS.CartDTO;
import com.SEP490_G9.models.embeddables.ReportItemKey;

public interface ReportService {
	 List<Report> getAllReport();  

	  void saveReport(Report report);  

	  void deleteReport(ReportItemKey id);  

	  Optional<Report> findReportById(ReportItemKey id);

	void saveEditedReport(Report reportEdit);  
}
