package com.SEP490_G9.services.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.models.DTOS.User;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.CommonService;

@Service
public class CommonServiceImpl implements CommonService {
	@Autowired
	UserRepository userRepository;
	
	@Override
	public User getUserInfoByUsername(String username) {
		User user = userRepository.findByUsername(username);
		return user;
	}

}
