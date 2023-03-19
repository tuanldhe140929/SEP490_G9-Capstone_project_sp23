package com.SEP490_G9.service;

public interface PayPalAPIService {
	void createPayment(Double total, String currency, String method, String intent, String description,
			String cancelUrl, String successUrl);
}
