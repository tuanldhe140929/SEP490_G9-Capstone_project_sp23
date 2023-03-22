package com.SEP490_G9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.service.PaypalService;
import com.SEP490_G9.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Payer;

@RequestMapping("/transaction")
@RestController
public class TransactionController {
	// tao 1 transaction voi paypal
	// redirect user toi man hinh thanh toan
	// thanh toan xong, nhan success url
	// thanh toan khong thanh cong, nhan cancel url

	@Autowired
	TransactionService transactionService;

	@Autowired
	PaypalService paypalService;

	@PostMapping("/purchase")
	public ResponseEntity<?> purchase(@RequestParam(name = "cartId") Long cartId) {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		Transaction transaction = transactionService.createTransaction(cartId, account);
		System.out.println(transaction);
		return ResponseEntity.ok(transaction);
	}

	@GetMapping("/reviewTransaction")
	public ResponseEntity<?> reviewTransaction(@RequestParam("paymentId") String paymentId) {
		Transaction transaction = transactionService.getByPaymentId(paymentId);
		transaction.setStatus(Transaction.Status.APPROVED);
		transactionService.updateTransaction(transaction);
		Payer payer = paypalService.getPayerById(paymentId);
		transaction.setPayer(payer);
		return ResponseEntity.ok(transaction);
	}

	@GetMapping("/cancel/{transactionId}/paypal")
	public ResponseEntity<?> cancelPaypalPayment(@PathVariable(name = "transactionId") String transactionId,
			@RequestParam(name = "token") String token) {
		Long transId = Long.parseLong(transactionId);
		Transaction transaction = transactionService.cancel(transId);
		return ResponseEntity.ok(transaction);
	}

	@PostMapping("/executeTransaction")
	public ResponseEntity<?> execute(@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String payerId) {
		Transaction transaction = transactionService.executeTransaction(paymentId, payerId);
		return ResponseEntity.ok(transaction);
	}
	
	@PostMapping("/cancelTransaction")
	public ResponseEntity<?> cancelTransaction(@RequestParam("transId") Long transId) {
		Transaction ret = transactionService.cancel(transId);
		return ResponseEntity.ok(ret);
	}

	@GetMapping("checkTransactionStatus")
	public ResponseEntity<?> checkPaymentStatus(@RequestParam(name = "transactionId") Long transactionId) {
		Transaction transaction = transactionService.getByTransactionId(transactionId);
		Transaction.Status status = transaction.getStatus();
		return ResponseEntity.ok(status);
	}
}
