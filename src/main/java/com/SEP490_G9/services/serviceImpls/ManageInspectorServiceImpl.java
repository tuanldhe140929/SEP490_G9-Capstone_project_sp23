package com.SEP490_G9.services.serviceImpls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.AuthRequestException;
import com.SEP490_G9.exceptions.DuplicateFieldException;
import com.SEP490_G9.models.Entities.Role;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.repositories.RoleRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.ManageInspectorService;

@Service
public class ManageInspectorServiceImpl implements ManageInspectorService{

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Override
	public List<User> getAllInspectors() {
		List<User> inspectorList = userRepo.findByRole(new Role((long)3,"ROLE_INSPECTOR"));
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

	

}
