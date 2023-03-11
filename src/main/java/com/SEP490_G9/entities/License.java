package com.SEP490_G9.entities;

import java.util.ArrayList;
import java.util.List;

import com.SEP490_G9.entities.ProductDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


@JsonIgnoreProperties(value= {"productDetails"})

@Entity
@Table(name = "license")
public class License {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "name")
	private String name;
	@Column(name = "acrynosm")
	private String acrynosm;

	@Column(name = "details", length = 1024)
	private String details;

	@Column(name = "reference_link")
	private String referenceLink;

	@OneToMany(mappedBy = "license", fetch = FetchType.EAGER)
	List<ProductDetails> productDetails = new ArrayList<>();

	public License() {
		// TODO Auto-generated constructor stub
	}

	public License(int id, String name, String acrynosm, String details, List<ProductDetails> productDetails) {
		super();
		this.id = id;
		this.name = name;
		this.acrynosm = acrynosm;
		this.details = details;
		this.productDetails = productDetails;
	}

	public String getAcrynosm() {
		return acrynosm;
	}

	public void setAcrynosm(String acrynosm) {
		this.acrynosm = acrynosm;
	}

	public List<ProductDetails> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetails> productDetails) {
		this.productDetails = productDetails;
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
}
