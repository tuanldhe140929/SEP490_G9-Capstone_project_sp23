package com.SEP490_G9.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.serviceImpls.CustomUserDetailsServiceImpl;


@Configuration
@Component
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {
@Autowired
JwtAuthenticationEntryPoint authenEntryPoint;
	
	@Value("${spring.websecurity.debug:false}")
	boolean webSecurityDebug;

	@Autowired
	AuthenticationProvider authProvider;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	CustomUserDetailsServiceImpl userDetailsService;

	@Autowired
	JwtRequestFilter filter;
	@Bean
	public SecurityFilterChain setfilterChains(HttpSecurity http) throws Exception {

		http
		.cors().and().csrf().disable().authorizeHttpRequests()
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeHttpRequests()
		.requestMatchers("/public/**").permitAll()
		.and().httpBasic().authenticationEntryPoint(authenEntryPoint)
		.and().authorizeHttpRequests().anyRequest().authenticated()
		.and().authenticationProvider(authProvider)
		.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*");
			}
		};
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.debug(webSecurityDebug);
	}


}
