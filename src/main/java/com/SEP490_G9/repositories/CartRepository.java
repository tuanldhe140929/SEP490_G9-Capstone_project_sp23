package com.SEP490_G9.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.User;

public interface CartRepository extends JpaRepository<Cart, Long> {
	public Cart findByUserId(Long userId);
//    Optional<Cart> findByUserId(Long userId);
    
        
    
}
