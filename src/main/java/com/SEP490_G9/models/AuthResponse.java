package com.SEP490_G9.models;

import java.io.Serializable;
import java.util.List;

public class AuthResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String email;
	private String accessToken;
	private String role;
 public AuthResponse() {
	// TODO Auto-generated constructor stub
}
	public AuthResponse(String email, String accessToken, String role) {
		this.email = email;
		this.accessToken = accessToken;
		this.role = role;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}



}