package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	void test1ACS1() {
		Role staffRoleInDB = new Role(2, "ROLE_STAFF");
		when(roleRepo.findById(Constant.STAFF_ROLE_ID)).thenReturn(staffRoleInDB);

		List<Role> staffRole = new ArrayList<>();
		Role role = roleRepo.findById(Constant.STAFF_ROLE_ID);
		staffRole.add(role);

		List<Role> userRole = new ArrayList<>();
		Role urole = roleRepo.findById(Constant.USER_ROLE_ID);
		userRole.add(urole);
		
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

	@Test
	void test2ACS1() {
		Role staffRoleInDB = new Role(2, "ROLE_STAFF");
		when(roleRepo.findById(Constant.STAFF_ROLE_ID)).thenReturn(staffRoleInDB);

		List<Role> staffRole = new ArrayList<>();
		Role role = roleRepo.findById(Constant.STAFF_ROLE_ID);
		staffRole.add(role);

		List<Role> userRole = new ArrayList<>();
		Role urole = roleRepo.findById(Constant.USER_ROLE_ID);
		userRole.add(urole);
		
		List<Role> adminRole = new ArrayList<>();
		Role arole = roleRepo.findById(Constant.ADMIN_ROLE_ID);
		adminRole.add(arole);
		
		List<Account> expected = new ArrayList<>();

		Account userAccount = new Account();
		userAccount.setRoles(userRole);
		userAccount.setId((long) 1);
		expected.add(userAccount);

		Account adminAccount = new Account();
		adminAccount.setRoles(adminRole);
		adminAccount.setId((long) 2);
		expected.add(adminAccount);

		when(accountRepo.findByRolesIn(staffRole)).thenReturn(expected);

		int expectedSize = 0;
		List<Account> result = accountServiceImpl.getAllStaffs();
		assertThat(result).isEqualTo(expected);
		assertThat(result.size()).isEqualTo(2);
	}
	
	@Test
	void test3ACS1() {
		Role staffRoleInDB = new Role(2, "ROLE_STAFF");
		when(roleRepo.findById(Constant.STAFF_ROLE_ID)).thenReturn(staffRoleInDB);

		List<Role> staffRole = new ArrayList<>();
		Role role = roleRepo.findById(Constant.STAFF_ROLE_ID);
		staffRole.add(role);

		List<Role> userRole = new ArrayList<>();
		Role urole = roleRepo.findById(Constant.USER_ROLE_ID);
		userRole.add(urole);
		
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
	
	@Test
	void test1ACS2() {
		Account expected = new Account();
		expected.setEmail("expected@gmail.com");
		expected.setPassword("password");
		Account result = accountServiceImpl.addStaff(expected);
		assertEquals(expected, result);
	}
	
	@Test
	void test2ACS2() {
		Account expected = new Account();
		expected.setEmail("asdwdaw@gmail.com");
		expected.setPassword("password");
		Account result = accountServiceImpl.addStaff(expected);
		assertEquals(expected, result);
	}
	
	@Test
	void test3ACS2() {
		Account expected = new Account();
		expected.setEmail("morbius221@gmail.com");
		expected.setPassword("password");
		Account result = accountServiceImpl.addStaff(expected);
		assertEquals(expected, result);
	}
	
	@Test
	void test4ACS2() {
		Account a1 = new Account();
		a1.setEmail("user1@gmail.com");
		a1.setPassword("waerwearewww");
		List<Account> accList = new ArrayList<>();
		accList.add(a1);
		
		when(accountRepo.findAll()).thenReturn(accList);
		assertEquals(1, accountServiceImpl.getAllAccounts().size());
	}
	
	@Test
	void test5ACS2() {
		Account a1 = new Account();
		a1.setEmail("beoboe123@gmail.com");
		a1.setPassword("passworwerd");
		List<Account> accList = new ArrayList<>();
		accList.add(a1);
		
		when(accountRepo.findAll()).thenReturn(accList);
		assertEquals(1, accountServiceImpl.getAllAccounts().size());
	}
	
	@Test
	void test6ACS2() {
		Account a1 = new Account();
		a1.setEmail("muaowls@gmail.com");
		a1.setPassword("password231");
		List<Account> accList = new ArrayList<>();
		accList.add(a1);
		
		when(accountRepo.findAll()).thenReturn(accList);
		assertEquals(1, accountServiceImpl.getAllAccounts().size());
	}
	
	@Test
	void test7ACS2() {
		Throwable exception = assertThrows(Exception.class, () -> {
			Account expected = new Account();
			expected.setEmail("abcxyz");
			expected.setPassword("password");
			Account result = accountServiceImpl.addStaff(expected);
		});
		//nam sua
		assertEquals(IllegalArgumentException.class , exception.getClass());
	}
	@Test
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
	@Test
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

		when(accountRepo.findById(anyLong())).thenReturn(Optional.of(expected));

		Account result = accountRepo.findById(expectedId).get();
		assertThat(result.getId()).isEqualTo(expectedId);
	}
	@Test
	void testUpdate() {
		Account account = new Account();
		account.setId(1L);
		account.setPassword("asdasdasd");
		when(accountRepo.save(account)).thenReturn(account);

		Account result = accountServiceImpl.update(account);
		assertThat(result).isEqualTo(account);
	}
	
	
	@Test
	void test1ACS3() {
		Account a1 = new Account();
		a1.setEmail("user134@gmail.com");
		a1.setPassword("waerwearewww");
		List<Account> accList = new ArrayList<>();
		accList.add(a1);
		
		when(accountRepo.findAll()).thenReturn(accList);
		assertEquals(1, accountServiceImpl.getAllAccounts().size());
	}

	@Test
	void test2ACS3() {
		Account a1 = new Account();
		a1.setEmail("godlovegame324@gmail.com");
		a1.setPassword("waerasdwrewww");
		List<Account> accList = new ArrayList<>();
		accList.add(a1);
		
		when(accountRepo.findAll()).thenReturn(accList);
		assertEquals(1, accountServiceImpl.getAllAccounts().size());
	}
	
	
//	@Test
//	void testUpdateStaffStatus() {
//		List<Role> staffRole = new ArrayList<>();
//		Role role = roleRepo.findById(Constant.STAFF_ROLE_ID);
//		staffRole.add(role);
//		
//		Account a1 = new Account();
//		a1.setId(1L);
//		a1.setRoles(staffRole);
//		a1.setEmail("email1@gmail.com");
//		a1.setPassword("password");
//		a1.setEnabled(true);
//		
//		List<Account> accList = new ArrayList<>();
//		accList.add(a1);
//		
//		when(accountRepo.findAll()).thenReturn(accList);
//		
//		Account result = accountServiceImpl.updateStaffStatus(a1.getId());
//		assertEquals(false, result.isEnabled());
//	}
	
}
