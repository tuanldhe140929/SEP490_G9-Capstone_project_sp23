package com.SEP490_G9.service.serviceImpls;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.Transaction.Type;
import com.SEP490_G9.entities.TransactionFee;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.TransactionFeeRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.service.CartService;
import com.SEP490_G9.service.PayoutService;
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
	final String RETURN_URL = "transaction/reviewTransaction";
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
	
	@Autowired
	PayoutService payoutService;

	@Override
	public Transaction purchase(Long cartId, Account account) {
		Cart cart = cartService.getCart(cartId);
		User user = userService.getById(account.getId());

		if (!checkIfUserOwnCart(cartId, user)) {
			throw new IllegalAccessError("You don't have right to purchase this cart");
		}

		boolean cartIsPurchased = checkIfCartHadPurchased(cart);
		if (cartIsPurchased) {
			throw new IllegalArgumentException("Cart had already purchased");
		}

		if (cart.getItems().size() == 0) {
			throw new IllegalArgumentException("Cart is empty");
		}
		List<Cart> carts = cartService.getByUser(user);
		boolean userOwnItem = checkIfUserOwnItem(cart, carts);
		if (userOwnItem) {
			throw new IllegalArgumentException("User already own item in cart");
		}
		TransactionFee fee = transFeeRepo.findById(1).get();
		double totalPrice = caculateCartTotalAmount(cart);
		double afterFeeCaculated = (totalPrice * fee.getPercentage()) / 100;
		afterFeeCaculated = new BigDecimal(afterFeeCaculated).setScale(2, RoundingMode.HALF_UP).doubleValue();

		Transaction transaction = new Transaction();
		transaction.setCart(cart);
		transaction.setAmount(afterFeeCaculated);
		transaction.setCreateDate(new Date());
		transaction.setFee(fee);
		transaction.setStatus(Transaction.Status.CREATED);
		transaction = transactionRepo.save(transaction);
		if (transaction.getAmount() > 1) {
			transaction.setDescription("pay with paypal");
			Payment payment = null;
			payment = paypalService.createPayment(transaction.getAmount(), CURRENCY, METHOD, SALE_INTENT,
					transaction.getDescription(),
					"http://localhost:9000/transaction/cancel/" + transaction.getId() + "/paypal",
					"http://localhost:4200/" + RETURN_URL);

			for (Links link : payment.getLinks()) {
				if (link.getRel().equals("approval_url")) {
					transaction.setApprovalUrl(link.getHref());
				}
			}
			transaction.setPaypalId(payment.getId());
			
			payoutService.preparePayout(transaction.getId());
		} else {
			transaction.setDescription("free");
			transaction.setStatus(Transaction.Status.COMPLETED);
		}
		Transaction saved = transactionRepo.save(transaction);
		return transaction;
	}

	private boolean checkIfCartHadPurchased(Cart cart) {
		for (Transaction transaction : cart.getTransactions()) {
			if (transaction.getStatus().equals(Transaction.Status.COMPLETED)) {
				return true;
			}
		}
		return false;
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
		return total;
	}

	private boolean checkIfUserOwnItem(Cart cart, List<Cart> carts) {
		boolean ret = false;
		List<Cart> purchasedCart = new ArrayList<>();
		for (Cart c : carts) {
			if (checkIfCartHadPurchased(cart)) {
				purchasedCart.add(c);
			}
		}
		for (Cart c : purchasedCart) {
			if (c.getId() != cart.getId()) {
				for (CartItem item : cart.getItems()) {
					for (CartItem i : c.getItems()) {
						if (item.getProductDetails().getProduct().getId() == i.getProductDetails().getProduct().getId())
							ret = true;
					}
				}
			}
		}

		return ret;
	}

	@Override
	public Transaction executeTransaction(String paymentId, String payerId) {
		Transaction transaction = transactionRepo.findByPaypalId(paymentId);
		if (!transaction.getStatus().equals(Transaction.Status.APPROVED)) {
			throw new IllegalAccessError("The transaction isn't ready or has been commit");
		}
		Payment payment = null;

		payment = paypalService.executePayment(paymentId, payerId);

		int numChecks = 0;
		int maxChecks = 15;
		while (numChecks < maxChecks) {
			String state = paypalService.checkPaymentStatus(paymentId);
			if (state.equals("approved")) {
				System.out.println("Payment has been approved.");
				transaction.setStatus(Transaction.Status.COMPLETED);
				payoutService.commitPayout(transaction.getId());
				break;
			} else if (state.equals("failed")) {
				payoutService.cancelPayout(transaction.getId());
				transaction.setStatus(Transaction.Status.FAILED);
				break;
			} else if (state.equals("expired") || state.equals("canceled")) {
				payoutService.cancelPayout(transaction.getId());
				transaction.setStatus(Transaction.Status.CANCELED);
				System.out.println("Payment has expired.");
				break;
			}

			try {
				Thread.sleep(3 * 1000L); // Wait for the specified interval before checking again
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			numChecks++;
		}
		transaction.setLastModified(new Date());
		transactionRepo.save(transaction);
		return transaction;
	}

	@Override
	public Transaction cancel(Long transactionId) {
		Transaction transaction = transactionRepo.findById(transactionId).orElseThrow();
		if (transaction == null) {
			throw new ResourceNotFoundException("Transaction", "id", transactionId);
		}
		if (transaction.getStatus().equals(Transaction.Status.COMPLETED)
				|| transaction.getStatus().equals(Transaction.Status.FAILED))
			throw new IllegalAccessError("The transaction has ben made");

		transaction.setStatus(Transaction.Status.CANCELED);
		transaction.setLastModified(new Date());
		transaction = transactionRepo.save(transaction);
		return transaction;
	}

	@Override
	public Transaction getByPaymentId(String paymentId) {
		Transaction ret = transactionRepo.findByPaypalId(paymentId);
		return ret;
	}

	@Override
	public Transaction getByTransactionId(Long transactionId) {
		Transaction ret = transactionRepo.findById(transactionId).orElseThrow();
		return ret;
	}

	@Override
	public Transaction updateTransaction(Transaction transaction) {
		if (!transactionRepo.existsById(transaction.getId()))
			throw new ResourceNotFoundException("Transaction", "id", transaction.getId());
		Transaction ret = transactionRepo.save(transaction);
		return ret;
	}

	@Override
	public boolean cancelByTransactionId(Long transId) {
		if (!transactionRepo.existsById(transId))
			throw new ResourceNotFoundException("Transaction", "id", transId);
		Transaction transaction = transactionRepo.findById(transId).get();
		if (transaction.getStatus().equals(Transaction.Status.COMPLETED)
				|| transaction.getStatus().equals(Transaction.Status.FAILED))
			throw new IllegalAccessError("The transaction has ben made");
		transaction.setStatus(Transaction.Status.CANCELED);
		transactionRepo.save(transaction);
		return true;
	}

}
