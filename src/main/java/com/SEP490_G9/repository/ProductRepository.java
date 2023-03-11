package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Seller;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

	public List<Product> findBySeller(Seller user);
	
	public Product findBySellerAndId(Seller user, Long pId);

	public Product findByIdAndSellerId(String productName, Long sellerId);

	public List<Product> findBySellerId(Long sellerId);

}
