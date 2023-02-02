package com.SEP490_G9.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;

public interface ManageProductService {
	
	public User getCurrentUserInfo();
	
	public Product addProduct(Product product);
	
	public Product updateProduct(Product product);
	
	public Product deleteProduct(Long id);
	
	public List<Product> getProductsByUser();

	public Product newProduct();
	
	public Product uploadCoverImage(MultipartFile coverImage, Long productId) throws IOException;

	public File getCoverImage(Long productId);

	public Product createNewProduct();
}
