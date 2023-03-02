package com.SEP490_G9.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.SEP490_G9.entity.Preview;
import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.entity.ProductFile;
import com.SEP490_G9.entity.Seller;
import com.SEP490_G9.entity.Tag;
import com.SEP490_G9.repository.PreviewRepository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.SEP490_G9.entity.Category;
import com.SEP490_G9.entity.License;

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

	private LicenseDTO license;

	private String activeVersion;

	private boolean draft = true;

	private int price = 0;

	private Preview previewVideo;

	private List<Preview> previewPictures = new ArrayList<>();

	private Seller seller;

	private List<Tag> tags = new ArrayList<Tag>();

	@NotNull
	private Category category;

	private List<ProductFileDTO> files = new ArrayList<ProductFileDTO>();

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
		this.lastModified = productDetails.getLastModified();
		this.draft = productDetails.isDraft();
		this.price = productDetails.getPrice();
		this.activeVersion = productDetails.getProduct().getActiveVersion();
		if (productDetails.getLicense() != null) {
			this.license = new LicenseDTO(productDetails.getLicense());
		}
		this.previewVideo = getPreviewVideoSource(productDetails);
		this.previewPictures = getPreviewPicturesSource(productDetails);
		this.seller = productDetails.getProduct().getSeller();
		this.tags = productDetails.getTags();
		this.category = productDetails.getCategory();
		for (ProductFile file : productDetails.getFiles()) {
			ProductFileDTO dto = new ProductFileDTO(file);
			dto.setFileState(ProductFileDTO.FileState.UPLOADED);
			this.files.add(dto);
		}
	}

	private List<Preview> getPreviewPicturesSource(ProductDetails productDetails) {
		List<Preview> ret = new ArrayList<>();
		for (Preview preview : productDetails.getPreviews()) {
			if (preview.getType().equalsIgnoreCase("picture"))
				ret.add(preview);
		}
		return ret;
	}

	private Preview getPreviewVideoSource(ProductDetails productDetails) {
		Preview ret = null;
		for (Preview preview : productDetails.getPreviews()) {
			if (preview.getType().equalsIgnoreCase("video"))
				ret = preview;
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

	public LicenseDTO getLicense() {
		return license;
	}

	public void setLicense(LicenseDTO license) {
		this.license = license;
	}

	public String getActiveVersion() {
		return activeVersion;
	}

	public void setActiveVersion(String activeVersion) {
		this.activeVersion = activeVersion;
	}

}
