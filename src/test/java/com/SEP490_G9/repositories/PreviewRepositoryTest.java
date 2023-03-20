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
import com.SEP490_G9.entities.Preview;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.repository.PreviewRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class PreviewRepositoryTest {

	@Autowired
	PreviewRepository previewRepo;

	@Test
	void testFindAll() {
		List<Preview> results = previewRepo.findAll();
		assertNotNull(results);
	}

	@Test
	void testFindById() {
		Long expectedId = 1L;
		Preview result = previewRepo.findById(expectedId).get();
		assertThat(result.getId()).isEqualTo(expectedId);
	}
	
	@Test
	void testFindByIdA() {
		Long expectedId = -1L;
		Preview result = previewRepo.findById(expectedId).get();
		assertThat(result.getId()).isEqualTo(expectedId);
	}

	@Test
	void testFindByProductIdAndVersion() {
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));

		List<Preview> results = previewRepo.findByProductDetailsAndType(pd, "video");
		assertNotNull(results);
	}
	
	@Test
	void testFindByProductIdAndVersionA() {
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(-1L, "1.0.0"));

		List<Preview> results = previewRepo.findByProductDetailsAndType(pd, "video");
		assertTrue(results.isEmpty());
	}

	@Test
	void testDeleteById() {
		boolean beforeDelete = previewRepo.existsById(1L);
		assertTrue(beforeDelete);
		previewRepo.deleteById(1L);
		boolean afterDelete = previewRepo.existsById(1L);
		assertTrue(!afterDelete);
	}
	
	@Test
	void testDeleteByIdA() {
		boolean beforeDelete = previewRepo.existsById(1L);
		assertTrue(beforeDelete);
		previewRepo.deleteById(-1L);
		boolean afterDelete = previewRepo.existsById(1L);
		assertTrue(afterDelete);
	}
}
