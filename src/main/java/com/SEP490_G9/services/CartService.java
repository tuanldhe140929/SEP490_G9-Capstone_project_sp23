package com.SEP490_G9.services;

import org.springframework.stereotype.Service;


import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.models.DTOS.CartDTO;

@Service
public interface CartService {
	public CartDTO addProduct(Long productId);
    public CartDTO removeProduct(Long productId);
    public CartDTO getCurrentCartDTO();
    public CartDTO checkOut(Cart cart, User user);
	public Cart getCart(Long cartId);
	
	
}