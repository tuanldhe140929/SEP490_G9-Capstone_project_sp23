package com.SEP490_G9.service;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.User;

public interface ManageAccountInfoService {
	public User getUserInfo();
	public boolean changeAccountPassword(String newPassword, String oldPassword); 
	public User changeAccountInfo(String newUserName, String newFirstName, String newLastName);
	public String uploadProfileImage(MultipartFile coverImage) throws IOException;
	public File serveProfileImage(Long userId);
}
