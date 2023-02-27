package com.SEP490_G9.service;

import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.RefreshToken;

public interface AccountService {

	Account getByRefreshToken(RefreshToken findByToken);

	Account getById(Long id);

	Account getByEmail(String email);

	Account update(Account account);

}
