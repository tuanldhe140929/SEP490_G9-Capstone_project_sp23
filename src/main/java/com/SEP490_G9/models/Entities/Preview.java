package com.SEP490_G9.models.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value="product")
@Entity
@Table(name = "previews")
public class Preview {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "source")
	private String source;

	@Column(name = "type")
	private String type;
	
	@ManyToOne
	@JoinColumn(name="product_id", nullable=false)
	private Product product;
	
	public Preview() {
	}

	public Preview(Long id, String source, String type) {
		super();
		this.id = id;
		this.source = source;
		this.type= type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String path) {
		this.source = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
