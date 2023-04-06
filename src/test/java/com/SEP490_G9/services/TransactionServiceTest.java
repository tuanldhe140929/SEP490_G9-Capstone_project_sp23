package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.TransactionFee;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.embeddable.CartItemKey;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.repository.TransactionFeeRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.service.PaypalService;
import com.SEP490_G9.service.TransactionService;
import com.SEP490_G9.service.serviceImpls.CartServiceImplement;
import com.SEP490_G9.service.serviceImpls.PaypalServiceImpl;
import com.SEP490_G9.service.serviceImpls.TransactionServiceImpl;
import com.SEP490_G9.service.serviceImpls.UserServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class TransactionServiceTest {

	@InjectMocks
	TransactionServiceImpl ts;

	@Mock
	PaypalServiceImpl ps;

	@Mock
	TransactionRepository tr;
	
	@Mock
	CartServiceImplement cs;
	
	@Mock
	UserServiceImpl us;

	@Mock
	TransactionFeeRepository feeRepo;
	@Test
	void testGetByPaypalId() {
		Transaction expected = new Transaction();
		expected.setId(2L);
		expected.setAmount(10);
		expected.setFee(new TransactionFee(1));
		expected.setCart(new Cart());
		expected.setPaypalId("ABC");

		when(tr.findByPaypalId(expected.getPaypalId())).thenReturn(expected);

		Transaction result = ts.getByPaymentId(expected.getPaypalId());
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void testGetById() {
		Transaction expected = new Transaction();
		expected.setId(2L);
		expected.setAmount(10);
		expected.setFee(new TransactionFee(1));
		expected.setCart(new Cart());
		expected.setPaypalId("ABC");

		when(tr.findById(expected.getId())).thenReturn(Optional.of(expected));

		Transaction result = ts.getByTransactionId(expected.getId());
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void testCreateTransaction() {
		
		User user = new User();
		user.setId(1L);
		Cart cart = new Cart();
		cart.setId(1L);
		cart.setUser(user);
		
		Product product = new Product();
		product.setId(1L);
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(new ProductVersionKey(1L,"1.0.0"));
		productDetails.setPrice(2);
		CartItem item = new CartItem();
		item.setCart(cart);
		item.setProductDetails(productDetails);
		CartItemKey cik = new CartItemKey();
		cik.setCartId(cart.getId());
		cik.setProductVersionKey(productDetails.getProductVersionKey());
		cart.addItem(item);
		
		
		TransactionFee fee = new TransactionFee();
		fee.setId(1);
		fee.setPercentage(10);
		
		Transaction expected = new Transaction();
		expected.setAmount(2.2);
		expected.setCart(cart);
		expected.setFee(fee);
		when(us.getById(user.getId())).thenReturn(user);
		when(cs.isUserOwnCart(1L, 1L)).thenReturn(true);
		when(cs.isCartHadPurchased(1L)).thenReturn(false);
		when(cs.getById(1L)).thenReturn(cart);
		when(tr.save(any())).thenReturn(expected);
		when(feeRepo.findById(1)).thenReturn(Optional.of(fee));
		
		Transaction result = ts.createTransaction(cart.getId(), user);
		assertThat(result.getStatus()).isEqualTo(Transaction.Status.CREATED);
		
	}

	@Test
	void testExeuteTransaction() {

	}

	@Test
	void testCancel() {

	}

	@Test
	void testFetchTransactionStatus() {

	}

}
