package com.SEP490_G9.services;

import java.util.List;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;

public interface ManageProductService {
	
	public User getCurrentUserInfo();
	
	public Product addProduct(Product product);
	
	public Product updateProduct(Product product);
	
	public Product deleteProduct(Long id);
	
	public List<Product> getProductsByUser();
}
