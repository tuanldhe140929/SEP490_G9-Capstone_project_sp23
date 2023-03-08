package com.SEP490_G9.service;

import java.util.List;
import java.util.Optional;

import com.SEP490_G9.entity.Report;
import com.SEP490_G9.entity.embeddable.ReportItemKey;

public interface ReportService {
	List<Report> getAllReport();

	Report saveReport(Report report);

	void deleteReport(ReportItemKey id);

	Optional<Report> findReportById(ReportItemKey id);

	void saveEditedReport(Report reportEdit);
}
