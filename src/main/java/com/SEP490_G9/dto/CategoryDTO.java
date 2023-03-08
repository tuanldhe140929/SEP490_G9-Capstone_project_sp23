package com.SEP490_G9.dto;

import com.SEP490_G9.entity.Category;

public class CategoryDTO {
	private int id;
	private String name;

	public CategoryDTO() {
		// TODO Auto-generated constructor stub
	}

	public CategoryDTO(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
