package com.SEP490_G9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/transaction")
@RestController
public class TransactionController {
	// tao 1 transaction voi paypal
	// redirect user toi man hinh thanh toan
	// thanh toan xong, nhan success url
	// thanh toan khong thanh cong, nhan cancel url

	@Autowired
	TransactionService transactionService;

	@PostMapping("/purchase")
	public ResponseEntity<?> purchase(@RequestParam(name = "cartId") Long cartId) {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		Transaction transaction = transactionService.purchase(cartId, account);
		System.out.println(transaction);
		return ResponseEntity.ok(transaction);
	}

	@GetMapping("/processTransaction")
	public ResponseEntity<?> success(@RequestParam("paymentId") String paymentId,
			@RequestParam("token") String token,
			@RequestParam("PayerID") String payerId) {
		Transaction transaction = transactionService.processTransaction(paymentId,token, payerId);

		return ResponseEntity.ok(transaction);
	}

	@GetMapping("/cancel")
	public ResponseEntity<?> cancel(@RequestParam String token) throws JsonMappingException, JsonProcessingException {
		Transaction transaction = transactionService.cancel(token);
		return ResponseEntity.ok(null);
	}
}
