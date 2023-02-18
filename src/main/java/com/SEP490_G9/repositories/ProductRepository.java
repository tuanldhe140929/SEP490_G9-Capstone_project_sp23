package com.SEP490_G9.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.Seller;
import com.SEP490_G9.models.Entities.User;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

	public List<Product> findBySeller(Seller user);
	
	public Product findBySellerAndId(Seller user, Long pId);

	public Product findByIdAndSellerId(String productName, Long sellerId);
}
