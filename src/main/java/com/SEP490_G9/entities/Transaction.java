package com.SEP490_G9.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.paypal.api.payments.Payer;

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

@JsonIgnoreProperties(value = { "cart" })
@Entity
@Table(name = "transactions")
public class Transaction {

	public enum Status {
		COMPLETED, CANCELED, FAILED, CREATED, APPROVED, EXPIRED
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "cart_id", nullable = false)
	private Cart cart;

	@Column(name = "amount")
	private double amount;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@ManyToOne
	@JoinColumn(name = "transaction_fee_id", nullable = false)
	private TransactionFee fee;

	@Column(name = "created_date")
	private Date createDate;

	@Column(name = "last_modified")
	private Date lastModified;

	@Column(name = "description")
	private String description;

	@Column(name = "paypal_id")
	private String paypalId;

	@Transient
	private String approvalUrl;

	@Transient
	private Payer payer;

	public Transaction() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
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

	public Payer getPayer() {
		return payer;
	}

	public void setPayer(Payer payer) {
		this.payer = payer;
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

	public TransactionFee getFee() {
		return fee;
	}

	public void setFee(TransactionFee fee) {
		this.fee = fee;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", cart=" + cart + ", amount=" + amount + ", status=" + status + ", fee=" + fee
				+ ", createDate=" + createDate + ", lastModified=" + lastModified + ", description=" + description
				+ ", paypalId=" + paypalId + ", approvalUrl=" + approvalUrl + ", payer=" + payer + "]";
	}

}
