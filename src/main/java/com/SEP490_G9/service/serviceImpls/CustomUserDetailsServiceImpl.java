package com.SEP490_G9.service.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.UserDetailsImpl;
import com.SEP490_G9.exception.AuthRequestException;
import com.SEP490_G9.repository.AccountRepository;


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