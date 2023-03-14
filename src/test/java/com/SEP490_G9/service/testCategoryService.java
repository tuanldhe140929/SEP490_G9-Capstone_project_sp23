package com.SEP490_G9.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.SEP490_G9.entities.Category;
import com.SEP490_G9.repository.CategoryRepository;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class testCategoryService {

	@Mock
	CategoryRepository categoryRepo;
	
	@InjectMocks
	CategoryService categoryService;
	
	@Test
	void test() {
		boolean expected = true;
		Category category = new Category(50, "Story");
		Mockito.when(categoryRepo.save(category)).thenReturn(category);
		boolean result = categoryService.addCategory(category);
		assertEquals(expected, result);fail("Not yet implemented");
	}

}
