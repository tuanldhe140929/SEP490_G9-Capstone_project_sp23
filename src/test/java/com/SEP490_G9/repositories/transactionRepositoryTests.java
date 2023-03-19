package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.repository.CartRepository;
import com.SEP490_G9.repository.TransactionRepository;

import org.junit.jupiter.api.Test;

class transactionRepositoryTests {
	@Autowired
	private TransactionRepository traRepos;
	@Test
	void testfindByCartId() {

		Cart cart = new Cart();
		cart.setId(1l);

		Transaction transaction = new Transaction();
		transaction.setId(1l);
		long expected = 1l;
		assertThat(traRepos.findByCartId(cart.getId()).equals(expected));
		
	}
	
	@Test
	void testSaveTransaction() {
		 	User user = new User();
		    user.setId(1L);

		    Cart cart = new Cart(user);
		    cart.setId(1l);
		    Transaction transaction = new Transaction();
			transaction.setId(1l);
			Transaction result = traRepos.save(transaction);
		    
		    assertNotNull(result.getId());
		    assertThat(result.getCart().getId().equals(cart.getId()));
	}

}
