package com.SEP490_G9.dto;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.service.AccountService;

public class ViolationDTO {
	
	@Autowired
	AccountService accountservice;
	
	public ViolationDTO() {
	}
	
	private Long violation_id;
	private Date createdDate;
	private String description;
	private Long account_id;
	
	
	public ViolationDTO(Long violation_id, Date createdDate, String description, Long account_id) {
		super();
		this.violation_id = violation_id;
		this.createdDate = createdDate;
		this.description = description;
		this.account_id = account_id;
	}


	public Long getViolation_id() {
		return violation_id;
	}


	public void setViolation_id(Long violation_id) {
		this.violation_id = violation_id;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Long getAccount_id() {
		return account_id;
	}


	public void setAccount_id(Long account_id) {
		this.account_id = account_id;
	}
	
	public Account findById() {
		// TODO Auto-generated method stub
		return accountservice.getById(account_id);
	}

}
