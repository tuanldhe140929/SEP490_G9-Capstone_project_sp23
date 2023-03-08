package com.SEP490_G9.service;

import com.SEP490_G9.entity.User;

import jakarta.validation.Valid;

public interface UserService {
	User getById(Long userId);

	User createUser(@Valid User user);

	User getByEmail(String email);

	User update(User user);
	
	
}
