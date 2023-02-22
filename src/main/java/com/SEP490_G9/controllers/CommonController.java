package com.SEP490_G9.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entity.User;
import com.SEP490_G9.service.CommonService;

@RestController
@RequestMapping(value="public/common")
public class CommonController {
	@Autowired
	CommonService commonService;
	
	@GetMapping(value="getProductByIdAndUserId")
	public ResponseEntity<?> getProductByNameAndUserId(@RequestParam(name="productId",required=true) Long productId,@RequestParam(name="userId",required=true) Long userId) {
		ProductDetailsDTO product = commonService.getLastVersionProductDetailsDTOByIdAndUserId(productId,userId);
		return ResponseEntity.ok(product);
	}
	
	@GetMapping(value="getUserInfoByUsername")
	public ResponseEntity<?> getUserInfoByUsername(@RequestParam String username) {
		User user = commonService.getUserInfoByUsername(username);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping(value="getProductsByUsername")
	public ResponseEntity<?> getProductsByUsername(@RequestParam String username) {
		List<ProductDetailsDTO> products = commonService.getProductsByUsername(username);
		return ResponseEntity.ok(products);
	}
	
	@GetMapping(value="checkIfPurchased")
	public ResponseEntity<?> checkIfPurchased(@RequestParam(name="userId",required=true) Long userId,
			@RequestParam(name="productId",required=true) Long productId){
		boolean ret = commonService.checkIfPurchased(userId,productId);
		return ResponseEntity.ok(ret);
	}
	
	@GetMapping(value="getSellerTotalProductCount")
	public ResponseEntity<?> totalProductCount(@RequestParam(name="sellerId", required = true) Long sellerId){
		int ret = commonService.totalProductCount(sellerId);
		return ResponseEntity.ok(ret);
	}
}
