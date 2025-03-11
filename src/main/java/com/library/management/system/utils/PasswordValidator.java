package com.library.management.system.utils;

import java.util.regex.Pattern;

public class PasswordValidator {

	// Regular expression for strong password validation
	private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

	private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

	/**
	 * Validate password strength
	 */
	public static boolean isValidPassword(String password) {
		return password != null && pattern.matcher(password).matches();
	}
}
