package com.SEP490_G9.models.Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.SEP490_G9.helpers.Constant;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@PrimaryKeyJoinColumn(name="account_id")
public class User extends Account implements Serializable{

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "username can't be blank")
	@Column(name = "username", nullable = false, unique = true)
	@Size(min = 3, max = 30)
	private String username;
	
	@Column(name="first_name" )
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name = "avatar")
	private String avatar;
	
	@Column(name = "email_verified")
	private boolean emailVerified = false;

	@Column(name = "created_date")
	private Date userCreatedDate;

	@Column(name="last_modified")
	private Date userLastModified;
	
	@OneToOne(cascade = CascadeType.ALL)
	private Cart cart;

	public User() {
	}
	
	public User(Account account) {
		
	}

	public User(@NotBlank(message = "username can't be blank") @Size(min = 3, max = 30) String username,
			String firstName, String lastName, String avatar, boolean emailVerified, Date userCreatedDate,
			Date userLastModified) {
		super();
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatar = avatar;
		this.emailVerified = emailVerified;
		this.userCreatedDate = userCreatedDate;
		this.userLastModified = userLastModified;
	}
	
	public User(Long id, @Email(message = "invalid format") @NotBlank(message = "email can't be blank") String email,
			@NotBlank(message = "password can't be blank") @Size(min = 8, max = 100) String password,
			Date accountCreatedDate, Date accountLastModifed, boolean enabled, List<Role> roles,
			RefreshToken refreshToken) {
		super(id, email, password, accountCreatedDate, accountLastModifed, enabled, roles, refreshToken);
		this.username = email.substring(0, email.indexOf("@"));
		this.emailVerified = true;
		this.userCreatedDate = new Date();
		this.userLastModified = new Date();
	}

	public User(String email) {
		setEmail(email);
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

	public Date getUserCreatedDate() {
		return userCreatedDate;
	}

	public void setUserCreatedDate(Date createdDate) {
		this.userCreatedDate = createdDate;
	}

	public Date getUserLastModified() {
		return userLastModified;
	}

	public void setUserLastModified(Date userLastModified) {
		this.userLastModified = userLastModified;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
