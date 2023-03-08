package com.SEP490_G9.dto;

import com.SEP490_G9.entity.Engine;

public class EngineDTO {
	private int id;
	private String name;

	public EngineDTO() {
		// TODO Auto-generated constructor stub
	}

	public EngineDTO(Engine engine) {
		if (engine != null) {
			this.id = engine.getId();
			this.name = engine.getName();
		}
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
