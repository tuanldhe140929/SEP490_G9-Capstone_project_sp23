package com.SEP490_G9.repositories;


import static org.junit.jupiter.api.Assertions.*;


import static org.assertj.core.api.Assertions.assertThat;

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

import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.repository.UserRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class SellerRepositoryTest {


	
	@Autowired
	UserRepository userRepo;
	


	@Autowired
	private SellerRepository sellerRepository;
	@Test
	public void findByUsername() {
		Seller s = new Seller();
		s.setPhoneNumber("951123339");
		s.setUsername("seller1");
		s.setId((long)2);

		int expectedvalue = 2;
		
		Seller result = sellerRepository.findByUsername("seller1");
		assertThat(result.getId()).isEqualTo(expectedvalue);
	}
	@Test
	public void findById() {
		Seller s = new Seller();
		s.setPhoneNumber("951123339");
		s.setUsername("seller1");
		s.setId((long)2);
		
		String expectedvalue = "seller1";
		Seller result = sellerRepository.findById((long)2);
		assertThat(result.getUsername()).isEqualTo(expectedvalue);

	}

}
