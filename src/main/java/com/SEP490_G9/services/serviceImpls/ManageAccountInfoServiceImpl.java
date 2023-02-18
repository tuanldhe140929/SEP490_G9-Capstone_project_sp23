package com.SEP490_G9.services.serviceImpls;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.exceptions.FileFormatNotAccept;
import com.SEP490_G9.exceptions.ResourceNotFoundException;
import com.SEP490_G9.helpers.StorageProperties;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.FileStorageService;
import com.SEP490_G9.services.ManageAccountInfoService;

@Service
public class ManageAccountInfoServiceImpl implements ManageAccountInfoService {
	final String[] IMAGE_EXTENSIONS = { "image/png", "image/jpeg", "image/svg+xml" };
	@Autowired
	UserRepository userRepo;

	@Autowired
	StorageProperties storageProperties;
	
	@Autowired
	FileStorageService fileStorageService;
	
	@Autowired
	ServeMediaService serveMediaService;
	
	@Override
	public Account getAccountInfo() {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
		return account;
	}

	@Override
	public boolean changeAccountPassword(String newPassword,String oldPassword) {
		Account account= ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		if(encoder.matches(oldPassword, account.getPassword())) {

			String encodedPassword = encoder.encode(newPassword);
			user.setPassword(encodedPassword);
			userRepo.save(user);
		}else {
			return false;
		}	
		return true;
	}

	@Override
	public User changeAccountName(String newName) {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		user.setUsername(newName);
		userRepo.save(user);
		return user;
	}

	@Override
	public String uploadProfileImage(MultipartFile profileImage) throws IOException {
		if (!checkFileType(profileImage, IMAGE_EXTENSIONS)) {
			throw new FileFormatNotAccept(profileImage.getContentType() + " file not accept");
		} else {
			User user = getUserById();
			String profileImageLocation =user.getUsername()+"\\profileImageFolder\\";
			File coverImageDir = new File(storageProperties.getLocation() + profileImageLocation);
			coverImageDir.mkdirs();
			fileStorageService.store(profileImage, profileImageLocation);
			user.setImage(profileImageLocation+ profileImage.getOriginalFilename());
			userRepo.save(user);
			return user.getImage();
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
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();	
		return user;
	}

	@Override
	public File serveProfileImage(Long userId) {
		User user = userRepo.getReferenceById(userId);

		if (user == null) {
			throw new ResourceNotFoundException("Product id:",userId.toString(), "");
		}
		return serveMediaService.serveImage(storageProperties.getLocation() + user.getImage());
	}

}
