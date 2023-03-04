package com.SEP490_G9.service.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.entity.Seller;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.service.ProductDetailsService;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {

	@Autowired
	ProductDetailsRepository productDetailsRepo;

	@Override
	public ProductDetails getProductDetailsByProductId(Long productId) {
		ProductDetails ret = productDetailsRepo.findFirstByProductIdOrderByLastModifiedDesc(productId);
		if (ret == null) {
			throw new ResourceNotFoundException("product details:", productId.toString(), "null");
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
	public ProductDetails getProductDetailsByProductIdAndVersionAndSeller(ProductDetailsDTO productDetailsDTO,
			Seller seller) {
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


}
