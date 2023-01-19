package com.SEP490_G9.models.DTOS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value="product")
@Entity
@Table(name = "previews")
public class Preview {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "path")
	private String path;

	@ManyToOne
	@JoinColumn(name="product_id", nullable=false)
	private Product product;
	
	public Preview() {
		// TODO Auto-generated constructor stub
	}

	public Preview(Long id, String path) {
		super();
		this.id = id;
		this.path = path;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
