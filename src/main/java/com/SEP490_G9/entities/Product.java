package com.SEP490_G9.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(value = { "reports", "seller" })
@Entity
@Table(name = "products")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "enabled")
	private boolean enabled = true;

	@ManyToOne
	@JoinColumn(name = "seller_id", nullable = false)
	private Seller seller;

	@Column(name = "active_version", nullable = false, length = 30)
	private String activeVersion;

	@OneToMany(mappedBy = "product")
	private List<ProductDetails> productDetails = new ArrayList<>();

	@Column(name = "draft", nullable = false)
	private boolean draft = false;

	@OneToMany(mappedBy = "product")
	private List<Report> reports = new ArrayList<>();

	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="last_modified")
	private Date lastModified;
	
	@ManyToOne
	@JoinColumn(name = "license_id")
	private License license;
	
	public Product() {
	}

	public Product(Seller seller) {
		this.seller = seller;
	}

	public Product(Long id, boolean enabled, Seller seller, List<ProductDetails> productDetails) {
		super();
		this.id = id;
		this.enabled = enabled;
		this.seller = seller;
		this.productDetails = productDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public List<ProductDetails> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetails> productDetails) {
		this.productDetails = productDetails;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	public String getActiveVersion() {
		return activeVersion;
	}

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public void setActiveVersion(String activeVersion) {
		this.activeVersion = activeVersion;
	}

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
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

}
