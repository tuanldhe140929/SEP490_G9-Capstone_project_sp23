package com.SEP490_G9.exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;

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
		System.out.println("Method argument");
		return new ErrorResponse(messages, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse resolveException(MethodArgumentTypeMismatchException ex) {
		String message = "Parameter '" + ex.getParameter().getParameterName() + "' must be '"
				+ Objects.requireNonNull(ex.getRequiredType()).getSimpleName() + "'";
		List<String> messages = new ArrayList<>(1);
		messages.add(message);
		System.out.println("Method argument mismatch");
		return new ErrorResponse(messages, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse resolveException(ResourceNotFoundException exception) {
		ErrorResponse errorResponse = exception.getErrorResponse();
		System.out.println("Resource not found");
		return errorResponse;
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse resolveException(UsernameNotFoundException exception) {
		List<String> msgs = new ArrayList<>();
		msgs.add(exception.getMessage());
		return new ErrorResponse(msgs, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateFieldException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse resolveException(DuplicateFieldException exception) {
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

	@ExceptionHandler(AuthRequestException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse resolveException(AuthRequestException exception) {
		System.out.println("auth request");
		ErrorResponse errorResponse = exception.getErrorResponse();
		return errorResponse;
	}

	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse resolveException(AuthenticationException exception) {
		List<String> msgs = new ArrayList<>();
		msgs.add(exception.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(msgs, HttpStatus.INTERNAL_SERVER_ERROR);
		return errorResponse;
	}

	@ExceptionHandler(UserPrincipalNotFoundException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorResponse resolveException(UserPrincipalNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		List<String> msgs = new ArrayList<>();
		msgs.add(ex.getMessage());
		errorResponse.setMessages(msgs);
		errorResponse.setStatus(HttpStatus.FORBIDDEN);
		return errorResponse;
	}

	@ExceptionHandler(AccessDeniedException.class)
	public final ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		List<String> msgs = new ArrayList<>();
		msgs.add(ex.getMessage());
		errorResponse.setMessages(msgs);
		errorResponse.setStatus(HttpStatus.FORBIDDEN);
		return errorResponse;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public final ErrorResponse handleAccessDeniedException(ConstraintViolationException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		List<String> msgs = new ArrayList<>();
		msgs.add(ex.getMessage());
		errorResponse.setMessages(msgs);
		errorResponse.setStatus(HttpStatus.FORBIDDEN);
		return errorResponse;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		List<String> msgs = new ArrayList<>();
		msgs.add(ex.getMessage());
		errorResponse.setMessages(msgs);
		errorResponse.setStatus(HttpStatus.BAD_REQUEST);
		return errorResponse;
	}

	@ExceptionHandler(HttpMessageNotWritableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final ErrorResponse handleHttpMessageNotReadableException(HttpMessageNotWritableException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		List<String> msgs = new ArrayList<>();
		msgs.add(ex.getMessage());
		errorResponse.setMessages(msgs);
		errorResponse.setStatus(HttpStatus.BAD_REQUEST);
		return errorResponse;
	}

	@ExceptionHandler(FileNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public final ErrorResponse handleHttpMessageNotReadableException(FileNotFoundException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		List<String> msgs = new ArrayList<>();
		msgs.add(ex.getMessage());
		errorResponse.setMessages(msgs);
		errorResponse.setStatus(HttpStatus.NOT_FOUND);
		return errorResponse;
	}

	@ExceptionHandler(IOException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public final ErrorResponse handleHttpMessageNotReadableException(IOException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		List<String> msgs = new ArrayList<>();
		msgs.add(ex.getMessage());
		errorResponse.setMessages(msgs);
		errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		return errorResponse;
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final ErrorResponse exceptionResolve(MissingServletRequestParameterException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		List<String> msgs = new ArrayList<>();
		msgs.add(ex.getMessage());
		errorResponse.setMessages(msgs);
		errorResponse.setStatus(HttpStatus.BAD_REQUEST);
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
