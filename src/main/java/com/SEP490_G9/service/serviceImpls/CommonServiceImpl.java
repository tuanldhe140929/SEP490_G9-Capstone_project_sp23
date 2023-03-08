package com.SEP490_G9.service.serviceImpls;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entity.Product;
import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.entity.Seller;
import com.SEP490_G9.entity.User;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.PreviewRepository;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.CommonService;
import com.SEP490_G9.util.StorageUtil;


@Service
public class CommonServiceImpl implements CommonService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	PreviewRepository previewRepository;

	@Autowired
	ProductDetailsRepository productDetailsRepo;

	@Autowired
	SellerRepository sellerRepository;
	
	@Autowired
	StorageUtil storageUtil;

	@Override
	public User getUserInfoByUsername(String username) {
		Seller ret = sellerRepository.findByUsername(username);
		return ret;
	}

	@Override
	public ProductDetailsDTO getLastVersionProductDetailsDTOByIdAndUserId(Long productId, Long sellerId) {
		Seller seller = sellerRepository.findById(sellerId).get();
		
		ProductDetails productDetails = productDetailsRepo.findFirstByProductIdOrderByCreatedDateDesc(productId);
		if (productDetails.getProduct().getSeller() != seller) {
			throw new ResourceNotFoundException("product id:", productId.toString(), "seller");
		}
		ProductDetailsDTO ret = new ProductDetailsDTO(productDetails, previewRepository);
		return ret;
	}

	@Override
	public List<ProductDetailsDTO> getProductsByUsername(String username) {
		Seller seller = sellerRepository.findByUsername(username);
		List<Product> products = productRepository.findBySeller(seller);
		List<ProductDetails> latestVersionProducts = new ArrayList<>();
		for (Product product : products) {
			latestVersionProducts.add(productDetailsRepo.findFirstByProductIdOrderByCreatedDateDesc(product.getId()));
		}
		List<ProductDetailsDTO> dtos = new ArrayList<>();
		return dtos;
	}
	
	

	@Override
	public boolean checkIfPurchased(Long userId, Long productId) {
		
		return false;
	}

	@Override
	public int totalProductCount(Long sellerId) {
		List<Product> products = productRepository.findBySellerId(sellerId);
		return products.size();
	}

}
