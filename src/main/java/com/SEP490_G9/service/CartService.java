package com.SEP490_G9.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.SEP490_G9.dto.CartDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.embeddable.CartItemKey;
@Service
public interface CartService {
	public Cart addItem(Long productId, Long cartId);

	public Cart removeItem(Long productId, Long cartId);

	public Cart getCurrentCart();

	public Cart getById(Long cartId);
	
	public Cart createCart(Cart cart);

	public CartDTO removeAllProduct(Long productId);

	public List<Cart> getByUserId(Long userId);

	public boolean isUserOwnCart(Long userId, Long cartId);

	public boolean isCartHadPurchased(Long cartId);

	public boolean isUserPurchasedProduct(Long userId, Long productId);

	
}