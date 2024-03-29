package com.SEP490_G9.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value = { "product", "cartItems" })
@Entity
@Table(name = "product_versions", uniqueConstraints = {
		@UniqueConstraint(name = "uk_product_version", columnNames = { "product_id", "version" }) })
public class ProductDetails implements Serializable,Comparable<ProductDetails> {
	public enum Status {
		NEW, PENDING, APPROVED, REJECTED
	}

	@EmbeddedId
	private ProductVersionKey productVersionKey = new ProductVersionKey();

	@MapsId("productId")
	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id", columnDefinition = "bigint")
	private Product product;

	@Column(name = "name", length = 30, nullable = true)
	private String name;

	@Column(name = "description", length = 100)
	private String description;

	@Column(name = "cover_image")
	private String coverImage;

	@Column(name = "detailDescription", length = 1000)
	private String detailDescription;

	@Column(name = "price", nullable = false, columnDefinition = "real check( price < 1000 )")
	private double price = 0;

	@Column(name = "instruction")
	private String instruction;

	@Column(name = "upload_date", nullable = false)
	private Date createdDate;

	@Column(name = "last_update", nullable = false)
	private Date lastModified;

	@Column(name= "approved_date")
	private Date approvedDate;
	
	@Column(name = "flagged", nullable = false)
	private boolean flagged;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status approved = Status.NEW;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "product_version_tag", joinColumns = {
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

	public ProductDetails(ProductDetailsDTO productDetailsDTO) {

		this.productVersionKey.setProductId(productDetailsDTO.getId());
		this.productVersionKey.setVersion(productDetailsDTO.getVersion());
		this.category = productDetailsDTO.getCategory();
		this.name = productDetailsDTO.getName();
		this.description = productDetailsDTO.getDescription();
		this.price = productDetailsDTO.getPrice();
		this.tags = productDetailsDTO.getTags();
		this.detailDescription = productDetailsDTO.getDetails();
		this.flagged = productDetailsDTO.isFlagged();

	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
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

	public Status getApproved() {
		return approved;
	}

	public void setApproved(Status approved) {
		this.approved = Status.valueOf(approved.toString().toUpperCase());
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}

	@Override
	public int compareTo(ProductDetails o) {
		return this.getLastModified().compareTo(this.getLastModified());
	}

}