package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.entities.ViolationType;

public interface ViolationService {
	public boolean addVio(Violation vio);
	
	public List<ViolationType> getVioTypeList(); 
}
