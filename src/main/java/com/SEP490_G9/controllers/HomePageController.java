package com.SEP490_G9.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductService;

@RestController
@RequestMapping("/home")
public class HomePageController {
@Autowired
ProductService productService;
@Autowired
ProductDetailsRepository productDetailRepo;
@Autowired
ProductDetailsService productDetailsService;

@GetMapping(value = "/getAllProducts")
public ResponseEntity<?> getAllProducts(){
	List<ProductDetails> allProducts = productDetailsService.getAll();
	List<ProductDetailsDTO> allProductsDto = new ArrayList<>();
	for(ProductDetails product: allProducts) {
		allProductsDto.add(new ProductDetailsDTO(product));
	}
	return ResponseEntity.ok(allProductsDto);
}
@GetMapping(value = "getByApprovalStatus")
public ResponseEntity<?> getByApprovalStatus(@RequestParam(name = "status") String status){
	List<ProductDetails> allStatusPd = productDetailsService.getProductsByApprovalStatus(status);
	List<ProductDetailsDTO> allDtoPd = new ArrayList<>();
	for(ProductDetails pd: allStatusPd) {
		allDtoPd.add(new ProductDetailsDTO(pd));
	}
	return ResponseEntity.ok(allDtoPd);
}

}
