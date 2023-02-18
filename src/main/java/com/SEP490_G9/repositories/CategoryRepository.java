package com.SEP490_G9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.models.Entities.Category;

public interface CategoryRepository extends JpaRepository<Category,Integer> {

}
