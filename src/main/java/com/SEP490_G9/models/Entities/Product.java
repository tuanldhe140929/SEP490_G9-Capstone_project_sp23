package com.SEP490_G9.models.Entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties(value= {"user"})
@Entity
@Table(name="products")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="url")
	private String url;
	
	@Column(name="description")
	private String description;
	
	@Column(name="localPath")
	private String localPath;
	
	@Column(name="cover_image")
	private String coverImage;
	
	@Column(name="upload_date")
	private Date uploadDate = new Date();
	
	@Column(name="last_update")
	private Date lastUpdate = new Date();
	
	@Column(name="draft")
	private boolean draft = true;
	
	@Column(name="price")
	private int price = 0;

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
	
	
	public Product() {}
	
	public Product(User user) {
		this.user=user;
		this.uploadDate = new Date();
		this.draft = true;
	}
	
	public Product(Long id, String name, String description,String localPath, String coverImage, Date uploadDate,
			int price, List<Tag> tags, List<Preview> previews, User user, Type type, String url,boolean draft, Date lastUpdate) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.localPath = localPath;
		this.coverImage = coverImage;
		this.uploadDate = uploadDate;
		this.lastUpdate = lastUpdate;
		this.price = price;
		this.tags = tags;
		this.previews = previews;
		this.user = user;
		this.type = type;
		this.url = url;
		this.draft = draft;
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

	public String getLocalPath() {
		return localPath;
	}

	public Product setLocalPath(String localPath) {
		this.localPath = localPath;
		return this;
	}

	public User getUser() {
		return user;
	}

	public Product setUser(User user) {
		this.user = user;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Product setUrl(String url) {
		this.url = url;
		return this;
	}

	public boolean isDraft() {
		return draft;
	}

	public Product setDraft(boolean draft) {
		this.draft = draft;
		return this;
	}
	
	
	
}
