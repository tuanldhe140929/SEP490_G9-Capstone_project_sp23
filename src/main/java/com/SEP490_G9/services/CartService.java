package com.SEP490_G9.services;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.ResourceNotFoundException;
import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.repositories.CartRepository;
import com.SEP490_G9.repositories.ProductRepository;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Cart addProduct(Long productId) {
    	Product product = productRepository.findById(productId).orElse(null);
    	if (product == null) {
    	    throw new ResourceNotFoundException("Product not found", null, product);
    	}
        Cart cart = cartRepository.findByUserId(product.getUser().getId()).orElse(new Cart());
        cart.addProduct(product);
        cartRepository.save(cart);
        return cart;
    }

    public Cart removeProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        Cart cart = cartRepository.findByUserId(product.getUser().getId()).orElse(new Cart());
        cart.removeProduct(product);
        cartRepository.save(cart);
        return cart;
    }

    public Cart getCurrentCart() {
        // Retrieve the current cart based on user session or other criteria
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return cartRepository.findByUserId(user.getId()).orElse(new Cart());
    }
}