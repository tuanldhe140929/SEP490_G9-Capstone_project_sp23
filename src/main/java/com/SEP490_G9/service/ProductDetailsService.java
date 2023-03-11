package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entity.ProductDetails;

public interface ProductDetailsService {
	// public ProductDetails getProductDetailsByProductId(Long productId);

	public ProductDetails createProductDetails(ProductDetails productDetails);

	public ProductDetails updateProductDetails(ProductDetails notEdited);

	public ProductDetails getActiveVersion(Long productId);

	public ProductDetails createNewVersion(Long id, String newVersion);
	
	public ProductDetails getByProductIdAndVersion(Long productId, String version);

	public List<ProductDetails> getBySellerIdAndIsDraft(Long sellerId, boolean isDraft);

	public List<ProductDetails> getAllByProductId(Long id);

	public List<ProductDetails> getByKeyword(String keyword);

	public List<ProductDetails> getAll();

	public boolean existByProductIdAndVersion(Long productId, String version);


}
