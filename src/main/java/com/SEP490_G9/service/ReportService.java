package com.SEP490_G9.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.embeddable.ReportItemKey;

public interface ReportService {
	
	Report sendReport(long productId, long accountId, String description, long violationTypeId);
}
