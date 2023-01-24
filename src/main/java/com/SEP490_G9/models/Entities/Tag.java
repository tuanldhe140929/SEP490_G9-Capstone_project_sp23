package com.SEP490_G9.models.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value = { "products" })
@Entity
@Table(name="tags")
public class Tag implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="name")
	private String name;
	public Tag() {
	}
	
	@ManyToMany(mappedBy="tags")
    private List<Product> products = new ArrayList<>();
	
	public Tag(int id, String name) {
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
	
}
