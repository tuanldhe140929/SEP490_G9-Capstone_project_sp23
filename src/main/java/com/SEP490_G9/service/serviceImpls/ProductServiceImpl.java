package com.SEP490_G9.service.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entity.Product;
import com.SEP490_G9.entity.Seller;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Override
	public Product createProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public Product updateProduct(Product product) {
		return productRepository.save(product);
	}

	@Override
	public boolean deleteProductByIdAndSeller(Long id, Seller seller) {
		Product product = productRepository.findById(id).get();
		if (product == null || product.getSeller() != seller) {
			throw new ResourceNotFoundException("product id", id.toString(), seller);
		}
		return true;
	}

	@Override
	public Product getProductByIdAndSeller(Long productId, Seller currentSeller) {
		Product product = productRepository.findById(productId).get();
		if (product == null || product.getSeller() != currentSeller) {
			throw new ResourceNotFoundException("product id", productId.toString(), currentSeller);
		}
		return product;
	}

	@Override
	public List<Product> getProductsBySellerId(Long sellerId) {
		return productRepository.findBySellerId(sellerId);
	}

	@Override
	public boolean setActiveVersion(Long productId, String version) {
		Product product = productRepository.findById(productId).orElseThrow();
		product.setActiveVersion(version);
		productRepository.save(product);
		return true;
	}

	@Override
	public Product getProductById(Long id) {
		Product product = productRepository.findById(id).orElseThrow();
		return product;
	}
}
