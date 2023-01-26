package com.SEP490_G9.services;

import com.SEP490_G9.models.Entities.User;

public interface ManageAccountInfoService {
	public User getAccountInfo();
	public User changeAccountPassword(String newPassword); 
	public User changeAccountName(String newName);
}
