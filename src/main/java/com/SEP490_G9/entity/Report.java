package com.SEP490_G9.entity;

import java.io.Serializable;
import java.util.Date;

import com.SEP490_G9.entity.embeddable.ReportItemKey;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "reports")
public class Report{
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ReportItemKey reportKey = new ReportItemKey();
	
	@MapsId("userId")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id",referencedColumnName = "account_id")
	private User user;

	@MapsId("productId")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id",referencedColumnName = "id")
	private Product product;
	
	@Column(name = "description", unique = false, nullable = false, insertable = true)
	private String description;
	
	@Column(name = "created_date", unique = false, nullable = false, insertable = true)
	private Date created_date = new Date();
	@Column(name = "status", unique = false, nullable = false, insertable = true)
	private String status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "violation_type_id", unique = false, nullable = true)//sual lai la false
	private ViolationType violation_types;

	public Report() {
		
	}

	public Report(ReportItemKey cartItemKey, User user, Product product, String description, Date created_date,
			String status, ViolationType violation_types) {
		this.reportKey = cartItemKey;
		this.user = user;
		this.product = product;
		this.description = description;
		this.created_date = created_date;
		this.status = status;
		this.violation_types = violation_types;
	}

	public ReportItemKey getReportKey() {
		return reportKey;
	}

	public void setCartItemKey(ReportItemKey reportKey) {
		this.reportKey = reportKey;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public ViolationType getViolation_types() {
		return violation_types;
	}

	public void setViolation_types(ViolationType violation_types) {
		this.violation_types = violation_types;
	}
	
}
