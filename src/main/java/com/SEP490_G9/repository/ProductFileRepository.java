package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductFile;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFile, Long> {
	Boolean existsByName(String name);
	
	List<ProductFile> findByProductDetails(ProductDetails productDetails);

	boolean existsByNameAndProductDetails(String name,ProductDetails productDetails);
}
