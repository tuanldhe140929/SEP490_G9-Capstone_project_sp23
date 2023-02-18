package com.SEP490_G9.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.models.Entities.Preview;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.ProductDetails;

public interface PreviewRepository extends JpaRepository<Preview, Long> {

	List<Preview> findByProductDetailsAndType(ProductDetails productDetails, String string);

}
