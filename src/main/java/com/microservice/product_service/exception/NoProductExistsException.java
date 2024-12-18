package com.microservice.product_service.exception;

public class NoProductExistsException extends RuntimeException {

	private static final long serialVersionUID = 8546716672413535845L;

	private String message;

	public NoProductExistsException() {
		super();
	}

	public NoProductExistsException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
