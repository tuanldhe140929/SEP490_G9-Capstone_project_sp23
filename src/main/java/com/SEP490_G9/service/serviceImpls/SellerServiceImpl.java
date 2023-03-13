package com.SEP490_G9.service.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.service.SellerService;

@Service
public class SellerServiceImpl implements SellerService {

	@Autowired
	SellerRepository sellerRepository;

	@Override
	public Seller getSellerById(Long sellerId) {
		Seller seller = sellerRepository.findById(sellerId).get();
		if (seller == null) {
			throw new ResourceNotFoundException("seller", sellerId.toString(), "null");
		}
		return seller;
	}

}
