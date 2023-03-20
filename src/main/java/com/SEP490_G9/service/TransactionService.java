package com.SEP490_G9.service;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;

public interface TransactionService {

	Transaction purchase(Long cartId, Account account);

	Transaction executeTransaction(String paymentId, String payerId);

	Transaction cancel(Long transactionId);

	Transaction getByPaymentId(String paymentId);

	Transaction getByTransactionId(Long transactionId);

	Transaction updateTransaction(Transaction transaction);

	boolean cancelByTransactionId(Long transId);

}
