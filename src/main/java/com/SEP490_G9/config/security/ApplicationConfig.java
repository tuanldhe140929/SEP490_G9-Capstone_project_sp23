package com.SEP490_G9.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.SEP490_G9.config.filter.TrimFilter;
import com.SEP490_G9.service.serviceImpls.CustomUserDetailsServiceImpl;

@Configuration
public class ApplicationConfig {
	@Autowired
	CustomUserDetailsServiceImpl customUserDetailsService;

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(customUserDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public FilterRegistrationBean<TrimFilter> trimFilter() {
		FilterRegistrationBean<TrimFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new TrimFilter());
		registrationBean.addUrlPatterns("/product/*");
		return registrationBean;
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
