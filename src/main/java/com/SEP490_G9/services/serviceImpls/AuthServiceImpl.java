package com.SEP490_G9.services.serviceImpls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.models.AuthRequest;
import com.SEP490_G9.models.AuthResponse;
import com.SEP490_G9.models.Role;
import com.SEP490_G9.models.User;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.security.JwtTokenUtil;
import com.SEP490_G9.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;


@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	AuthenticationProvider authenticationProvider;
	
	@Autowired
	JwtTokenUtil jwtUtil;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public int register(User user) {
		String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setEnabled(true);
		user.setVerified(false);

		user.setRole(new Role((long) 2,"ROLE_USER"));
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
	public AuthResponse loginWithGoogle(String code, HttpServletRequest request)
			throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
