package com.SEP490_G9.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.entity.embeddable.ProductVersionKey;


public interface ProductDetailsRepository extends JpaRepository<ProductDetails, ProductVersionKey> {
	ProductDetails findByProductIdAndProductVersionKeyVersion(Long productId, String version);

	ProductDetails findFirstByProductIdOrderByCreatedDateDesc(Long productId);
}
