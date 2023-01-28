package com.SEP490_G9.services.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.ManageAccountInfoService;

@Service
public class ManageAccountInfoServiceImpl implements ManageAccountInfoService {

	@Autowired
	UserRepository userRepo;

	@Override

	public User getAccountInfo() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		return user;
	}

	@Override
	public User changeAccountPassword(String newPassword, String oldPassword) {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		String encodedOldPassword = new BCryptPasswordEncoder().encode(oldPassword);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(encoder.matches(oldPassword, user.getPassword())) {
			String encodedPassword = new BCryptPasswordEncoder().encode(newPassword);
			user.setPassword(encodedPassword);
			userRepo.save(user);
			return user;
		}else {
			return null;
		}
	}

	@Override
	public User changeAccountName(String newName) {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		user.setUsername(newName);
		userRepo.save(user);
		return user;
	}

}
