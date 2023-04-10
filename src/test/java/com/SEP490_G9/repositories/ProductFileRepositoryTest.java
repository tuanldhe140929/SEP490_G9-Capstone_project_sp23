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
	void testFindByProductIdAndVersionN() {
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		List<ProductFile> results = productFileRepository.findByProductDetails(pd);
		assertNotNull(results);
	}
	
	@Test
	void testFindByProductIdAndVersionA() {
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(0L, "1.0.000000"));
		List<ProductFile> results = productFileRepository.findByProductDetails(pd);
		assertEquals(results.size(),0);
	}



}
