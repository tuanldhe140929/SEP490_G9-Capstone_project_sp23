package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.User;

public interface TransactionService {

	Transaction getByPaymentId(String paymentId);

	Transaction getByTransactionId(Long transactionId);
	
	Transaction createTransaction(Long cartId, Long accountId);
	
	Transaction createTransactionPayment(Long transactionId);
	
	Transaction executeTransaction(String paymentId, String payerId);

	public Transaction executeFreeTransaction(Long transactionId);
	
	Transaction cancel(Long transactionId);

	Transaction fetchTransactionStatus(Long transactionId);

	Transaction updateTransaction(Transaction transaction);
	
	public boolean isCartHadPurchased(Long cartId);
	
	public List<ProductDetails> getListCartUserPurchasedProduct(Long userId);
}
