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
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.entities.Category;

import com.SEP490_G9.service.CategoryService;




@RequestMapping("private/manageCategory")
@RestController
public class CategoryController {

	@Autowired
	CategoryService manageCategoryService;
	
	@GetMapping("categories")
	public ResponseEntity<?> getAllCategories(){
		List<Category> catList = manageCategoryService.getAllCategories();
		return ResponseEntity.ok(catList);
	}
	
	@PostMapping("addCategory")
	public ResponseEntity<?> addCategory(@RequestBody Category category){
		boolean canAdd = manageCategoryService.addCategory(category);
		return ResponseEntity.ok(canAdd);
	}
	
	@PutMapping("updateCategory/{id}")
	public ResponseEntity<?> updateCategory(@RequestBody Category category, @PathVariable("id") int id){
		boolean canUpdate = manageCategoryService.updateCategory(category, id);
		return ResponseEntity.ok(canUpdate);
	}
}
