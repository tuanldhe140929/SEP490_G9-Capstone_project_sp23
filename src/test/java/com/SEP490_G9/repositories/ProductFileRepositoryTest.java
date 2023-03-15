package com.SEP490_G9.repositories;

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
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductFile;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.repository.ProductFileRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class ProductFileRepositoryTest {

	@Autowired
	ProductFileRepository productFileRepository;

	@Test
	void testFindAll() {
		int expectedSize = 7;
		List<ProductFile> results = productFileRepository.findAll();
		assertEquals(expectedSize, results.size());
	}

	@Test
	void testFindByProductIdAndVersion() {
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		List<ProductFile> results = productFileRepository.findByProductDetails(pd);
		assertNotNull(results);
	}

	@Test
	void testExistByNameAndProductDetails() {
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		String existName = "Database V2.drawio.png";
		boolean result = productFileRepository.existsByNameAndProductDetails(existName, pd);
		assertTrue(result);
	}

	@Test
	void testDeleteById() {
		Long id = 2L;
		assertTrue(productFileRepository.existsById(id));
		productFileRepository.deleteById(id);
		assertTrue(!productFileRepository.existsById(id));
	}

}
