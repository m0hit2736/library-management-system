package com.library.management.system.exception;

public class WeakPasswordException extends Exception {

	private static final long serialVersionUID = 1L;

	public WeakPasswordException(String message) {
		super(message);
	}
}
