package com.SEP490_G9.models.DTOS;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;

import com.SEP490_G9.models.Entities.Preview;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.ProductFile;
import com.SEP490_G9.models.Entities.Tag;
import com.SEP490_G9.models.Entities.Type;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.repositories.PreviewRepository;


public class ProductDTO {
	
	private Long id;
	
	private String name;
	
	private String url;
	
	private String description;
	
	private String coverImage;
	
	private String details;
	
	private String instruction;
	
	private Date uploadDate = new Date();
	
	private Date lastUpdate = new Date();
	
	private boolean draft = true;
	
	private int price = 0;

	private String previewVideo;
	
	private List<String> previewPictures;
	
    private User user;
	
	private List<Tag> tags = new ArrayList<Tag>();
	
    private Type type;
	
	private List<ProductFile> files = new ArrayList<ProductFile>();
	
	public ProductDTO() {
		// TODO Auto-generated constructor stub
	}

	public ProductDTO(Product product, PreviewRepository previewRepository) {
		super();
		this.id = product.getId();
		this.name = product.getName();
		this.url = product.getUrl();
		this.description = product.getDescription();
		this.coverImage = product.getCoverImage();
		this.details = product.getDetails();
		this.instruction = "";
		this.uploadDate = product.getUploadDate();
		this.lastUpdate = product.getLastUpdate();
		this.draft = product.isDraft();
		this.price = product.getPrice();
		this.previewVideo = getPreviewVideoSource(product,previewRepository);
		this.previewPictures = getPreviewPicturesSource(product,previewRepository);;
		this.user = product.getUser();
		this.tags = product.getTags();
		this.type = product.getType();
		this.files = product.getFiles();
	}

	private List<String> getPreviewPicturesSource(Product product, PreviewRepository previewRepository) {
		List<Preview> previews = previewRepository.findByProductAndType(product,"picture");
		List<String> ret = new ArrayList<>();
		for(Preview preview:previews) {
			ret.add(preview.getSource());
		}
		return ret;
	}

	private String getPreviewVideoSource(Product product, PreviewRepository previewRepository) {
		List<Preview> previews = previewRepository.findByProductAndType(product,"video");
		String ret ="";
		if(previews.size()>0) {
			ret = previews.get(0).getSource();
		}
		
		return ret;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getPreviewVideo() {
		return previewVideo;
	}

	public void setPreviewVideo(String previewVideo) {
		this.previewVideo = previewVideo;
	}

	public List<String> getPreviewPictures() {
		return previewPictures;
	}

	public void setPreviewPictures(List<String> previewPictures) {
		this.previewPictures = previewPictures;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<ProductFile> getFiles() {
		return files;
	}

	public void setFiles(List<ProductFile> files) {
		this.files = files;
	}
	
}
