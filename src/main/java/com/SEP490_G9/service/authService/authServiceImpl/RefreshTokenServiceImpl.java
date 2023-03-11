package com.SEP490_G9.service.authService.authServiceImpl;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.RefreshToken;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.RefreshTokenRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.authService.RefreshTokenService;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
	@Value("${jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public RefreshToken getByToken(String token) {
		RefreshToken refreshToken = refreshTokenRepository.findByToken(token);
		if (refreshToken == null) {
			throw new ResourceNotFoundException("refresh token", "token", token);
		}
		return refreshToken;

	}

	@Override
	public RefreshToken createRefreshToken(Account account) {
		RefreshToken refreshToken;
		refreshToken = refreshTokenRepository.findByAccount(account);
		if (refreshToken == null) {
			refreshToken = new RefreshToken();
		}
		refreshToken.setAccount(account);
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	@Override
	public boolean verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public int deleteByAccountId(Long accoutId) {
		return refreshTokenRepository.deleteByAccount(userRepository.findById(accoutId).get());
	}
}
