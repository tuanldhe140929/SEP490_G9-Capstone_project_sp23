package com.SEP490_G9.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SEP490_G9.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {
	public List<Tag> findAll();

	public boolean existsByName(String name);

	public Tag findById(int id);
}
