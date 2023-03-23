package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.common.Constant;
import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.RefreshToken;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.InternalServerException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.RefreshTokenRepository;
import com.SEP490_G9.repository.RoleRepository;
import com.SEP490_G9.service.AccountService;
import com.SEP490_G9.service.serviceImpls.AccountServiceImpl;

import jakarta.transaction.Transactional;

//@AutoConfigureTestDatabase(replace = Replace.NONE)
//@DataJpaTest
//@RunWith(SpringRunner.class)
//@Import(TestConfig.class)
@SpringBootTest
@Transactional
class AccountServiceTest {

	@Mock
	RoleRepository roleRepo;

	@Mock
	AccountRepository accountRepo;

	@Mock
	RefreshTokenRepository refreshTokenRepo;

	@InjectMocks
	AccountServiceImpl accountServiceImpl;

	@Test
	void testGetAllStaff() {
		Role staffRoleInDB = new Role(2, "ROLE_STAFF");
		when(roleRepo.findById(Constant.STAFF_ROLE_ID)).thenReturn(staffRoleInDB);

		List<Role> staffRole = new ArrayList<>();
		Role role = roleRepo.findById(Constant.STAFF_ROLE_ID);
		staffRole.add(role);

		List<Account> expected = new ArrayList<>();

		Account staffAccount = new Account();
		staffAccount.setRoles(staffRole);
		staffAccount.setId((long) 1);
		expected.add(staffAccount);

		Account staffAccount2 = new Account();
		staffAccount2.setRoles(staffRole);
		staffAccount2.setId((long) 1);
		expected.add(staffAccount2);

		when(accountRepo.findByRolesIn(staffRole)).thenReturn(expected);

		int expectedSize = 1;
		List<Account> result = accountServiceImpl.getAllStaffs();
		assertThat(result).isEqualTo(expected);
		assertThat(result.size()).isEqualTo(2);
	}

	void testGetByRefreshToken() {
		Account expected = new Account();
		expected.setId(1L);

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setId(1L);

		expected.setRefreshToken(refreshToken);
		when(accountRepo.findByRefreshToken(refreshToken)).thenReturn(expected);

		Account result = accountServiceImpl.getByRefreshToken(refreshToken);
		assertThat(result.getId()).isEqualTo(expected.getId());
	}

	void testGetByEmail() {
		String expectedEmail = "seller1@gmail.com";
		Account expected = new Account();
		expected.setId(1L);
		expected.setEmail(expectedEmail);
		when(accountRepo.findByEmail("seller1@gmail.com")).thenReturn(expected);

		Account result = accountServiceImpl.getByEmail(expectedEmail);

		assertThat(result.getEmail()).isEqualTo(expectedEmail);
	}

	@Test
	void testGetById() {
		Long expectedId = 1L;
		Account expected = new Account();
		expected.setId(expectedId);

		when(accountRepo.findById(expectedId).get()).thenReturn(expected);

		Account result = accountRepo.findById(expectedId).get();
		assertThat(result.getId()).isEqualTo(expectedId);
	}

	void testUpdate() {
		Account account = new Account();
		account.setId(1L);
		account.setPassword("asdasdasd");
		when(accountRepo.save(account)).thenReturn(account);

		Account result = accountServiceImpl.update(account);
		assertThat(result.getPassword()).isEqualTo(account.getPassword());
	}
	
	@Test
	void testAddStaff() {
		Account expected = new Account();
		expected.setEmail("expected@gmail.com");
		expected.setPassword("password");
		Account result = accountServiceImpl.addStaff(expected);
		assertEquals(expected, result);
	}
	
	@Test
	void testAddStaffDuplicate() {
		Throwable exception = assertThrows(Exception.class, () -> {
			Account expected = new Account();
			expected.setEmail("user1@gmail.com");
			expected.setPassword("password");
			Account result = accountServiceImpl.addStaff(expected);
		});
		assertEquals(exception.getClass(), DuplicateFieldException.class);
	}
	
	@Test
	void testAddStaffInvalid() {
		Throwable exception = assertThrows(Exception.class, () -> {
			Account expected = new Account();
			expected.setEmail("abcxyz");
			expected.setPassword("password");
			Account result = accountServiceImpl.addStaff(expected);
		});
		//nam sua
		assertEquals(exception.getClass(), InternalServerException.class);
	}
	
	@Test
	void testUpdateStaffStatus() {
		Account result = accountServiceImpl.getById((long)3);
		assertEquals(false, result.isEnabled());
	}
	
}
