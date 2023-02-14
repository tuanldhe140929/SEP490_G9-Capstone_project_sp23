package com.SEP490_G9.models.Entities;

import java.util.Date;

import com.SEP490_G9.models.embeddables.CartItemKey;
import com.SEP490_G9.models.embeddables.ReportItemKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reports")
public class Report {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ReportItemKey cartItemKey = new ReportItemKey();
	
	@MapsId("userId")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id",referencedColumnName = "account_id")
	private User user;

	@MapsId("productId")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id",referencedColumnName = "id")
	private Product product;
	
	@Column(name = "description", unique = false, nullable = false, insertable = true, updatable = false)
	private String description;
	
	@Column(name = "created_date", unique = false, nullable = false, insertable = true, updatable = false)
	private Date created_date;
	@Column(name = "status", unique = false, nullable = false, insertable = true, updatable = false)
	private String status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "violation_type_id", unique = false, nullable = false)
	private ViolationType violation_types;

	public Report() {

	}

	public Report(ReportItemKey cartItemKey, User user, Product product, String description, Date created_date,
			String status, ViolationType violation_types) {
		this.cartItemKey = cartItemKey;
		this.user = user;
		this.product = product;
		this.description = description;
		this.created_date = created_date;
		this.status = status;
		this.violation_types = violation_types;
	}

	public ReportItemKey getCartItemKey() {
		return cartItemKey;
	}

	public void setCartItemKey(ReportItemKey cartItemKey) {
		this.cartItemKey = cartItemKey;
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
