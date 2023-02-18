package com.SEP490_G9.services.serviceImpls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.ResourceNotFoundException;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.models.DTOS.ProductDTO;
import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.repositories.PreviewRepository;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.CommonService;

@Service
public class CommonServiceImpl implements CommonService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	PreviewRepository previewRepository;

	@Override
	public User getUserInfoByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new ResourceNotFoundException("User", "username", username);
		}
		return user;
	}

	@Override
	public User getCurrentLogedInUser() {
		// TODO Auto-generated method stub
		return null;
	}
}
