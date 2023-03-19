package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entities.Category;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Tag;

public interface ProductDetailsService {
	// public ProductDetails getProductDetailsByProductId(Long productId);

	// by Nam Dinh

	public ProductDetails getActiveVersion(Long productId);

	public ProductDetails createNewVersion(Long id, String newVersion);

	public ProductDetails createProductDetails(ProductDetails productDetails);

	public ProductDetails updateProductDetails(ProductDetails notEdited);
	
	public List<ProductDetails> getBySellerIdAndIsDraft(Long sellerId, boolean isDraft);
	// supporting methods

	public List<ProductDetails> getAll();

	public List<ProductDetails> getByLatestVer(List<ProductDetails> listPd);

	public List<ProductDetails> getBySeller(List<ProductDetails> listPd, long sellerId);

	public List<ProductDetails> getByPending(List<ProductDetails> listPd);

	public List<ProductDetails> getByApproved(List<ProductDetails> listPd);

	public List<ProductDetails> getByRejected(List<ProductDetails> listPd);

	public List<ProductDetails> getByDrafted(List<ProductDetails> listPd);

	public List<ProductDetails> getByPublished(List<ProductDetails> listPd);

	public List<ProductDetails> getByEnabled(List<ProductDetails> listPd);

	public List<ProductDetails> getByDisabled(List<ProductDetails> listPd);

	public List<ProductDetails> getByKeyword(List<ProductDetails> listPd, String keyword);

	public List<ProductDetails> getByCategory(List<ProductDetails> listPd, int categoryId);

	public List<ProductDetails> getByTag(List<ProductDetails> listPd, List<Integer> tagId);

	public List<ProductDetails> getByPriceRange(List<ProductDetails> listPd, int min, int max);

	public List<ProductDetails> getAllByProductId(Long id);

	public boolean existByProductIdAndVersion(Long productId, String version);


	// by Quan Nguyen

	public List<ProductDetails> getProductForSearching(String keyword, int categoryid, int min, int max);

	public List<ProductDetails> getProductBySellerForSeller(long sellerId, String keyword, int categoryId, int min,
			int max);

	public List<ProductDetails> getProductBySellerForUser(long sellerId, String keyword, int categoryId, int min,
			int max);

	public ProductDetails getByProductIdAndVersion(Long productId, String version);

}
