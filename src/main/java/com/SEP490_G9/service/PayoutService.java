package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entities.Payout;
import com.SEP490_G9.entities.Transaction;

public interface PayoutService {
	public List<Payout> preparePayout(Long transactionId);
	public List<Payout> commitPayout(Long transactionId);
	public List<Payout> cancelPayout(Long id);
	
}
