package com.SEP490_G9.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.services.CartService;

@RestController
@RequestMapping("private/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addProduct(@PathVariable Long productId) {
         Cart cart = cartService.addProduct(productId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeProduct(@PathVariable Long productId) {
    	Cart cart = cartService.removeProduct(productId);
    	return ResponseEntity.ok(cart);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<?> getCart(@PathVariable Long cartId) {
        Cart cart =  cartService.getCart(cartId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/")
    public ResponseEntity<?> createCart(@RequestParam(value = "cartId", required = false) Long cartId) {
        Cart cart = cartService.createCart(cartId);
        return ResponseEntity.ok(cart);
    }
}