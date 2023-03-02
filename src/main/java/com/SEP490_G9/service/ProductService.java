package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entity.Product;
import com.SEP490_G9.entity.Seller;

public interface ProductService {
	List<Product> getProductsBySeller(Seller seller);

	Product createProduct(Product product);

	Product updateProduct(Product product);

	boolean deleteProductByIdAndSeller(Long id,Seller seller);

	Product getProductByIdAndSeller(Long productId, Seller currentSeller);
}
