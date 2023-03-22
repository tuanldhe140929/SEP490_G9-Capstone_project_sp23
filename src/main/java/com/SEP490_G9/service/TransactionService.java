package com.SEP490_G9.service;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;

public interface TransactionService {

	Transaction getByPaymentId(String paymentId);

	Transaction getByTransactionId(Long transactionId);
	
	Transaction createTransaction(Long cartId, Account account);
	
	Transaction createTransactionPayment(Long transactionId);
	
	Transaction executeTransaction(String paymentId, String payerId);

	public Transaction executeFreeTransaction(Long transactionId);
	
	Transaction cancel(Long transactionId);

	Transaction fetchTransactionStatus(String paymenId, Long transactionId);

	Transaction updateTransaction(Transaction transaction);
}
