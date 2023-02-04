package com.SEP490_G9.models.Entities;

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
	@JoinColumn(name="user_id", nullable=false)
	private User user;
	
	public Cart() {
		// TODO Auto-generated constructor stub
	}
	public Cart(User user) {
		this.user= user;
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


}