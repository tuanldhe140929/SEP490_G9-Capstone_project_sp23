package com.SEP490_G9.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
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
	JwtRequestFilter filter;
	@Bean
	public SecurityFilterChain setfilterChains(HttpSecurity http) throws Exception {

		http.cors().configurationSource(request ->{
			CorsConfiguration configuration = new CorsConfiguration();
			configuration.setAllowedOrigins(List.of("http://localhost:4200"));
			configuration.setAllowCredentials(true);
			configuration.setAllowedMethods(List.of("HEAD","GET","POST","PUT","DELETE","PATCH","OPTIONS"));
			configuration.addExposedHeader("Message");
			configuration.setAllowedHeaders(List.of("Authorization","Cache-Control","Content-Type"));
			return configuration;
		}).and().csrf().disable();
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	
		http.authorizeHttpRequests()
		.requestMatchers("/public/**").permitAll().and()
		.authorizeHttpRequests().requestMatchers("/home").hasAnyRole("USER")
		//.and().authorizeHttpRequests().requestMatchers("/private/api/cart").hasAnyRole("USER")
		.and().authorizeHttpRequests().requestMatchers("/private/**").hasAnyRole("USER")
		.and().httpBasic().authenticationEntryPoint(authenEntryPoint)
		.and().authorizeHttpRequests().anyRequest().authenticated()
		.and().authenticationProvider(authProvider)
		.logout().logoutRequestMatcher(new AntPathRequestMatcher("public/auth/logout"))
		.and().addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
		
		return http.build();
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**").allowedMethods("*").allowedOrigins("http://localhost:4200")
//				.allowCredentials(true).exposedHeaders("Message").allowedHeaders("*");
//			}
//		};
//	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.debug(webSecurityDebug);
	}

	
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

}
