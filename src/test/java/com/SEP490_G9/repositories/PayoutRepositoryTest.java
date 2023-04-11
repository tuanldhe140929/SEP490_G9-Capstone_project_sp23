package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.Payout;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.TransactionFee;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.repository.CartRepository;
import com.SEP490_G9.repository.PayoutRepository;
import com.SEP490_G9.repository.TransactionFeeRepository;
import com.SEP490_G9.repository.TransactionRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class PayoutRepositoryTest {

	@Autowired
	PayoutRepository payoutRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	TransactionFeeRepository feeRepo;

	@Autowired
	CartRepository cartRepo;

	@Test
	void testFindById() {
		Payout po = new Payout();
		po.setId(1L);
		po = payoutRepository.save(po);

		Payout result = payoutRepository.findById(po.getId()).get();
		assertThat(result.getId()).isEqualTo(po.getId());
	}

	@Test
	void testFindByIdA() {
		Payout po = new Payout();
		po.setId(1L);
		po = payoutRepository.save(po);

		assertThrows(NoSuchElementException.class, () -> {
			payoutRepository.findById(-1L).get();
		});
	}

	@Test
	void testFindByTransactionId() {
		Payout po = new Payout();
		po.setId(1L);
		po.setBatchId("ABC");
		po.setAmount((double) 120);
		po.setStatus(Payout.Status.CREATED);
		po.setCreatedDate(new Date());
		po.setLastModified(new Date());
		po.setPayoutFee(0D);
		User user = new User();
		user.setId(1L);

		Cart cart = new Cart(user);
		cart.setId(2L);
		cart.setUser(user);

		Cart saved = cartRepo.save(cart);
		Transaction transaction = new Transaction();
		TransactionFee fee = new TransactionFee();
		fee.setId(1);
		fee.setPercentage(10);
		feeRepo.save(fee);
		transaction.setId(1L);
		transaction.setCart(saved);
		transaction.setPaypalId("ABC");
		transaction.setCreateDate(new Date());
		transaction.setLastModified(new Date());
		transaction.setAmount(10);
		transaction.setFee(fee);
		transaction.setStatus(Transaction.Status.APPROVED);
		transaction = transactionRepository.save(transaction);
		po.setTransaction(transaction);

		payoutRepository.save(po);

		Payout p = payoutRepository.findByTransactionId(po.getTransaction().getId()).get(0);
		assertThat(p.getTransaction()).isEqualTo(po.getTransaction());
	}

	@Test
	void testFindByTransactionIdA() {
		Payout po = new Payout();
		po.setId(1L);
		po.setBatchId("ABC");
		po.setAmount((double) 120);
		po.setStatus(Payout.Status.CREATED);
		po.setCreatedDate(new Date());
		po.setLastModified(new Date());
		po.setPayoutFee(0D);

		User user = new User();
		user.setId(1L);

		Cart cart = new Cart(user);
		cart.setId(2L);
		cart.setUser(user);

		Cart saved = cartRepo.save(cart);
		Transaction transaction = new Transaction();
		TransactionFee fee = new TransactionFee();
		fee.setId(1);
		fee.setPercentage(10);
		feeRepo.save(fee);
		transaction.setId(1L);
		transaction.setCart(saved);
		transaction.setPaypalId("ABC");
		transaction.setCreateDate(new Date());
		transaction.setLastModified(new Date());
		transaction.setAmount(10);
		transaction.setFee(fee);
		transaction.setStatus(Transaction.Status.APPROVED);
		transaction = transactionRepository.save(transaction);
		po.setTransaction(transaction);

		Payout result =payoutRepository.save(po);

		assertThat(result.getTransaction()).isEqualTo(po.getTransaction());

	}

	@Test
	void testSave() {
		Payout po = new Payout();
		po.setId(1L);
		po.setBatchId("ABC");
		po.setAmount((double) 120);
		po.setStatus(Payout.Status.CREATED);
		po.setCreatedDate(new Date());
		po.setLastModified(new Date());
		po.setPayoutFee(0D);

		User user = new User();
		user.setId(1L);

		Cart cart = new Cart(user);
		cart.setId(2L);
		cart.setUser(user);

		Cart saved = cartRepo.save(cart);
		Transaction transaction = new Transaction();
		TransactionFee fee = new TransactionFee();
		fee.setId(1);
		fee.setPercentage(10);
		feeRepo.save(fee);
		transaction.setId(1L);
		transaction.setCart(saved);
		transaction.setPaypalId("ABC");
		transaction.setCreateDate(new Date());
		transaction.setLastModified(new Date());
		transaction.setAmount(10);
		transaction.setFee(fee);
		transaction.setStatus(Transaction.Status.APPROVED);
		transaction = transactionRepository.save(transaction);
		po.setTransaction(transaction);

		Payout result =payoutRepository.save(po);

		assertThat(result.getTransaction()).isEqualTo(po.getTransaction());
	}
}
