package com.SEP490_G9.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value = "products")
@Entity
@Table(name = "sellers")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "account_id")
public class Seller extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="paypal_email")
	private String paypalEmail;

	@Column(name = "seller_enabled")
	private boolean sellerEnabled;

	@OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
	private List<Product> products = new ArrayList<>();

	public Seller() {
		// TODO Auto-generated constructor stub
	}

	public Seller(boolean sellerEnabled, List<Product> products) {
		super();
		this.sellerEnabled = sellerEnabled;
		this.products = products;
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

	public String getPaypalEmail() {
		return paypalEmail;
	}

	public void setPaypalEmail(String paypalEmail) {
		this.paypalEmail = paypalEmail;
	}

}
