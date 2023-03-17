package com.SEP490_G9.repositories;

import static org.junit.jupiter.api.Assertions.*;

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
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.repository.UserRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class SellerRepositoryTest {

	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	SellerRepository sellerRepo;
	
	@Test
	void testFindByUsername() {
		Seller expectedSeller = new Seller();
		expectedSeller.setEmail("expected@gmail.com");
		expectedSeller.setPassword("expected");
		expectedSeller.setUsername("foreveralone");
		sellerRepo.save(expectedSeller);
		
		Seller result = sellerRepo.findByUsername("foreveralone");
		assertEquals(expectedSeller, result);
	}
	
	@Test
	void testFindById() {
		Seller expected = new Seller();
		expected.setId(1L);
		expected.setEmail("expected@gmail.com");
		expected.setPassword("foreveralone");
		sellerRepo.save(expected);
		
		Seller result = sellerRepo.findById(1L);
		assertEquals(expected, result);
	}

}
