package com.SEP490_G9.service.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entity.License;
import com.SEP490_G9.repository.LicenseRepository;
import com.SEP490_G9.service.LicenseService;

@Service
public class LicenseServiceImpl implements LicenseService {

	@Autowired
	LicenseRepository licenseRepo;
	
	@Override
	public List<License> getAllLicense() {
		return licenseRepo.findAll();
	}

}
