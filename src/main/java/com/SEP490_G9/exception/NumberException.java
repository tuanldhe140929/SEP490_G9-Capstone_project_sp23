package com.SEP490_G9.exception;

public class NumberException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public NumberException(String message) {
		super(message);
	}

	public NumberException(String message, Throwable cause) {
		super(message, cause);
	}
}
