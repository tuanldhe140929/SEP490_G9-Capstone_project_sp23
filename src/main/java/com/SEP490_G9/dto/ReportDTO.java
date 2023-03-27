package com.SEP490_G9.dto;

import java.util.Date;

import com.SEP490_G9.entities.ViolationType;

public class ReportDTO {
	private Long userId;
	private Long productId;
	private String version;
	private String description;
	private Date created_date;
	private String status;
	private ViolationType violationtype;
	
	public ReportDTO(Long userId, Long productId, String version, String description, Date created_date, String status,
			ViolationType violationtype) {
		super();
		this.userId = userId;
		this.productId = productId;
		this.version = version;
		this.description = description;
		this.created_date = created_date;
		this.status = status;
		this.violationtype = violationtype;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ViolationType getViolationtype() {
		return violationtype;
	}
	public void setViolationtype(ViolationType violationtype) {
		this.violationtype = violationtype;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
}
