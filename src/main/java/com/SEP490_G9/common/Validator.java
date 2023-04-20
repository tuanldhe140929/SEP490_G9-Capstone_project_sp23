package com.SEP490_G9.common;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class Validator {
	private static final String SPECIAL_CHARACTERS = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";
	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public boolean validateEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		return pattern.matcher(email).matches();
	}

	public String removeSpareSpaces(String string) {
		return string.trim().replaceAll("\\s+", " ");
	}

	public boolean containsSpecialCharacter(String string) {
		for (int i = 0; i < string.length(); i++) {
			char c = string.charAt(i);
			if (SPECIAL_CHARACTERS.indexOf(c) != -1) {
				return true;
			}
		}
		return false;
	}

	public String trimString(String string) {
		return string.trim();
	}

	public boolean validateLength(String string, int min, int max, boolean nullable) {

		if (nullable && string == null) {
			return true;
		}

		if (!nullable && string == null) {
			return false;
		}

		if (string.length() >= min && string.length() <= max) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validateNumber(int number, int min, int max) {
		if (number >= min && number <= max) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validateNumber(long number, long min, long max) {
		if (number >= min && number <= max) {
			return true;
		} else {
			return false;
		}
	}
}
