package com.SEP490_G9.services.authService.authServiceImpl;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.SEP490_G9.exceptions.AuthRequestException;
import com.SEP490_G9.exceptions.DuplicateFieldException;
import com.SEP490_G9.exceptions.RefreshTokenException;
import com.SEP490_G9.exceptions.ResourceNotFoundException;
import com.SEP490_G9.helpers.Constant;
import com.SEP490_G9.helpers.GoogleLogin;
import com.SEP490_G9.models.DTOS.AuthRequest;
import com.SEP490_G9.models.DTOS.AuthResponse;
import com.SEP490_G9.models.DTOS.EmailResponse;
import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.RefreshToken;
import com.SEP490_G9.models.Entities.Role;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.repositories.AccountRepository;
import com.SEP490_G9.repositories.RoleRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.security.JwtTokenUtil;
import com.SEP490_G9.security.PasswordGenerator;
import com.SEP490_G9.services.authService.AuthService;
import com.SEP490_G9.services.authService.EmailService;
import com.SEP490_G9.services.authService.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthServiceImpl implements AuthService {

	@Value("${jwtRefreshExpirationMs}")
	private int REFRESH_TOKEN_VALIDITY;

	@Autowired
	AuthenticationProvider authenticationProvider;

	@Autowired
	JwtTokenUtil jwtUtil;

	@Autowired
	AccountRepository accountRepository;

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

	@Autowired
	EmailService emailService;

	@Override
	public boolean register(User user) {

		if (accountRepository.existsByEmail(user.getEmail())) {
			throw new DuplicateFieldException("email", user.getEmail());
		}
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new DuplicateFieldException("username", user.getUsername());
		}

		user.setUsername(user.getUsername().trim());
		if (user.getUsername().contains(" ")) {
			// invalidate input exception
			throw new AuthRequestException("username can not contains spaces");
		}
		List<Role> userRoles = new ArrayList<>();
		userRoles.add(roleRepository.getReferenceById(Constant.USER_ROLE_ID));
		String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setAccountCreatedDate(new Date());
		user.setRoles(userRoles);
		userRepository.save(user);
		Account account = accountRepository.findByEmail(user.getEmail());
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(account);
		return true;
	}

	@Override
	public AuthResponse login(AuthRequest authRequest, HttpServletResponse response) {

		Authentication authentication = authenticationProvider.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail().trim(), authRequest.getPassword()));

		if (authentication == null) {
			String trimedPassword = authRequest.getPassword().trim();
			authentication = authenticationProvider.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getEmail().trim(), trimedPassword));
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(account);
		Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
		cookie.setMaxAge(REFRESH_TOKEN_VALIDITY);
		cookie.setDomain("localhost");
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		response.addCookie(cookie);

		String jwt = jwtUtil.generateToken(account.getEmail());
		Object[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray();
		List<String> roles = new ArrayList<>();
		for (Object authority : authorities) {
			roles.add(authority.toString());
		}
		return new AuthResponse(account.getEmail(), jwt, roles);
	}

	@Override
	public AuthResponse loginWithGoogle(final String code, HttpServletResponse response)
			throws ClientProtocolException, IOException {
		User googleLoginUser = googleLogin.getUserInfo(code);
		googleLoginUser.setPassword(passwordGenerator.generatePassword(8).toString());

		googleLoginUser.setUsername(googleLoginUser.getEmail().substring(0, googleLoginUser.getEmail().indexOf("@")));

		if (!userRepository.existsByEmail(googleLoginUser.getEmail())) {
			googleLoginUser.setEmailVerified(true);
			register(googleLoginUser);
		}

		Account account = accountRepository.findByEmail(googleLoginUser.getEmail());

		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(account);
		Cookie cookie = new Cookie("refreshToken", refreshToken.getToken());
		cookie.setMaxAge(REFRESH_TOKEN_VALIDITY);
		cookie.setDomain("localhost");
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		response.addCookie(cookie);

		String jwt = jwtUtil.generateToken(account.getEmail());
		Object[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray();
		List<String> roles = new ArrayList<>();
		for (Object authority : authorities) {
			roles.add(authority.toString());
		}
		return new AuthResponse(account.getEmail(), jwt, roles);
	}

	public AuthResponse refreshToken(String token) {
		AuthResponse authResponse = new AuthResponse();
		RefreshToken refreshToken = refreshTokenService.findByToken(token);
		if (refreshTokenService.verifyExpiration(refreshToken)) {
			throw new RefreshTokenException(token, "Expired");
		} else {
			Account account = accountRepository.findByRefreshToken(refreshTokenService.findByToken(token));
			authResponse.setAccessToken(jwtUtil.generateToken(account.getEmail()));
			authResponse.setEmail(account.getEmail());
			Object[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray();
			List<String> roles = new ArrayList<>();
			for (Object authority : authorities) {
				roles.add(authority.toString());
			}
		}
		return authResponse;
	}

	@Override
	public AuthResponse validate(Cookie refreshTokenCookie) {
		String token = refreshTokenCookie.getValue();
		AuthResponse authResponse = refreshToken(token);
		return authResponse;
	}

	@Override
	public boolean verifyEmail(String verifyLink, String email) {

		boolean ret = false;
		Account account = accountRepository.findByEmail(email);

		if (verifyLink.equals(account.getRefreshToken().getToken())) {
			ret = true;
			User user = userRepository.findByEmail(account.getEmail());
			user.setEmailVerified(ret);
			userRepository.save(user);
			return ret;
		} else {
			return ret;
		}

	}

	@Override
	public EmailResponse sendVerifyEmail(String email) {
		if (email == null) {
			throw new AuthRequestException("User not logged in");
		}
		User user = userRepository.findByEmail(email);

		return emailService.sendVerifyEmail(email);
	}

	@Override
	public EmailResponse sendRecoveryPasswordToEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			return emailService.sendRecoveryPasswordToEmail(email);
		} else {
			throw new ResourceNotFoundException("Email", "email", email);
		}

	}

}
