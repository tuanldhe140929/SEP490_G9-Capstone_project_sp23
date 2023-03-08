package com.SEP490_G9.entity;

import com.SEP490_G9.dto.EngineDTO;

import jakarta.persistence.*;

@Entity
@Table(name = "engines")
public class Engine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	public Engine() {
		// TODO Auto-generated constructor stub
	}

	public Engine(EngineDTO dto) {
		this.id = dto.getId();
		this.name = dto.getName();
	}

	public Engine(int id, String name) {
		super();
		this.id = id;
		this.name = name;
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
