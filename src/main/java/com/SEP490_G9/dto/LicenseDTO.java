package com.SEP490_G9.dto;

import com.SEP490_G9.entity.License;

public class LicenseDTO {
	private int id;

	private String name;

	private String acrynosm;

	private String details;

	private String referenceLink;

	public LicenseDTO(int id, String name, String acrynosm, String details, String referenceLink) {
		super();
		this.id = id;
		this.name = name;
		this.acrynosm = acrynosm;
		this.details = details;
		this.referenceLink = referenceLink;
	}

	public LicenseDTO(License license) {
		this.id = license.getId();
		this.name = license.getName();
		this.acrynosm = license.getAcrynosm();
		this.details = license.getDetails();
		this.referenceLink = license.getReferenceLink();
	}

	public String getReferenceLink() {
		return referenceLink;
	}

	public void setReferenceLink(String referenceLink) {
		this.referenceLink = referenceLink;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcrynosm() {
		return acrynosm;
	}

	public void setAcrynosm(String acrynosm) {
		this.acrynosm = acrynosm;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
