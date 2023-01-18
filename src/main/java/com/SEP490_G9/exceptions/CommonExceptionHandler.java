package com.SEP490_G9.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;


@RestControllerAdvice
public class CommonExceptionHandler {
	
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleNotFoundException(MethodArgumentNotValidException ex) {
		return new ErrorResponse("Parameter not valid",HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ CustomException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse customExceptionHandle(CustomException ex) {
		return new ErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({ NullPointerException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse customExceptionHandle(NullPointerException ex) {
		return new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({ConstraintViolationException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse constrainViolation(ConstraintViolationException ex) {
		return new ErrorResponse(ex.getMessage(),HttpStatus.CONFLICT);
	}
}
