package com.SEP490_G9.services.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.SEP490_G9.models.DTOS.Product;
import com.SEP490_G9.models.DTOS.User;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.CommonService;

@Service
public class CommonServiceImpl implements CommonService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@Override
	public User getUserInfoByUsername(String username) {
		User user = userRepository.findByUsername(username);
		return user;
	}

	@Override
	public List<Product> getProductsByUsername(String username) {
		User user = null;
		try {
			user = userRepository.findByUsername(username);
		} catch (Exception e) {
			//throw new CustomException("Khong tim thay user");
		}
		List<Product> products = productRepository.findByUser(user);
		if (products == null) {
			//throw new CustomException("User nay khong co san pham nao");
		}
		return products;
	}

}
