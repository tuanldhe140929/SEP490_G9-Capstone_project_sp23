package com.SEP490_G9.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "carts")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(mappedBy="cart", fetch = FetchType.EAGER)
	private List<CartItem> items = new ArrayList<CartItem>();
	
	@ManyToOne
	@JoinColumn(name="account_id", nullable=false)
	private Account account;
	
	public Cart() {
		// TODO Auto-generated constructor stub
	}
	public Cart(Account account) {
		this.account = account;
	}

	public Cart(Long id, List<CartItem> items, Account account) {
		super();
		this.id = id;
		this.items = items;
		this.account = account;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}

	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public void addItem(CartItem newItem) {
		this.items.add(newItem);
	}
	public Cart createCart() {
		return new Cart();
    }


}