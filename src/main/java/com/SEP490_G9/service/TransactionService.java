package com.SEP490_G9.service;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Transaction;

public interface TransactionService {
	void createTransaction();
	void updateTransaction();
	
	Transaction purchase(Long cartId, Account account);
	Transaction processTransaction(String paymentId,String token, String payerId);
	Transaction cancel(String paymentId);
}
