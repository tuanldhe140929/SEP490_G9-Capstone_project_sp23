package com.SEP490_G9.entities;

import java.util.ArrayList;
import java.util.List;

import com.SEP490_G9.entities.ProductDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


@JsonIgnoreProperties(value = { "productDetails" })
@Entity
@Table(name = "licenses")
public class License  {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "acronyms")
	private String acronyms;

	@Column(name = "details", length = 1024, nullable = false)
	private String details;

	@Column(name = "reference_link")
	private String referenceLink;

	@Column(name= "provider")
	private String provider;

	@OneToMany(mappedBy = "license", fetch = FetchType.EAGER)
	List<Product> products = new ArrayList<>();

	public License() {
		// TODO Auto-generated constructor stub
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider){
		this.provider = provider;
	}

	public String getAcronyms() {
		return acronyms;
	}

	public void setAcronyms(String acronyms) {
		this.acronyms = acronyms;
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String content) {
		this.details = content;
	}

	public String getReferenceLink() {
		return referenceLink;
	}

	public void setReferenceLink(String referenceLink) {
		this.referenceLink = referenceLink;
	}

	@Override
	public String toString() {
		return "License [id=" + id + ", name=" + name + ", acrynosm=" + acronyms + ", details=" + details
				+ ", referenceLink=" + referenceLink + ", productDetails=" + products + "]";
	}
	
}
