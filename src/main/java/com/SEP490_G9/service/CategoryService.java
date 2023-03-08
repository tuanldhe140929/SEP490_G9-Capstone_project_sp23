package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entity.Category;



public interface CategoryService {
	public List<Category> getAllCategories();
	
	public boolean addCategory(Category category);
	
	public boolean updateCategory(Category category, int id);
}
