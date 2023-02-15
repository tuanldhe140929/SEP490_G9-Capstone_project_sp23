package com.SEP490_G9.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.services.FileStorageService;
import com.SEP490_G9.services.ManageProductService;

@RequestMapping(value = "private/report")
@RestController
public class ReportController {

	@Autowired
	ManageProductService manageProductService;

	@Autowired
	FileStorageService fileStorageService;

	@GetMapping(value = "getCurrentUserInfo")
	public ResponseEntity<?> getCurrentUserInfo() {
		User user = manageProductService.getCurrentUserInfo();
		User user2 = new User();
		user2.setId(user.getId());
		user2.setEnabled(user.isEnabled());
		user2.setEmail(user.getEmail());
		user2.setPassword(null);
		user2.setUsername(user.getUsername());
		return ResponseEntity.ok(user2);
	}

	@GetMapping(value = "getProductsByUser")
	public ResponseEntity<?> getProductsByUser() {
		List<Product> products = manageProductService.getProductsByUser();
		return ResponseEntity.ok(products);
	}

	@PostMapping(value = "addProduct")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		Product ret = manageProductService.addProduct(product);
		return ResponseEntity.ok(ret);
	}

}
