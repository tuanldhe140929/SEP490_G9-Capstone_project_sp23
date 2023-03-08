package com.SEP490_G9.service.authService;

import java.util.Optional;

import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.RefreshToken;

public interface RefreshTokenService {

	RefreshToken createRefreshToken(Account user);

	boolean verifyExpiration(RefreshToken token);

	int deleteByAccountId(Long accountId);

	RefreshToken getByToken(String token);
}
