package com.SEP490_G9.service.serviceImpls;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.TransactionFee;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.InternalServerException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.CartRepository;
import com.SEP490_G9.repository.TransactionFeeRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.service.CartService;
import com.SEP490_G9.service.PayoutService;
import com.SEP490_G9.service.PaypalService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.TransactionService;
import com.SEP490_G9.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import jakarta.transaction.InvalidTransactionException;

@Service
public class TransactionServiceImpl implements TransactionService {

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

	@Autowired
	CartRepository cartRepo;

	@Autowired
	ProductDetailsService pds;

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
	public Transaction createTransaction(Long cartId, Long accountId) {
		Cart cart = cartService.getById(cartId);
		User user = userService.getById(accountId);

		if (!cartService.isUserOwnCart(user.getId(), cart.getId())) {
			throw new IllegalAccessError("You don't have right to purchase this cart");
		}
		if (cartService.isCartHadPurchased(cart.getId())) {
			throw new IllegalArgumentException("Cart had already purchased");
		}
		if (cart.getItems().size() == 0) {
			throw new IllegalArgumentException("Cart is empty");
		}
		for (CartItem item : cart.getItems()) {
			if (cartService.isUserPurchasedProduct(user.getId(), item.getProductDetails().getProduct().getId())) {
				throw new IllegalArgumentException("User already own item in cart");
			}
		}

		TransactionFee fee = transFeeRepo.findById(1).get();
		double totalPrice = caculateCartTotalAmount(cart);
		System.out.println(totalPrice);

		double afterFeeCaculated = (totalPrice * (1 + (double) (fee.getPercentage() / 100f)));
		System.out.println(afterFeeCaculated);
		double afterFeeCaculatedRounded = new BigDecimal(afterFeeCaculated).setScale(2, RoundingMode.HALF_UP)
				.doubleValue();
		System.out.println(afterFeeCaculatedRounded);
		Transaction transaction = new Transaction();
		transaction.setCart(cart);
		transaction.setAmount(afterFeeCaculatedRounded);
		transaction.setCreateDate(new Date());
		transaction.setFee(fee);
		transaction.setPaypalId("Not yet");
		transaction.setLastModified(new Date());
		transaction.setStatus(Transaction.Status.CREATED);
		transaction = transactionRepo.save(transaction);

		if (transaction.getAmount() > 0) {
			createTransactionPayment(transaction.getId());
		} else {
			executeFreeTransaction(transaction.getId());
		}
		Transaction saved = transactionRepo.save(transaction);
		return transaction;
	}

	@Override
	public Transaction createTransactionPayment(Long transactionId) {
		Transaction transaction = transactionRepo.findById(transactionId).get();
		transaction.setDescription("pay with paypal");
		Payment payment = null;
		payment = paypalService.createPayment(transaction);
		String state = payment.getState();

		switch (state) {
		case "created": {
			for (Links link : payment.getLinks()) {
				if (link.getRel().equals("approval_url")) {
					transaction.setApprovalUrl(link.getHref());
				}
			}
			transaction.setPaypalId(payment.getId());
			payoutService.preparePayout(transaction.getId());
			break;
		}
		case "failed":
			transaction.setStatus(Transaction.Status.CANCELED);
			transaction = transactionRepo.save(transaction);
			throw new InternalServerException("Cannot create payment with paypal");
		}
		return transaction;
	}

	@Override
	public Transaction executeFreeTransaction(Long transactionId) {
		Transaction transaction = transactionRepo.findById(transactionId).orElseThrow();
		transaction.setStatus(Transaction.Status.COMPLETED);
		transaction.setDescription("Free purchased");
		return transaction;
	}

	@Override
	public Transaction executeTransaction(String paymentId, String payerId) {
		Transaction transaction = transactionRepo.findByPaypalId(paymentId);
		if (payerId.isBlank() || payerId.isEmpty()) {
			throw new IllegalArgumentException("PayerId can not be blank");
		}
		if (transaction == null) {
			throw new ResourceNotFoundException("transaction", "paymentId", paymentId);
		}
		if (!transaction.getStatus().equals(Transaction.Status.APPROVED)) {
			throw new IllegalAccessError("The transaction isn't ready or has been commit");
		}
		Payment payment = paypalService.executePayment(paymentId, payerId);
		Transaction ret = fetchTransactionStatus(transaction.getId());
		return ret;
	}

