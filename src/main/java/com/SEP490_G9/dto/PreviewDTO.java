package com.SEP490_G9.dto;

import com.SEP490_G9.entity.Preview;

public class PreviewDTO {
	private Long id;

	private String source;

	private String type;
	
	public PreviewDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public PreviewDTO(Preview preview) {
		this.id = preview.getId();
		this.source = preview.getSource();
		this.type = preview.getType();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
