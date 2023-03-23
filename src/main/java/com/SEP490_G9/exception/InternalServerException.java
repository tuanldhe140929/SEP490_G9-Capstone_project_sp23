package com.SEP490_G9.exception;

public class InternalServerException extends RuntimeException {
	public InternalServerException(String message) {
		super(message);
	}

	public InternalServerException(String message, Throwable cause) {
		super(message, cause);
	}
}
