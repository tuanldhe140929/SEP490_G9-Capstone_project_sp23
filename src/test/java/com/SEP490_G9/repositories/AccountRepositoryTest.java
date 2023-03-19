package com.SEP490_G9.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.RoleRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class AccountRepositoryTest {

	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Test
	void testExistsByEmail() {
		boolean expected = false;
		Account acc1 = new Account();
		acc1.setEmail("acc1@gmail.com");
		acc1.setPassword("12345678");
		accountRepo.save(acc1);
		String emailToCompare = "acc2@gmail.com";
		boolean result = accountRepo.existsByEmail(emailToCompare);
		assertEquals(expected, result);
	}

	@Test
	void testFindByEmail() {
		Account expectedAccount = new Account();
		expectedAccount.setEmail("expected@gmail.com");
		expectedAccount.setPassword("12345678");
		accountRepo.save(expectedAccount);
		Account resultAccount = accountRepo.findByEmail("expected@gmail.com");
		assertEquals(expectedAccount, resultAccount);
	}
	
	@Test
	void testFindById() {
		Account result =  accountRepo.findById((long)2).get();
		String expectedEmail = "seller1@gmail.com";
		assertEquals(expectedEmail, result.getEmail());
	}
	
	@Test
	void testFindAll() {		
		List<Account> accountList = accountRepo.findAll();
		assertEquals(4, accountList.size());
	}
	
	@Test
	void testFindByRolesIn() {
		List<Role> givenRoles = new ArrayList<>();
		givenRoles.add(roleRepo.findById(3));
		givenRoles.add(roleRepo.findById(4));
		List<Account> result = accountRepo.findByRolesIn(givenRoles);
		assertEquals(2, result.size());
	}
}
