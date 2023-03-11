package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.entity.Product;
import com.SEP490_G9.entity.Seller;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

	public List<Product> findBySeller(Seller user);

	public List<Product> findBySellerId(Long sellerId);

}
