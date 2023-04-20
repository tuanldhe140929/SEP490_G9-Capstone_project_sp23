package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.dto.ViolationDTO;
import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.entities.ViolationType;

public interface ViolationService {
	public boolean addViolation(Violation vio);
	public boolean updateSellerStatus(long account_id);
	public List<ViolationType> getVioTypeList(); 
}
