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
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.repository.RoleRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class RoleRepositoryTest {

	@Autowired
	RoleRepository roleRepository;

	@Test
	void testFindById() {
		Role results = roleRepository.findById(1);

		int expectedId = 1;
		String expectedName = "ROLE_ADMIN";

		assertThat(results.getId()).isEqualTo(expectedId);
		assertThat(results.getName()).isEqualTo(expectedName);
	}

	@Test
	void testFindAll() {
		List<Role> results = roleRepository.findAll();
		int expectedSize = 4;
		assertEquals(results.size(), expectedSize);
		assertNotNull(results.get(0));
	}

	@Test
	void testFindByName() {
		Role results = roleRepository.findByName("ROLE_ADMIN");
		String expectedName = "ROLE_ADMIN";
		assertThat(results.getName()).isEqualTo(expectedName);
	}
}
