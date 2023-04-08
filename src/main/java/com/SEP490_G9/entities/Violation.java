package com.SEP490_G9.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties(value = { "accounts" })
@Entity
@Table(name = "violations")
public class Violation {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "description", unique = false, nullable = false, insertable = true)
	private String description;

	@Column(name = "created_date", unique = false, nullable = false, insertable = true)
	private Date created_date = new Date();
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id", unique = false, nullable = true) // sual lai la false
	private Account account;
	
	public Violation() {
	}

	public Violation(Long id, String description, Date created_date, Account account) {
		super();
		this.id = id;
		this.description = description;
		this.created_date = created_date;
		this.account = account;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
