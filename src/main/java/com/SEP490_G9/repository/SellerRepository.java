package com.SEP490_G9.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entities.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {

	Seller findByUsername(String username);

	Seller findById(long id);
	
}
