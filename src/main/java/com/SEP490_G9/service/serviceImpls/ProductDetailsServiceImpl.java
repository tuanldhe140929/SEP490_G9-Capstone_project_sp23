package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;
import java.util.List;import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entities.Category;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductService;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {

	@Autowired
	ProductDetailsRepository productDetailsRepo;
	
	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	ProductService productService;
	
	@Override
	public ProductDetails getActiveVersion(Long productId) {
		Product product = productRepo.findById(productId).orElseThrow();
		ProductDetails ret = productDetailsRepo.findByProductIdAndProductVersionKeyVersion(productId, product.getActiveVersion());
		if(ret==null) {
			throw new ResourceNotFoundException("Product details", "version", product.getActiveVersion());
		}
		return ret;
	}
	
	@Override
	public ProductDetails createProductDetails(ProductDetails productDetails) {
		if (productDetailsRepo.existsByProductAndProductVersionKeyVersion(productDetails.getProduct(),
				productDetails.getVersion())) {
			throw new DuplicateFieldException("version ", productDetails.getVersion());
		}
		return productDetailsRepo.save(productDetails);
	}

	@Override
	public ProductDetails getByProductIdAndVersion(ProductDetailsDTO productDetailsDTO) {
		Seller seller = null;
		ProductDetails ret = productDetailsRepo.findByProductIdAndProductVersionKeyVersion(productDetailsDTO.getId(),
				productDetailsDTO.getVersion());
		if (ret == null || ret.getProduct().getSeller() != seller) {
			throw new ResourceNotFoundException("product id, version",
					productDetailsDTO.getId() + " " + productDetailsDTO.getVersion(), seller);
		}
		return ret;
	}

	@Override
	public ProductDetails updateProductDetailsStatus(ProductDetails edited) {
		return productDetailsRepo.save(edited);
	}
	

	@Override
	public ProductDetails updateProductDetails(ProductDetails edited) {

		if (edited.getName() == null || edited.getName() == "") {
			throw new DuplicateFieldException("name must not be blank", null);
		}
		if (edited.getCategory() == null) {
			throw new DuplicateFieldException("category must not be blank", null);
		}

		return productDetailsRepo.save(edited);
	}

	@Override
	public List<ProductDetails> getAllByProductId(Long id) {
		List<ProductDetails> ret = null;
		return productDetailsRepo.findByProductId(id);
	}

	@Override
	public ProductDetails getByIdAndVersion(Long productId, String activeVersion) {
		ProductDetails ret = productDetailsRepo.findByProductIdAndProductVersionKeyVersion(productId, activeVersion);
		if (ret == null) {
			throw new ResourceNotFoundException("product details", "version", activeVersion);
		}
		return ret;
	}

	@Override
	public List<ProductDetails> getByKeyword(String keyword) {
		List<ProductDetails> allProductDetails = productDetailsRepo.findAll();
		List<ProductDetails> searchResult = new ArrayList<>();
		for(ProductDetails pd: allProductDetails) {
			if(pd.getName().trim().toLowerCase().contains(keyword.trim().toLowerCase())) {
				searchResult.add(pd);
			}
		}
		return searchResult;
	}

	@Override
	public List<ProductDetails> getAll() {
		List<ProductDetails> allProductDetails = productDetailsRepo.findAll();
		return allProductDetails;
	}
	
	@Override
	public List<ProductDetails> getByKeywordCategoryTags(String keyword, int categoryid, int min, int max){
		List<ProductDetails> allProductDetails = productDetailsRepo.findAll();
		List<ProductDetails> searchResult = new ArrayList<>();
		List<ProductDetails> searchResultLatestVersion = new ArrayList<>();
		for(ProductDetails pd: allProductDetails) {
			if(categoryid == 0) {
				if(pd.getName().trim().toLowerCase().contains(keyword.trim().toLowerCase())  && pd.getPrice()>=min && pd.getPrice()<=max) {
					searchResult.add(pd);
				}
			}else {
				if(pd.getName().trim().toLowerCase().contains(keyword.trim().toLowerCase())  && pd.getCategory().getId() == categoryid && pd.getPrice()>=min && pd.getPrice()<=max) {
					searchResult.add(pd);
				}
			}
		}
		for(ProductDetails pd: searchResult) {
			Product product = pd.getProduct();
			String activeVersion = product.getActiveVersion();
			searchResultLatestVersion.add(getByIdAndVersion(product.getId(), activeVersion));
		}
		List<ProductDetails> finalResult = searchResultLatestVersion.stream().distinct().collect(Collectors.toList());
		return finalResult;
	}

}
