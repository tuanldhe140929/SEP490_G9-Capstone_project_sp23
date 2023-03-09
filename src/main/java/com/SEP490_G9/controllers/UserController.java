package com.SEP490_G9.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.common.Constant;
import com.SEP490_G9.common.GoogleAPI;
import com.SEP490_G9.common.JwtTokenUtil;
import com.SEP490_G9.common.PasswordGenerator;
import com.SEP490_G9.dto.AuthResponse;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.RefreshToken;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.AuthRequestException;
import com.SEP490_G9.service.AccountService;
import com.SEP490_G9.service.RoleService;
import com.SEP490_G9.service.UserService;
import com.SEP490_G9.service.authService.EmailService;
import com.SEP490_G9.service.authService.RefreshTokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping(value = "user")
@RestController
public class UserController {
	@Value("${jwtRefreshExpirationMs}")
	private int REFRESH_TOKEN_VALIDITY;

	@Autowired
	EmailService emailService;

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	AccountService accountService;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	GoogleAPI googleUtil;

	@Autowired
	PasswordGenerator passwordGenerator;

	@Autowired
	JwtTokenUtil jwtUtil;

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@Valid @RequestBody User user, HttpServletRequest request) {
		user.setUsername(user.getUsername().trim());
		if (user.getUsername().contains(" ")) {
			// invalidate input exception
			throw new AuthRequestException("username can not contains spaces");
		}

		List<Role> userRoles = new ArrayList<>();
		userRoles.add(roleService.getRoleById(Constant.USER_ROLE_ID));

		String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setCreatedDate(new Date());
		user.setRoles(userRoles);
		User saved = userService.createUser(user);
		Account account = accountService.getById(saved.getId());
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(account);
		emailService.sendVerifyEmail(account.getEmail());
		return ResponseEntity.ok(true);
	}

	@RequestMapping(value = "loginWithGoogle", method = RequestMethod.POST)
	public ResponseEntity<?> loginWithGoogle(@RequestBody final String code, HttpServletResponse response)
			throws ClientProtocolException, IOException {

		User googleLoginUser = googleUtil.getUserInfo(code);
		String randomPassword = passwordGenerator.generatePassword(8).toString();
		String encodedPassword = new BCryptPasswordEncoder().encode(randomPassword);
		googleLoginUser.setPassword(encodedPassword);
		googleLoginUser.setUsername(googleLoginUser.getEmail().substring(0, googleLoginUser.getEmail().indexOf("@")));
		googleLoginUser.setCreatedDate(new Date());
		googleLoginUser.setEmailVerified(true);
		googleLoginUser.setEnabled(true);
		List<Role> userRoles = new ArrayList<>();
		userRoles.add(roleService.getRoleById(Constant.USER_ROLE_ID));
		googleLoginUser.setRoles(userRoles);
		User saved = userService.createUser(googleLoginUser);
		Account account = accountService.getByEmail(googleLoginUser.getEmail());

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
		return ResponseEntity.ok(new AuthResponse(account.getEmail(), jwt, roles));
	}

	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setPath("/");
		cookie.setDomain("localhost");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		session.invalidate();
		request.getSession().invalidate();
		SecurityContextHolder.getContext().setAuthentication(null);
		return ResponseEntity.ok(null);
	}

	@RequestMapping(value = "sendVerifyEmail", method = RequestMethod.GET)
	public ResponseEntity<?> sendVerifyEmail(@RequestParam(name = "email") String email) {
		boolean ret = emailService.sendVerifyEmail(email);
		return ResponseEntity.ok(ret);
	}

	@RequestMapping(value = "verifyEmail/{verifyLink}", method = RequestMethod.GET)
	public ResponseEntity<?> verifyEmail(@PathVariable(name = "verifyLink", required = true) String verifyLink,
			@RequestParam(name = "email") String email) {
		boolean ret = false;
		Account account = accountService.getByEmail(email);
		if (verifyLink.equals(account.getRefreshToken().getToken())) {
			ret = true;
			User user = userService.getByEmail(account.getEmail());
			user.setEmailVerified(ret);
			userService.update(user);
		}
		return ResponseEntity.ok(ret);
	}
	
//	@GetMapping(value = "getUserInfo")
//	public ResponseEntity<?> getUserInfo() {
//		User user;
//		user = manageAccountInfoService.getUserInfo();
//		return ResponseEntity.ok(user);
//	}
//
//	@PostMapping(value = "changeAccountPassword")
//	public ResponseEntity<?> changeAccountPassword(
//			@Valid @RequestParam(name = "newPassword", required = true) @Size(min = 8, max = 30) String newPassword,
//			@Valid @RequestParam(name = "oldPassword", required = true) @Size(min = 8, max = 30) String oldPassword) {
//		User user;
//		boolean ret = manageAccountInfoService.changeAccountPassword(newPassword, oldPassword);
//		return ResponseEntity.ok(ret);
//	}
//
//	@PostMapping(value = "uploadProfileImage")
//	public ResponseEntity<?> uploadProfileImage(@RequestParam(name = "profileImage") MultipartFile profileImage)
//			throws IOException {
//		String src = manageAccountInfoService.uploadProfileImage(profileImage);
//		return ResponseEntity.ok(src);
//	}
//
//	@PostMapping(value = "changeAccountInfo")
//	public ResponseEntity<?> changeAccountInfo(
//			@Valid @RequestParam(name="newUserName", required = true) @Size(min = 3, max = 30) String newUserName,
//			@Valid @RequestParam(name="newFirstName", required = true) @Size(min = 3, max = 30) String newFirstName,
//			@Valid @RequestParam(name="newLastName", required = true) @Size(min = 3, max = 30) String newLastName){
//		User user;
//		user = manageAccountInfoService.changeAccountInfo(newUserName, newFirstName, newLastName);
//		return ResponseEntity.ok(user);
//	}
}
