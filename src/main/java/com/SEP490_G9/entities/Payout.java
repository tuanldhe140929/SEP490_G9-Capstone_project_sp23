package com.SEP490_G9.entities;

import java.util.Date;

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

@Entity
@Table(name = "payouts")
public class Payout {

	public enum Status {
		SUCCESS, FAILED, CANCELED, CREATED, PROCESSING
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "last_modified")
	private Date lastModified;

	@Enumerated(EnumType.STRING)
	@Column(name = "payout_status")
	private Status status;

	@Column(name = "batch_id")
	private String batchId;

	@Column(name = "payout_fee")
	private Double payoutFee;

	@Column(name = "description")
	private String description;

	@ManyToOne
	@JoinColumn(name = "account_id")
	private Seller seller;

	@ManyToOne
	@JoinColumn(name = "transaction_id")
	private Transaction transaction;

	public Payout() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public Double getPayoutFee() {
		return payoutFee;
	}

	public void setPayoutFee(Double payoutFee) {
		this.payoutFee = payoutFee;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

}
