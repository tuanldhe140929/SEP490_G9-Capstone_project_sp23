package com.SEP490_G9.services.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.SEP490_G9.models.DTOS.User;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.repositories.UserRepository;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Transactional
	@Override
	public UserDetailsImpl loadUserByUsername(String name) throws DataAccessException {
		User domainUser = userRepository.findByEmail(name);
		if(domainUser==null) {
			throw new UsernameNotFoundException("Not found user with email: "+name);
		}
		UserDetailsImpl customUserDetail = new UserDetailsImpl();
		customUserDetail.setUser(domainUser);
		return customUserDetail;
	}

}