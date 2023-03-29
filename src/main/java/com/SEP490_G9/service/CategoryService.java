package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entities.Category;

public interface CategoryService {
	public List<Category> getAllCategories();

	public Category addCategory(Category category);

	public boolean updateCategory(Category category, int id);
}
