package com.SEP490_G9.models.DTOS;

import com.SEP490_G9.models.Entities.Cart;

import com.SEP490_G9.models.Entities.CartItem;
import com.SEP490_G9.models.Entities.ProductDetails;
import com.SEP490_G9.models.embeddables.CartItemKey;



public class CartItemDTO {
	
	private ProductDetails product;
	private Long cartId;

	public CartItemDTO(CartItem cartItem) {
		super();
		this.product= cartItem.getProductDetails();
		this.cartId= cartItem.getCart().getId();

	}

	public CartItemDTO() {

	}

	public ProductDetails getProduct() {
		return product;
	}

	public void setProduct(ProductDetails product) {
		this.product = product;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
}
