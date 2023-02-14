package com.SEP490_G9.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

	public List<Product> findByUser(User user);
	
	public Product findByUserAndId(User user, Long pId);

	public Product findByNameAndUserId(String productName, Long userId);
}
