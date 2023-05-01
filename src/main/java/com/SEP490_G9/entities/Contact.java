package com.SEP490_G9.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties(value = { "" })
@Entity
@Table(name = "contacts")
public class Contact implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "firstname", nullable = false, unique = true)
	private String first;
	
	@Column(name = "lastname", nullable = false, unique = true)
	private String last;

	@Column(name = "created_date", unique = false, nullable = false, insertable = true)
	private Date created_date = new Date();
	
	@Column(name="phone", unique = false, nullable = false, insertable = true)
	private String phone;
	
	@Column(name="address", unique = false, nullable = false, insertable = true)
	private String address;
	
	@Column(name="message", unique = false, nullable = false, insertable = true)
	private String message;
	
	@Column(name="email", unique = false, nullable = false, insertable = true)
	private String email;
	
	public Contact() {
	}

	public Contact(Long id, String first, String last, Date created_date, String phone, String address, String message,
			String email) {
		super();
		this.id = id;
		this.first = first;
		this.last = last;
		this.created_date = created_date;
		this.phone = phone;
		this.address = address;
		this.message = message;
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
