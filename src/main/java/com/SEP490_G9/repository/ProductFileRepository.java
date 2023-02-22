package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.entity.ProductFile;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFile, Long> {
	Boolean existsByName(String name);
	
	List<ProductFile> findByProductDetails(ProductDetails productDetails);

	boolean existsByNameAndProductDetails(String name,ProductDetails productDetails);
}
