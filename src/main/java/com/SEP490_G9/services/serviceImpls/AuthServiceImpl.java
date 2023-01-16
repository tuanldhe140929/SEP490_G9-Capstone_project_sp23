package com.SEP490_G9.services.serviceImpls;

import java.io.IOException;
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

import com.SEP490_G9.helpers.GoogleLogin;
import com.SEP490_G9.models.AuthRequest;
import com.SEP490_G9.models.AuthResponse;
import com.SEP490_G9.models.Role;
import com.SEP490_G9.models.User;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.repositories.RoleRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.security.JwtTokenUtil;
import com.SEP490_G9.security.PasswordGenerator;
import com.SEP490_G9.services.AuthService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import jakarta.servlet.http.HttpServletRequest;


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
	@Override
	public int register(User user) {
		String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setEnabled(true);
		user.setVerified(false);

		user.setRole(roleRepository.getReferenceById((long) 2));
		userRepository.save(user);
		// gui mail;;
		return 1;
	}

	@Override
	public AuthResponse login(AuthRequest authRequest)  {

		User user = userRepository.findByEmail(authRequest.getEmail());
	
		AuthResponse response = authenticateUser(user, authRequest);
		return response;
		
	}

	private AuthResponse authenticateUser(User user, AuthRequest authRequest) {
		String jwt = "";
		String role = "";
		Authentication authentication = authenticationProvider.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
	
			jwt = jwtUtil.generateToken(authRequest.getEmail());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			Object[] authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray();
			role = authorities[0].toString();
			
			return new AuthResponse(authRequest.getEmail(), jwt, role);
		
	}

	@Override
	public AuthResponse loginWithGoogle(final String code, HttpServletRequest request)
			throws ClientProtocolException, IOException {
		User googleLoginUser = googleLogin.getUserInfo(code);
		String jwt = "";

		User foundUser = userRepository.findByEmail(googleLoginUser.getEmail());
		if (foundUser == null) {
			// chua co trong database thi register 
			googleLoginUser.setPassword(passwordGenerator.generatePassword(8).toString());
			register(googleLoginUser);
			foundUser = userRepository.findByEmail(googleLoginUser.getEmail());
		}
		
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setUser(foundUser);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());

		jwt = jwtUtil.generateToken(foundUser.getEmail());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String role = foundUser.getRole().toString();
		return new AuthResponse(foundUser.getEmail(), jwt, role);
	}
}
