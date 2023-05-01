package com.SEP490_G9.service.serviceImpls;

import java.io.File;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.common.Validator;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.UserService;
import com.paypal.base.rest.PayPalRESTException;

import io.netty.handler.codec.http.HttpResponse;
import jakarta.validation.Valid;

@Service
public class UserServiceImpl implements UserService {

	final String[] IMAGE_EXTENSIONS = { "image/png", "image/jpeg", "image/svg+xml" };

	@Autowired
	UserRepository userRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	FileIOService fileIOService;

	@Value("${root.location}")
	private String ROOT_LOCATION;

	@Autowired
	PaypalServiceImpl paypalimpl;

	@Autowired
	Validator validator = new Validator();

	@Override
	public User getById(Long userId) {
		User user = userRepository.findById(userId).get();
		if (user == null) {
			throw new ResourceNotFoundException("Product id:", userId.toString(), "");
		}
		return user;
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
	public User update(User user) {
		user.setEmail(validator.removeSpareSpaces(user.getEmail().trim()));
		user.setFirstName(validator.removeSpareSpaces(user.getFirstName().trim()));
		user.setLastName(validator.removeSpareSpaces(user.getLastName().trim()));
		user.setUsername(validator.removeSpareSpaces(user.getUsername().trim()));
		
		if (!validator.validateEmail(user.getEmail())) {
			throw new IllegalArgumentException();
		}
		if (!validator.validateLength(user.getEmail(), 1, 320, false)) {
			throw new IllegalArgumentException();
		}

		if (!validator.validateLength(user.getFirstName(), 0, 255, true)) {
			throw new IllegalArgumentException();
		}

		if (!validator.validateLength(user.getLastName(), 0, 255, true)) {
			throw new IllegalArgumentException();
		}

		if (!validator.validateLength(user.getUsername(), 3, 30, false)
				|| validator.containsSpecialCharacter(user.getUsername())) {
			throw new IllegalArgumentException();
		}

		return userRepository.save(user);
	}

	public User getUserInfo() {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		User user = userRepository.findById(account.getId()).get();
		return user;

	}

	@Override
	public boolean changePassword(String newPassword, String oldPassword) {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if (encoder.matches(oldPassword, account.getPassword())) {
			String encodedPassword = encoder.encode(newPassword);
			account.setPassword(encodedPassword);
			accountRepository.save(account);
		} else {
			return false;
		}
		return true;
	}

	@Override
	public String uploadAvatar(MultipartFile profileImage) {
		if (!checkFileType(profileImage, IMAGE_EXTENSIONS)) {
			throw new FileUploadException(profileImage.getContentType() + " file not accept");
		} else {
			
			if(!validator.validateLength(profileImage.getOriginalFilename(), 1, 100, false)) {
				throw new IllegalArgumentException();
			}
			if(!validator.validateNumber(profileImage.getSize(), 1, 1024*1024*20)) {
				throw new IllegalArgumentException();
			}
	
			User user = getUserById();
			String profileImageLocation = "account_id_" + user.getId() + "/profile";
			File coverImageDir = new File(ROOT_LOCATION + profileImageLocation);
			coverImageDir.mkdirs();
			String storedPath = fileIOService.storeV2(profileImage, ROOT_LOCATION + profileImageLocation);
			user.setAvatar(storedPath.replace(ROOT_LOCATION, ""));
			userRepository.save(user);
			return user.getAvatar();
		}
	}

	private boolean checkFileType(MultipartFile file, String... extensions) {
		for (String extension : extensions) {
			if (file.getContentType().endsWith(extension)) {
				return true;
			}
		}
		return false;
	}

	private User getUserById() {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		User user = userRepository.findById(account.getId()).orElseThrow();
		return user;
	}

	@Override
	public User updateUser(String newUserName, String newFirstName, String newLastName) {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		User user = userRepository.findById(account.getId()).get();
		user.setFirstName(newFirstName);
		user.setUsername(newUserName);
		user.setLastName(newLastName);
		userRepository.save(user);
		return user;
	}

	public String getROOT_LOCATION() {
		return ROOT_LOCATION;
	}

	public void setROOT_LOCATION(String rOOT_LOCATION) {
		ROOT_LOCATION = rOOT_LOCATION;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> allUsers = userRepository.findAll();
		return allUsers;
	}

}
