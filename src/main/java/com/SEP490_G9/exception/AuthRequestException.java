package com.SEP490_G9.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class AuthRequestException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private ErrorResponse errorResponse;

	public AuthRequestException() {
		// TODO Auto-generated constructor stub
	}

	public AuthRequestException(String message) {
		super();
		this.message = message;
		setErrorResponse();
	}

	public String getmessage() {
		return message;
	}

	public void setmessage(String message) {
		this.message = message;
	}

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse() {
		String message = this.message;
		List<String> msgs = new ArrayList<String>();
		msgs.add(message);
		this.errorResponse = new ErrorResponse(msgs, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}