package com.SEP490_G9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.SEP490_G9.dto.CartDTO;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.service.CartService;

@RestController
@RequestMapping("private/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping("/add/{productId}")
	public ResponseEntity<?> addProduct(@PathVariable(name = "productId") Long productId) {
		Cart currentCart = cartService.getCurrentCart();
		System.out.println(currentCart.getId() + " cart id");
		Cart cart = cartService.addItem(productId, currentCart.getId());
		CartDTO dto = new CartDTO(cart);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/remove/{productId}")
	public ResponseEntity<?> removeProduct(@PathVariable(name = "productId") Long productId) {
		Cart currentCart = cartService.getCurrentCart();
		
		Cart cart = cartService.removeItem(productId, currentCart.getId());
		CartDTO dto = new CartDTO(cart);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/getCurrentCartDTO")
	public ResponseEntity<?> getCart() {
		Cart cart = cartService.getCurrentCart();
		System.out.println(cart.getId());
		CartDTO dto = new CartDTO(cart);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/removeAll/{productId}")
	public ResponseEntity<?> removeAllProduct(@PathVariable(name = "productId") Long productId) {
		CartDTO cart = cartService.removeAllProduct(productId);
		return ResponseEntity.ok(cart);
	}
	
	@GetMapping("/isUserPurchasedProduct")
	public ResponseEntity<?> isUserPurchasedProduct(@RequestParam(name="userId")Long userId, @RequestParam(name="productId") Long productId){
		boolean isUserPurchasedProduct = cartService.isUserPurchasedProduct(userId, productId);
		return ResponseEntity.ok(isUserPurchasedProduct);
	}
}