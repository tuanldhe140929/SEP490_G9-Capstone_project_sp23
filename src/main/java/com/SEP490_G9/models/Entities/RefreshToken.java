package com.SEP490_G9.models.Entities;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value = "account")
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne
	@JoinColumn(name = "account_id", referencedColumnName = "id", unique = true, nullable = false)
	private Account account;

	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false)
	private Instant expiryDate;

	public RefreshToken() {
	}

	public RefreshToken(long id, Account account, String token, Instant expiryDate) {
		super();
		this.id = id;
		this.account = account;
		this.token = token;
		this.expiryDate = expiryDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Instant getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Instant expiryDate) {
		this.expiryDate = expiryDate;
	}

}
