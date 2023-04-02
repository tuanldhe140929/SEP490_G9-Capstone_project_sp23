package com.SEP490_G9.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value = { "productDetails" })
@Entity
@Table(name = "files")
public class ProductFile {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "source", length = 255)
	private String source;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "size", nullable = false)
	private Long size;

	@Column(name="created_date", nullable = false)
	private Date createdDate;
	
	@Column(name="last_modified", nullable = false)
	private Date lastModified;
	
	@Column(name="enabled", nullable = false)
	private boolean enabled;
	
	@Column(name="new_uploaded", nullable = false)
	private boolean newUploaded;
	
	@Column(name="reviewed", nullable = false)
	private boolean reviewed;
	
	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "product_id", nullable = false),
			@JoinColumn(name = "version", nullable = false) })
	private ProductDetails productDetails;

	public ProductFile() {
		// TODO Auto-generated constructor stub
	}

	public ProductFile(MultipartFile file, String source, ProductDetails productDetails) {
		this.name = file.getOriginalFilename();
		this.source = source + this.name;
		this.type = file.getContentType();
		this.size = file.getSize();
		this.productDetails = productDetails;
	}

	public ProductFile(String source, MultipartFile file, ProductDetails productDetails) {
		this.name = file.getOriginalFilename();
		this.source = source;
		this.type = file.getContentType();
		this.size = file.getSize();
		this.productDetails = productDetails;
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


	public boolean isNewUploaded() {
		return newUploaded;
	}

	public void setNewUploaded(boolean newUploaded) {
		this.newUploaded = newUploaded;
	}

	public boolean isReviewed() {
		return reviewed;
	}

	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	public ProductDetails getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(ProductDetails productDetails) {
		this.productDetails = productDetails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
