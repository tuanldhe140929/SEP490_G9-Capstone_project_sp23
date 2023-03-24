package com.SEP490_G9.service;

import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.ViolationType;

public interface ViolationService {
	ViolationType sendVio(String name, long violationTypeId);
}
