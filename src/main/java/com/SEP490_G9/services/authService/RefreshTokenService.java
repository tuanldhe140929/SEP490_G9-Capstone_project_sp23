package com.SEP490_G9.services.authService;

import java.util.Optional;

import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.RefreshToken;
import com.SEP490_G9.models.Entities.User;

public interface RefreshTokenService {

	RefreshToken createRefreshToken(Account account);

	boolean verifyExpiration(RefreshToken token);

	int deleteByAccountId(Long accountId);

	RefreshToken findByToken(String token);
}
