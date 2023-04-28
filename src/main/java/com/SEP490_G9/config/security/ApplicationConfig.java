package com.SEP490_G9.config.security;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.SEP490_G9.config.filter.TrimFilter;
import com.SEP490_G9.service.serviceImpls.CustomUserDetailsServiceImpl;

@Configuration
public class ApplicationConfig implements WebMvcConfigurer {
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
	
	 @Bean
	    public JavaMailSender javaMailSender() {
	        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	        mailSender.setHost("smtp.gmail.com");
	        mailSender.setPort(587);
	       
	        mailSender.setUsername("namdinhdvh@gmail.com");
	        mailSender.setPassword("hpkcganvdckblmyl");
	        
	        Properties props = mailSender.getJavaMailProperties();
	        props.put("mail.transport.protocol", "smtp");
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.debug", "true");
	        
	        return mailSender;
	    }
	 
//	 @Override
//	    public void configurePathMatch(PathMatchConfigurer configurer) {
//	        configurer.setUseSuffixPatternMatch(false)
//	                  .setUseTrailingSlashMatch(false)
//	                  .setPathMatcher(new AntPathMatcher());
//	    }
//	 
//	    @Bean
//	    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
//	        return new HandlerMappingIntrospector();
//	    }
}
