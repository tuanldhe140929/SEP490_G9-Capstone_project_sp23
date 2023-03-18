package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.repository.ProductDetailsRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
public class ProductDetailsRepositoryTest {

	@Autowired
	ProductDetailsRepository pdRepo;

	@Test
	public void testFindByProductIdAndVersion() {
		Long expectedId = 1L;
		String expectedVersion = "1.0.0";
		ProductDetails result = pdRepo.findByProductIdAndProductVersionKeyVersion(expectedId, expectedVersion);
		assertThat(result.getProduct().getId()).isEqualTo(expectedId);
		assertThat(result.getVersion()).isEqualTo(expectedVersion);
	}


	@Test
	public void testFindByProductId() {
		List<ProductDetails> result = pdRepo.findByProductId(1L);
		assertThat(result.get(0).getProduct().getId()).isEqualTo(1L);
	}

	@Test
	public void testFindByNameContaining() {
		String searchString = "TEST";
		List<ProductDetails> result = pdRepo.findByNameContaining(searchString);
		assertTrue(result.get(0).getName().contains(searchString));
	}

	@Test
	public void test1() {
		Long existPid = 1L;
		String existVersion = "1.0.0";
		boolean result = pdRepo.existsByProductIdAndProductVersionKeyVersion(existPid, existVersion);
		boolean expected = true;
		assertThat(result).isEqualTo(expected);
	}

}
