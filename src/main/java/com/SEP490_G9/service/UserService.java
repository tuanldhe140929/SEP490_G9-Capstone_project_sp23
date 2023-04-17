package com.SEP490_G9.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.entities.User;

import jakarta.validation.Valid;

public interface UserService {
	User getById(Long userId);

	User createUser(@Valid User user);

	User getByEmail(String email);

	User update(User user);

	public User getUserInfo();

	public boolean changePassword(String newPassword, String oldPassword);

	public User updateUser(String newUserName, String newFirstName, String newLastName);

	public String uploadAvatar(MultipartFile coverImage);


	List<User> getAllUsers();

}
