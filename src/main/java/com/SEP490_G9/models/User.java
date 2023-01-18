package com.SEP490_G9.models;

import java.io.Serializable;
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
	@Size(min = 5, max = 30)
	private String email;

	@NotBlank(message = "password can't be blank")
	@Column(name = "password")

	@Size(min = 8, max = 100)
	private String password;

	@Column(name = "username", nullable = true, unique = true)
	private String username;
	@Column(name = "enabled")
	private boolean enabled = true;
	@Column(name = "verified")
	private boolean verified = false;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", unique = false, nullable = false)
	private Role role;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private RefreshToken refreshToken;
	
	public User() {
	}

	public User(Long id,
			@Email(message = "invalid format") @NotBlank(message = "email can't be blank") @Size(min = 5, max = 30) String email,
			@NotBlank(message = "password can't be blank") @Size(min = 8, max = 100) String password, String username,
			boolean enabled, boolean verified) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.username = username;
		this.enabled = enabled;
		this.verified = verified;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
