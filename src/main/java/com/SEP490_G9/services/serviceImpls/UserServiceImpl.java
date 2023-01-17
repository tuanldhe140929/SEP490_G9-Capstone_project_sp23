package com.SEP490_G9.services.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.models.User;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public User getUserInfo(String email) {
		return userRepository.findByEmail(email);
	}

}
