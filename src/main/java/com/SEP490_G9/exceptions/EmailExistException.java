package com.SEP490_G9.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class EmailExistException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email;
	private ErrorResponse errorResponse;
	public EmailExistException() {
		// TODO Auto-generated constructor stub
	}
	public EmailExistException(String email) {
		super();
		this.email = email;
		setErrorResponse();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}
	public void setErrorResponse() {
		String message = String.format("Email: %s exist'", this.email);
		List<String> msgs = new ArrayList<String>();
		msgs.add(message);
		this.errorResponse = new ErrorResponse(msgs,HttpStatus.CONFLICT);
	}
	
	

}
