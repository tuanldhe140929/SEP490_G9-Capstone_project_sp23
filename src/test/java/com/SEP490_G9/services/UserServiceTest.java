package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.serviceImpls.UserServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)

class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@InjectMocks
	UserServiceImpl userServiceImpl;

	@Test
	void testGetById() {
		Long expectedId = (long) 1;
		User expected = new User();
		expected.setId(expectedId);

		when(userRepository.findById(expectedId).get()).thenReturn(expected);
		User result = userRepository.findById(expectedId).get();
		assertThat(result.getId()).isEqualTo(expectedId);
	}
	
	@Test
	void testCreateUser() {
		User u = new User();
		u.setId((long)1);
		when(userRepository.save(any(User.class))).thenReturn(u);
		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		
		User result = userServiceImpl.createUser(u);
		assertThat(result.getId()).isEqualTo((long)1);
	}
	

	@Test
	void testGetByEmail() {
		String expectedEmail = "user1@gmail.com";
		User expected = new User();
		expected.setId((long) 1);
		expected.setEmail(expectedEmail);
		when(userServiceImpl.getByEmail("user1@gmail.com")).thenReturn(expected);

		User result = userServiceImpl.getByEmail(expectedEmail);
		assertThat(result.getEmail()).isEqualTo(expectedEmail);
	}

	@Test
	void testUpdate() {
		User u = new User();
		u.setId(1L);
		u.setPassword("1234");
		when(userRepository.save(u)).thenReturn(u);
		User result = userServiceImpl.update(u);
		assertThat(result.getPassword()).isEqualTo(u.getPassword());
	}

	@Test
	void testgetUserInfo() {
		User u =new User();
		u.setId((long)1);
		u.setUsername("son123");
		when(userServiceImpl.getUserInfo()).thenReturn(u);
		User result = userServiceImpl.getUserInfo();
		assertThat(result.getEmail()).isEqualTo(u.getEmail());
	}
//	@Test
//	void testuploadAvatar() {
//		userServiceImpl.setROOT_LOCATION("1a");
//		User u = new User();
//		u.setId((long)1);
//		MultipartFile avatar = new MockMultipartFile("avatar", "test.png","image/png","test".getBytes());
//		
//		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
//		
//	}
}
