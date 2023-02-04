package com.SEP490_G9.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.ProductFile;

@Repository
public interface ProductFileRepository extends JpaRepository<ProductFile, Long> {
	Boolean existsByName(String name);
	
	List<ProductFile> findByProduct(Product product);
}
