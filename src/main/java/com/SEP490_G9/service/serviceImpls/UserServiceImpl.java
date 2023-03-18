package com.SEP490_G9.service.serviceImpls;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.ServeMediaService;
import com.SEP490_G9.service.UserService;

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
	String ROOT_LOCATION;

	@Autowired
	ServeMediaService serveMediaService;

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
	public String uploadAvatar(MultipartFile profileImage) throws IOException {
		if (!checkFileType(profileImage, IMAGE_EXTENSIONS)) {
			throw new FileUploadException(profileImage.getContentType() + " file not accept");
		} else {
			User user = getUserById();
			String profileImageLocation = user.getUsername() + "\\profileImageFolder\\";
			File coverImageDir = new File(ROOT_LOCATION + profileImageLocation);
			coverImageDir.mkdirs();
			fileIOService.storeV2(profileImage, profileImageLocation);
			user.setAvatar(profileImageLocation + profileImage.getOriginalFilename());
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
		User user = userRepository.getReferenceById(account.getId());
		return user;
	}

	@Override
	public File serveAvatar(Long userId) {
		User user = userRepository.getReferenceById(userId);

		if (user == null) {
			throw new ResourceNotFoundException("Product id:", userId.toString(), "");
		}

		return serveMediaService.serveImage(ROOT_LOCATION + user.getAvatar());
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

}
