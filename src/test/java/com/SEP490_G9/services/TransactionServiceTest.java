package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
import com.SEP490_G9.entities.Transaction.Status;
import com.SEP490_G9.entities.TransactionFee;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.embeddable.CartItemKey;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.TransactionFeeRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.service.PaypalService;
import com.SEP490_G9.service.TransactionService;
import com.SEP490_G9.service.serviceImpls.CartServiceImplement;
import com.SEP490_G9.service.serviceImpls.PayoutServiceImpl;
import com.SEP490_G9.service.serviceImpls.PaypalServiceImpl;
import com.SEP490_G9.service.serviceImpls.TransactionServiceImpl;
import com.SEP490_G9.service.serviceImpls.UserServiceImpl;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;

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

	@Mock
	PayoutServiceImpl pos;

	@Test
	void testTS11() {

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
		productDetails.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
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
		expected.setId(1L);
		expected.setAmount(2.2);
		expected.setCart(cart);
		expected.setFee(fee);
		expected.setStatus(Status.CREATED);
		when(us.getById(user.getId())).thenReturn(user);
		when(cs.isUserOwnCart(1L, 1L)).thenReturn(true);
		when(cs.isCartHadPurchased(1L)).thenReturn(false);
		when(tr.findById(anyLong())).thenReturn(Optional.of(expected));
		when(cs.getById(1L)).thenReturn(cart);
		when(tr.save(any())).thenReturn(expected);
		when(feeRepo.findById(1)).thenReturn(Optional.of(fee));
		Payment p = new Payment();
		p.setState("created");
		p.setId("dummy");
		List<Links> links = new ArrayList<>();
		Links link = new Links();
		link.setHref("ABC");
		link.setRel("approval_url");
		p.setLinks(links);
		when(ps.createPayment(any())).thenReturn(p);
		when(pos.preparePayout(anyLong())).thenReturn(anyList());
		Transaction result = ts.createTransaction(cart.getId(), user.getId());
		assertThat(result.getStatus()).isEqualTo(Transaction.Status.CREATED);

	}

	@Test
	void testTS12() {
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
		productDetails.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
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
		when(cs.isUserOwnCart(1L, 1L)).thenReturn(false);
		when(cs.isCartHadPurchased(1L)).thenReturn(false);
		when(cs.getById(1L)).thenReturn(cart);
		when(tr.save(any())).thenReturn(expected);
		when(feeRepo.findById(1)).thenReturn(Optional.of(fee));

		assertThrows(IllegalAccessError.class, () -> {
			ts.createTransaction(cart.getId(), user.getId());
		});
	}

	@Test
	void testTS13() {
		User user = new User();
		user.setId(2L);
		Cart cart = new Cart();
		cart.setId(1L);

		Product product = new Product();
		product.setId(1L);
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
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
		when(cs.isUserOwnCart(1L, 2L)).thenReturn(true);
		when(cs.isCartHadPurchased(1L)).thenReturn(false);
		when(cs.getById(1L)).thenReturn(cart);
		when(tr.save(any())).thenReturn(expected);
		when(feeRepo.findById(1)).thenReturn(Optional.of(fee));

		assertThrows(IllegalAccessError.class, () -> {
			ts.createTransaction(cart.getId(), user.getId());
		});
	}

	@Test
	void testTS14() {

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
		productDetails.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
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
		expected.setId(1L);
		expected.setAmount(2.2);
		expected.setCart(cart);
		expected.setFee(fee);
		expected.setStatus(Status.CREATED);
		when(us.getById(user.getId())).thenReturn(user);
		when(cs.isUserOwnCart(1L, 1L)).thenReturn(true);
		when(cs.isCartHadPurchased(1L)).thenReturn(false);
		when(tr.findById(anyLong())).thenReturn(Optional.of(expected));
		when(cs.getById(1L)).thenReturn(cart);
		when(tr.save(any())).thenReturn(expected);
		when(feeRepo.findById(1)).thenReturn(Optional.of(fee));
		Payment p = new Payment();
		p.setState("created");
		p.setId("dummy");
		List<Links> links = new ArrayList<>();
		Links link = new Links();
		link.setHref("ABC");
		link.setRel("approval_url");
		p.setLinks(links);
		when(ps.createPayment(any())).thenReturn(p);
		when(pos.preparePayout(anyLong())).thenReturn(anyList());
		Transaction result = ts.createTransaction(cart.getId(), user.getId());
		assertThat(result.getStatus()).isEqualTo(Transaction.Status.CREATED);

	}

	@Test
	void testTS15() {

		User user = new User();
		user.setId(2L);
		Cart cart = new Cart();
		cart.setId(2L);
		cart.setUser(user);

		Product product = new Product();
		product.setId(1L);
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
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
		expected.setId(1L);
		expected.setAmount(2.2);
		expected.setCart(cart);
		expected.setFee(fee);
		expected.setStatus(Status.CREATED);
		when(us.getById(user.getId())).thenReturn(user);
		when(cs.isUserOwnCart(2L, 2L)).thenReturn(true);
		when(cs.isCartHadPurchased(1L)).thenReturn(false);
		when(tr.findById(anyLong())).thenReturn(Optional.of(expected));
		when(cs.getById(1L)).thenReturn(cart);
		when(tr.save(any())).thenReturn(expected);
		when(feeRepo.findById(1)).thenReturn(Optional.of(fee));
		Payment p = new Payment();
		p.setState("created");
		p.setId("dummy");
		List<Links> links = new ArrayList<>();
		Links link = new Links();
		link.setHref("ABC");
		link.setRel("approval_url");
		p.setLinks(links);
		when(ps.createPayment(any())).thenReturn(p);
		when(cs.getById(anyLong())).thenReturn(cart);
		when(pos.preparePayout(anyLong())).thenReturn(anyList());
		Transaction result = ts.createTransaction(cart.getId(), user.getId());
		assertThat(result.getStatus()).isEqualTo(Transaction.Status.CREATED);

	}

	@Test
	void testTS21() {
		Transaction mockTransaction = new Transaction();
		mockTransaction.setPaypalId("mock_payment_id");
		mockTransaction.setStatus(Transaction.Status.APPROVED);
		when(tr.findByPaypalId("mock_payment_id")).thenReturn(mockTransaction);
		Payment p = new Payment();
		p.setState("approved");
		p.setId("dummy");
		List<Links> links = new ArrayList<>();
		Links link = new Links();
		link.setHref("ABC");
		link.setRel("approval_url");
		p.setLinks(links);
		when(ps.getPaymentByPaypalId(any())).thenReturn(p);
		when(ps.executePayment("mock_payment_id", "mock_payer_id")).thenReturn(p);
		// when(ts.fetchTransactionStatus(anyLong())).thenReturn(mockTransaction);
		when(tr.findById(any())).thenReturn(Optional.of(mockTransaction));
		when(tr.save(mockTransaction)).thenReturn(mockTransaction);
		// Call the method and verify that the transaction is returned successfully
		Transaction result = ts.executeTransaction("mock_payment_id", "mock_payer_id");
		assertEquals(mockTransaction, result);
	}

	@Test
	void testTS23() {
		 // Mock the transaction repo to return null
	    when(tr.findByPaypalId("paymentId2")).thenReturn(null);
	    // Call the method and expect a ResourceNotFoundException to be thrown
	    assertThrows(ResourceNotFoundException.class, () -> {
	    	ts.executeTransaction("invalid_payment_id", "mock_payer_id");
	    });
	}

	@Test
	void testTS22() {
		Transaction mockTransaction = new Transaction();
		mockTransaction.setPaypalId("mock_payment_id");
		mockTransaction.setStatus(Transaction.Status.CANCELED);
		when(tr.findByPaypalId("mock_payment_id")).thenReturn(mockTransaction);
		Payment p = new Payment();
		p.setState("approved");
		p.setId("dummy");
		List<Links> links = new ArrayList<>();
		Links link = new Links();
		link.setHref("ABC");
		link.setRel("approval_url");
		p.setLinks(links);
		when(ps.getPaymentByPaypalId(any())).thenReturn(p);
		when(ps.executePayment("mock_payment_id", "mock_payer_id")).thenReturn(p);
		// when(ts.fetchTransactionStatus(anyLong())).thenReturn(mockTransaction);
		when(tr.findById(any())).thenReturn(Optional.of(mockTransaction));
		when(tr.save(mockTransaction)).thenReturn(mockTransaction);
		// Call the method and verify that the transaction is returned successfully

		assertThrows(IllegalAccessError.class, () -> {
			ts.executeTransaction("mock_payment_id", "mock_payer_id");
		});
	}

	@Test
	void testTS24() {
		Transaction mockTransaction = new Transaction();
		mockTransaction.setPaypalId("mock_payment_id");
		mockTransaction.setStatus(Transaction.Status.APPROVED);
		when(tr.findByPaypalId("mock_payment_id")).thenReturn(mockTransaction);
		Payment p = new Payment();
		p.setState("approved");
		p.setId("dummy");
		List<Links> links = new ArrayList<>();
		Links link = new Links();
		link.setHref("ABC");
		link.setRel("approval_url");
		p.setLinks(links);
		when(ps.getPaymentByPaypalId(any())).thenReturn(p);
		when(ps.executePayment("mock_payment_id", "mock_payer_id")).thenReturn(p);
		// when(ts.fetchTransactionStatus(anyLong())).thenReturn(mockTransaction);
		when(tr.findById(any())).thenReturn(Optional.of(mockTransaction));
		when(tr.save(mockTransaction)).thenReturn(mockTransaction);
		// Call the method and verify that the transaction is returned successfully

		assertThrows(IllegalArgumentException.class, () -> {
			ts.executeTransaction("mock_payment_id", "");
		});
	}

	@Test
	void testTS31() {
		// Arrang
		Transaction mockTrans = new Transaction();
		mockTrans.setId(1L);
		mockTrans.setStatus(Status.CREATED);

		Transaction expected = new Transaction();
		expected.setId(1L);
		expected.setStatus(Status.CANCELED);
		// Act
		when(tr.save(any())).thenReturn(expected);
		when(tr.findById(anyLong())).thenReturn(Optional.of(mockTrans));
		Transaction result = ts.cancel(1L);

		// Assert
		assertEquals(Transaction.Status.CANCELED, result.getStatus());
	}

	@Test
	void testTS32() {
		// Arrang
		Transaction mockTrans = new Transaction();
		mockTrans.setId(1L);
		mockTrans.setStatus(Status.COMPLETED);

		Transaction expected = new Transaction();
		expected.setId(1L);
		expected.setStatus(Status.CANCELED);
		// Act
		when(tr.save(any())).thenReturn(expected);
		when(tr.findById(anyLong())).thenReturn(Optional.of(mockTrans));

		// Assert
		assertThrows(IllegalArgumentException.class, () -> {
			ts.cancel(1L);
		});
	}

	@Test
	void testTS33() {
		// Arrang
		Transaction mockTrans = new Transaction();
		mockTrans.setId(1L);
		mockTrans.setStatus(Status.EXPIRED);

		Transaction expected = new Transaction();
		expected.setId(1L);
		expected.setStatus(Status.CANCELED);
		// Act
		when(tr.save(any())).thenReturn(expected);
		when(tr.findById(anyLong())).thenReturn(Optional.of(mockTrans));

		// Assert
		assertThrows(IllegalArgumentException.class, () -> {
			ts.cancel(1L);
		});
	}

	@Test
	void testTS34() {
		// Arrang
		Transaction mockTrans = new Transaction();
		mockTrans.setId(1L);
		mockTrans.setStatus(Status.EXPIRED);

		Transaction expected = new Transaction();
		expected.setId(1L);
		expected.setStatus(Status.CANCELED);
		// Act
		when(tr.save(any())).thenReturn(expected);
		when(tr.findById(anyLong())).thenReturn(Optional.of(mockTrans));
		// Assert
		assertThrows(IllegalArgumentException.class, () -> {
			ts.cancel(1L);
		});
	}

	@Test
	void testTS41() {
		// Arrang
		Long transactionId = 12345L;
		Transaction transaction = new Transaction();
		transaction.setId(transactionId);
		transaction.setPaypalId("paypal-id");
		transaction.setStatus(Transaction.Status.CREATED);
		Payment payment = new Payment();
		payment.setId("paypal-id");
		payment.setState("completed");
		when(ps.getPaymentByPaypalId("paypal-id")).thenReturn(payment);
		when(tr.findById(transactionId)).thenReturn(java.util.Optional.of(transaction));
		
		Transaction expectedTransaction = new Transaction();
		transaction.setId(transactionId);
		expectedTransaction.setStatus(Transaction.Status.COMPLETED);
		expectedTransaction.setDescription(null);
		when(tr.save(any())).thenReturn(expectedTransaction);

		// Act
		Transaction actualTransaction = ts.fetchTransactionStatus(transactionId);

		// Assert
		assertEquals(expectedTransaction, actualTransaction);
		
	}
	
	@Test
	void testTS42() {
		// Arrang
		Long transactionId = 12345L;
		Transaction transaction = new Transaction();
		transaction.setId(transactionId);
		transaction.setPaypalId("paypal-id");
		transaction.setStatus(Transaction.Status.CREATED);
		Payment payment = new Payment();
		payment.setId("paypal-id");
		payment.setState("failed");
		when(ps.getPaymentByPaypalId("paypal-id")).thenReturn(payment);
		when(tr.findById(transactionId)).thenReturn(java.util.Optional.of(transaction));
		
		Transaction expectedTransaction = transaction;
		transaction.setId(transactionId);
		expectedTransaction.setStatus(Transaction.Status.FAILED);
		expectedTransaction.setDescription(null);
		when(tr.save(expectedTransaction)).thenReturn(expectedTransaction);

		// Act
		Transaction actualTransaction = ts.fetchTransactionStatus(transactionId);

		// Assert
		assertEquals(expectedTransaction, actualTransaction);
		
	}
	
	@Test
	void testTS43() {
		// Arrang
		Long transactionId = 12345L;
		Transaction transaction = new Transaction();
		transaction.setId(transactionId);
		transaction.setPaypalId("paypal-id");
		transaction.setStatus(Transaction.Status.CREATED);
		Payment payment = new Payment();
		payment.setId("paypal-id");
		payment.setState("");
		when(ps.getPaymentByPaypalId("paypal-id")).thenReturn(payment);
		when(tr.findById(transactionId)).thenReturn(java.util.Optional.of(transaction));
		
		Transaction expectedTransaction = transaction;
		expectedTransaction.setStatus(Transaction.Status.EXPIRED);

		when(tr.save(expectedTransaction)).thenReturn(expectedTransaction);

		// Act
		Transaction actualTransaction = ts.fetchTransactionStatus(transactionId);

		// Assert
		assertEquals(expectedTransaction, actualTransaction);
		
	}
	
	@Test
	void testTS4() {
		// Arrang
		Long transactionId = 12345L;
		Transaction transaction = new Transaction();
		transaction.setId(transactionId);
		transaction.setPaypalId("paypal-id");
		transaction.setStatus(Transaction.Status.CREATED);
		Payment payment = new Payment();
		payment.setId("paypal-id");
		payment.setState("approved");
		when(tr.findById(anyLong())).thenThrow(NoSuchElementException.class);
		// Assert
		assertThrows(NoSuchElementException.class, ()->{
			ts.fetchTransactionStatus(transactionId);
		});
	}
}
