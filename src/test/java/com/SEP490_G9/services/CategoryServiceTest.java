package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

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
	void testGetAllCategories() {
		List<Category> expected = new ArrayList<>();
		Category cat1 = new Category();
		cat1.setId(1);
		cat1.setName("Name");
		
		Category cat2 = new Category();
		cat2.setId(2);
		cat2.setName("Name");
		
		categoryRepo.save(cat1);
		categoryRepo.save(cat2);
		
		when(categoryRepo.findAll()).thenReturn(expected);
		expected.add(cat1);
		expected.add(cat2);
		
		List<Category> result = categoryService.getAllCategories();
		assertNotEquals(expected, null);
		assertEquals(expected, result);
		assertEquals(expected.size(), 2);
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
