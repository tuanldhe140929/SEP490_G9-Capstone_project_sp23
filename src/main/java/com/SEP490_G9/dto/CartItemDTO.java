
package com.SEP490_G9.dto;

import com.SEP490_G9.repository.PreviewRepository;
import com.SEP490_G9.entities.CartItem;

public class CartItemDTO {

	private ProductDetailsDTO product;
	private Long cartId;

	public CartItemDTO(CartItem cartItem) {
		super();

		ProductDetailsDTO pdd = new ProductDetailsDTO(cartItem.getProductDetails());
		this.product = pdd;

		this.cartId = cartItem.getCart().getId();

	}

	public CartItemDTO() {

	}

	public ProductDetailsDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDetailsDTO product) {
		this.product = product;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
}
