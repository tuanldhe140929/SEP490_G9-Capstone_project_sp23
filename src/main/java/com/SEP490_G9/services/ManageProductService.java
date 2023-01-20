package com.SEP490_G9.services;

import java.util.List;

import com.SEP490_G9.models.DTOS.Product;
import com.SEP490_G9.models.DTOS.User;

public interface ManageProductService {
	
	public User getCurrentUserInfo(String email);
	
	public Product addProduct(Product product);
	
	public Product updateProduct(Product product);
	
	public Product deleteProduct(Long id);
	
	public List<Product> getProductsByUser();
}
