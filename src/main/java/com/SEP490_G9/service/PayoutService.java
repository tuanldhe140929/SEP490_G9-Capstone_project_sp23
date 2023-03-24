package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entities.Payout;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;
import com.paypal.api.payments.PayoutBatch;

public interface PayoutService {
	public List<Payout> preparePayout(Long transactionId);

	public List<Payout> executePayout(Long transactionId);

	public List<Payout> cancelPayout(Long id);
	
	public Payout fetchPayoutStatus(Long payoutId, String batchId);
	
	Payout preparePayoutForSeller(Seller seller, Double amount, Transaction transaction);
	
	public Payout getById(Long id);
}
