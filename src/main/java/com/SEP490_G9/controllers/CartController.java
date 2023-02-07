package com.SEP490_G9.controllers;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.DTOS.CartDTO;
import com.SEP490_G9.services.CartService;

@RestController
@RequestMapping("private/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addProduct(@PathVariable(name="productId") Long productId) {
    	CartDTO cartDTO = cartService.addProduct(productId);
        return ResponseEntity.ok(cartDTO);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable(name="productId") Long productId) {
    	CartDTO cart = cartService.removeProduct(productId);
    	return ResponseEntity.ok(cart);
    }

    
    @GetMapping("/getCurrentCartDTO")
    public ResponseEntity<?> getCart() {
        CartDTO cart = cartService.getCurrentCartDTO();
        return ResponseEntity.ok(cart);
    }	
    
    @GetMapping("/")
    public ResponseEntity<?> checkOut() {
        CartDTO cart = cartService.getCurrentCartDTO();
        return ResponseEntity.ok(cart);
    }	
}