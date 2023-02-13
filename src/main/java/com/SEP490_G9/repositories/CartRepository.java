package com.SEP490_G9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SEP490_G9.models.Entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	public Cart findByUserId(Long userId);

	@Query(value="Select top 1 * from carts where user_id=:userId ORDER BY cart_id DESC",nativeQuery = true)
	public Cart findCurrentCart(Long userId);

}
