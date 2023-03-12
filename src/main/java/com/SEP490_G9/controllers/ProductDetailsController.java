package com.SEP490_G9.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public ResponseEntity<?> getFilteredProducts(@RequestParam("keyword") String keyword,
			@RequestParam("categoryid") int categoryid, @RequestParam("min") int min, @RequestParam("max") int max) {
		List<ProductDetails> filteredProducts = productDetailsService.getByKeywordCategoryTags(keyword, categoryid, min,
				max);
		List<ProductDetailsDTO> filteredProductsDto = new ArrayList<>();
		for (ProductDetails result : filteredProducts) {
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


	@GetMapping(value = "getPublishedProductsBySeller")
	public ResponseEntity<?> getProductsBySeller(@RequestParam(name = "sellerId") Long sellerId) {
		List<ProductDetails> publishedProducts = productDetailsService.getBySellerIdAndIsDraft(sellerId, false);
		List<ProductDetailsDTO> dtos = new ArrayList<>();
		for (ProductDetails pd : publishedProducts) {
			dtos.add(new ProductDetailsDTO(pd));
		}

		return ResponseEntity.ok(dtos);
	}

	@GetMapping(value = "getActiveVersion")
	public ResponseEntity<?> getActiveVersion(@RequestParam(name = "productId") Long productId) {
		ProductDetails pd = productDetailsService.getActiveVersion(productId);
		ProductDetailsDTO dto = new ProductDetailsDTO(pd);
		return ResponseEntity.ok(dto);
	}

	@GetMapping(value = "getAllVersion")
	public ResponseEntity<?> getAllVersion(@RequestParam(name = "productId", required = true) Long productId) {
		List<ProductDetails> productDetailss = productDetailsService.getAllByProductId(productId);
		List<ProductDetailsDTO> ret = new ArrayList<>();
		for (ProductDetails dt : productDetailss) {
			ret.add(new ProductDetailsDTO(dt));
		}
		return ResponseEntity.ok(ret);
	}

	// get by id and
	@PostMapping(value = "getByIdAndVersion")
	public ResponseEntity<?> createNewVersionV2(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "version", required = true) String newVersion) {
		ProductDetails productDetails = productDetailsService.getByProductIdAndVersion(productId, newVersion);
		ProductDetailsDTO ret = new ProductDetailsDTO(productDetails);
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value = "createNewVersionV2")
	public ResponseEntity<?> createNewVersionV2(@RequestBody ProductDetailsDTO productDetailsDTO,
			@RequestParam(name = "newVersion", required = true) String newVersion) {
		ProductDetails newPD = productDetailsService.createNewVersion(productDetailsDTO.getId(), newVersion);
		ProductDetailsDTO dto = new ProductDetailsDTO(newPD);
		return ResponseEntity.ok(dto);
	}
}
