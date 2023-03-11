package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entities.User;

public interface CommonService {
	public User getUserInfoByUsername(String username);

	public ProductDetailsDTO getLastVersionProductDetailsDTOByIdAndUserId(Long productId, Long userId);

	public List<ProductDetailsDTO> getProductsByUsername(String username);

	public boolean checkIfPurchased(Long userId, Long productId);

	public int totalProductCount(Long sellerId);
}
