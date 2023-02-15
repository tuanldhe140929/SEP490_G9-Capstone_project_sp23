package com.SEP490_G9.services;

import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.User;

public interface ManageAccountInfoService {
	public Account getAccountInfo();
	public boolean changeAccountPassword(String newPassword, String oldPassword); 
	public User changeAccountName(String newName);
}
