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

		total = total * (1 - transaction.getFee().getPercentage() / 100D);

		return total;
	}

	@Override
	public Payout preparePayoutForSeller(Seller seller, Double amount, Transaction transaction) {
		Payout payout = new Payout();
		payout.setAmount(amount);
		payout.setSeller(seller);
		payout.setTransaction(transaction);
		payout.setCreatedDate(new Date());
		payout.setDescription("Wait for buy transaction to complete");
		payout.setStatus(Payout.Status.CREATED);
		payoutRepository.save(payout);
		return payout;
	}

	@Override
	public List<Payout> executePayout(Long transactionId) {
		List<Payout> payouts = payoutRepository.findByTransactionId(transactionId);
		for (Payout payout : payouts) {
			payout.setLastModified(new Date());
			payout.setDescription("commited payout");
			payout.setStatus(Payout.Status.PENDING);
			PayoutBatch batch = paypalService.executePayout(payout.getSeller().getEmail(), payout.getAmount());
			payout.setBatchId(batch.getBatchHeader().getPayoutBatchId());
			fetchPayoutStatus(payout.getId(), batch.getBatchHeader().getPayoutBatchId());
		}
		payouts = payoutRepository.saveAll(payouts);
		return payouts;
	}

	@Override
	public Payout fetchPayoutStatus(Long payoutId, String batchId) {
		Payout payout = getById(payoutId);

		int numCheck = 0;
		int maxCheck = 30;
		String state = "";
		while (numCheck < maxCheck) {
			state = paypalService.checkPayoutStatus(batchId);
			System.out.println(state);
			if (state.equals("SUCCESS")) {
				double fee = Double.parseDouble(paypalService.checkPayoutFee(batchId));
				payout.setDescription("Payout has been send");
				payout.setStatus(Payout.Status.SUCCESS);
				payout.setPayoutFee(fee);
				payoutRepository.save(payout);
				return payout;
			} else if (state.equals("PENDING")) {
				payout.setDescription("Pending and will be process soon");
				payout.setStatus(Payout.Status.PENDING);
				payoutRepository.save(payout);

			} else if (state.equals("PROCESSING")) {
				payout.setDescription("Payout is being processed");
				payout.setStatus(Payout.Status.PROCESSING);
				payoutRepository.save(payout);

			} else if (state.equals("CANCELED")) {
				payout.setDescription("Cancel by DPM system");
				payout.setStatus(Payout.Status.CANCELED);
				payoutRepository.save(payout);
				return payout;
			} else if (state.equals("DENIED")) {
				payout.setDescription("Denied by Paypal");
				payout.setStatus(Payout.Status.FAILED);
				payoutRepository.save(payout);
				return payout;
			}
			try {
				Thread.sleep(3 * 1000L); // Wait for the specified interval before checking again
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			numCheck++;
		}
		payout.setStatus(Payout.Status.UNDEFINED);
		payout.setDescription("Fetching status time out");
		return payoutRepository.save(payout);
	}

	@Override
	public Payout getById(Long payoutId) {
		Payout ret = payoutRepository.findById(payoutId).orElseThrow();
		return ret;
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
