package com.SEP490_G9.models.Entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "license")
public class License {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "content")
	private String content;

	@OneToMany(mappedBy = "license",fetch = FetchType.EAGER)
	List<ProductDetails> productDetails = new ArrayList<>();
	
	public License() {
		// TODO Auto-generated constructor stub
	}

	public License(int id, String name, String content) {
		super();
		this.id = id;
		this.name = name;
		this.content = content;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
