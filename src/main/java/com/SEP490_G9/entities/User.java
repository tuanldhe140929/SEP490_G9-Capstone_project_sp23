package com.SEP490_G9.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name = "account_id")
public class User extends Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "username can't be blank")
	@Column(name = "username", nullable = false, unique = true)
	@Size(min = 3, max = 30)
	private String username;

	@Size(max = 255)
	@Column(name = "first_name")
	private String firstName;

	@Size(max = 255)
	@Column(name = "last_name")
	private String lastName;

	@Size(max = 255)
	@Column(name = "avatar")
	private String avatar;

	@Column(name = "email_verified", nullable = false)
	private boolean emailVerified = false;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Cart> carts;

	public User() {
	}

	public User(Account account) {
		super(account);
		
	}

	public User(@NotBlank(message = "username can't be blank") @Size(min = 3, max = 30) String username,
			String firstName, String lastName, String avatar, boolean emailVerified) {
		super();
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatar = avatar;
		this.emailVerified = emailVerified;
	}

	public User(Long id, @Email(message = "invalid format") @NotBlank(message = "email can't be blank") String email,
			@NotBlank(message = "password can't be blank") @Size(min = 8, max = 100) String password,
			Date accountCreatedDate, Date accountLastModifed, boolean enabled, List<Role> roles,
			RefreshToken refreshToken) {
		super(id, email, password, accountCreatedDate, accountLastModifed, enabled, roles, refreshToken);
		this.username = email.substring(0, email.indexOf("@"));
		this.emailVerified = true;
	}

	public User(String email) {
		setEmail(email);
	}

	public User(Long id, String email, String password, String username2) {
		super(id,email,password);
		this.username=username2;
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}
}
