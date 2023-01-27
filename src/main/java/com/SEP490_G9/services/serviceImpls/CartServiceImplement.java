package com.SEP490_G9.services.serviceImpls;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.ResourceNotFoundException;
import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.CartItem;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.models.embeddables.CartItemKey;
import com.SEP490_G9.repositories.CartItemRepository;
import com.SEP490_G9.repositories.CartRepository;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.CartService;

@Service
public class CartServiceImplement implements CartService {
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CartRepository cartRepository;
	
	@Override
	public Cart addProduct(Long productId) {
		CartItem item = new CartItem();
		Product product = productRepository.getReferenceById(productId);
		item.setProduct(product);
		Cart cart = getCurrentCart();
		cart.addItem(item);
		
		return cart;
	}

	@Override
	public Cart removeProduct(Long productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cart getCurrentCart() {
		return null;
	}

   
}