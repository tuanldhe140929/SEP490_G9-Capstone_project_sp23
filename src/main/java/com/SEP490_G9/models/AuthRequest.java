package com.SEP490_G9.models;

import java.io.Serializable;
import jakarta.validation.constraints.*;

public class AuthRequest implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Email(message = "invalid format")
	@NotBlank(message = "email can't be blank")
	@Size(min = 5, max = 100)
	private String email;
	
	@NotBlank(message = "email can't be blank")
	@Size(min = 8, max = 100)
	private String password;
	
	private boolean rememberMe;

	public AuthRequest() {
		// TODO Auto-generated constructor stub
	}

	public AuthRequest(String email, String password, boolean rememberMe) {
		super();
		this.email = email;
		this.password = password;
		this.rememberMe = rememberMe;
	}

	public AuthRequest(String email) {
		super();
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	
}
