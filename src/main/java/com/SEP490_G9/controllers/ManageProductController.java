package com.SEP490_G9.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.services.FileStorageService;
import com.SEP490_G9.services.ManageProductService;

import org.springframework.core.io.Resource;

@RequestMapping(value = "private/manageProduct")
@RestController
public class ManageProductController {

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
		user2.setVerified(user.isVerified());
		user2.setEmail(user.getEmail());
		user2.setPassword(null);
		user2.setRole(user.getRole());
		user2.setUsername(user.getUsername());
		return ResponseEntity.ok(user2);
	}

	@GetMapping(value = "getProductsByUser")
	public ResponseEntity<?> getProductsByUser() {
		List<Product> products = manageProductService.getProductsByUser();
		return ResponseEntity.ok(products);
	}

	@PostMapping(value = "newProductRequest")
	public ResponseEntity<?> newProductRequest() {
		Product ret = manageProductService.newProduct();
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value = "addProduct")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		Product ret = manageProductService.addProduct(product);
		return ResponseEntity.ok(ret);
	}

	@DeleteMapping(value = "deleteProduct/{id}")
	public ResponseEntity<?> deleteProducts(@PathVariable(name = "id") Long id) {
		Product ret = manageProductService.deleteProduct(id);
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value = "updateProduct")
	public ResponseEntity<?> updateProduct(@RequestBody Product product) {
		Product ret = manageProductService.updateProduct(product);
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value="createNewProductRequest")
	public ResponseEntity<?> createNewProductRequest(){
		Product product = manageProductService.createNewProduct();
		return ResponseEntity.ok(product);
	}
	
	@PostMapping(value = "uploadCoverImage")
	public ResponseEntity<?> uploadCoverImage(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "coverImage") MultipartFile coverImage) throws IOException {
		Product product = manageProductService.uploadCoverImage(coverImage, productId);
		return ResponseEntity.ok(product);
	}

	@GetMapping("serveCoverImage")
	public ResponseEntity<byte[]> serveCoverImage(@RequestParam(name = "productId") long productId) {
		File file = manageProductService.getCoverImage(productId);
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		byte[] image = new byte[0];
		try {
			image = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().contentType(MediaType.valueOf(mimeType)).body(image);
	}

}
