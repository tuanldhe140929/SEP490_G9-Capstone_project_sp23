package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
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
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.repository.CartRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.RoleRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class CartRepositoryTest {
	@Autowired
	private CartRepository cartRepository;

	@Test
	void testfindById() {
		User user = new User();
		user.setId(1L);

		Cart cart1 = new Cart();
		cart1.setUser(user);
		cart1.setId(1L);

		Cart result = cartRepository.findById(cart1.getId()).orElseThrow();
		assertThat(result.getUser().getId()).isEqualTo(user.getId());
		
	}

	@Test
	void testfindFirstByUserOrderByIdDesc() {
		User user1 = new User();
		user1.setId(2L);

		Cart result = cartRepository.findFirstByUserOrderByIdDesc(user1);
		int expectedUserId = 2;
		int expectedCartId = 2;
		assertThat(result.getId()).isEqualTo(expectedCartId);
		assertThat(result.getUser().getId()).isEqualTo(expectedUserId);
	}
	
}
