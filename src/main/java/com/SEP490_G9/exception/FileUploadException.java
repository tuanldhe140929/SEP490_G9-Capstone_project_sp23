package com.SEP490_G9.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class FileUploadException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private ErrorResponse errorResponse;
	public FileUploadException() {
		super();
	}
	public FileUploadException(String message) {
		super();
		this.message = message;
		setErrorResponse();
	}
	private void setErrorResponse() {
		String message = this.message;
		List<String> msgs = new ArrayList<String>();
		msgs.add(message);
		this.errorResponse = new ErrorResponse(msgs, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}
	public void setErrorResponse(ErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
