package com.SEP490_G9.service;

import com.SEP490_G9.entity.User;

public interface GoogleAuthService {
	public User getUserInfo(final String code);
}
