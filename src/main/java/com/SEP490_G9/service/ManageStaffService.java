package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entity.Account;

public interface ManageStaffService {
	public List<Account> getAllStaffs();
	
	public boolean addStaff(Account staff);
	
	public boolean updateStaffStatus(Long id);

}
