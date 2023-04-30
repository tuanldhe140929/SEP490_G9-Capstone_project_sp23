package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Payout;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.Transaction.Status;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.ResourceNotFoundException;
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
		if(transaction.getCart().getItems().size()==0) {
			throw new IllegalArgumentException("Cannot create payout for 0 item");
		}
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
				total += item.getPrice();
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
		payout.setBatchId("Not yet");
		payout.setPayoutFee(0D);
		payout.setStatus(Payout.Status.CREATED);
		payout.setLastModified(new Date());
		payoutRepository.save(payout);
		return payout;
	}

	@Override
	public List<Payout> executePayout(Long transactionId) {

		List<Payout> payouts = payoutRepository.findByTransactionId(transactionId);
		if (payouts.isEmpty()) {
			throw new ResourceNotFoundException("payout", "trans id", transactionId);
		} else {
			if (payouts.get(0).getTransaction().getStatus() != Transaction.Status.COMPLETED) {
				throw new IllegalArgumentException("Transaction hasn't been commited");
			}
			for (Payout payout : payouts) {
				payout.setLastModified(new Date());
				payout.setDescription("commited payout");
				payout.setStatus(Payout.Status.PROCESSING);
				PayoutBatch batch = paypalService.executePayout(payout.getSeller().getEmail(), payout.getAmount());
				payout.setBatchId(batch.getBatchHeader().getPayoutBatchId());
				fetchPayoutStatus(payout.getId(), batch.getBatchHeader().getPayoutBatchId());
			}
			payouts = payoutRepository.saveAll(payouts);
		}
		return payouts;
	}

	@Override
	public Payout fetchPayoutStatus(Long payoutId, String batchId) {
		Payout payout = getById(payoutId);

		int numCheck = 0;
		int maxCheck = 30;
		String state = "";
		while (numCheck<maxCheck) {
			PayoutBatch batch = paypalService.getPayoutByBatchId(batchId);
		//	batch.getItems().get(0).getError();
			state = batch.getBatchHeader().getBatchStatus();
			System.out.println(state);
			if (state.equals("SUCCESS")) {
				double fee = Double.parseDouble(paypalService.getPayoutFee(batchId));
				payout.setStatus(Payout.Status.SUCCESS);
				payout.setPayoutFee(fee);
				payoutRepository.save(payout);
				return payout;
			} else if (state.equals("PENDING")) {
				payout.setDescription("Pending and will be process soon");
				payout.setStatus(Payout.Status.PROCESSING);
				payoutRepository.save(payout);

			} else if (state.equals("PROCESSING")) {
				payout.setDescription("Payout is being processed");
				payout.setStatus(Payout.Status.PROCESSING);
				payoutRepository.save(payout);

			} else if (state.equals("CANCELED")) {
				payout.setDescription("Cancel by Paypal service");
				payout.setStatus(Payout.Status.FAILED);
				payoutRepository.save(payout);
				return payout;
			} else if (state.equals("DENIED")) {
				payout.setDescription("Denied by Paypal");
				payout.setStatus(Payout.Status.FAILED);
				payoutRepository.save(payout);
				return payout;
			} else if (state.equals("FAILED")) {
				payout.setDescription("Failed by Paypal");
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
		if (!payout.getStatus().equals(Payout.Status.CANCELED) || !payout.getStatus().equals(Payout.Status.FAILED)
				|| !payout.getStatus().equals(Payout.Status.SUCCESS)
				|| !!payout.getStatus().equals(Payout.Status.CANCELED))
			payout.setDescription("fetching timeout");
		payout.setStatus(Payout.Status.FAILED);
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
		if (payouts.isEmpty()) {
			throw new ResourceNotFoundException("payout", "trans id", transactionId);
		} else {
//			if(payouts.get(0).getTransaction().getStatus()!=Status.APPROVED &&
//					payouts.get(0).getTransaction().getStatus()!=Status.CREATED) {
//				throw new IllegalArgumentException("Cannot cancel committed transaction");
//			}
			for (Payout payout : payouts) {
				
				payout.setLastModified(new Date());
				payout.setDescription("Buyer canceled or failed transaction");
				payout.setStatus(Payout.Status.CANCELED);
			}
			payouts = payoutRepository.saveAll(payouts);
		}
		return payouts;
	}

	@Override
	public List<Payout> getPayoutHistory() {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		List<Payout> Allpayout = payoutRepository.findBySellerId(account.getId());
		List<Payout> allPayoutHistory = new ArrayList<>();
		for (Payout po : Allpayout) {
			if (po.getStatus().equals(Payout.Status.SUCCESS)) {
				allPayoutHistory.add(po);
			}
			if (po.getStatus().equals(Payout.Status.FAILED)) {
				allPayoutHistory.add(po);
			}
		}
		return allPayoutHistory.stream().distinct().toList();
	}
}
