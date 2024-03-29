package com.SEP490_G9.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.SEP490_G9.entities.Category;
import com.SEP490_G9.entities.License;
import com.SEP490_G9.entities.Preview;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductDetails.Status;
import com.SEP490_G9.entities.ProductFile;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.repository.PreviewRepository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDetailsDTO {

	private Long id;

	private String version;

	@NotBlank
	@Size(min = 3, max = 30)
	private String name;

	@Size(max = 50)
	private String description;

	private String coverImage;

	@Size(max = 1000)
	private String details;

	@Size(max = 200)
	private String instruction;

	private Date createdDate = new Date();

	private Date lastModified = new Date();


	private String activeVersion;

	private boolean draft = true;

	private double price = 0;

	private PreviewDTO previewVideo;

	private List<PreviewDTO> previewPictures = new ArrayList<>();

	private SellerDTO seller;

	private List<Tag> tags = new ArrayList<>();
	
	private Status approved;
	
	@NotNull
	private Category category;

	private List<ProductFileDTO> files = new ArrayList<ProductFileDTO>();
	
	private boolean flagged;

	private Date approvedDate;
	public ProductDetailsDTO() {
		// TODO Auto-generated constructor stub
	}

	public ProductDetailsDTO(ProductDetails productDetails) {
		super();
		this.id = productDetails.getProduct().getId();
		this.version = productDetails.getVersion();
		this.name = productDetails.getName();
		this.description = productDetails.getDescription();
		this.coverImage = productDetails.getCoverImage();
		this.details = productDetails.getDetailDescription();
		this.instruction = productDetails.getInstruction();
		this.createdDate = productDetails.getCreatedDate();
		this.approved = productDetails.getApproved();
		this.lastModified = productDetails.getLastModified();
		this.draft = productDetails.getProduct().isDraft();
		this.price = productDetails.getPrice();
		this.activeVersion = productDetails.getProduct().getActiveVersion();
		this.approvedDate = productDetails.getApprovedDate();
		this.previewVideo = getPreviewVideoSource(productDetails);
		this.previewPictures = getPreviewPicturesSource(productDetails);
		this.seller = new SellerDTO(productDetails.getProduct().getSeller());
		tags = productDetails.getTags();

		this.category = productDetails.getCategory();
		for (ProductFile file : productDetails.getFiles()) {
			ProductFileDTO dto = new ProductFileDTO(file);
			dto.setFileState(ProductFileDTO.FileState.STORED);
			this.files.add(dto);
		}
		this.flagged = productDetails.isFlagged();
	}

	private List<PreviewDTO> getPreviewPicturesSource(ProductDetails productDetails) {
		List<PreviewDTO> ret = new ArrayList<>();
		for (Preview preview : productDetails.getPreviews()) {
			if (preview.getType().equalsIgnoreCase("picture"))
				ret.add(new PreviewDTO(preview));
		}
		return ret;
	}

	private PreviewDTO getPreviewVideoSource(ProductDetails productDetails) {
		Preview ret = null;
		for (Preview preview : productDetails.getPreviews()) {
			if (preview.getType().equalsIgnoreCase("video"))
				ret = preview;
				}

		if (ret == null) {
			return null;
		}

		return new PreviewDTO(ret);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
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

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}



	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public SellerDTO getSeller() {
		return seller;
	}

	public void setSeller(SellerDTO seller) {
		this.seller = seller;
	}

	public PreviewDTO getPreviewVideo() {
		return previewVideo;
	}

	public void setPreviewVideo(PreviewDTO previewVideo) {
		this.previewVideo = previewVideo;
	}

	public List<PreviewDTO> getPreviewPictures() {
		return previewPictures;
	}

	public void setPreviewPictures(List<PreviewDTO> previewPictures) {
		this.previewPictures = previewPictures;
	}

	public List<ProductFileDTO> getFiles() {
		return files;
	}

	public void setFiles(List<ProductFileDTO> files) {
		this.files = files;
	}

	public String getActiveVersion() {
		return activeVersion;
	}

	public void setActiveVersion(String activeVersion) {
		this.activeVersion = activeVersion;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Status getApproved() {
		return approved;
	}

	public void setApproved(Status approved) {
		this.approved = approved;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}

}
