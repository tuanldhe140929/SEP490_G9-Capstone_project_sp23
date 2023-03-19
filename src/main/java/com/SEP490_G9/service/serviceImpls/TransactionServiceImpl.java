package com.SEP490_G9.service.serviceImpls;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.Transaction.Type;
import com.SEP490_G9.entities.TransactionFee;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.TransactionFeeRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.service.CartService;
import com.SEP490_G9.service.PaypalService;
import com.SEP490_G9.service.TransactionService;
import com.SEP490_G9.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class TransactionServiceImpl implements TransactionService {

	final String METHOD = "paypal";
	final String CURRENCY = "USD";
	final String RETURN_URL = "transaction/processTransaction";
	final String CANCEL_URL = "transaction/cancel";
	final String BUY_INTENT = "buy";
	final String SALE_INTENT = "sale";

	@Autowired
	CartService cartService;

	@Autowired
	UserService userService;

	@Autowired
	TransactionRepository transactionRepo;

	@Autowired
	TransactionFeeRepository transFeeRepo;

	@Autowired
	PaypalService paypalService;

	@Override
	public void createTransaction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateTransaction() {
		// TODO Auto-generated method stub

	}

	@Override
	public Transaction purchase(Long cartId, Account account) {
		Cart cart = cartService.getCart(cartId);
		User user = userService.getById(account.getId());

		if (!checkIfUserOwnCart(cartId, user)) {
			// throw new Exception();
		}
		TransactionFee fee = transFeeRepo.findById(1).get();
		Transaction transaction = new Transaction();
		transaction.setCart(cart);
		transaction.setAmount(caculateCartTotalAmount(cart));
		transaction.setCreateDate(new Date());
		transaction.setStatus(Transaction.Status.CREATED);
		transaction.setDescription("sell");
		transaction.setType(Type.SELL);
		transaction.setFee(fee);
		transactionRepo.save(transaction);

		Payment payment = null;

		payment = paypalService.createPayment(transaction.getAmount(), CURRENCY, METHOD, SALE_INTENT,
				transaction.getDescription(), "http://localhost:9000/" + CANCEL_URL,
				"http://localhost:9000/" + RETURN_URL, fee);

		for (Links link : payment.getLinks()) {
			if (link.getRel().equals("approval_url")) {
				transaction.setApprovalUrl(link.getHref());
			}
		}
		transaction.setPaypalId(payment.getId());
		transaction.setPaypalToken(payment.getCart());
		Transaction saved = transactionRepo.save(transaction);
		return transaction;
	}

	@Override
	public Transaction processTransaction(String paymentId, String token, String payerId) {
		Transaction transaction = transactionRepo.findByPaypalId(paymentId);
		Payment payment = null;

		payment = paypalService.executePayment(paymentId, token, payerId);

		int numChecks = 0;
		int maxChecks = 15;
		while (numChecks < maxChecks) {
			String state = paypalService.checkPaymentStatus(paymentId);
			if (state.equals("approved")) {
				System.out.println("Payment has been approved.");
				transaction.setStatus(Transaction.Status.COMPLETED);
				break;
			} else if (state.equals("failed")) {
				transaction.setStatus(Transaction.Status.FAILED);
				break;
			} else if (state.equals("expired") || state.equals("canceled")) {
				transaction.setStatus(Transaction.Status.CANCELED);
				System.out.println("Payment has expired.");
				break;
			}

			try {
				Thread.sleep(10 * 1000L); // Wait for the specified interval before checking again
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			numChecks++;
		}

		transactionRepo.save(transaction);
		return transaction;
	}

	@Override
	public Transaction cancel(String token) {
		if (token == null || token.isBlank()) {
			throw new IllegalArgumentException("token can't be blank");
		}
		Transaction transaction = transactionRepo.findByPaypalToken(token);
		if (transaction == null) {
			throw new ResourceNotFoundException("Transaction", "paypal token", token);
		}
		transaction.setStatus(Transaction.Status.CANCELED);
		transaction = transactionRepo.save(transaction);
		return transaction;
	}

	boolean checkIfUserOwnCart(Long cartId, User user) {
		boolean ret = false;
		for (Cart userCart : user.getCarts()) {
			if (userCart.getId() == cartId) {
				ret = true;
			}
		}
		return ret;
	}

	double caculateCartTotalAmount(Cart cart) {
		double total = 0;
		for (CartItem item : cart.getItems()) {
			total += item.getProductDetails().getPrice();
		}
		if (total == 0) {
			// throw new Exception();
		}
		return total;
	}
}
