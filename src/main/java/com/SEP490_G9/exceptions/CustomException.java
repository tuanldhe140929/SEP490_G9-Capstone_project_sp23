package com.SEP490_G9.exceptions;

public class CustomException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomException() {
		// TODO Auto-generated constructor stub
	}

	public CustomException(String message) {
		super(message);
	}

	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}
}