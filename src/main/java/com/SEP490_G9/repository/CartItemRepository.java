package com.SEP490_G9.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entity.CartItem;
import com.SEP490_G9.entity.embeddable.CartItemKey;


public interface CartItemRepository extends JpaRepository<CartItem, CartItemKey> {

}
