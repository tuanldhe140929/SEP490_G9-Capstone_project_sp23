package com.SEP490_G9.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.models.DTOS.User;

public interface ManageAccountInfoService {
	public User getUserInfo(String email);
}
