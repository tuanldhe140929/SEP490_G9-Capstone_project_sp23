package com.SEP490_G9.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.entities.Category;

import com.SEP490_G9.service.CategoryService;

@RequestMapping("category")
@RestController
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@GetMapping("categories")
	public ResponseEntity<?> getAllCategories() {
		List<Category> catList = categoryService.getAllCategories();
		return ResponseEntity.ok(catList);
	}

	@PostMapping("addCategory")
	public ResponseEntity<?> addCategory(@RequestBody Category category) {
		Category addedCategory = categoryService.addCategory(category);
		return ResponseEntity.ok(addedCategory);
	}

	@PutMapping("updateCategory/{id}")
	public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable("id") int id) {
		boolean canUpdate = categoryService.updateCategory(category, id);
		return ResponseEntity.ok(canUpdate);
	}
}
