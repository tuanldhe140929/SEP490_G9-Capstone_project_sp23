package com.SEP490_G9.models.Entities;

import jakarta.persistence.*;

@Entity
@Table(name="sellers")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name="account_id")
public class Seller extends User{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="phone_number")
	private String phoneNumber;
	
	@Column(name="enabled")
	private boolean enabled;
	
	public Seller() {
		// TODO Auto-generated constructor stub
	}

	public Seller(String phoneNumber, boolean enabled) {
		super();
		this.phoneNumber = phoneNumber;
		this.enabled = enabled;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
