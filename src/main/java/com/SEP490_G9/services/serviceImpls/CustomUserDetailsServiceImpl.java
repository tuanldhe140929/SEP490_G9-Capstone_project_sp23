package com.SEP490_G9.services.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.exceptions.AuthRequestException;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.repositories.AccountRepository;
import com.SEP490_G9.repositories.UserRepository;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AccountRepository accountRepository;

	@Transactional
	@Override
	public UserDetailsImpl loadUserByUsername(String name) throws UsernameNotFoundException {
		Account account = null;
		if (accountRepository.existsByEmail(name)) {
			account = accountRepository.findByEmail(name);
		} else {
			throw new AuthRequestException("Email: "+name+" not found.");
		}
		UserDetailsImpl customUserDetail = new UserDetailsImpl();
		customUserDetail.setAccount(account);
		return customUserDetail;
	}

}