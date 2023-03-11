package com.SEP490_G9.service;

import java.util.List;


import com.SEP490_G9.dto.AuthRequest;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.RefreshToken;
public interface AccountService {

	Account getByRefreshToken(RefreshToken findByToken);

	Account getById(Long id);

	Account getByEmail(String email);

	Account update(Account account);

	List<Account> getAllStaffs();

	boolean addStaff(Account staff);

	boolean updateStaffStatus(Long id);

	String resetPassword(Account account);
}
