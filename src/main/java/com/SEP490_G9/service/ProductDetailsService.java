package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entities.Category;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Tag;

public interface ProductDetailsService {
	//public ProductDetails getProductDetailsByProductId(Long productId);

	public ProductDetails createProductDetails(ProductDetails productDetails);

	public ProductDetails getByProductIdAndVersion(ProductDetailsDTO productDetailsDTO);

	public ProductDetails updateProductDetails(ProductDetails notEdited);

	public List<ProductDetails> getAllByProductId(Long id);

	public ProductDetails getByIdAndVersion(Long productId, String activeVersion);

	public ProductDetails updateProductDetailsStatus(ProductDetails edited);
	
	public List<ProductDetails> getByKeyword(String keyword);
	
	public List<ProductDetails> getAll();

	public ProductDetails getActiveVersion(Long productId);
	
	public List<ProductDetails> getByKeywordCategoryTags(String keyword, int categoryid, int min, int max);
	
	public List<ProductDetails> getProductBySeller(long sellerid, String keyword, int categoryid, int min, int max);

}
