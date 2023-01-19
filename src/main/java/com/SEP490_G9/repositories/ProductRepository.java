package com.SEP490_G9.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.models.DTOS.Product;
import com.SEP490_G9.models.DTOS.User;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

	public List<Product> findByUser(User user);
	
	public Product findByUserAndId(User user, Long pId);
}