	@Override
	public Transaction fetchTransactionStatus(Long transactionId) {
		Transaction transaction = transactionRepo.findById(transactionId).orElseThrow();
		int numChecks = 0;
		int maxChecks = 300;
		while (numChecks < maxChecks) {
			Payment payment = paypalService.getPaymentByPaypalId(transaction.getPaypalId());
			String state = payment.getState();
			System.out.println("Payment State: " + state);
			if (state.equals("approved")) {
				System.out.println("Payment has been approved.");
				transaction.setStatus(Transaction.Status.COMPLETED);
				payoutService.executePayout(transaction.getId());
				return transactionRepo.save(transaction);

			} else if (state.equals("completed")) {
				System.out.println("Payment has been complete.");
				transaction.setStatus(Transaction.Status.COMPLETED);
				payoutService.executePayout(transaction.getId());
				return transactionRepo.save(transaction);

			} else if (state.equals("failed")) {
				payoutService.cancelPayout(transaction.getId());
				transaction.setStatus(Transaction.Status.FAILED);
				transaction.setDescription("Payment has failed");
				return transactionRepo.save(transaction);

			} else if (state.equals("expired")) {
				payoutService.cancelPayout(transaction.getId());
				transaction.setStatus(Transaction.Status.EXPIRED);
				transaction.setDescription("Payment has been expired");
				return transactionRepo.save(transaction);

			} else if (state.equals("canceled")) {
				payoutService.cancelPayout(transaction.getId());
				transaction.setStatus(Transaction.Status.CANCELED);
				transaction.setDescription("Payment has been canceled");
				return transactionRepo.save(transaction);

			}

			try {
				Thread.sleep(3 * 1000L); // Wait for the specified interval before checking again
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			numChecks++;
		}
		if (!transaction.getStatus().equals(Transaction.Status.CANCELED)
				|| !transaction.getStatus().equals(Transaction.Status.COMPLETED)
				|| !transaction.getStatus().equals(Transaction.Status.FAILED))
			transaction.setStatus(Transaction.Status.EXPIRED);
		transaction.setDescription("Trasaction is expired");
		transaction = transactionRepo.save(transaction);
		return transaction;
	}

	@Override
	public Transaction cancel(Long transactionId) {
		Transaction transaction = transactionRepo.findById(transactionId).orElseThrow();

		if (transaction.getStatus().equals(Transaction.Status.COMPLETED)
				|| transaction.getStatus().equals(Transaction.Status.FAILED)
				|| transaction.getStatus().equals(Transaction.Status.EXPIRED)
				|| transaction.getStatus().equals(Transaction.Status.CANCELED))
			throw new IllegalArgumentException("The transaction has ben made or has been cancelled");
		
		transaction.setStatus(Transaction.Status.CANCELED);
		transaction.setLastModified(new Date());
		transaction = transactionRepo.save(transaction);
		return transaction;
	}

	double caculateCartTotalAmount(Cart cart) {
		double total = 0;
		for (CartItem item : cart.getItems()) {
			total += item.getProductDetails().getPrice();
		}
		return total;
	}

	@Override
	public boolean isCartHadPurchased(Long cartId) {
		Cart cart = cartRepo.findById(cartId).orElseThrow();
		for (Transaction transaction : cart.getTransactions()) {
			if (transaction.getStatus().equals(Transaction.Status.COMPLETED)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<ProductDetails> getListCartUserPurchasedProduct() {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		List<Cart> carts = new ArrayList<>();
		List<Cart> purchasedCart = new ArrayList<>();
		for (Cart cart : carts) {
			if (isCartHadPurchased(cart.getId()))
				purchasedCart.add(cart);
		}
		List<ProductDetails> purchasedProductList = new ArrayList<>();
		List<CartItem> purchasedCartItem = new ArrayList<>();

		for (Cart c : purchasedCart) {
			List<CartItem> pCI = c.getItems();
			for (CartItem CI : pCI) {
				purchasedProductList.add(pds.getActiveVersion(CI.getProductDetails().getProduct().getId()));
			}
		}
		return purchasedProductList;
	}

}
