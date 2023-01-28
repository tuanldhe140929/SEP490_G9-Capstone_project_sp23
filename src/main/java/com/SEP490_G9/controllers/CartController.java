package com.SEP490_G9.controllers;
import org.springframework.web.bind.annotation.*;

import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.services.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add/{productId}")
    public Cart addProduct(@PathVariable Long productId) {
        return cartService.addProduct(productId);
    }

    @DeleteMapping("/remove/{productId}")
    public Cart removeProduct(@PathVariable Long productId) {
        return cartService.removeProduct(productId);
    }

    @GetMapping("/{cartId}")
    public Cart getCart(@PathVariable Long cartId) {
        return ((CartController) cartService).getCart(cartId);
    }

    @GetMapping("/")
    public Cart getCurrentCart() {
        return cartService.getCurrentCart();
    }
}