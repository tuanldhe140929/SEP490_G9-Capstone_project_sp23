package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.embeddable.CartItemKey;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemKey> {

	List<CartItem> findByProductDetails(ProductDetails productDetails);

}
