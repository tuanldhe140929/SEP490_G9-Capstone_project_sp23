package com.SEP490_G9.service.serviceImpls;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.TransactionFee;
import com.SEP490_G9.service.PaypalService;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Currency;
import com.paypal.api.payments.Order;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Payout;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.api.payments.PayoutItem;
import com.paypal.api.payments.PayoutSenderBatchHeader;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaypalServiceImpl implements PaypalService {

	final String METHOD = "paypal";
	final String CURRENCY = "USD";
	final String RETURN_URL = "http://localhost:4200/transaction/reviewTransaction";
	final String CANCEL_URL = "transaction/cancel";
	final String INTENT = "sale";

	@Autowired
	private APIContext apiContext;

	@Override
	public Payment createPayment(com.SEP490_G9.entities.Transaction t) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		int expirationTimeInSeconds = 80;
		Amount amount = new Amount();
		amount.setCurrency(CURRENCY);
		double totalAmount = t.getAmount();
		totalAmount = new BigDecimal(totalAmount).setScale(2, RoundingMode.HALF_UP).doubleValue();
		amount.setTotal(String.format("%.2f", totalAmount));

		Transaction transaction = new Transaction();
		transaction.setDescription(transaction.getDescription());
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod(METHOD.toString());

		Payment payment = new Payment();
		payment.setIntent(INTENT.toString());
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		String cancelUrl = "http://localhost:9000/transaction/cancel/" + t.getId() + "/paypal";
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(RETURN_URL);
		payment.setRedirectUrls(redirectUrls);
		payment.setCreateTime(dateFormat.format(new Date()));
		payment.setUpdateTime(
				dateFormat.format(new Date(System.currentTimeMillis() + expirationTimeInSeconds * 1000L))); // Set the
																											// payment
		Payment ret = null;
		try {
			ret = payment.create(apiContext);
		} catch (PayPalRESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public Payment executePayment(String paymentId, String payerId) {
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		Payment ret = null;
		try {
			ret = payment.execute(apiContext, paymentExecute);
		} catch (PayPalRESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public Payment getPaymentByPaypalId(String paymentId) {
		Payment payment = null;
		try {
			payment = Payment.get(apiContext, paymentId);
		} catch (PayPalRESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return payment;
	}

	@Override
	public Payer getPayerById(String paymentId) {
		Payment payment = null;
		try {
			payment = Payment.get(apiContext, paymentId);
		} catch (PayPalRESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return payment.getPayer();
	}

	@Override
	public PayoutBatch executePayout(String email, Double total) {
		// Set up the sender information
		PayoutSenderBatchHeader senderBatchHeader = new PayoutSenderBatchHeader();
		senderBatchHeader.setSenderBatchId(UUID.randomUUID().toString());
		senderBatchHeader.setEmailSubject("You have a payout!");

		// Set up the payout item
		Currency currency = new Currency();
		currency.setValue(total.toString());
		currency.setCurrency("USD");

		PayoutItem payoutItem = new PayoutItem();
		payoutItem.setRecipientType("EMAIL");
		payoutItem.setReceiver(email);
		payoutItem.setAmount(currency);
		payoutItem.setSenderItemId(UUID.randomUUID().toString());
		payoutItem.setNote("Payout from DPM");
		// Create the payout batch
		List<PayoutItem> payoutItems = new ArrayList<PayoutItem>();
		payoutItems.add(payoutItem);

		Payout payout = new Payout();
		payout.setSenderBatchHeader(senderBatchHeader);
		payout.setItems(payoutItems);

		PayoutBatch createdPayout = null;
		try {
			createdPayout = payout.create(apiContext, null);
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		}
		return createdPayout;
	}

	@Override
	public PayoutBatch getPayoutByBatchId(String batchId) {
		PayoutBatch batch = null;
		try {
			batch = Payout.get(apiContext, batchId);
		} catch (PayPalRESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return batch;
	}

	@Override
	public String getPayoutFee(String batchId) {
		PayoutBatch batch = null;
		try {
			batch = Payout.get(apiContext, batchId);
		} catch (PayPalRESTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Currency fee = batch.getBatchHeader().getFees();
		return fee.getValue();
	}

}