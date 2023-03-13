package com.SEP490_G9.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.SEP490_G9.entities.Role;
import com.SEP490_G9.entities.Seller;

public class SellerDTO {
	private long id;
	private String email;
	private Date createdDate;
	private Date lastModified;
	private boolean enabled = true;
	private List<Role> roles = new ArrayList<Role>();
	private String username;
	private String firstName;
	private String lastName;
	private String avatar;
	private boolean emailVerified = false;
	private String phoneNumber;
	private boolean sellerEnabled;

	public SellerDTO() {
		// TODO Auto-generated constructor stub
	}

	public SellerDTO(Seller seller) {
		this.id = seller.getId();
		this.email = seller.getEmail();
		this.createdDate = seller.getCreatedDate();
		this.lastModified = seller.getLastModified();
		this.enabled = seller.isEnabled();
		this.roles = seller.getRoles();
		this.username = seller.getUsername();
		this.lastModified = seller.getLastModified();
		this.avatar = seller.getAvatar();
		this.emailVerified = seller.isEmailVerified();
		this.phoneNumber = seller.getPhoneNumber();
		this.sellerEnabled = seller.isSellerEnabled();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isSellerEnabled() {
		return sellerEnabled;
	}

	public void setSellerEnabled(boolean sellerEnabled) {
		this.sellerEnabled = sellerEnabled;
	}

}
