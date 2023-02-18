package com.SEP490_G9.models.DTOS;

import java.util.List;

import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.CartItem;


public class CartDTO {
	private Long id;
	private Account account;
	private List<CartItem> items;
	private int totalPrice;

	public CartDTO(Long id, Account account, List<CartItem> items, int totalPrice) {
		super();
		this.id = id;
		this.account = account;
		this.items = items;
		this.totalPrice = totalPrice;
	}

	public CartDTO() {

	}

	public CartDTO(Cart cart) {
		this.id = cart.getId();
		this.account = cart.getAccount();
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

	
	

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
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
