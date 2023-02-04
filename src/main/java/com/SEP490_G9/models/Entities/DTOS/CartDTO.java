package com.SEP490_G9.models.Entities.DTOS;

import java.util.List;

import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.CartItem;
import com.SEP490_G9.models.Entities.User;

public class CartDTO {
	private Long id;
	private User user;
	private List<CartItem> items;
	private int totalPrice;

	public CartDTO(Long id, User user, List<CartItem> items, int totalPrice) {
		super();
		this.id = id;
		this.user = user;
		this.items = items;
		this.totalPrice = totalPrice;
	}

	public CartDTO() {

	}

	public CartDTO(Cart cart) {
		this.id = cart.getId();
		this.user = cart.getUser();
		this.items = cart.getItems();
		for (CartItem item : cart.getItems()) {
	       this.totalPrice += item.getProduct().getPrice();
	    }
	    

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

}
