package com.SEP490_G9.services.serviceImpls;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.CustomException;
import com.SEP490_G9.helpers.GoogleLogin;
import com.SEP490_G9.models.AuthRequest;
import com.SEP490_G9.models.AuthResponse;
import com.SEP490_G9.models.RefreshToken;
import com.SEP490_G9.models.Role;
import com.SEP490_G9.models.User;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.repositories.RoleRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.security.JwtTokenUtil;
import com.SEP490_G9.security.PasswordGenerator;
import com.SEP490_G9.services.AuthService;
import com.SEP490_G9.services.RefreshTokenService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	AuthenticationProvider authenticationProvider;

	@Autowired
	JwtTokenUtil jwtUtil;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	GoogleLogin googleLogin;

	@Autowired
	PasswordGenerator passwordGenerator;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Override
	public int register(User user) {
		String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setEnabled(true);
		user.setVerified(false);
		user.setRole(roleRepository.getReferenceById((long) 2));
		try {
			userRepository.save(user);}
		catch(Exception e) {
			throw new CustomException("Can't register new user");
		}
		
		return 1;
	}

	@Override
	public AuthResponse login(AuthRequest authRequest, HttpServletResponse response) {
		User user = userRepository.findByEmail(authRequest.getEmail());

		Authentication authentication = authenticationProvider.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		if (authRequest.isRememberMe()) {
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
			Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
			cookie.setMaxAge(120000);
			cookie.setDomain("localhost");
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			cookie.setSecure(true);
			response.addCookie(cookie);
		}

		String jwt = jwtUtil.generateToken(authRequest.getEmail());
		Object[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray();
		String role = authorities[0].toString();
		return new AuthResponse(authRequest.getEmail(), jwt, role);
	}

	@Override
	public AuthResponse loginWithGoogle(final String code, HttpServletRequest request)
			throws ClientProtocolException, IOException {
		User googleLoginUser = googleLogin.getUserInfo(code);
		User foundUser = userRepository.findByEmail(googleLoginUser.getEmail());

		if (foundUser == null) {
			googleLoginUser.setPassword(passwordGenerator.generatePassword(8).toString());
			register(googleLoginUser);
			foundUser = userRepository.findByEmail(googleLoginUser.getEmail());
		}

		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setUser(foundUser);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtil.generateToken(foundUser.getEmail());
		String role = foundUser.getRole().toString();
		return new AuthResponse(foundUser.getEmail(), jwt, role);
	}

	public AuthResponse refreshToken(String token) {
		RefreshToken refreshToken = refreshTokenService.findByToken(token);
		boolean isExpired = refreshTokenService.verifyExpiration(refreshToken);
		AuthResponse authResponse = new AuthResponse();
		if (isExpired) {
			throw new CustomException("Refresh token expired");
		} else {
			User user = userRepository.findByRefreshToken(refreshTokenService.findByToken(token));

			authResponse.setAccessToken(jwtUtil.generateToken(user.getEmail()));
			authResponse.setEmail(user.getEmail());
			authResponse.setRole(user.getRole().toString());
		}
		return authResponse;
	}

	@Override
	public AuthResponse validate(Cookie refreshTokenCookie) {
		String token = refreshTokenCookie.getValue();
		AuthResponse authResponse = refreshToken(token);
		return authResponse;
	}

}
