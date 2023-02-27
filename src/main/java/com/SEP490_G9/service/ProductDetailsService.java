package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.entity.Seller;

public interface ProductDetailsService {
	public ProductDetails getProductDetailsByProductId(Long productId);

	public ProductDetails createProductDetails(ProductDetails productDetails);

	public ProductDetails getProductDetailsByProductIdAndVersionAndSeller(ProductDetailsDTO productDetailsDTO,
			Seller seller);

	public ProductDetails updateProductDetails(ProductDetails notEdited);

	public List<ProductDetails> getAllByProductId(Long id);

}
