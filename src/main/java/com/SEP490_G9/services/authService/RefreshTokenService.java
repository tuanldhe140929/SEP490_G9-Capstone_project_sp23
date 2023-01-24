package com.SEP490_G9.services.authService;

import java.util.Optional;

import com.SEP490_G9.models.Entities.RefreshToken;
import com.SEP490_G9.models.Entities.User;

public interface RefreshTokenService {

	RefreshToken createRefreshToken(User user);

	boolean verifyExpiration(RefreshToken token);

	int deleteByUserId(Long userId);

	RefreshToken findByToken(String token);
}
