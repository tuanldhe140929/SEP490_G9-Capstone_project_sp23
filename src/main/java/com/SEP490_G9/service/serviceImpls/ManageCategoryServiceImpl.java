package com.SEP490_G9.service.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entity.Category;
import com.SEP490_G9.exception.DuplicateFieldException;

import com.SEP490_G9.repository.CategoryRepository;
import com.SEP490_G9.service.ManageCategoryService;

@Service
public class ManageCategoryServiceImpl implements ManageCategoryService{

	@Autowired
	CategoryRepository categoryRepo;
	
	@Override
	public List<Category> getAllCategories() {
		List<Category> catList = categoryRepo.findAll();
		return catList;
	}

	@Override
	public boolean addCategory(Category category) {
		if(categoryRepo.existsByName(category.getName())) {
			throw new DuplicateFieldException("name", category.getName());
		}
		categoryRepo.save(category);
		return true;
	}

	@Override
	public boolean updateCategory(Category category, int id) {
		Category updatedCat = categoryRepo.findById(id);
		if(!category.getName().trim().equalsIgnoreCase(updatedCat.getName().trim())) {
			if(categoryRepo.existsByName(category.getName())) {
				throw new DuplicateFieldException("name", category.getName());
			}
		}
		updatedCat.setName(category.getName().trim());
		categoryRepo.save(updatedCat);
		return true;
	}

}
