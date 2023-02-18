package com.SEP490_G9.models.embeddables;

import java.io.Serializable;

import com.SEP490_G9.models.Entities.Product;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProductVersionKey implements Serializable {

	private Long productId;
	private String version;

	public ProductVersionKey() {
		// TODO Auto-generated constructor stub
	}

	public ProductVersionKey(Long productId, String version) {
		super();
		this.productId = productId;
		this.version = version;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}