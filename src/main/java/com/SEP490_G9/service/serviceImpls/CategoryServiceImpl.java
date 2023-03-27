package com.SEP490_G9.service.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Category;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.NumberException;
import com.SEP490_G9.repository.CategoryRepository;
import com.SEP490_G9.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepo;

	@Override
	public List<Category> getAllCategories() {
		List<Category> catList = categoryRepo.findAll();
		return catList;
	}

	@Override
	public Category addCategory(Category category) {
		if (categoryRepo.existsByName(category.getName())) {
			throw new DuplicateFieldException("name", category.getName());
		}
		if (category.getName().length() <= 2 || category.getName().length() >= 20) {
			throw new NumberException("category name length must be between 2 to 20");
		}
		categoryRepo.save(category);
		return category;
	}

	@Override
	public boolean updateCategory(Category category, int id) {
		Category updatedCat = categoryRepo.findById(id);
		if (!category.getName().trim().equalsIgnoreCase(updatedCat.getName().trim())) {
			if (categoryRepo.existsByName(category.getName())) {
				throw new DuplicateFieldException("name", category.getName());
			}
		}
		updatedCat.setName(category.getName().trim());
		categoryRepo.save(updatedCat);
		return true;
	}

}
