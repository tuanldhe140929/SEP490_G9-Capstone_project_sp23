package com.SEP490_G9.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(value = { "refreshToken"})
@Table(name = "accounts")
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "email", nullable = false, unique = true)
	@Email(message = "invalid format")
	@Size(min = 3, max = 320)
	@NotBlank(message = "email can't be blank")
	private String email;

	@NotBlank(message = "password can't be blank")
	@Column(name = "password", nullable = false)
	@Size(min = 8, max = 100)
	private String password;

	@Column(name = "created_date" , nullable = false)
	private Date createdDate;

	@Column(name = "last_modified")
	private Date lastModified;

	@Column(name = "enabled", nullable = false)
	private boolean enabled = true;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles = new ArrayList<Role>();

	@OneToOne(mappedBy = "account", fetch = FetchType.EAGER)
	private RefreshToken refreshToken;

	public Account(String email) {
		this.email = email;
		this.enabled = true;
	}

	public Account() {
		// TODO Auto-generated constructor stub
	}

	public Account(Long id, @Email(message = "invalid format") @NotBlank(message = "email can't be blank") String email,
			@NotBlank(message = "password can't be blank") @Size(min = 8, max = 100) String password,
			Date accountCreatedDate, Date accountLastModifed, boolean enabled, List<Role> roles,
			RefreshToken refreshToken) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.createdDate = accountCreatedDate;
		this.lastModified = accountLastModifed;
		this.enabled = enabled;
		this.roles = roles;
		this.refreshToken = refreshToken;
	}

	public Account(Account account) {
		
	}

	public Account(Long id2, String email2, String password2) {
		this.id= id2;
		this.email=email2;
		this.password=password2;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public RefreshToken getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(RefreshToken refreshToken) {
		this.refreshToken = refreshToken;
	}

}
