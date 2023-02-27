package com.SEP490_G9.service;

import org.springframework.stereotype.Service;

import com.SEP490_G9.dto.CartDTO;
import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.Cart;

@Service
public interface CartService {
	public CartDTO addProduct(Long productId);
    public CartDTO removeProduct(Long productId);
    public CartDTO getCurrentCartDTO();
    public CartDTO checkOut( Account account);
	public Cart getCart(Long cartId);
	public CartDTO removeAllProduct(Long productId);
	
}