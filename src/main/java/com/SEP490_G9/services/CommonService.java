package com.SEP490_G9.services;

import java.util.List;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;

public interface CommonService {
	public User getUserInfoByUsername(String username);

	public List<Product> getProductsByUsername(String username);
}
