//package com.SEP490_G9.exception;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//
//public class RefreshTokenException extends RuntimeException {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	private String token;
//	private String message;
//	private ErrorResponse errorResponse;
//
//	public RefreshTokenException() {
//		// TODO Auto-generated constructor stub
//	}
//
//	public RefreshTokenException(String token,String message) {
//		super();
//		this.token = token;
//		this.message = message;
//		setErrorResponse();
//	}
//
//	public String getmessage() {
//		return message;
//	}
//
//	public void setmessage(String message) {
//		this.message = message;
//	}
//
//	public ErrorResponse getErrorResponse() {
//		return errorResponse;
//	}
//
//	public void setErrorResponse() {
//		String message = String.format("Token %s %s",this.token, this.message);
//		List<String> msgs = new ArrayList<String>();
//		msgs.add(message);
//		this.errorResponse = new ErrorResponse(msgs, HttpStatus.BAD_REQUEST);
//	}
//
//}