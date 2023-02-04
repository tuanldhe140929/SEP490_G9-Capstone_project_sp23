package com.SEP490_G9.services;

import org.springframework.stereotype.Service;

import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.DTOS.CartDTO;

@Service
public interface CartService {
	public CartDTO addProduct(Long productId);
    public CartDTO removeProduct(Long productId);
    public CartDTO getCurrentCartDTO();
    
	public Cart getCart(Long cartId);
}