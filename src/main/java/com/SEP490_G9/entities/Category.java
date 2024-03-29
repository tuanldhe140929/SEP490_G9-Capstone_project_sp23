package com.SEP490_G9.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value = { "productDetails" })
@Entity
@Table(name = "categories")
public class Category implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "category")
	private List<ProductDetails> productDetails = new ArrayList<>();

	@Column(name="description")
	private String description;
	
	public Category() {
		// TODO Auto-generated constructor stub
	}

	public Category(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public Category(int id, String name, List<Product> products) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
