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
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.TransactionFee;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.repository.CartRepository;
import com.SEP490_G9.repository.TransactionFeeRepository;
import com.SEP490_G9.repository.TransactionRepository;

import org.junit.jupiter.api.Test;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class TransactionRepositoryTest {
	@Autowired
	private TransactionRepository traRepos;
	
	@Autowired
	private CartRepository cartRepo;
	
	@Autowired
	private TransactionFeeRepository feeRepo;
	@Test
	void testSaveTransaction() {
		 	User user = new User();
		    user.setId(1L);

		    Cart cart = new Cart(user);
		    cart.setId(1l);
		    cart.setUser(user);
		    
		    Cart saved = cartRepo.save(cart);
		    Transaction transaction = new Transaction();
			transaction.setId(1l);
			transaction.setCart(saved);
			Transaction result = traRepos.save(transaction);
		    
		    assertNotNull(result.getId());
		    assertThat(result.getCart().getId().equals(cart.getId()));
	}
	
	@Test
	void testSaveTransactionA() {
		 	User user = new User();
		    user.setId(1L);

		    Cart cart = new Cart(user);
		    cart.setId(2L);
		    cart.setUser(user);
		    
		    Cart saved = cartRepo.save(cart);
		    Transaction transaction = new Transaction();
			transaction.setId(-1L);
			transaction.setCart(saved);
			Transaction result = traRepos.save(transaction);
		    
		    assertNotNull(result.getId());
		    assertThat(result.getCart().getId().equals(cart.getId()));
	}
	
	@Test
	void testFindById() {
		User user = new User();
	    user.setId(1L);

	    Cart cart = new Cart(user);
	    cart.setId(2L);
	    cart.setUser(user);
	    
	    Cart saved = cartRepo.save(cart);
	    Transaction transaction = new Transaction();
		transaction.setId(1L);
		transaction.setCart(saved);
		transaction = traRepos.save(transaction);
	    
		Transaction result = traRepos.findById(transaction.getId()).get();
	    assertNotNull(result.getId());
	    assertThat(result.getCart().getId().equals(cart.getId()));
	}
	
	@Test
	void testFindByIdA() {
		User user = new User();
	    user.setId(1L);

	    Cart cart = new Cart(user);
	    cart.setId(2L);
	    cart.setUser(user);
	    
	    Cart saved = cartRepo.save(cart);
	    Transaction transaction = new Transaction();
		transaction.setId(1L);
		transaction.setCart(saved);
		transaction = traRepos.save(transaction);
	    
	
	    assertThrows(NoSuchElementException.class, () -> {traRepos.findById(-1L).get();});
	}
	
	@Test
	void testFindByPaypalId() {
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
		transaction.setCreatedDate(new Date());
		transaction.setLastModified(new Date());
		transaction.setAmount(10);
		transaction.setFee(fee);
		transaction.setStatus(Transaction.Status.APPROVED);
		transaction = traRepos.save(transaction);
	    
		Transaction result = traRepos.findByPaypalId("ABC");
	    assertNotNull(result.getId());
	    assertThat("ABC".equals(result.getPaypalId()));
	}
}
