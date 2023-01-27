package com.SEP490_G9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.models.Entities.CartItem;
import com.SEP490_G9.models.Entities.CartItem.CartItemKey;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemKey> {

}
