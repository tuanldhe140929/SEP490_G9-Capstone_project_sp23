package com.SEP490_G9.models.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value = { "product" })
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

	@Column(name="diplay_order")
	private String displayOrder;
	
	@Column(name = "last_update")
	private Date lastUpdate;

	@ManyToOne
	@JoinColumn(name="product_id", nullable=false)
	private Product product;

	public ProductFile() {
		// TODO Auto-generated constructor stub
	}
	
	public ProductFile(Long id, String name, String source, String type, Long size, String displayOrder,
			Date lastUpdate, Product product) {
		super();
		this.id = id;
		this.name = name;
		this.source = source;
		this.type = type;
		this.size = size;
		this.displayOrder = displayOrder;
		this.lastUpdate = lastUpdate;
		this.product = product;
	}

	public ProductFile(MultipartFile file, String source, Product product) {
		this.name = file.getOriginalFilename();
		this.source = source + this.name;
		this.type = file.getContentType();
		this.size = file.getSize();
		this.lastUpdate = new Date();
		this.product = product;
		this.displayOrder = "0";
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

}
