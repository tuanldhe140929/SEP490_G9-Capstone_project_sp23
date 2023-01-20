package com.SEP490_G9.exceptions;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ErrorResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpStatus status;
	private List<String> messages;
	private Instant timestamp;
	 
public ErrorResponse() {
	// TODO Auto-generated constructor stub
}

public ErrorResponse(List<String> messages, HttpStatus status) {
	super();

	this.status = status;
	this.messages = messages;
	this.timestamp = Instant.now();
}


public HttpStatus getStatus() {
	return status;
}

public void setStatus(HttpStatus status) {
	this.status = status;
}

public List<String> getMessages() {
	return messages;
}

public void setMessages(List<String> messages) {
	this.messages = messages;
}

public Instant getTimestamp() {
	return timestamp;
}

public void setTimestamp(Instant timestamp) {
	this.timestamp = timestamp;
}

	
}