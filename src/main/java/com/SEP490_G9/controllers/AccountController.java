package com.SEP490_G9.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.common.JwtTokenUtil;
import com.SEP490_G9.common.PasswordGenerator;
import com.SEP490_G9.dto.AuthRequest;
import com.SEP490_G9.dto.AuthResponse;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.RefreshToken;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.service.AccountService;
import com.SEP490_G9.service.authService.EmailService;
import com.SEP490_G9.service.authService.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@RequestMapping(value = "account")
@RestController
public class AccountController {
	@Autowired
	EmailService emailService;

	@Value("${jwtRefreshExpirationMs}")
	private int REFRESH_TOKEN_VALIDITY;

	@Autowired
	AuthenticationProvider authenticationProvider;

	@Autowired
	JwtTokenUtil jwtUtil;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	AccountService accountService;

	@Autowired
	PasswordGenerator passwordGenerator;

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@Valid @RequestBody AuthRequest authRequest, HttpServletResponse response,
			HttpServletRequest request) {
		AuthResponse authResponse = null;

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
		HttpSession session = request.getSession(true);

		session.setAttribute("key", "value");
		System.out.println("on login" + session.getId());

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

		authResponse = new AuthResponse(account.getEmail(), jwt, roles);
		return ResponseEntity.ok(authResponse);
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(name="token") String token) {

		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setPath("/");
		cookie.setDomain("localhost");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(0);
		jwtUtil.invalidateToken(token);
		response.addCookie(cookie);
		SecurityContextHolder.getContext().setAuthentication(null);
		return ResponseEntity.ok(null);
	}

	@RequestMapping(value = "resetPassword", method = RequestMethod.POST)
	public ResponseEntity<?> resetPassword(@Valid @Email @RequestParam(required = true) String email) {

		Account account = accountService.getByEmail(email);
		String newPassword = accountService.resetPassword(account);
		boolean ret = emailService.sendRecoveryPasswordToEmail(email, newPassword);
		return ResponseEntity.ok(ret);
	}

	@GetMapping("staffs")
	public ResponseEntity<?> getAllStaffs() {
		List<Account> staffList = accountService.getAllStaffs();
		return ResponseEntity.ok(staffList);
	}

	@PostMapping("addStaff")
	public ResponseEntity<?> addStaff(@RequestBody Account staff) {
		Account addedStaff = accountService.addStaff(staff);
		return ResponseEntity.ok(addedStaff);
	}

	@PutMapping("updateStaffStatus")
	public ResponseEntity<?> updateStaffStatus(@PathVariable(name = "id") Long id) {
		Account updatedStaff = accountService.updateStaffStatus(id);
		return ResponseEntity.ok(updatedStaff);
	}
	
	@GetMapping("allAccounts")
	public ResponseEntity<?> getAllAccounts(){
		List<Account> allAccounts = accountService.getAllAccounts();
		return ResponseEntity.ok(allAccounts);
	}
}
