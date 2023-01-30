package com.SEP490_G9.services;

import org.springframework.stereotype.Service;

import com.SEP490_G9.models.Entities.Cart;

@Service
public interface CartService {
	public Cart addProduct(Long productId);
    public Cart removeProduct(Long productId);
    public Cart getCurrentCart();
	public Cart getCart(Long cartId);
}