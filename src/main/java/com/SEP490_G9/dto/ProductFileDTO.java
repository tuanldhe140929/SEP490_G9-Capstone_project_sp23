package com.SEP490_G9.dto;

import com.SEP490_G9.entity.ProductFile;

public class ProductFileDTO {

	public enum FileState {
		UPLOADING, STORED, SCANNING, ERROR, MALICIOUS
	}

	private Long id;

	private String name;

	private String type;

	private Long size;

	private FileState fileState;

	private boolean isLastFile;

	public ProductFileDTO() {
		// TODO Auto-generated constructor stub
	}

	public ProductFileDTO(ProductFile productFile, boolean isLastFile) {
		super();
		this.id = productFile.getId();
		this.name = productFile.getName();
		this.type = productFile.getType();
		this.size = productFile.getSize();
		this.isLastFile = isLastFile;
	}

	public ProductFileDTO(ProductFile productFile) {
		super();
		this.id = productFile.getId();
		this.name = productFile.getName();
		this.type = productFile.getType();
		this.size = productFile.getSize();

		this.fileState = FileState.STORED;
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

	public boolean isLastFile() {
		return isLastFile;
	}

	public void setLastFile(boolean isLastFile) {
		this.isLastFile = isLastFile;
	}

}
