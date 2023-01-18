package com.SEP490_G9.helpers;

import org.springframework.stereotype.Component;


@Component
public class VerifyLinkGenerator {
	final int LINK_LENGHT = 16;
	public String generate() {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";

		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(LINK_LENGHT);

		for (int i = 0; i < LINK_LENGHT; i++) {

			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());

			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}

		return sb.toString();
	}

}
