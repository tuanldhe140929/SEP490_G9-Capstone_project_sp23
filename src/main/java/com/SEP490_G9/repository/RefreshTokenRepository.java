package com.SEP490_G9.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.RefreshToken;
import com.SEP490_G9.entity.User;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
	RefreshToken findByToken(String token);

	  @Modifying
	  int deleteByAccount(User user);

	RefreshToken findByAccount(Account account);
}