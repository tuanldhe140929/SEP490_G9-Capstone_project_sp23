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
	private Date uploadDate;
	
	@Column(name="last_update")
	private Instant lastUpdate = Instant.now();
	
	@Column(name="price")
	private int price;

	@OneToMany(mappedBy="product")
	private List<Preview> previews = new ArrayList<Preview>();
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "product_tag", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<Tag> tags = new ArrayList<Tag>();
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", unique = false, nullable = false)
    private Type type;
	
	
	public Product() {}
	
	public Product(Long id, String name, String description,String localPath, String coverImage, Date uploadDate,
			int price, List<Tag> tags, List<Preview> previews, User user, Type type, String url) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.localPath = localPath;
		this.coverImage = coverImage;
		this.uploadDate = uploadDate;
		this.lastUpdate = Instant.now();
		this.price = price;
		this.tags = tags;
		this.previews = previews;
		this.user = user;
		this.type = type;
		this.url = url;
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

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Instant getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Instant lastUpdate) {
		this.lastUpdate = lastUpdate;
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

	public List<Preview> getPreviews() {
		return previews;
	}

	public void setPreviews(List<Preview> previews) {
		this.previews = previews;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
