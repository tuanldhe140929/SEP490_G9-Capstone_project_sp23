package com.SEP490_G9.models.Entities;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "email", nullable = false, unique = true)
	@Email(message = "invalid format")
	@NotBlank(message = "email can't be blank")
	private String email;

	@NotBlank(message = "password can't be blank")
	@Column(name = "password")
	@Size(min = 8, max = 100)
	private String password;

	@NotBlank(message = "username can't be blank")
	@Column(name = "username", nullable = true, unique = true)
	@Size(min = 3, max = 30)
	private String username;
	@Column(name = "enabled")
	private boolean enabled = true;
	@Column(name = "verified")
	private boolean verified = false;

	@Column(name="joined_date")
	private Date joinedDate = new Date();
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", unique = false, nullable = false)
	private Role role;

	@OneToOne(mappedBy = "user",fetch = FetchType.EAGER)
    private RefreshToken refreshToken;
	

	private String image;
//	@OneToOne(cascade = CascadeType.ALL)
//	private Cart cart;

	public User() {
	}

	public User(Long id, @Email(message = "invalid format") @NotBlank(message = "email can't be blank") String email,
			@NotBlank(message = "password can't be blank") @Size(min = 8, max = 100) String password,
			@NotBlank(message = "username can't be blank") @Size(min = 3, max = 30) String username, boolean enabled,
			boolean verified, Date joinedDate, Role role, RefreshToken refreshToken, String image) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.username = username;
		this.enabled = enabled;
		this.verified = verified;
		this.joinedDate = joinedDate;
		this.role = role;
		this.refreshToken = refreshToken;
		this.image = image;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public User(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

	public Date getJoinedDate() {
		return joinedDate;
	}

	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}

	public RefreshToken getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
