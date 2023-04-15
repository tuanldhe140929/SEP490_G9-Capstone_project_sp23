package com.SEP490_G9.service.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.repository.AccountRepository;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AccountRepository accountRepository;

	@Transactional
	@Override
	public UserDetailsImpl loadUserByUsername(String name) throws UsernameNotFoundException {
		Account account = null;
		account = accountRepository.findByEmail(name);
		if(account==null) {
			throw new IllegalAccessError("Wrong credentials");
		}
		UserDetailsImpl customUserDetail = new UserDetailsImpl();
		customUserDetail.setAccount(account);
		return customUserDetail;
	}

}