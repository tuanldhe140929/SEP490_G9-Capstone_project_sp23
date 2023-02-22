package com.SEP490_G9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SEP490_G9.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
//	public Cart findByUserId(Long userId);

	@Query(value = "SELECT TOP 1 * FROM carts WHERE account_id = :accountId ORDER BY cart_id DESC", nativeQuery = true)
	public Cart findCurrentCart(Long accountId);


}
