package com.SEP490_G9.dto;

import java.util.ArrayList;
import java.util.List;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.User;

import com.SEP490_G9.repository.PreviewRepository;

public class CartDTO {
	private Long id;
	private User user;
	private List<CartItemDTO> items;
	private double totalPrice;
	private List<Change> changes;
	public CartDTO(Long id, User user, List<CartItemDTO> items, int totalPrice) {
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
		List<CartItemDTO> itemDtos = new ArrayList<>();
		for (CartItem item : cart.getItems()) {
			itemDtos.add(new CartItemDTO(item));
		}
		this.items = itemDtos;
		for (CartItem item : cart.getItems()) {
			this.totalPrice += item.getPrice();
		}
		this.changes = cart.getChanges();
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

	public List<CartItemDTO> getItems() {
		return items;
	}

	public void setItems(List<CartItemDTO> items) {
		this.items = items;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<Change> getChanges() {
		return changes;
	}

	public void setChanges(List<Change> changes) {
		this.changes = changes;
	}

}
