package com.SEP490_G9.service;

import com.SEP490_G9.entities.User;

public interface GoogleAuthService {
	public User getUserInfo(final String code);
	public String getToken(final String code);
}
