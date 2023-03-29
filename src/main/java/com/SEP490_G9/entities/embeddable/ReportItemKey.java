package com.SEP490_G9.entities.embeddable;

import java.io.Serializable;

public class ReportItemKey implements Serializable {
	private Long userId;
	private Long productId;
	private String version;

	public ReportItemKey() {

	}

	public ReportItemKey(Long uderId, Long productId, String version) {
		this.userId = uderId;
		this.productId = productId;
		this.version = version;
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
