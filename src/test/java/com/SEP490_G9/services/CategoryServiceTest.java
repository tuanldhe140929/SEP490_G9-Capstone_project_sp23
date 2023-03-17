package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Category;
import com.SEP490_G9.repository.CategoryRepository;
import com.SEP490_G9.service.serviceImpls.CategoryServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class CategoryServiceTest {

	@Mock
	CategoryRepository categoryRepo;
	
	@InjectMocks
	CategoryServiceImpl categoryService;
	
	@Test
	void testGetAllCategoriesN() {
		Category cat1 = new Category(1, "Sprite");
		Category cat2 = new Category(2, "Music");
		Category cat3 = new Category(3, "Sound Effect");
		List<Category> expected = new ArrayList<>();
		expected.add(cat1);
		expected.add(cat2);
		expected.add(cat3);
		List<Category> result = categoryService.getAllCategories();
		assertThat(result).isEqualTo(expected);
	}
	
	
	@Test
	void testAddCategoryN() {
		boolean expected = true;
		Category category = new Category(50, "Story");
		Mockito.when(categoryRepo.save(category)).thenReturn(category);
		boolean result = categoryService.addCategory(category);
		assertEquals(expected, result);
	}
	
	@Test
	void testUpdateCategory() {
		boolean expected = true;
		Category cat1 = new Category(100, "Script");
		categoryRepo.save(cat1);
		Category cat2 = new Category(100, "Animation");
		Mockito.when(categoryRepo.save(cat2)).thenReturn(cat2);
		boolean result = categoryService.addCategory(cat2);
		assertEquals(expected, result);
	}

}
