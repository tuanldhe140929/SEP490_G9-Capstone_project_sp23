package com.SEP490_G9.service.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.User;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.UserService;

import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public User getById(Long userId) {
		User user = userRepository.findById(userId).get();
		if (user == null) {
			throw new ResourceNotFoundException("Product id:", userId.toString(), "");
		}
		return user;
	}

	@Override
	public User createUser(@Valid User user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new DuplicateFieldException("email", user.getEmail());
		}
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new DuplicateFieldException("username", user.getUsername());
		}

		User saved = userRepository.save(user);
		return saved;
	}

	@Override
	public User getByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new ResourceNotFoundException("User", "email", email);
		}
		return user;
	}

	@Override
	public User update(User user) {
		return userRepository.save(user);
	}

}
