package com.SEP490_G9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
//	public Cart findByUserId(Long userId);
//
//	@Query(value = "SELECT TOP 1 * FROM carts WHERE account_id = :accountId ORDER BY id DESC", nativeQuery = true)
//	public Cart findCurrentCart(Long accountId);
//	
	public Cart findFirstByUserOrderByIdDesc(User user);


}
