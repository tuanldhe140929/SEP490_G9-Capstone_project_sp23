package com.SEP490_G9.services;

import java.util.List;

import com.SEP490_G9.models.Entities.Category;

public interface ManageCategoryService {
	public List<Category> getAllCategories();
	
	public boolean addCategory(Category category);
	
	public boolean updateCategory(Category category, int id);
}
