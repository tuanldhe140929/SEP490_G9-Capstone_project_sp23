package com.SEP490_G9.models.Entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(value= {"user"})
@Entity
@Table(name="products")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="name",length = 30)
	private String name;
	
	@Column(name="description", length = 100)
	private String description;
	
	@Column(name="cover_image")
	private String coverImage;
	
	@Column(name="details")
	private String details;
	
	@Column(name="instruction")
	private String instructionSource;
	
	@Column(name="upload_date")
	private Date uploadDate; 
	
	@Column(name="last_update")
	private Date lastUpdate;
	
	@Column(name="draft")
	private boolean draft = true;
	
	@Column(name="price")
	private int price = 0;

	@Column(name="active")
	private boolean active = true;
	
	@OneToMany(mappedBy="product")
	private List<Preview> previews = new ArrayList<Preview>();
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "product_tag", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags = new ArrayList<Tag>();
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", unique = false)
    private Type type;
	
	@OneToMany(mappedBy="product",fetch = FetchType.EAGER)
	private List<ProductFile> files = new ArrayList<ProductFile>();
	
	public Product() {}
	
	public Product(User user) {
		this.user=user;
		this.uploadDate = new Date();
		this.draft = true;
	}

	public Product(Long id, String name, String description, String coverImage, String details,
			String instructionSource, Date uploadDate, Date lastUpdate, boolean draft, int price, boolean active,
			List<Preview> previews, User user, List<Tag> tags, Type type, List<ProductFile> files) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.coverImage = coverImage;
		this.details = details;
		this.instructionSource = instructionSource;
		this.uploadDate = uploadDate;
		this.lastUpdate = lastUpdate;
		this.draft = draft;
		this.price = price;
		this.active = active;
		this.previews = previews;
		this.user = user;
		this.tags = tags;
		this.type = type;
		this.files = files;
	}

	public List<ProductFile> getFiles() {
		return files;
	}

	public void setFiles(List<ProductFile> files) {
		this.files = files;
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

	public Product setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Product setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public Product setCoverImage(String coverImage) {
		this.coverImage = coverImage;
		return this;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public Product setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
		return this;
	}

	public int getPrice() {
		return price;
	}

	public Product setPrice(int price) {
		this.price = price;
		return this;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public Product setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
return this;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public Product setTags(List<Tag> tags) {
		this.tags = tags;
		return this;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public List<Preview> getPreviews() {
		return previews;
	}

	public Product setPreviews(List<Preview> previews) {
		this.previews = previews;
		return this;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public User getUser() {
		return user;
	}

	public Product setUser(User user) {
		this.user = user;
		return this;
	}

	public boolean isDraft() {
		return draft;
	}

	public Product setDraft(boolean draft) {
		this.draft = draft;
		return this;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getInstructionSource() {
		return instructionSource;
	}

	public void setInstructionSource(String instructionSource) {
		this.instructionSource = instructionSource;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
}
