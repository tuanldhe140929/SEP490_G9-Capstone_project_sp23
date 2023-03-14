package com.SEP490_G9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.service.SellerService;

@RestController
@RequestMapping("/seller")
public class SellerController {

	@Autowired
	SellerService sellerService;

	@GetMapping("/getSeller")
	public ResponseEntity<?> getSellerById(@RequestParam("sellerid") long sellerid) {
		Seller seller = sellerService.getSellerById(sellerid);
		return ResponseEntity.ok(seller);
	}
}
