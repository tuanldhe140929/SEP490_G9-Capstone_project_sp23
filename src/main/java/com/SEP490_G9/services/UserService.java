package com.SEP490_G9.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.models.User;

public interface UserService {
	public User getUserInfo(String email);
}
