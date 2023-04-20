package com.SEP490_G9.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.RefreshToken;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.entities.User;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByUsername(String username);

	public User findByEmail(String email);

	Boolean existsByEmail(String email);

	Boolean existsByUsername(String username);

	public List<User> findByEnabled(boolean enabled);

}
