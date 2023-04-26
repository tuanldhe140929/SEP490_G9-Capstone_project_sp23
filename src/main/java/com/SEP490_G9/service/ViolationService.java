package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.dto.ViolationDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.entities.ViolationType;

public interface ViolationService {
	public Violation addViolation(Violation vio);
	public Account updateSellerStatus(long id);
	public List<ViolationType> getVioTypeList(); 
}
