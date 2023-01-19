package com.SEP490_G9.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.SEP490_G9.models.DTOS.User;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private User user;

	public UserDetailsImpl() {

	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		if (this.user == null) {
			return null;
		}
		return this.user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}

	public void setUser(User domainUser) {
		this.user = domainUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
//		List<UserRole> roles = null;
//		if (user != null) {
//			roles = user.getRoles();
//		}
//		if (roles != null) {
//			for (UserRole role : roles) {
//				authorities.add(new SimpleGrantedAuthority(role.getName()));
//			}
//		}
		String role = user.getRole().getName();
		authorities.add(new SimpleGrantedAuthority(role));
		return authorities;
	}

	public User getUser() {
		return user;
	}
	
	
}
