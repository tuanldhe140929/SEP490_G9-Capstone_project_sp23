package com.SEP490_G9.models.DTOS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.SEP490_G9.models.Entities.Preview;
import com.SEP490_G9.models.Entities.ProductDetails;
import com.SEP490_G9.models.Entities.ProductFile;
import com.SEP490_G9.models.Entities.Seller;
import com.SEP490_G9.models.Entities.Tag;
import com.SEP490_G9.models.Entities.Category;
import com.SEP490_G9.models.Entities.License;
import com.SEP490_G9.repositories.PreviewRepository;

public class ProductDetailsDTO {

	private Long id;

	private String version;

	private String name;

	private String description;

	private String coverImage;

	private String details;

	private String instruction;

	private Date createdDate = new Date();

	private Date lastModified = new Date();

	private License license;

	private boolean draft = true;

	private int price = 0;

	private Preview previewVideo;

	private List<Preview> previewPictures;

	private Seller seller;

	private List<Tag> tags = new ArrayList<Tag>();

	private Category category;

	private List<ProductFileDTO> files = new ArrayList<ProductFileDTO>();

	public ProductDetailsDTO() {
		// TODO Auto-generated constructor stub
	}

	public ProductDetailsDTO(ProductDetails productDetails, PreviewRepository previewRepository) {
		super();
		this.id = productDetails.getProduct().getId();
		this.version = productDetails.getVersion();
		this.name = productDetails.getName();
		this.description = productDetails.getDescription();
		this.coverImage = productDetails.getCoverImage();
		this.details = productDetails.getDetailDescription();
		this.instruction = "";
		this.createdDate = productDetails.getCreatedDate();
		this.lastModified = productDetails.getLastModified();
		this.draft = productDetails.getProduct().isDraft();
		this.price = productDetails.getPrice();
		this.license = productDetails.getLicense();
		this.previewVideo = getPreviewVideoSource(productDetails, previewRepository);
		this.previewPictures = getPreviewPicturesSource(productDetails, previewRepository);
		this.seller = productDetails.getProduct().getSeller();
		this.tags = productDetails.getTags();
		this.category = productDetails.getCategory();
		for (ProductFile file : productDetails.getFiles()) {
			ProductFileDTO dto = new ProductFileDTO(file);
			dto.setFileState(ProductFileDTO.FileState.UPLOADED);
			this.files.add(dto);
		}
	}

	private List<Preview> getPreviewPicturesSource(ProductDetails productDetails, PreviewRepository previewRepository) {
		List<Preview> previewPictures = previewRepository.findByProductDetailsAndType(productDetails, "picture");
		return previewPictures;
	}

	private Preview getPreviewVideoSource(ProductDetails productDetails, PreviewRepository previewRepository) {
		List<Preview> previewVideo = previewRepository.findByProductDetailsAndType(productDetails, "video");
		if (!previewVideo.isEmpty()) {
			return previewVideo.get(0);
		}
		return null;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
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

	public Preview getPreviewVideo() {
		return previewVideo;
	}

	public void setPreviewVideo(Preview previewVideo) {
		this.previewVideo = previewVideo;
	}

	public List<Preview> getPreviewPictures() {
		return previewPictures;
	}

	public void setPreviewPictures(List<Preview> previewPictures) {
		this.previewPictures = previewPictures;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<ProductFileDTO> getFiles() {
		return files;
	}

	public void setFiles(List<ProductFileDTO> files) {
		this.files = files;
	}

	public License getLicense() {
		// TODO Auto-generated method stub
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

}
