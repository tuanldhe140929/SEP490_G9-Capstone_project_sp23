package com.SEP490_G9.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.service.ProductDetailsService;

@RestController
@RequestMapping("/productDetails")
public class ProductDetailsController {
	
	@Autowired
	ProductDetailsService productDetailsService;
	
	@GetMapping(value = "/getFilteredProducts")
	public ResponseEntity<?> getFilteredProducts(@RequestParam("keyword") String keyword, @RequestParam("categoryid") int categoryid, @RequestParam("min") int min, @RequestParam("max") int max){
		List<ProductDetails> filteredProducts = productDetailsService.getByKeywordCategoryTags(keyword, categoryid, min, max);
		List<ProductDetailsDTO> filteredProductsDto = new ArrayList<>();
		for(ProductDetails result: filteredProducts) {
			filteredProductsDto.add(new ProductDetailsDTO(result));
		}
		return ResponseEntity.ok(filteredProductsDto);
	}
	
	@GetMapping(value = "/getProductsBySeller")
	public ResponseEntity<?> getProductsBySeller(@RequestParam("sellerid") long sellerid, @RequestParam("keyword") String keyword, @RequestParam("categoryid") int categoryid, @RequestParam("min") int min, @RequestParam("max") int max){
		List<ProductDetails> finalList = productDetailsService.getProductBySeller(sellerid,keyword,categoryid, min, max);
		List<ProductDetailsDTO> finalListDto = new ArrayList<>();
		for(ProductDetails result: finalList) {
			finalListDto.add(new ProductDetailsDTO(result));
		}
		return ResponseEntity.ok(finalListDto);
	}
}
