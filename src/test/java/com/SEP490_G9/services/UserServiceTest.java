package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.serviceImpls.CustomUserDetailsServiceImpl;
import com.SEP490_G9.service.serviceImpls.FileIOServiceImpl;
import com.SEP490_G9.service.serviceImpls.UserServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@InjectMocks
	UserServiceImpl userService;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private FileIOServiceImpl fileIOService;

	@Test
	void testGetByEmail_1() {
		String expectedEmail = "johndoe@gmail.com";
		User expected = new User();
		expected.setId((long) 1);
		expected.setEmail(expectedEmail);
		when(userRepository.findByEmail("johndoe@gmail.com")).thenReturn(expected);

		User result = userService.getByEmail(expectedEmail);
		assertThat(result.getEmail()).isEqualTo(expectedEmail);
	}

	@Test
	void testGetByEmail_2() {
		String expectedEmail = "johndoe@gmail.com";
		User expected = new User();
		expected.setId((long) 1);
		expected.setEmail(expectedEmail);
		when(userRepository.findByEmail("johndoe@gmail.com")).thenReturn(null);
		assertThrows(ResourceNotFoundException.class, () -> {
			userService.getByEmail(expectedEmail);
		});
	}

	@Test
	public void testCreateUser_1() {
		User existingUser = new User();
		existingUser.setEmail("johndoe@gmail.com");
		when(userRepository.existsByEmail(existingUser.getEmail())).thenReturn(true);

		assertThrows(DuplicateFieldException.class, () -> {
			userService.createUser(existingUser);
		});
	}

	@Test
	public void testCreateUser_2() {
		User existingUser = new User();
		existingUser.setUsername("johndoe@gmail.com");
		when(userRepository.existsByUsername(existingUser.getUsername())).thenReturn(true);

		assertThrows(DuplicateFieldException.class, () -> {
			userService.createUser(existingUser);
		});
	}

	@Test
	public void testCreateUser_3() {
		User newUser = new User();
		newUser.setUsername("johndoe");
		newUser.setEmail("johndoe@gmail.com");
		when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(false);
		when(userRepository.existsByUsername(newUser.getUsername())).thenReturn(false);
		when(userRepository.save(newUser)).thenReturn(newUser);

		User savedUser = userService.createUser(newUser);

		assertNotNull(savedUser);
		assertEquals(newUser, savedUser);
	}

	@Test
	public void testCreateUser_4() {
		User newUser = new User();
		newUser.setUsername("johndoe");
		newUser.setEmail("johndoe@gmail.com");
		when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(false);
		when(userRepository.existsByUsername(newUser.getUsername())).thenReturn(false);
		when(userRepository.save(newUser)).thenReturn(newUser);

		User savedUser = userService.createUser(newUser);

		assertNotNull(savedUser);
		assertEquals(newUser, savedUser);
	}

	@Test
	void testgetUserInfo_1() {
		Account account = new Account();
		account.setId(1L);
		account.setEmail("johndoe@gmail.com");
		account.setPassword("johndoe");
		User user = new User(account);

		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		User result = userService.getUserInfo();
		assertNotNull(result);

	}

	@Test
	void testgetUserInfo_2() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		account.setPassword("johndoe");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(null);

		assertThrows(NoSuchElementException.class, () -> {
			userService.getUserInfo();
		});

	}

	@Test
	public void testChangePassword_1() {
		String oldPassword = "user1234";
		String newPassword = "user54321";

		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		account.setPassword(new BCryptPasswordEncoder().encode(oldPassword));
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		Mockito.when(accountRepository.save(account)).thenReturn(account);
		boolean result = userService.changePassword(newPassword, oldPassword);

		assertTrue(result);
		Mockito.verify(accountRepository, Mockito.times(1)).save(account);
	}

	@Test
	public void testChangePassword_2() {
		String falseOldPassword = "user54321";
		String oldPassword = "user1234";
		String newPassword = "user12345";

		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		account.setPassword(new BCryptPasswordEncoder().encode(oldPassword));
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		Mockito.when(accountRepository.save(account)).thenReturn(account);
		boolean result = userService.changePassword(newPassword, falseOldPassword);

		assertTrue(!result);
	}

	@Test
	public void testChangePassword_3() {
		String falseOldPassword = "user";
		String oldPassword = "user1234";
		String newPassword = "newpassword";

		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		account.setPassword(new BCryptPasswordEncoder().encode(oldPassword));
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		Mockito.when(accountRepository.save(account)).thenReturn(account);
		boolean result = userService.changePassword(newPassword, falseOldPassword);

		assertTrue(!result);

	}

	@Test
	public void testChangePassword_4() {
		String falseOldPassword = "user";
		String oldPassword = "user1234";
		String newPassword = "user54321";

		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		account.setPassword(new BCryptPasswordEncoder().encode(oldPassword));
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		Mockito.when(accountRepository.save(account)).thenReturn(account);
		boolean result = userService.changePassword(newPassword, falseOldPassword);

		assertTrue(!result);

	}

	@Test
	public void testChangePassword_5() {
		String falseOldPassword = "user";
		String oldPassword = "user1234";
		String newPassword = "user54321";

		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		account.setPassword(new BCryptPasswordEncoder().encode(oldPassword));
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		Mockito.when(accountRepository.save(account)).thenReturn(account);
		boolean result = userService.changePassword(newPassword, falseOldPassword);

		assertTrue(!result);

	}

	@Test
	public void testChangePassword_6() {
		String falseOldPassword = "user";
		String oldPassword = "user1234";
		String newPassword = "user54321";

		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		account.setPassword(new BCryptPasswordEncoder().encode(oldPassword));
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		Mockito.when(accountRepository.save(account)).thenReturn(account);
		boolean result = userService.changePassword(newPassword, falseOldPassword);

		assertTrue(!result);

	}

	@Test
	void testUpdate_1() {
		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName("John");
		expected.setLastName("Doe");
		expected.setUsername(" ");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		assertThrows(IllegalArgumentException.class, () -> {
			userService.update(expected);
		});
	}

	@Test
	void testUpdate_2() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName("Johnny");
		expected.setLastName("Tayomaa");
		expected.setUsername("username12345");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		User result = userService.update(expected);
		assertNotNull(result);
	}

	@Test
	void testUpdate_3() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName("Johnny");
		expected.setLastName("Tayomaa");
		expected.setUsername("username123456789000000000000000000");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		assertThrows(IllegalArgumentException.class, () -> {
			userService.update(expected);
		});
	}

	@Test
	void testUpdate_4() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName("Johnny4*/-124.");
		expected.setLastName("Tayoma*$%U*");
		expected.setUsername("user.*/-4890");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		assertThrows(IllegalArgumentException.class, () -> {
			userService.update(expected);
		});
	}

	@Test
	void testUpdate_5() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName(" ");
		expected.setLastName("Tayomaa");
		expected.setUsername("username12345");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		User result = userService.update(expected);
		assertNotNull(result);
	}

	@Test
	void testUpdate_6() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName("Johnnnyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
		expected.setLastName("TaYOMAAAAAAAAAAAAAAAAAAAAa");
		expected.setUsername(" ");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		assertThrows(IllegalArgumentException.class, () -> {
			userService.update(expected);
		});
	}

	@Test
	void testUpdate_7() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName("Johnnnyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
		expected.setLastName("TaYOMAAAAAAAAAAAAAAAAAAAAa");
		expected.setUsername("user.*/-4890");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		assertThrows(IllegalArgumentException.class, () -> {
			userService.update(expected);
		});
	}

	@Test
	void testUpdate_8() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName("Johnnny");
		expected.setLastName("Tayomaa");
		expected.setUsername(" ");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		assertThrows(IllegalArgumentException.class, () -> {
			userService.update(expected);
		});
	}

	@Test
	void testUpdate_9() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName("Johnny");
		expected.setLastName("Tayomaa");
		expected.setUsername("username12345");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		User result = userService.update(expected);
		assertNotNull(result);
	}

	@Test
	void testUpdate_10() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName("Johnny");
		expected.setLastName("Tayomaa");
		expected.setUsername("username123456789000000000000000000");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		when(userRepository.save(expected)).thenReturn(expected);
		assertThrows(IllegalArgumentException.class, () -> {
			userService.update(expected);
		});
	}

	@Test
	void testUpdate_11() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName("Johnny");
		expected.setLastName("Tayomaa");
		expected.setUsername("user.*/-4890");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		assertThrows(IllegalArgumentException.class, () -> {
			userService.update(expected);
		});
	}

	@Test
	void testUpdate_12() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName(" ");
		expected.setLastName(" ");
		expected.setUsername("username12345");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		User result = userService.update(expected);
		assertNotNull(result);
	}

	@Test
	void testUpdate_13() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("12345678");
		expected.setFirstName("Johnnnyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
		expected.setLastName("TaYOMAAAAAAAAAAAAAAAAAAAAa");
		expected.setUsername(" ");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		assertThrows(IllegalArgumentException.class, () -> {
			userService.update(expected);
		});
	}

	@Test
	void testUpdate_14() {

		User expected = new User();
		expected.setId(1L);
		expected.setPassword("1234");
		expected.setFirstName("Johnn");
		expected.setLastName("Tayoma*$%U*");
		expected.setUsername("user.*/-4890");
		expected.setEmail("johndoe@gmail.com");

		when(userRepository.save(expected)).thenReturn(expected);
		assertThrows(IllegalArgumentException.class, () -> {
			userService.update(expected);
		});
	}

	@Test
	void testuploadAvatar_1() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 10];
		MultipartFile avatar = new MockMultipartFile("avatar", "test.png", "image/png", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		String result = userService.uploadAvatar(avatar);
		assertNotNull(result);
	}

	@Test
	void testuploadAvatar_6() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 10];
		MultipartFile avatar = new MockMultipartFile("avatar",
				"111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
				"image/png", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		assertThrows(IllegalArgumentException.class, () -> {
			userService.uploadAvatar(avatar);
		});
	}

	@Test
	void testuploadAvatar_2() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 20];
		MultipartFile avatar = new MockMultipartFile("avatar", "1", "image/jpeg", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		String result = userService.uploadAvatar(avatar);
		assertNotNull(result);
	}

	@Test
	void testuploadAvatar_3() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 20];
		MultipartFile avatar = new MockMultipartFile("avatar", "11111111111", "image/svg+xml", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		String result = userService.uploadAvatar(avatar);
		assertNotNull(result);
	}

	@Test
	void testuploadAvatar_4() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1];
		MultipartFile avatar = new MockMultipartFile("avatar", "11111111111", "image/jpeg", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		String result = userService.uploadAvatar(avatar);
		assertNotNull(result);
	}

	@Test
	void testuploadAvatar_5() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[0];
		MultipartFile avatar = new MockMultipartFile("avatar", "11111111111", "image/jpeg", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		assertThrows(IllegalArgumentException.class, () -> {
			userService.uploadAvatar(avatar);
		});
	}

	@Test
	void testuploadAvatar_7() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 20];
		MultipartFile avatar = new MockMultipartFile("avatar", "11111111111", "video/mov", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		assertThrows(FileUploadException.class, () -> {
			userService.uploadAvatar(avatar);
		});
	}

	@Test
	void testuploadAvatar_8() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 20];
		MultipartFile avatar = new MockMultipartFile("avatar", "1111111111111111", "image/png", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		String result = userService.uploadAvatar(avatar);
		assertNotNull(result);
	}

	@Test
	void testuploadAvatar_9() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 20];
		MultipartFile avatar = new MockMultipartFile("avatar", "11111111111111111111111111111111111111111", "image/png",
				bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		String result = userService.uploadAvatar(avatar);
		assertNotNull(result);
	}

	@Test
	void testuploadAvatar_10() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 20];
		MultipartFile avatar = new MockMultipartFile("avatar", "1111111111111111111111111", "image/png", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		String result = userService.uploadAvatar(avatar);
		assertNotNull(result);
	}

	@Test
	void testuploadAvatar_11() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 20];
		MultipartFile avatar = new MockMultipartFile("avatar", "1111111111111", "image/jpeg", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		String result = userService.uploadAvatar(avatar);
		assertNotNull(result);
	}

	@Test
	void testuploadAvatar_12() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 20];
		MultipartFile avatar = new MockMultipartFile("avatar", "1111111111111", "image/jpeg", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		String result = userService.uploadAvatar(avatar);
		assertNotNull(result);
	}

	@Test
	void testuploadAvatar_13() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 20];
		MultipartFile avatar = new MockMultipartFile("avatar", "1111111111111", "image/jpeg", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		String result = userService.uploadAvatar(avatar);
		assertNotNull(result);
	}

	@Test
	void testuploadAvatar_14() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

		userService.setROOT_LOCATION("C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\");
		byte[] bytes = new byte[1024 * 1024 * 20];
		MultipartFile avatar = new MockMultipartFile("avatar",
				"11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
				"image/jpeg", bytes);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		assertThrows(IllegalArgumentException.class, () -> {
			userService.uploadAvatar(avatar);
		});
	}
}
