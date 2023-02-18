package com.SEP490_G9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.models.Entities.ProductDetails;
import com.SEP490_G9.models.Entities.Seller;
import com.SEP490_G9.models.embeddables.ProductVersionKey;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, ProductVersionKey> {
	ProductDetails findByProductIdAndProductVersionKeyVersion(Long productId, String version);

	ProductDetails findFirstByProductIdOrderByCreatedDateDesc(Long productId);

}
