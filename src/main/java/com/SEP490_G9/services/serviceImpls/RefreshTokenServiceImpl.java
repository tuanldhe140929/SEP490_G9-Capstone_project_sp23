package com.SEP490_G9.services.serviceImpls;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.SEP490_G9.models.RefreshToken;
import com.SEP490_G9.models.User;
import com.SEP490_G9.repositories.RefreshTokenRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.RefreshTokenService;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
	@Value("${jwtRefreshExpiration}")
	private Long refreshTokenDurationMs;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public RefreshToken findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	@Override
	public RefreshToken createRefreshToken(User user) {
		RefreshToken refreshToken;
		refreshToken = refreshTokenRepository.findByUser(user);
		if(refreshToken == null) {
			refreshToken = new RefreshToken();
		}
		refreshToken.setUser(user);
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
	public int deleteByUserId(Long userId) {
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}
}
