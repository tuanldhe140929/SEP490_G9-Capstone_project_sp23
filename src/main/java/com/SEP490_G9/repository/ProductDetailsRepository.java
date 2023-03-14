package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, ProductVersionKey> {
	ProductDetails findByProductIdAndProductVersionKeyVersion(Long productId, String version);

	ProductDetails findFirstByProductIdOrderByCreatedDateDesc(Long productId);

	List<ProductDetails> findByProductId(Long id);

	List<ProductDetails> findByNameContaining(String name);

	List<ProductDetails> findAll();

	ProductDetails findFirstByProductIdOrderByLastModifiedDesc(Long productId);

	boolean existsByProductIdAndProductVersionKeyVersion(Long id, String newVersion);

}
