package com.SEP490_G9.models.Entities;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name="refresh_token")
public class RefreshToken {
	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long id;

	  @OneToOne
	  @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true, nullable = false)
	  private User user;

	  @Column(nullable = false, unique = true)
	  private String token;

	  @Column(nullable = false)
	  private Instant expiryDate;
	  
	  public RefreshToken() {}

	public RefreshToken(long id, User user, String token, Instant expiryDate) {
		super();
		this.id = id;
		this.user = user;
		this.token = token;
		this.expiryDate = expiryDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
