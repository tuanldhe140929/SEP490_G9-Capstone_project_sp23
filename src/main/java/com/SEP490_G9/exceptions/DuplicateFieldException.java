package com.SEP490_G9.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

public class DuplicateFieldException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fieldName;
	private String fieldValue;
	private ErrorResponse errorResponse;
	public DuplicateFieldException() {
		// TODO Auto-generated constructor stub
	}
	public DuplicateFieldException(String fieldName, String fieldValue) {
		super();
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		setErrorResponse();
	}

	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setErrorResponse(ErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}
	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}
	public void setErrorResponse() {
		String message = String.format("Field: %s with value: %s exist", this.fieldName,this.fieldValue);
		List<String> msgs = new ArrayList<String>();
		msgs.add(message);
		this.errorResponse = new ErrorResponse(msgs,HttpStatus.CONFLICT);
	}
	
	

}
