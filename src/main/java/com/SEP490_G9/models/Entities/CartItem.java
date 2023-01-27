package com.SEP490_G9.models.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import com.SEP490_G9.models.Entities.Cart;

@Entity
@Table(name = "cart_items")
public class CartItem {
	@EmbeddedId
	private CartItemKey cartItemKey;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	@Embeddable
	
	public class CartItemKey {
		@Column(name= "cart_id" )
		private Cart cart;

		@Column(name= "product_id" )
		private Product product;
	}

	public CartItem() {

	}

}
