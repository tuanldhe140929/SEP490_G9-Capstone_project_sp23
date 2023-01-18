package com.SEP490_G9.exceptions;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class ErrorResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String message;
	//private final Throwable throwable;
	private final HttpStatus httpStatus;
	//private final ZonedDateTime timestamp;
	 
	
	public ErrorResponse(String message, HttpStatus httpStatus) {
		super();
		this.message = message;
		this.httpStatus = httpStatus;

	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	
}