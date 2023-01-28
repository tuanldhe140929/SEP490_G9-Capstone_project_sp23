package com.SEP490_G9.models.Entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import com.SEP490_G9.models.embeddables.CartItemKey;

@Entity
@Table(name = "cart_items")
public class CartItem {
	

	
	@EmbeddedId
	private CartItemKey cartItemKey;

	@MapsId("cartId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id",referencedColumnName = "id")
	private Cart cart;

	@MapsId("productId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id",referencedColumnName = "id")
	private Product product;
	
	public CartItem() {

	}

	public CartItem(CartItemKey cartItemKey, Cart cart, Product product) {
		super();
		this.cartItemKey = cartItemKey;
		this.cart = cart;
		this.product = product;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	

}
