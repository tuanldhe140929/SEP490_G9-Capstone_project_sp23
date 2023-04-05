package com.SEP490_G9.dto;

import java.util.Date;

import com.SEP490_G9.entities.Payout;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;

public class PayoutDTO {

	public PayoutDTO(){
		
	}
	private Long id;
	private Double amount;
	private Date createdDate;
	private Date lastModified;
	private Payout.Status status;
	private String batchId;
	private Double payoutFee;
	private String description;
	private Seller seller;
	private Transaction transaction;
	
	public PayoutDTO(Payout payout) {
		super();
		this.id = payout.getId();
		this.amount = payout.getAmount();
		this.createdDate = payout.getCreatedDate();
		this.lastModified = payout.getLastModified();
		this.status = payout.getStatus();
		this.batchId = payout.getBatchId();
		this.payoutFee = payout.getPayoutFee();
		this.description = payout.getDescription();
		this.seller = payout.getSeller();
		this.transaction = payout.getTransaction();
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
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Payout.Status getStatus() {
		return status;
	}
	public void setStatus(Payout.Status status) {
		this.status = status;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public Double getPayoutFee() {
		return payoutFee;
	}
	public void setPayoutFee(Double payoutFee) {
		this.payoutFee = payoutFee;
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
	@Override
	public String toString() {
		return "PayoutDTO [id=" + id + ", amount=" + amount + ", createdDate=" + createdDate + ", lastModified="
				+ lastModified + ", status=" + status + ", batchId=" + batchId + ", payoutFee=" + payoutFee
				+ ", description=" + description + ", seller=" + seller + ", transaction=" + transaction + "]";
	}
	
}
