package com.SEP490_G9.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.embeddable.CartItemKey;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemKey> {

}
