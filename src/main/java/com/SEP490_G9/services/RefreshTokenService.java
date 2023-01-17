package com.SEP490_G9.services;

import java.util.Optional;

import com.SEP490_G9.models.RefreshToken;
import com.SEP490_G9.models.User;

public interface RefreshTokenService {

	RefreshToken createRefreshToken(User user);

	boolean verifyExpiration(RefreshToken token);

	int deleteByUserId(Long userId);

	RefreshToken findByToken(String token);
}
