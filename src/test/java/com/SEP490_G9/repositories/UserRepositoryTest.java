package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
import com.SEP490_G9.entities.RefreshToken;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.UserRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;
	@Test
	public void testFindByUsername() {
		User a = new User();
		a.setUsername((String) "usser1");
		User results = userRepository.findByUsername("usser1");
		assertThat(results.getUsername()).isEqualTo(a.getUsername());
	}
	@Test
	public void testFindByEmail() {
		Account a = new Account();
		a.setEmail((String) "user1@gmail.com");
		User results = userRepository.findByEmail("user1@gmail.com");
		assertThat(results.getEmail()).isEqualTo(a.getEmail());
	}

	@Test
	public void testExistsByEmail() {
//		Account a = new Account();
//		a.setId((long)1);
		Boolean result = userRepository.existsByEmail("user1@gmail.com");
		Boolean expectedValue = true;
		assertThat(result.booleanValue()).isEqualTo(expectedValue);
	}
	@Test
	public void testfindAll() {
		List<User> result = userRepository.findAll();
		int expectedvalue = 2;
		assertEquals(result.size(), expectedvalue);
		
	}
	@Test
	public void findByEnabled() {
		List<User> result = userRepository.findByEnabled(true);
		int expectedvalue = 2;
		assertEquals(result.size(), expectedvalue);
	}
}
