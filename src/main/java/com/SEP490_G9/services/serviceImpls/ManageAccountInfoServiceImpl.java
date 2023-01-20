package com.SEP490_G9.services.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.SEP490_G9.models.DTOS.User;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.ManageAccountInfoService;

@Service
public class ManageAccountInfoServiceImpl implements ManageAccountInfoService {

	@Autowired
	UserRepository userRepository;

	@Override
	public User getUserInfo(String email) {
		return userRepository.findByEmail(email);
	}

}
