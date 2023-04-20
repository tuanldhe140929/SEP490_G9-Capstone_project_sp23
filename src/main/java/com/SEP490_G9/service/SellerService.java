package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Seller;

public interface SellerService {
	Seller getSellerById(Long sellerId);

	Seller getSellerByAProduct(long productId);
	
	int getSellerNumberOfFlags(long sellerId);

	//supporting methods
	
	List<Seller> getAllSellers();
	
	List<Seller> getAllEnabledSellers(List<Seller> sellerList);
	
	List<Seller> getFlaggedSellers(List<Seller> sellerList);
	
	List<Seller> getSellersByKeyword(List<Seller> sellerList, String keyword);
	
	//main methods
	
	List<Seller> getSellersForSearching(String keyword);

	List<Seller> getFlaggedSellers();
	
	List<Seller> getPurchasedProduct(long sellerId);

	boolean createNewSeller(String paypalEmail);

	Seller getSellerByPaypalEmail(String paypalEmail);
	
	List<Seller> getReportedSellers();

}
