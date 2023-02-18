package com.SEP490_G9.models.Entities;

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

	@Column(name = "name", unique = true)
	private String name;

	@Column(name = "source", unique = true)
	private String source;

	@Column(name = "type")
	private String type;

	@Column(name = "size")
	private Long size;

	@Column(name = "diplay_order")
	private String displayOrder;

	@Column(name = "last_update")
	private Date lastUpdate;

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
		this.lastUpdate = new Date();
		this.productDetails = productDetails;
		this.displayOrder = "0";
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

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

}
