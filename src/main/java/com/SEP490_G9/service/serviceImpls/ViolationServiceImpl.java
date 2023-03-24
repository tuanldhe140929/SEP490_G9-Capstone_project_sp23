package com.SEP490_G9.service.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.repository.ViolationRepository;
import com.SEP490_G9.service.ViolationService;

@Service
public class ViolationServiceImpl implements ViolationService{

	@Autowired
	ViolationRepository vioRepo;
	
	@Override
	public boolean addVio(Violation vio) {
		vioRepo.save(vio);
		return true;
	}

}
