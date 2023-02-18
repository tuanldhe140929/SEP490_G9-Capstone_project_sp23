package com.SEP490_G9.models.DTOS;

import com.SEP490_G9.models.Entities.Cart;

import com.SEP490_G9.models.Entities.CartItem;



public class CartItemDTO {
	
	private Long productId;
	private Long cartId;
	private double price;

	public CartItemDTO(CartItem cartItem) {
		super();
		
		this.productId = cartItem.getProduct().getId();
		this.cartId= cartItem.getCart().getId();
		this.price = cartItem.getProduct().getPrice();
	}

	public CartItemDTO() {

	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}



}
