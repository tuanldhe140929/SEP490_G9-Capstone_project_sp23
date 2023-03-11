package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entities.Preview;
import com.SEP490_G9.entities.ProductDetails;


public interface PreviewRepository extends JpaRepository<Preview, Long> {

	List<Preview> findByProductDetailsAndType(ProductDetails productDetails, String string);

}
