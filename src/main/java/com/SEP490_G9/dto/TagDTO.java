package com.SEP490_G9.dto;

import com.SEP490_G9.entity.Tag;

public class TagDTO {
	private int id;
	private String name;

	public TagDTO() {
		// TODO Auto-generated constructor stub
	}

	public TagDTO(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public TagDTO(Tag tag) {
		this.id = tag.getId();
		this.name = tag.getName();
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
