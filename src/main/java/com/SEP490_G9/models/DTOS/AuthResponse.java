package com.SEP490_G9.models.DTOS;

import java.io.Serializable;
import java.util.List;

import com.SEP490_G9.models.Entities.Account;

public class AuthResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private String email;
	private String accessToken;
	private List<String> roles;

	public AuthResponse() {
		// TODO Auto-generated constructor stub
	}

	public AuthResponse(String email, String accessToken, List<String> roles) {
		this.email = email;
		this.accessToken = accessToken;
		this.roles = roles;

	}

	public AuthResponse(String email, String accessToken) {
		this.email = email;
		this.accessToken = accessToken;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}