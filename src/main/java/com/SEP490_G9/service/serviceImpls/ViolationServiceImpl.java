package com.SEP490_G9.service.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.dto.ViolationDTO;
import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.repository.ViolationRepository;
import com.SEP490_G9.repository.ViolationTypeRepository;
import com.SEP490_G9.service.ViolationService;

@Service
public class ViolationServiceImpl implements ViolationService{

	@Autowired
	ViolationRepository vioRepo;
	
	@Autowired
	ViolationTypeRepository vioTypeRepo;
	
	@Override
	public boolean addViolation(Violation vio) {
		vioRepo.save(vio);
		return true;
	}

	@Override
	public List<ViolationType> getVioTypeList() {
		List<ViolationType> viotypelist = vioTypeRepo.findAll();
		return viotypelist;
	}

}
