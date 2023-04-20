package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyListOf;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Payout;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.TransactionFee;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.entities.Transaction.Status;
import com.SEP490_G9.repository.PayoutRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.service.PayoutService;
import com.SEP490_G9.service.PaypalService;
import com.SEP490_G9.service.serviceImpls.PayoutServiceImpl;
import com.SEP490_G9.service.serviceImpls.PaypalServiceImpl;
import com.paypal.api.payments.PayoutBatch;
import com.paypal.api.payments.PayoutBatchHeader;
import com.paypal.api.payments.PayoutItemDetails;

import junit.framework.Assert;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class PayoutServiceTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Mock
	private TransactionRepository transactionRepo;

	@Mock
	private PayoutRepository payoutRepository;

	@InjectMocks
	PayoutServiceImpl payoutService;

	@Mock
	private PaypalServiceImpl paypalService;

	@Test
	void testPOS1_1() {
		// Set up a mock Transaction
		Transaction transaction = new Transaction();
		Cart cart = new Cart();
		List<CartItem> items = new ArrayList<>();
		Product product1 = new Product();
		ProductDetails details1 = new ProductDetails();
		Seller seller1 = new Seller();
		seller1.setId(1L);
		details1.setProduct(product1);
		product1.setSeller(seller1);
		details1.setPrice(10.0);
		CartItem item1 = new CartItem();
		item1.setProductDetails(details1);
		items.add(item1);
		Product product2 = new Product();
		ProductDetails details2 = new ProductDetails();

		details2.setProduct(product2);
		product2.setSeller(seller1);
		details2.setPrice(20.0);
		CartItem item2 = new CartItem();
		item2.setProductDetails(details2);
		items.add(item2);
		cart.setItems(items);
		transaction.setCart(cart);
		transaction.setStatus(Status.COMPLETED);
		TransactionFee fee = new TransactionFee();
		fee.setPercentage(10);
		transaction.setFee(fee);

		// Set up a mock Seller

		// Mock the transactionRepo and payoutRepository
		when(transactionRepo.findById(1L)).thenReturn(Optional.of(transaction));
		when(payoutRepository.save(any(Payout.class))).thenReturn(new Payout());

		// Call the method being tested
		List<Payout> payouts = payoutService.preparePayout(1L);

		// Verify the results
		assertEquals(1, payouts.size());
	}

	@Test
	void testPOS1_2() {
		// Set up a mock Transaction
		Transaction transaction = new Transaction();
		Cart cart = new Cart();
		List<CartItem> items = new ArrayList<>();
		Product product1 = new Product();
		ProductDetails details1 = new ProductDetails();
		Seller seller1 = new Seller();
		seller1.setId(1L);
		details1.setProduct(product1);
		product1.setSeller(seller1);
		details1.setPrice(10.0);
		CartItem item1 = new CartItem();
		item1.setProductDetails(details1);
		items.add(item1);
		Product product2 = new Product();
		ProductDetails details2 = new ProductDetails();
		Seller seller2 = new Seller();
		seller2.setId(2L);
		details2.setProduct(product2);
		product2.setSeller(seller2);
		details2.setPrice(20.0);
		CartItem item2 = new CartItem();
		item2.setProductDetails(details2);
		items.add(item2);
		cart.setItems(items);
		transaction.setCart(cart);
		transaction.setStatus(Status.CANCELED);
		TransactionFee fee = new TransactionFee();
		fee.setPercentage(10);
		transaction.setFee(fee);

		// Set up a mock Seller
		Seller seller = new Seller();
		seller.setId(1L);

		// Mock the transactionRepo and payoutRepository
		when(transactionRepo.findById(2L)).thenThrow(NoSuchElementException.class);
		when(payoutRepository.save(any(Payout.class))).thenReturn(new Payout());

		// Call the method being tested

		// Verify the results
		assertThrows(NoSuchElementException.class, () -> {
			payoutService.preparePayout(1L);
		});
	}

	@Test
	void testPOS1_3() {
		// Set up a mock Transaction
		Transaction transaction = new Transaction();
		Cart cart = new Cart(); 
		List<CartItem> items = new ArrayList<>();
		Product product1 = new Product();
		ProductDetails details1 = new ProductDetails();
		Seller seller1 = new Seller();
		seller1.setId(1L);
		details1.setProduct(product1);
		product1.setSeller(seller1);
		details1.setPrice(10.0);
		CartItem item1 = new CartItem();
		item1.setProductDetails(details1);
		// items.add(item1);
		Product product2 = new Product();
		ProductDetails details2 = new ProductDetails();
		Seller seller2 = new Seller();
		seller2.setId(2L);
		details2.setProduct(product2);
		product2.setSeller(seller2);
		details2.setPrice(20.0);
		CartItem item2 = new CartItem();
		item2.setProductDetails(details2);
		// items.add(item2);
		cart.setItems(items);
		transaction.setCart(cart);
		transaction.setStatus(Status.CANCELED);
		TransactionFee fee = new TransactionFee();
		fee.setPercentage(10);
		transaction.setFee(fee);

		// Set up a mock Seller
		Seller seller = new Seller();
		seller.setId(1L);

		// Mock the transactionRepo and payoutRepository
		when(transactionRepo.findById(1L)).thenReturn(Optional.of(transaction));
		when(payoutRepository.save(any(Payout.class))).thenReturn(new Payout());

		// Call the method being tested

		// Verify the results
		assertThrows(IllegalArgumentException.class, () -> {
			payoutService.preparePayout(1L);
		});
	}

	@Test
	void testPOS1_4() {
		// Set up a mock Transaction
		Transaction transaction = new Transaction();
		Cart cart = new Cart();
		List<CartItem> items = new ArrayList<>();
		Product product1 = new Product();
		ProductDetails details1 = new ProductDetails();
		Seller seller1 = new Seller();
		seller1.setId(1L);
		details1.setProduct(product1);
		product1.setSeller(seller1);
		details1.setPrice(10.0);
		CartItem item1 = new CartItem();
		item1.setProductDetails(details1);
		items.add(item1);
		Product product2 = new Product();
		ProductDetails details2 = new ProductDetails();
		Seller seller2 = new Seller();
		seller2.setId(2L);
		details2.setProduct(product2);
		product2.setSeller(seller2);
		details2.setPrice(20.0);
		CartItem item2 = new CartItem();
		item2.setProductDetails(details2);
		items.add(item2);

		cart.setItems(items);
		transaction.setCart(cart);
		transaction.setStatus(Status.CANCELED);
		TransactionFee fee = new TransactionFee();
		fee.setPercentage(10);
		transaction.setFee(fee);

		// Set up a mock Seller
		Seller seller = new Seller();
		seller.setId(1L);

		// Mock the transactionRepo and payoutRepository
		when(transactionRepo.findById(2L)).thenReturn(Optional.of(transaction));
		when(payoutRepository.save(any(Payout.class))).thenReturn(new Payout());

		// Call the method being tested
		List<Payout> payouts = payoutService.preparePayout(2L);

		// Verify the results
		assertEquals(2, payouts.size());

	}

	@Test
	void testPOS2_1() {
		Transaction transaction = new Transaction();
		transaction.setStatus(Transaction.Status.COMPLETED);
		transaction.setId(1L);

		// Create a sample payout
		Payout payout = new Payout();
		payout.setId(1L);
		payout.setSeller(new Seller());
		payout.setAmount(100.0);
		payout.setTransaction(transaction);

		// Create a sample list of payouts
		List<Payout> payouts = new ArrayList<>();
		payouts.add(payout);

		Mockito.when(payoutRepository.findByTransactionId(1L)).thenReturn(payouts);
		PayoutBatch batch = new PayoutBatch();
		PayoutBatchHeader batchHeader = new PayoutBatchHeader();
		batchHeader.setPayoutBatchId("ABC");

		batch.setBatchHeader(batchHeader);
		batchHeader.setBatchStatus("SUCCESS");

		Mockito.when(paypalService.executePayout(payout.getSeller().getEmail(), payout.getAmount())).thenReturn(batch);

		Payout payout2 = payout;
		payout2.setStatus(Payout.Status.SUCCESS);
		when(payoutRepository.findById(1L)).thenReturn(Optional.of(payout));
		when(paypalService.getPayoutByBatchId("ABC")).thenReturn(batch);
		when(paypalService.getPayoutFee(anyString())).thenReturn("0.25");
		when(payoutRepository.saveAll(anyList())).thenReturn(payouts);
		List<Payout> result = payoutService.executePayout(1L);

		// Assert
		assertEquals(1, result.size());

	}
	
	@Test
	void testPOS2_2() {
		Transaction transaction = new Transaction();
		transaction.setStatus(Transaction.Status.EXPIRED);
		transaction.setId(1L);	

		// Create a sample payout
		Payout payout = new Payout();
		payout.setId(1L);
		payout.setSeller(new Seller());
		payout.setAmount(100.0);
		payout.setTransaction(transaction);

		// Create a sample list of payouts
		List<Payout> payouts = new ArrayList<>();
		payouts.add(payout);

		Mockito.when(payoutRepository.findByTransactionId(1L)).thenReturn(payouts);
		PayoutBatch batch = new PayoutBatch();
		PayoutBatchHeader batchHeader = new PayoutBatchHeader();
		batchHeader.setPayoutBatchId("ABC");

		batch.setBatchHeader(batchHeader);
		batchHeader.setBatchStatus("SUCCESS");

		Mockito.when(paypalService.executePayout(payout.getSeller().getEmail(), payout.getAmount())).thenReturn(batch);

		Payout payout2 = payout;
		payout2.setStatus(Payout.Status.SUCCESS);
		when(payoutRepository.findById(1L)).thenReturn(Optional.of(payout));
		when(paypalService.getPayoutByBatchId("ABC")).thenReturn(batch);
		when(paypalService.getPayoutFee(anyString())).thenReturn("0.25");
		when(payoutRepository.saveAll(anyList())).thenReturn(payouts);
		
		assertThrows(IllegalArgumentException.class, () -> {
			payoutService.executePayout(1L);
		});

	}
	
	@Test
	void testPOS2_3() {
		Transaction transaction = new Transaction();
		transaction.setStatus(Transaction.Status.COMPLETED);
		transaction.setId(1L);	

		// Create a sample payout
		Payout payout = new Payout();
		payout.setId(1L);
		payout.setSeller(new Seller());
		payout.setAmount(100.0);
		payout.setTransaction(transaction);

		// Create a sample list of payouts
		List<Payout> payouts = new ArrayList<>();
	

		Mockito.when(payoutRepository.findByTransactionId(1L)).thenReturn(payouts);
		PayoutBatch batch = new PayoutBatch();
		PayoutBatchHeader batchHeader = new PayoutBatchHeader();
		batchHeader.setPayoutBatchId("ABC");

		batch.setBatchHeader(batchHeader);
		batchHeader.setBatchStatus("SUCCESS");

		Mockito.when(paypalService.executePayout(payout.getSeller().getEmail(), payout.getAmount())).thenReturn(batch);

		Payout payout2 = payout;
		payout2.setStatus(Payout.Status.SUCCESS);
		when(payoutRepository.findById(1L)).thenReturn(Optional.of(payout));
		when(paypalService.getPayoutByBatchId("ABC")).thenReturn(batch);
		when(paypalService.getPayoutFee(anyString())).thenReturn("0.25");
		when(payoutRepository.saveAll(anyList())).thenReturn(payouts);
		
		assertThrows(ResourceNotFoundException.class, () -> {
			payoutService.executePayout(1L);
		});

	}
	
	@Test
	void testPOS2_4() {
		Transaction transaction = new Transaction();
		transaction.setStatus(Transaction.Status.COMPLETED);
		transaction.setId(1L);	

		// Create a sample payout
		Payout payout = new Payout();
		payout.setId(1L);
		payout.setSeller(new Seller());
		payout.setAmount(100.0);
		payout.setTransaction(transaction);

		// Create a sample list of payouts
		List<Payout> payouts = new ArrayList<>();
		payouts.add(payout);
		payouts.add(payout);
		payouts.add(payout);
		payouts.add(payout);
		payouts.add(payout);
		payouts.add(payout);
		payouts.add(payout);
		payouts.add(payout);
		payouts.add(payout);
		payouts.add(payout);

		Mockito.when(payoutRepository.findByTransactionId(1L)).thenReturn(payouts);
		PayoutBatch batch = new PayoutBatch();
		PayoutBatchHeader batchHeader = new PayoutBatchHeader();
		batchHeader.setPayoutBatchId("ABC");

		batch.setBatchHeader(batchHeader);
		batchHeader.setBatchStatus("SUCCESS");

		Mockito.when(paypalService.executePayout(payout.getSeller().getEmail(), payout.getAmount())).thenReturn(batch);

		Payout payout2 = payout;
		payout2.setStatus(Payout.Status.SUCCESS);
		when(payoutRepository.findById(1L)).thenReturn(Optional.of(payout));
		when(paypalService.getPayoutByBatchId("ABC")).thenReturn(batch);
		when(paypalService.getPayoutFee(anyString())).thenReturn("0.25");
		when(payoutRepository.saveAll(anyList())).thenReturn(payouts);
		List<Payout> result = payoutService.executePayout(1L);
		assertEquals(10, result.size());

	}
	
	@Test
	public void testPOS3_3() {
	    Long transactionId = 100L;
	    List<Payout> payouts = new ArrayList<>();
	    Transaction transaction = new Transaction();
	    transaction.setStatus(Status.COMPLETED);
	    Payout payout = new Payout();
	    payout.setTransaction(transaction);
	    payouts.add(payout);
	    Mockito.when(payoutRepository.findByTransactionId(transactionId)).thenReturn(payouts);
	    
	    assertThrows(IllegalArgumentException.class, () -> {
	    	payoutService.cancelPayout(transactionId);
	    });
	}
	
	@Test
	public void testPOS3_1() {
	    Long transactionId = 100L;
	    List<Payout> payouts = new ArrayList<>();
	    Transaction transaction = new Transaction();
	    transaction.setStatus(Status.APPROVED);
	    Payout payout = new Payout();
	    payout.setTransaction(transaction);
	    payouts.add(payout);
	    when(payoutRepository.saveAll(anyList())).thenReturn(payouts);
	    Mockito.when(payoutRepository.findByTransactionId(transactionId)).thenReturn(payouts);
	    List<Payout> results = payoutService.cancelPayout(transactionId);
	    assertThat(results.size()).isEqualTo(1);
	}
	
	@Test
	public void testPOS3_2() {
	    Long transactionId = 100L;
	    List<Payout> payouts = new ArrayList<>();
	    Transaction transaction = new Transaction();
	    transaction.setStatus(Status.CREATED);
	    Payout payout = new Payout();
	    payout.setTransaction(transaction);
	    payouts.add(payout);
	    Mockito.when(payoutRepository.findByTransactionId(transactionId)).thenReturn(payouts);
when(payoutRepository.saveAll(anyList())).thenReturn(payouts);
	    List<Payout> results = payoutService.cancelPayout(transactionId);
	    assertThat(results.size()).isEqualTo(1);
	}
	
	@Test
	public void testPOS3_4() {
	    Long transactionId = 100L;
	    List<Payout> payouts = new ArrayList<>();
	    Transaction transaction = new Transaction();
	    transaction.setStatus(Status.CREATED);
	    Payout payout = new Payout();
	    payout.setTransaction(transaction);
	   
	    Mockito.when(payoutRepository.findByTransactionId(transactionId)).thenReturn(payouts);
	    assertThrows(ResourceNotFoundException.class, () -> {
	    	payoutService.cancelPayout(transactionId);
	    });
	}
	
	@Test
	public void testPOS3_5() {
	    Long transactionId = 100L;
	    List<Payout> payouts = new ArrayList<>();
	    Transaction transaction = new Transaction();
	    transaction.setStatus(Status.CREATED);
	    Payout payout = new Payout();
	    payout.setTransaction(transaction);
	    payouts.add(payout);
	    payouts.add(payout);
	    payouts.add(payout);
	    payouts.add(payout);
	    payouts.add(payout);
	    payouts.add(payout);
	    payouts.add(payout);
	    payouts.add(payout);
	    payouts.add(payout);
	    payouts.add(payout);
	    when(payoutRepository.saveAll(anyList())).thenReturn(payouts);
	    Mockito.when(payoutRepository.findByTransactionId(transactionId)).thenReturn(payouts);
	    List<Payout> results = payoutService.cancelPayout(transactionId);
	    assertThat(results.size()).isEqualTo(10);
	}
	
	@Test
	public void testPOS4_1() {
		   // Arrange
	    Long payoutId = 1234L;
	    String batchId = "batch1234";
	    Payout payout = new Payout();
	    payout.setId(payoutId);
	    Mockito.when(payoutRepository.findById(1234L)).thenReturn(Optional.of(payout));

	    PayoutBatch payoutBatch = new PayoutBatch();
	    PayoutBatchHeader header = new PayoutBatchHeader();
	    header.setBatchStatus("SUCCESS");
	    payoutBatch.setBatchHeader(header);
	    Mockito.when(paypalService.getPayoutByBatchId(batchId)).thenReturn(payoutBatch);

	    double expectedFee = 2.5;
	    Mockito.when(paypalService.getPayoutFee(batchId)).thenReturn(String.valueOf(expectedFee));

	    // Act
	    Payout result = payoutService.fetchPayoutStatus(payoutId, batchId);

	    // Assert
	    Mockito.verify(payoutRepository, Mockito.times(1)).save(payout);
	    assertEquals(Payout.Status.SUCCESS, result.getStatus());
	    assertEquals(expectedFee, result.getPayoutFee(), 0.01);
	}
	
	
	@Test
	public void testPOS4_2() {
		   // Arrange
	    Long payoutId = 1234L;
	    String batchId = "batch1234";
	    Payout payout = new Payout();
	    payout.setId(payoutId);
	    Mockito.when(payoutRepository.findById(1234L)).thenReturn(Optional.of(payout));

	    PayoutBatch payoutBatch = new PayoutBatch();
	    PayoutBatchHeader header = new PayoutBatchHeader();
	    header.setBatchStatus("PENDING");
	    payoutBatch.setBatchHeader(header);
	    Mockito.when(paypalService.getPayoutByBatchId(batchId)).thenReturn(payoutBatch);

	    double expectedFee = 2.5;
	    Mockito.when(paypalService.getPayoutFee(batchId)).thenReturn(String.valueOf(expectedFee));

	    // Act
	    Payout result = payoutService.fetchPayoutStatus(payoutId, batchId);

	    // Assert
	    Mockito.verify(payoutRepository, Mockito.times(1)).save(payout);
	    assertEquals(Payout.Status.PROCESSING, result.getStatus());
	    assertEquals(expectedFee, result.getPayoutFee(), 0.01);
	}
	
	@Test
	public void testPOS4_3() {
		   // Arrange
	    Long payoutId = 1234L;
	    String batchId = "batch1234";
	    Payout payout = new Payout();
	    payout.setId(payoutId);
	    Mockito.when(payoutRepository.findById(1234L)).thenReturn(Optional.of(payout));

	    PayoutBatch payoutBatch = new PayoutBatch();
	    PayoutBatchHeader header = new PayoutBatchHeader();
	    header.setBatchStatus("DENIED");
	    payoutBatch.setBatchHeader(header);
	    Mockito.when(paypalService.getPayoutByBatchId(batchId)).thenReturn(payoutBatch);

	    double expectedFee = 2.5;
	    Mockito.when(paypalService.getPayoutFee(batchId)).thenReturn(String.valueOf(expectedFee));

	    // Act
	    Payout result = payoutService.fetchPayoutStatus(payoutId, batchId);

	    // Assert
	    Mockito.verify(payoutRepository, Mockito.times(1)).save(payout);
	    assertEquals(Payout.Status.FAILED, result.getStatus());
	    assertEquals(expectedFee, result.getPayoutFee(), 0.01);
	}
	
	@Test
	public void testPOS4_5() {
		   // Arrange
	    Long payoutId = 1234L;
	    String batchId = "batch1234";
	    Payout payout = new Payout();
	    payout.setId(payoutId);
	    Mockito.when(payoutRepository.findById(1234L)).thenReturn(Optional.of(payout));

	    PayoutBatch payoutBatch = new PayoutBatch();
	    PayoutBatchHeader header = new PayoutBatchHeader();
	    header.setBatchStatus("");
	    payoutBatch.setBatchHeader(header);
	    Mockito.when(paypalService.getPayoutByBatchId(batchId)).thenReturn(payoutBatch);

	    double expectedFee = 2.5;
	    Mockito.when(paypalService.getPayoutFee(batchId)).thenReturn(String.valueOf(expectedFee));

	    // Act
	    Payout result = payoutService.fetchPayoutStatus(payoutId, batchId);

	    // Assert
	    assertEquals(Payout.Status.FAILED, result.getStatus());
	}
}
