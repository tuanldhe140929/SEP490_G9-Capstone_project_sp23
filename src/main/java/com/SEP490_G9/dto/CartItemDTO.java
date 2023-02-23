
package com.SEP490_G9.dto;

import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.entity.CartItem;

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
