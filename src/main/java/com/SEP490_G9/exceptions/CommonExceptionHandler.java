package com.SEP490_G9.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
public class CommonExceptionHandler {
	
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse resolveException(MethodArgumentNotValidException ex) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<String> messages = new ArrayList<>(fieldErrors.size());
		for (FieldError error : fieldErrors) {
			messages.add(error.getField() + " - " + error.getDefaultMessage());
		}
		return new ErrorResponse(messages,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse resolveException(MethodArgumentTypeMismatchException ex) {
		String message = "Parameter '" + ex.getParameter().getParameterName() + "' must be '"
				+ Objects.requireNonNull(ex.getRequiredType()).getSimpleName() + "'";
		List<String> messages = new ArrayList<>(1);
		messages.add(message);
		return new ErrorResponse(messages, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse resolveException(ResourceNotFoundException exception) {
		ErrorResponse errorResponse = exception.getErrorResponse();
		return errorResponse;
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse resolveException(UsernameNotFoundException exception) {
		List<String> msgs = new ArrayList<>();
		msgs.add(exception.getMessage());
		return new ErrorResponse(msgs, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(EmailExistException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse resolveException(EmailExistException exception) {
		ErrorResponse errorResponse = exception.getErrorResponse();
		return errorResponse;
	}
	
	@ExceptionHandler(RefreshTokenException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse resolveException(RefreshTokenException exception) {
		ErrorResponse errorResponse = exception.getErrorResponse();
		return errorResponse;
	}
	
	
	@ExceptionHandler(EmailServiceException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse resolveException(EmailServiceException exception) {
		ErrorResponse errorResponse = exception.getErrorResponse();
		return errorResponse;
	}
//	@ExceptionHandler({ CustomException.class })
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	public ErrorResponse customExceptionHandle(CustomException ex) {
//		return new ErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST);
//	}
//	
//	@ExceptionHandler({ NullPointerException.class })
//	@ResponseStatus(HttpStatus.NOT_FOUND)
//	public ErrorResponse customExceptionHandle(NullPointerException ex) {
//		return new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND);
//	}
//	
//	@ExceptionHandler({ConstraintViolationException.class})
//	@ResponseStatus(HttpStatus.CONFLICT)
//	public ErrorResponse constrainViolation(ConstraintViolationException ex) {
//		return new ErrorResponse(ex.getMessage(),HttpStatus.CONFLICT);
//	}
}
