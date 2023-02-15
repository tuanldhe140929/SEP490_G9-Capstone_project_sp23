package com.SEP490_G9.services;

import java.util.List;

import com.SEP490_G9.models.DTOS.ProductDTO;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;

public interface CommonService {
	public User getUserInfoByUsername(String username);

	public List<ProductDTO> getProductsByUsername(String username);

	public ProductDTO getProductByNameAndUserId(String productName, Long userId);

	public User getCurrentLogedInUser();
}
