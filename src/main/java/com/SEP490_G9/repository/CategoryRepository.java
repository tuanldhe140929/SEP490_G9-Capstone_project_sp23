package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entities.Category;


public interface CategoryRepository extends JpaRepository<Category,Integer> {
	public List<Category> findAll();

	public boolean existsByName(String name);
	
	public Category findById(int id);
}
