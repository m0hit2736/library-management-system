package com.library.management.system.exception;

public class InvalidCredsException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidCredsException(String message) {
		super(message);
	}
}
