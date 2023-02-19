package com.SEP490_G9.services;

import org.springframework.stereotype.Service;

import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.models.DTOS.CartDTO;

@Service
public interface CartService {
	public CartDTO addProduct(Long productId, String version);
    public CartDTO removeProduct(Long productId);
    public CartDTO getCurrentCartDTO();
    public CartDTO checkOut( Account account);
	public Cart getCart(Long cartId);
	public CartDTO removeAllProduct(Long productId);
	
}