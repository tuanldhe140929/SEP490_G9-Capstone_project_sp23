package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Payout;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.repository.PayoutRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.service.PayoutService;
import com.SEP490_G9.service.PaypalService;
import com.paypal.api.payments.PayoutBatch;

@Service
public class PayoutServiceImpl implements PayoutService {
	@Autowired
	TransactionRepository transactionRepo;

	@Autowired
	PayoutRepository payoutRepository;

	@Autowired
	PaypalService paypalService;

	@Override
	public List<Payout> preparePayout(Long transactionId) {
		Transaction transaction = transactionRepo.findById(transactionId).orElseThrow();

		List<Seller> sellers = new ArrayList<>();
		for (CartItem item : transaction.getCart().getItems()) {
			Seller seller = item.getProductDetails().getProduct().getSeller();
			if (!sellers.contains(seller))
				sellers.add(seller);
		}

		List<Payout> ret = new ArrayList<>();

		for (Seller seller : sellers) {
			Payout payout = preparePayoutForSeller(seller, caculateSellerPayout(transaction, seller), transaction);
			ret.add(payout);
		}
		return ret;
	}

	private Double caculateSellerPayout(Transaction transaction, Seller seller) {
		Cart cart = transaction.getCart();
		double total = 0;
		for (CartItem item : cart.getItems()) {
			if (item.getProductDetails().getProduct().getSeller().getId() == seller.getId()) {
				total += item.getProductDetails().getPrice();
			}
		}
		total = total * (1 - transaction.getFee().getPercentage() / 100);

		return total;
	}

	Payout preparePayoutForSeller(Seller seller, Double amount, Transaction transaction) {
		Payout payout = new Payout();
		payout.setAmount(amount);
		payout.setSeller(seller);
		payout.setTransaction(transaction);
		payout.setCreatedDate(new Date());
		payout.setDescription("Payout");
		payout.setStatus(Payout.Status.CREATED);
		payoutRepository.save(payout);
		return payout;
	}

	@Override
	public List<Payout> commitPayout(Long transactionId) {
		List<Payout> payouts = payoutRepository.findByTransactionId(transactionId);
		for (Payout payout : payouts) {
			payout.setLastModified(new Date());
			payout.setDescription("commited payout");
			payout.setStatus(Payout.Status.PENDING);
			PayoutBatch batch = paypalService.payout(payout.getSeller().getEmail(), payout.getAmount());
			fetchPayoutStatus(payout, batch);
		}
		payouts = payoutRepository.saveAll(payouts);
		return payouts;
	}

	private void fetchPayoutStatus(Payout payout, PayoutBatch batch) {

		String state = "";
		while (state != "success") {
			state = paypalService.checkPayoutStatus(batch.getBatchHeader().getPayoutBatchId());
			if (state.equals("approved")) {
				payout.setDescription("success");
				payout.setStatus(Payout.Status.SUCCESS);
				break;
			} else if (state.equals("failed")) {
				payout.setDescription("failed");
				payout.setStatus(Payout.Status.FAILED);
				break;
			} else if (state.equals("blocked") || state.equals("refunded")) {
				payout.setDescription("cancel by seller");
				payout.setStatus(Payout.Status.CANCELED);
				break;
			} else {
				payout.setDescription("cancel by seller");
				payout.setStatus(Payout.Status.PENDING);
			}
			payoutRepository.save(payout);
			try {
				Thread.sleep(3 * 1000L); // Wait for the specified interval before checking again
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Payout> cancelPayout(Long transactionId) {
		List<Payout> payouts = payoutRepository.findByTransactionId(transactionId);
		for (Payout payout : payouts) {
			payout.setLastModified(new Date());
			payout.setDescription("Buyer canceled or failed transaction");
			payout.setStatus(Payout.Status.CANCELED);
		}
		payouts = payoutRepository.saveAll(payouts);
		return payouts;
	}
}
