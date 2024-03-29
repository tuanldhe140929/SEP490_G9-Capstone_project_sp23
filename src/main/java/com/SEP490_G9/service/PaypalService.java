package com.SEP490_G9.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.TransactionFee;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Payout;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

public interface PaypalService {

	public Payment createPayment(com.SEP490_G9.entities.Transaction t);

	public Payment executePayment(String paymentId, String payerId);

	Payment getPaymentByPaypalId(String paymentId);

	public Payer getPayerById(String paymentId);

	public PayoutBatch executePayout(String email, Double total);

	public PayoutBatch getPayoutByBatchId(String batchId);


	
	String getPayPalAccessToken() throws PayPalRESTException;
	String getPayoutFee(String batchId);


}