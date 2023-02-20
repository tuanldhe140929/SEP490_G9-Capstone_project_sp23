package com.SEP490_G9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.SEP490_G9.services.FileStorageService;

@EntityScan("com.SEP490_G9.models")
@ComponentScan(basePackages = "com.SEP490_G9")
@EnableJpaRepositories("com.SEP490_G9.repositories")
@SpringBootApplication
public class Sep490G9Application {

	public static void main(String[] args) {	
		SpringApplication.run(Sep490G9Application.class, args);
	}
	
	@Bean
	CommandLineRunner init(FileStorageService storageService) {
		return (args) -> {	
			String[] script = {"cmd.exe", "/c", "net", "start", "clamd"};
			Process process = Runtime.getRuntime().exec(script);
		    
			//storageService.deleteAll();
			storageService.init();
		};
	}

}
