package com.SEP490_G9.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.models.Entities.RefreshToken;
import com.SEP490_G9.models.Entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	public User findByUsername(String username);
	
	public User findByEmail(String email);
	
	public User findByRefreshToken(RefreshToken refreshToken);
	
	Boolean existsByEmail(String email);

	Boolean existsByUsername(String username);
}
