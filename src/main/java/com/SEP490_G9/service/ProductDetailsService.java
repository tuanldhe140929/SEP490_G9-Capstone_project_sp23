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

	
	//p1 : pd1 , pd2 , activersion: pd1 , seller1
	//p2 : pd3, pd4, activersion: pd3, seller1
	//p3 : pd5, pd6, aciversion:pd5, seller2
	
	
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

	public List<ProductDetails> getByTags(List<ProductDetails> listPd, List<Integer> tagIdList);

	public List<ProductDetails> getByPriceRange(List<ProductDetails> listPd, double min, double max);
	
	public List<ProductDetails> getProductByTime(List<ProductDetails> listPd);

	public List<ProductDetails> getAllByProductId(Long id);
	
	public boolean existByProductIdAndVersion(Long productId, String version);
	
	
	// by Quan Nguyen

	public List<ProductDetails> getProductForSearching(String keyword, int categoryid, List<Integer> tagIdList, double min, double max);

	public List<ProductDetails> getProductBySellerForSeller(long sellerId, String keyword, int categoryId, List<Integer> tagidlist, double min,
			double max);

	public List<ProductDetails> getProductBySellerForUser(long sellerId, String keyword, int categoryId, List<Integer> tagidlist ,double min,
			double max);

	public ProductDetails getByProductIdAndVersionIncludingDisabled(Long productId, String version);
	
	public ProductDetails getByProductIdAndVersion(Long productId, String version);
	
	public List<ProductDetails> getProductsByReportStatus(String report);
	
	public List<ProductDetails> getProductsByApprovalStatus(String status);
	
	public ProductDetails updateApprovalStatus(long productId, String version, String status);

	public List<ProductDetails> getAllByLatestVersion();
	
	public List<ProductDetails>getHotProduct(Long productId);

	public ProductDetails getActiveVersionForDownload(Long productId);

	public int getTotalPurchasedCount(Long productId);
	
	public String getCurrentVersion(long productId);

	ProductDetails cancelVerify(long productId, String version, String status);
	
	
	
}
