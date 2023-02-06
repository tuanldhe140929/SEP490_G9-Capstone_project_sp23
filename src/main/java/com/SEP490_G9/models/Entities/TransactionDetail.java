package com.SEP490_G9.models.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="transaction_details")
public class TransactionDetail {
	
	@Id
	@ManyToOne
	@JoinColumn(name = "transaction_id")
	private Transaction transaction;
	
	@Id
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;
	
	private Long price;

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}
}
