package com.SEP490_G9.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.SEP490_G9.dto.CartDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.User;
@Service
public interface CartService {
	public CartDTO addProduct(Long productId);

	public CartDTO removeProduct(Long productId);

	public Cart getCurrentCart();

	public Cart getCart(Long cartId);

	public CartDTO removeAllProduct(Long productId);

	public List<Cart> getByUser(User user);

}