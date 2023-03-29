package com.SEP490_G9.dto;

import java.util.Date;

import com.SEP490_G9.entities.ProductFile;

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

	private boolean enabled;

	private boolean newUploaded;

	private boolean reviewed;

	private Date createdDate;

	private Date lastModified;

	public ProductFileDTO() {
		// TODO Auto-generated constructor stub
	}

	public ProductFileDTO(ProductFile productFile, boolean isLastFile) {
		super();
		this.id = productFile.getId();
		this.name = productFile.getName();
		this.type = productFile.getType();
		this.size = productFile.getSize();
		this.enabled = productFile.isEnabled();
		this.reviewed = productFile.isReviewed();
		this.newUploaded = productFile.isNewUploaded();
		this.createdDate = productFile.getCreatedDate();
		this.lastModified = productFile.getLastModified();
		this.isLastFile = isLastFile;
	}

	public ProductFileDTO(ProductFile productFile) {
		super();
		this.id = productFile.getId();
		this.name = productFile.getName();
		this.type = productFile.getType();
		this.size = productFile.getSize();
		this.enabled = productFile.isEnabled();
		this.reviewed = productFile.isReviewed();
		this.newUploaded = productFile.isNewUploaded();
		this.createdDate = productFile.getCreatedDate();
		this.lastModified = productFile.getLastModified();
		this.fileState = FileState.STORED;
	}

	public boolean isNewUploaded() {
		return newUploaded;
	}

	public void setNewUploaded(boolean newUploaded) {
		this.newUploaded = newUploaded;
	}

	public boolean isReviewed() {
		return reviewed;
	}

	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
