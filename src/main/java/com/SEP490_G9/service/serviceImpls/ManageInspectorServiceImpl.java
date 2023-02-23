package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entity.Role;
import com.SEP490_G9.entity.User;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.repository.RoleRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.ManageInspectorService;
import com.SEP490_G9.util.Constant;

@Service
public class ManageInspectorServiceImpl implements ManageInspectorService{

	@Autowired
	UserRepository accountRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Override
	public List<User> getAllInspectors() {
		List<User> inspectorList = accountRepo.findByRolesIn(new Role(Constant.STAFF_ROLE_ID,"ROLE_STAFF"));
		return inspectorList;
	}

	@Override
	public boolean addInspector(User inspector) {
		if(userRepo.existsByEmail(inspector.getEmail())) {
			throw new DuplicateFieldException("email", inspector.getEmail());
		}
		if(userRepo.existsByUsername(inspector.getUsername())) {
			throw new DuplicateFieldException("username", inspector.getUsername());
		}
		inspector.setUsername(inspector.getUsername().trim());
		if (inspector.getUsername().contains(" ")) {
			throw new AuthRequestException("username can not contains spaces");
		}
		String encodedPassword = new BCryptPasswordEncoder().encode(inspector.getPassword().trim());
		inspector.setPassword(encodedPassword);
		inspector.setEnabled(true);
		inspector.setVerified(true);
		inspector.setRole(roleRepo.getReferenceById((long)3));
		userRepo.save(inspector);
		return true;
	}

	@Override
	public boolean activateInspector(Long id) {
		Optional<User> optionalInspector = userRepo.findById(id);
		User inspector = optionalInspector.get();
		inspector.setEnabled(true);
		userRepo.save(inspector);
		return true;
	}

	@Override
	public boolean deactivateInspector(Long id) {
		Optional<User> optionalInspector = userRepo.findById(id);
		User inspector = optionalInspector.get();
		inspector.setEnabled(false);
		userRepo.save(inspector);
		return true;
	}

	@Override
	public boolean deleteInspector(Long id) {
		userRepo.deleteById(id);
		return true;
	}

	@Override
	public boolean updateInspector(User inspector) {
		User existingInspector = userRepo.findById(inspector.getId()).orElse(null);
		if (userRepo.existsByEmail(inspector.getEmail())) {
			throw new DuplicateFieldException("email", inspector.getEmail());
		}
		if (userRepo.existsByUsername(inspector.getUsername())) {
			throw new DuplicateFieldException("username", inspector.getUsername());
		}
		existingInspector.setEmail(inspector.getEmail());
		existingInspector.setUsername(inspector.getUsername());
		existingInspector.setPassword(inspector.getPassword());
		userRepo.save(existingInspector);
		return true;
	}


	
	

}