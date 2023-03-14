package com.SEP490_G9.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.SEP490_G9.exception.FileUploadException;

@Component
public class Md5Hash {
	@Value("${secret.key}")
	private String SECRET_KEY;

	public String generateToken(long userId, long productId) {
		String tokenData = userId + "-" + productId + "-" + SECRET_KEY;

		String token = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(tokenData.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			token = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return token;
	}

	public boolean validateToken(long userId, long productId, String token) {
		String generatedToken = generateToken(userId, productId);
		return generatedToken.equals(token);
	}
}
