package com.SEP490_G9.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@JsonIgnoreProperties(value= {"cart"})
@Entity
@Table(name = "transactions")
public class Transaction {

	public enum Status {
		COMPLETED, CANCELED, FAILED, CREATED,
	}

	public enum Type{
		BUY,SELL
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@Column(name = "amount")
	private double amount;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type")
	private Type type;
	
	@ManyToOne
	@JoinColumn(name = "transaction_fee_id",nullable = false)
	private TransactionFee fee;
	
	@Column(name = "created_date")
	private Date createDate;

	@Column(name = "last_modified")
	private Date lastModified;

	@Column(name = "description")
	private String description;

	@Column(name="paypal_id")
	private String paypalId;
	
	@Column(name="paypal_token")
	private String paypalToken;
	
	@Transient
	private String approvalUrl;
	
	public Transaction() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPaypalToken() {
		return paypalToken;
	}

	public void setPaypalToken(String paypalToken) {
		this.paypalToken = paypalToken;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getApprovalUrl() {
		return approvalUrl;
	}

	public void setApprovalUrl(String approvalUrl) {
		this.approvalUrl = approvalUrl;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}



	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getPaypalId() {
		return paypalId;
	}

	public void setPaypalId(String paypalId) {
		this.paypalId = paypalId;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", cart=" + cart + ", amount=" + amount + ", status=" + status + ", type="
				+ type + ", createDate=" + createDate + ", lastModified=" + lastModified + ", description="
				+ description + ", paypalId=" + paypalId + ", approvalUrl=" + approvalUrl + "]";
	}

	public TransactionFee getFee() {
		return fee;
	}

	public void setFee(TransactionFee fee) {
		this.fee = fee;
	}
}
