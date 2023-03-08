package com.SEP490_G9.entity;

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

	@Column(name = "name")
	private String name;

	@Column(name = "source")
	private String source;

	@Column(name = "type")
	private String type;

	@Column(name = "size")
	private Long size;

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

}
