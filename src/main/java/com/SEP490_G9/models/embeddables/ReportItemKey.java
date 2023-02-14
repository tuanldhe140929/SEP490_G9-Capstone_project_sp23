package com.SEP490_G9.models.embeddables;

public class ReportItemKey {
	private Long userId;
	private Long productId;
	
	public ReportItemKey() {
	
	}
	public ReportItemKey(Long uderId, Long productId) {
		this.userId = uderId;
		this.productId = productId;
	}
	public Long getUderId() {
		return userId;
	}
	public void setUderId(Long uderId) {
		this.userId = uderId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	
}
