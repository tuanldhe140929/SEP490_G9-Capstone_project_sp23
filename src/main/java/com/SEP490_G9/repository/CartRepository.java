package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import com.SEP490_G9.entities.User;

import com.SEP490_G9.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
//	public Cart findByUserId(Long userId);
//
//	@Query(value = "SELECT TOP 1 * FROM carts WHERE account_id = :accountId ORDER BY id DESC", nativeQuery = true)
//	public Cart findCurrentCart(Long accountId);
//	
	public Cart findFirstByUserOrderByIdDesc(User user);

	public boolean existsByUserId(Long userId);

	public List<Cart> findByUserId(Long userid);

	public boolean existsByIdAndUserId(Long cartId, Long userId);

}
