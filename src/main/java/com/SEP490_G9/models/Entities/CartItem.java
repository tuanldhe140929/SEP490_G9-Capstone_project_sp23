package com.SEP490_G9.models.Entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import java.io.Serializable;

import com.SEP490_G9.models.embeddables.CartItemKey;
import com.SEP490_G9.models.embeddables.ProductVersionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "cart" })
@Entity
@Table(name = "cart_items")
public class CartItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CartItemKey cartItemKey = new CartItemKey();;

	@MapsId("cartId")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cart_id", referencedColumnName = "id")
	private Cart cart;

	@MapsId("productVersionKey")
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "product_id", nullable = false),
			@JoinColumn(name = "version", nullable = false) })
	private ProductDetails productDetails;

	public CartItem() {

	}

	public CartItem(Cart cart, ProductDetails productDetails) {
		super();
		ProductVersionKey key = productDetails.getProductVersionKey();
		this.cartItemKey = new CartItemKey(cart.getId(),key);
		this.cart = cart;
		this.productDetails = productDetails;
	}

	public CartItem(CartItemKey cartItemKey, Cart cart, ProductDetails productDetails) {
		super();
		this.cartItemKey = cartItemKey;
		this.cart = cart;
		this.productDetails = productDetails;
	}

	public CartItemKey getCartItemKey() {
		return cartItemKey;
	}

	public void setCartItemKey(CartItemKey cartItemKey) {
		this.cartItemKey = cartItemKey;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public ProductDetails getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(ProductDetails productDetails) {
		this.productDetails = productDetails;
	}
}
