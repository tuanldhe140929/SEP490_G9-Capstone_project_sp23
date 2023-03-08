package com.SEP490_G9.dto;

public class EmailResponse {
	private String email;
	private String link;

	public EmailResponse() {
		// TODO Auto-generated constructor stub
	}

	public EmailResponse(String email, String link) {
		this.email = email;
		this.link = link;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVerifyLink() {
		return link;
	}

	public void setVerifyLink(String verifyLink) {
		this.link = verifyLink;
	}
}
