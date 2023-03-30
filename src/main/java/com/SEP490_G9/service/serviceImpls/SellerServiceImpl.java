package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.service.SellerService;

@Service
public class SellerServiceImpl implements SellerService {

	@Autowired
	SellerRepository sellerRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	AccountRepository accountRepository;

	@Override
	public Seller getSellerById(Long sellerId) {
		Seller seller = sellerRepository.findById(sellerId).get();
		if (seller == null) {
			throw new ResourceNotFoundException("seller", sellerId.toString(), "null");
		}
		return seller;
	}

	@Override
	public Seller getSellerByAProduct(long productId) {
		Product product = productRepository.findById(productId).get();
		Seller seller = product.getSeller();
		return seller;
	}

	@Override
	public int getSellerNumberOfFlags(long sellerId) {
		int amountOfFlags = 0;
		Seller seller = sellerRepository.findById(sellerId);
		List<Product> sellerProducts = seller.getProducts();
		for(Product product: sellerProducts) {
			if(!product.isEnabled()) {
				amountOfFlags = amountOfFlags + 1;
			}
		}
		return amountOfFlags;
	}

	//supporting methods
	
	@Override
	public List<Seller> getAllSellers() {
		List<Seller> allSellers = sellerRepository.findAll();
		return allSellers;
	}

	@Override
	public List<Seller> getAllEnabledSellers(List<Seller> sellerList) {
		List<Seller> allEnabled = new ArrayList<>();
		List<Account> allAccount = accountRepository.findAll();
		for(Seller seller: sellerList) {
			for(Account account: allAccount) {
				if(account.getId().equals(seller.getId()) && account.isEnabled()) {
					allEnabled.add(seller);
				}
			}
		}
		return allEnabled;
	}

	@Override
	public List<Seller> getFlaggedSellers(List<Seller> sellerList) {
		List<Seller> flaggedSellers = new ArrayList<>();
		for(Seller seller: sellerList) {
			if(getSellerNumberOfFlags(seller.getId())>0) {
				flaggedSellers.add(seller);
			}
		}
		return flaggedSellers;
	}

	@Override
	public List<Seller> getSellersByKeyword(List<Seller> sellerList, String keyword) {
		List<Seller> sellerByKeyword = new ArrayList<>();
		for(Seller seller: sellerList) {
			if(seller.getUsername().trim().toLowerCase().replaceAll("\\s+", " ").contains(keyword.trim().toLowerCase().replaceAll("\\s+", " "))) {
				sellerByKeyword.add(seller);
			}
		}
		return sellerByKeyword;
	}
	
	//main method
	@Override
	public List<Seller> getSellersForSearching(String keyword) {
		List<Seller> allSellers = getAllSellers();
		List<Seller> allEnabled	= getAllEnabledSellers(allSellers);
		List<Seller> allByKeyword = getSellersByKeyword(allEnabled, keyword);
		return allByKeyword;
	}

	@Override
	public List<Seller> getFlaggedSellers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Seller> getPurchasedProduct(long sellerId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
