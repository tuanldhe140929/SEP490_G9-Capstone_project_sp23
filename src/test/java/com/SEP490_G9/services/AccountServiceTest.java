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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.common.Constant;
import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.RoleRepository;
import com.SEP490_G9.service.AccountService;
import com.SEP490_G9.service.serviceImpls.AccountServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class AccountServiceTest {
	
	@Mock
	RoleRepository roleRepo;
	
	@Mock
	AccountRepository accountRepo;
	
	@InjectMocks
	AccountServiceImpl accountServiceImpl;

	@Test
	void testGetAllStaff() {
		Role staffRoleInDB = new Role(2,"ROLE_STAFF");
		when(roleRepo.findById(Constant.STAFF_ROLE_ID)).thenReturn(staffRoleInDB);
		
		List<Role> staffRole = new ArrayList<>();
		Role role = roleRepo.findById(Constant.STAFF_ROLE_ID);
		staffRole.add(role);
	
		
		List<Account> expected = new ArrayList<>();
		
		Account staffAccount = new Account();
		staffAccount.setRoles(staffRole);
		staffAccount.setId((long)1);
		expected.add(staffAccount);
		
		Account staffAccount2 = new Account();
		staffAccount2.setRoles(staffRole);
		staffAccount2.setId((long)1);
		expected.add(staffAccount2);
		
		
		when(accountRepo.findByRolesIn(staffRole)).thenReturn(expected);
		
		int expectedSize= 1;
		List<Account> result = accountServiceImpl.getAllStaffs();
		assertThat(result).isEqualTo(expected);
		assertThat(result.size()).isEqualTo(2);
	}

}
