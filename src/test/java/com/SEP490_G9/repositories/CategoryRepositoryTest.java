package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.SEP490_G9.entities.Category;
import com.SEP490_G9.repository.CategoryRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class CategoryRepositoryTest {

	@Autowired
	CategoryRepository categoryRepo;
	
	@Test
	void testFindAll() {
		Category cat1 = new Category(1, "Music");
		Category cat2 = new Category(2, "Background");
		Category cat3 = new Category(3, "Sound Effect");
		categoryRepo.save(cat1);
		categoryRepo.save(cat2);
		categoryRepo.save(cat3);
		List<Category> expected = categoryRepo.findAll();
		assertThat(expected.size()).isEqualTo(3);
	}
	
	@Test
	void testExistByName() {
		Category cat1 = new Category(1, "Sprite");
		categoryRepo.save(cat1);
		boolean existed = categoryRepo.existsByName("Sprite");
		assertThat(existed).isEqualTo(true);
	}
	
	@Test
	void testFindById() {
		Category cat1 = new Category(1, "Animation");
		categoryRepo.save(cat1);
		Category result = categoryRepo.findById(1);
		assertThat(result).isEqualTo(cat1);
	}

}
