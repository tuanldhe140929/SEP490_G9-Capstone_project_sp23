package com.SEP490_G9.service;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Seller;

public interface SellerService {
	Seller getSellerById(Long sellerId);

	Seller getSellerByAProduct(long productId);
}
