package com.SEP490_G9.dto;

import com.SEP490_G9.entity.ProductFile;

public class ProductFileDTO {

	public enum FileState {
		UPLOADING, UPLOADED, SCANNING, ERROR
	}

	private Long id;

	private String name;

	private String type;

	private Long size;

	private FileState fileState;

	public ProductFileDTO() {
		// TODO Auto-generated constructor stub
	}

	public ProductFileDTO(ProductFile productFile) {
		super();
		this.id = productFile.getId();
		this.name = productFile.getName();
		this.type = productFile.getType();
		this.size = productFile.getSize();
		this.fileState = FileState.UPLOADED;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}
	public FileState getFileState() {
		return fileState;
	}

	public void setFileState(FileState fileState) {
		this.fileState = fileState;
	}


}
