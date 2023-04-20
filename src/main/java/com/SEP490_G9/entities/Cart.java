package com.SEP490_G9.entities;

import java.util.ArrayList;
import java.util.List;

import com.SEP490_G9.dto.Change;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value = { "user", "transactions" })
@Entity
@Table(name = "carts")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(mappedBy = "cart", fetch = FetchType.EAGER)
	private List<CartItem> items = new ArrayList<CartItem>();

	@ManyToOne
	@JoinColumn(name = "account_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "cart", fetch = FetchType.EAGER)
	private List<Transaction> transactions = new ArrayList<>();

	@Transient
	List<Change> changes = new ArrayList<>();

	public Cart() {
		// TODO Auto-generated constructor stub
	}

	public Cart(User user) {
		this.user = user;
	}

	public Cart(Long id, List<CartItem> items, User user) {
		super();
		this.id = id;
		this.items = items;
		this.user = user;
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

	public User getUser() {
		return user;

	}

	public void setUser(User user) {
		this.user = user;
	}

	public void addItem(CartItem newItem) {
		this.items.add(newItem);
	}

	public Cart createCart() {
		return new Cart();
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public List<Change> getChanges() {
		return changes;
	}

	public void setChanges(List<Change> changes) {
		this.changes = changes;
	}

}