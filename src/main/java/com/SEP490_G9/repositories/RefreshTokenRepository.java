package com.SEP490_G9.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.SEP490_G9.models.Entities.RefreshToken;
import com.SEP490_G9.models.Entities.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
	RefreshToken findByToken(String token);
	
	RefreshToken findByUser(User user);

	  @Modifying
	  int deleteByUser(User user);
}
