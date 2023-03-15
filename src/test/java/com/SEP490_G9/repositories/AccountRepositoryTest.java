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
		Account expected1 = new Account();
		expected1.setId(1L);
		expected1.setEmail("expected1@gmail.com");
		expected1.setPassword("12345678");
		
		Account expected2 = new Account();
		expected2.setId(2L);
		expected2.setEmail("expected2@gmail.com");
		expected2.setPassword("12345678");
		
		Account result = accountRepo.findById(2L).get();
		assertEquals(expected2, result);
	}
	
	@Test
	void testFindAll() {
		Account expected1 = new Account();
		expected1.setEmail("expected1@gmail.com");
		expected1.setPassword("12345678");
		
		Account expected2 = new Account();
		expected2.setEmail("expected2@gmail.com");
		expected2.setPassword("12345678");
		
		accountRepo.save(expected1);
		accountRepo.save(expected2);
		
		List<Account> accountList = accountRepo.findAll();
		assertEquals(2, accountList.size());
	}
	
	@Test
	void testFindByRolesIn() {
		Role adminRole = new Role(1, "ROLE_ADMIN");
		Role staffRole = new Role(2, "ROLE_STAFF");
		Role userRole = new Role(3, "ROLE_USER");
		Role sellerRole = new Role(4, "ROLE_SELLER");
		roleRepo.save(adminRole);
		roleRepo.save(staffRole);
		roleRepo.save(userRole);
		roleRepo.save(sellerRole);
		
		Account expected1 = new Account();
		expected1.setEmail("expected1@gmail.com");
		expected1.setPassword("12345678");
		List<Role> expectedRoles1 = new ArrayList<>();
		expectedRoles1.add(sellerRole);
		expectedRoles1.add(userRole);
		expected1.setRoles(expectedRoles1);
		accountRepo.save(expected1);
		
		Account expected2 = new Account();
		expected2.setEmail("expected2@gmail.com");
		expected2.setPassword("12345678");
		List<Role> expectedRoles2 = new ArrayList<>();
		expectedRoles2.add(userRole);
		expected2.setRoles(expectedRoles2);
		accountRepo.save(expected2);
		
		List<Role> givenRoles = new ArrayList<>();
		givenRoles.add(sellerRole);
		givenRoles.add(userRole);
		List<Account> result = accountRepo.findByRolesIn(givenRoles);
		
		System.out.println(result);
		
		assertEquals(1, result.size());
	}
}
