package com.SEP490_G9.service.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.RefreshToken;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository accountRepo;
	
	@Override
	public Account getByRefreshToken(RefreshToken findByToken) {
		Account ret = accountRepo.findByRefreshToken(findByToken);
		if(ret==null) {
			throw new ResourceNotFoundException("account", "refresh token", findByToken.getToken());
		}
		return ret;
	}

	@Override
	public Account getById(Long id) {
		Account ret = accountRepo.findById(id).get();
		if(ret==null) {
			throw new ResourceNotFoundException("account", "id", id);
		}
		return ret;
	}

	@Override
	public Account getByEmail(String email) {
		Account ret = accountRepo.findByEmail(email);
		if(ret==null) {
			throw new ResourceNotFoundException("account", "email", email);
		}
		return ret;
	}

	@Override
	public Account update(Account account) {
		Account saved = accountRepo.save(account);
		return saved;
	}

}
