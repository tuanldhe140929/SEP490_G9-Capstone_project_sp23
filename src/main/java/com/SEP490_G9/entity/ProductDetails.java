package com.SEP490_G9.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entity.embeddable.ProductVersionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value={"product","cartItems"})
@Entity
@Table(name = "product_details")
public class ProductDetails implements Serializable {

	@EmbeddedId
	private ProductVersionKey productVersionKey = new ProductVersionKey();

	@MapsId("productId")
	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id", columnDefinition = "bigint")
	private Product product;

	@Column(name = "name", length = 30)
	private String name;

	@Column(name = "description", length = 100)
	private String description;

	@Column(name = "cover_image")
	private String coverImage;

	@Column(name = "detailDescription")
	private String detailDescription;

	@Column(name = "price")
	private int price = 0;

	@Column(name = "instruction")
	private String instruction;

	@Column(name = "upload_date")
	private Date createdDate;

	@Column(name = "last_update")
	private Date lastModified;

	@ManyToOne
	@JoinColumn(name = "license_id")
	private License license;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "product_details_tag", joinColumns = {
			@JoinColumn(name = "product_id", referencedColumnName = "product_id"),
			@JoinColumn(name = "version", referencedColumnName = "version") }, inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags = new ArrayList<Tag>();

	@OneToMany(mappedBy = "productDetails")
	private List<Preview> previews = new ArrayList<Preview>();

	@OneToMany(mappedBy = "productDetails", fetch = FetchType.EAGER)
	private List<ProductFile> files = new ArrayList<ProductFile>();

	@OneToMany(mappedBy = "productDetails", fetch = FetchType.EAGER)
	private List<CartItem> cartItems = new ArrayList<>();

	public ProductDetails() {
		// TODO Auto-generated constructor stub
	}

	public ProductDetails(ProductVersionKey productVersionKey, Product product, String name, String description,
			String coverImage, String detailDescription, int price, String instruction, Date createdDate,
			Date lastModified, License license, Category category, List<Tag> tags, List<Preview> previews,
			List<ProductFile> files, List<CartItem> cartItems) {
		super();
		this.productVersionKey = productVersionKey;
		this.product = product;
		this.name = name;
		this.description = description;
		this.coverImage = coverImage;
		this.detailDescription = detailDescription;
		this.price = price;
		this.instruction = instruction;
		this.createdDate = createdDate;
		this.lastModified = lastModified;
		this.license = license;
		this.category = category;
		this.tags = tags;
		this.previews = previews;
		this.files = files;
		this.cartItems = cartItems;
	}

	public ProductDetails(ProductDetailsDTO productDetailsDTO) {
		
		this.productVersionKey.setProductId(productDetailsDTO.getId());
		this.productVersionKey.setVersion(productDetailsDTO.getVersion());
		this.category = productDetailsDTO.getCategory();
		this.name = productDetailsDTO.getName();
		this.description = productDetailsDTO.getDescription();
		this.price = productDetailsDTO.getPrice();
		this.license = productDetailsDTO.getLicense();
		this.tags = productDetailsDTO.getTags();
		this.detailDescription = productDetailsDTO.getDetails();
		
	}

	public String getVersion() {
		return this.productVersionKey.getVersion();
	}

	public void setVersion(String version) {
		this.productVersionKey.setVersion(version);
	}

	public ProductVersionKey getProductVersionKey() {
		return productVersionKey;
	}

	public void setProductVersionKey(ProductVersionKey productVersionKey) {
		this.productVersionKey = productVersionKey;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public String getDetailDescription() {
		return detailDescription;
	}

	public void setDetailDescription(String detailDescription) {
		this.detailDescription = detailDescription;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
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

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Preview> getPreviews() {
		return previews;
	}

	public void setPreviews(List<Preview> previews) {
		this.previews = previews;
	}

	public List<ProductFile> getFiles() {
		return files;
	}

	public void setFiles(List<ProductFile> files) {
		this.files = files;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

}