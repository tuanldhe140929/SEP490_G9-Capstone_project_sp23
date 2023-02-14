package com.SEP490_G9.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SEP490_G9.models.DTOS.ProductDTO;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.services.CommonService;

@RestController
@RequestMapping(value="public/common")
public class CommonController {
	@Autowired
	CommonService commonService;
	
	@GetMapping(value="getCurrentLogedInUser")
	public ResponseEntity<?> getCurrentLogedInUser() {
		User user = commonService.getCurrentLogedInUser();
		return ResponseEntity.ok(user);
	}
	
	@GetMapping(value="getProductByNameAndUserId")
	public ResponseEntity<?> getProductByNameAndUserId(@RequestParam(name="productName",required=true) String productName,@RequestParam(name="userId",required=true) Long userId) {
		ProductDTO product = commonService.getProductByNameAndUserId(productName,userId);
		return ResponseEntity.ok(product);
	}
	
	@GetMapping(value="getUserInfoByUsername")
	public ResponseEntity<?> getUserInfoByUsername(@RequestParam String username) {
		User user = commonService.getUserInfoByUsername(username);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping(value="getProductsByUsername")
	public ResponseEntity<?> getProductsByUsername(@RequestParam String username) {
		List<ProductDTO> products = commonService.getProductsByUsername(username);
		return ResponseEntity.ok(products);
	}
}
