package com.SEP490_G9.models.embeddables;

import jakarta.persistence.Embeddable;

@Embeddable
public class CartItemKey {
	private Long cartId;
	private Long productId;
	
	public CartItemKey() {
		// TODO Auto-generated constructor stub
	}

	public CartItemKey(Long cartId, Long productId) {
		super();
		this.cartId = cartId;
		this.productId = productId;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
}
