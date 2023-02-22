package com.SEP490_G9.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "sellers")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "account_id")
public class Seller extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "seller_enabled")
	private boolean sellerEnabled;

	@OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
	private List<Product> products = new ArrayList<>();

	public Seller() {
		// TODO Auto-generated constructor stub
	}

	public Seller(String phoneNumber, boolean sellerEnabled, List<Product> products) {
		super();
		this.phoneNumber = phoneNumber;
		this.sellerEnabled = sellerEnabled;
		this.products = products;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isSellerEnabled() {
		return sellerEnabled;
	}

	public void setSellerEnabled(boolean sellerEnabled) {
		this.sellerEnabled = sellerEnabled;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
