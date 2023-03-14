package com.SEP490_G9.entities.embeddable;

import java.io.Serializable;

import jakarta.persistence.Column;

public class RateItemKey implements Serializable {

	  @Column(name = "user_id")
	  private Long userId;

	  @Column(name = "product_id")
	  private Long productId;

	 public RateItemKey(Long userId, Long productId) {
		super();
		this.userId = userId;
		this.productId = productId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public RateItemKey() {
		
	}

	 
	}