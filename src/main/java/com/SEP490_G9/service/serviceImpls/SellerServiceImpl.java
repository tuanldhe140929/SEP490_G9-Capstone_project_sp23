package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.service.SellerService;

@Service
public class SellerServiceImpl implements SellerService {

	@Autowired
	SellerRepository sellerRepository;
	
	@Autowired
	ProductRepository productRepository;

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

	@Override
	public List<Seller> getFlaggedSellers() {
		List<Seller> allSellers = sellerRepository.findAll();
		List<Seller> flaggedSellers = new ArrayList<>();
		for(Seller seller: allSellers) {
			if(getSellerNumberOfFlags(seller.getId())>0) {
				flaggedSellers.add(seller);
			}
		}
		return flaggedSellers;
	}
	
}
