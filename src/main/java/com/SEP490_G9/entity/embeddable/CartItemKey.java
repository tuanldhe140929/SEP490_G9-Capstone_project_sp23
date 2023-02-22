package com.SEP490_G9.entity.embeddable;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class CartItemKey implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long cartId;
	private ProductVersionKey productVersionKey;

	public CartItemKey() {
		// TODO Auto-generated constructor stub
	}

	public CartItemKey(Long cartId, ProductVersionKey productVersionKey) {
		super();
		this.cartId = cartId;
		this.productVersionKey = productVersionKey;
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public ProductVersionKey getProductVersionKey() {
		return productVersionKey;
	}

	public void setProductVersionKey(ProductVersionKey productVersionKey) {
		this.productVersionKey = productVersionKey;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

}
