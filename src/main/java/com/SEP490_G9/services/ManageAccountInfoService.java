package com.SEP490_G9.services;

import com.SEP490_G9.models.Entities.User;

public interface ManageAccountInfoService {
	public User getAccountInfo();
	public boolean changeAccountPassword(String newPassword, String oldPassword); 
	public boolean changeAccountName(String newName);
}
