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
	public List<Product> getProductsBySeller(Seller seller) {
		return productRepository.findBySellerId(seller.getId());
	}

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
		if(product==null||product.getSeller()!=currentSeller) {
			throw new ResourceNotFoundException("product id", productId.toString(), currentSeller);
		}
		return product;
	}

}
