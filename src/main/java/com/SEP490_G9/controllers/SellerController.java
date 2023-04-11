package com.SEP490_G9.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.AccountService;
import com.SEP490_G9.service.SellerService;
import com.SEP490_G9.service.serviceImpls.AccountServiceImpl;
import com.SEP490_G9.service.serviceImpls.SellerServiceImpl;
import com.SEP490_G9.service.serviceImpls.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/seller")
public class SellerController {
	@Autowired
	SellerRepository sellerRepository;
	@Autowired
	SellerService sellerService;
	@Autowired
	AccountService accountService;

	@GetMapping("/getSeller")
	public ResponseEntity<?> getSellerById(@RequestParam("sellerid") long sellerid) {
		Seller seller = sellerService.getSellerById(sellerid);
		return ResponseEntity.ok(seller);
	}

	@GetMapping("/getSellersForSearching")
	public ResponseEntity<?> getSellersForSearching(@RequestParam("keyword") String keyword) {
		List<Seller> sellerList = sellerService.getSellersForSearching(keyword);
		return ResponseEntity.ok(sellerList);
	}

	@PostMapping(value = "/createNewSeller")
	public ResponseEntity<?> createNewSeller(@RequestParam("paypalEmail") String paypalEmail,
			HttpServletRequest request) {

		boolean seller  ;

		seller = sellerService.createNewSeller(paypalEmail);
		return ResponseEntity.ok(seller);
	}
	@GetMapping(value = "/getSellerByPaypalEmail")
	public ResponseEntity<?> getSellerByPaypalEmail(@RequestParam("paypalEmail") String paypalEmail) {
		Seller seller = sellerService.getSellerByPaypalEmail(paypalEmail);
		return ResponseEntity.ok(seller);
	}
	@GetMapping("/reportedsellerlist")
	public ResponseEntity<?> getReportedSeller() {
		List<Seller> seller = sellerService.getReportedSellers();
		return ResponseEntity.ok(seller);
	}
}
