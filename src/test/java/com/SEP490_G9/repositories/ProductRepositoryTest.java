package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.repository.ProductRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	public void testFindBySellerId() {
		Long sellerId = 2L;
		int expectedSize = 4;
		List<Product> products = productRepository.findBySellerId(sellerId);
		assertThat(products.size()).isEqualTo(expectedSize);
	}
	
	@Test
	public void testFindBySellerIdA() {
		Long sellerId = -2L;
		int expectedSize = 4;
		List<Product> products = productRepository.findBySellerId(sellerId);
		assertThat(products.size()).isEqualTo(0);
	}

	@Test
	public void testFindById() {
		Long expectedId = 1L;
		Product result = productRepository.findById(expectedId).orElseThrow();
		assertThat(result.getId()).isEqualTo(expectedId);
	}

	@Test
	public void testFindByEnabled() {
		int expectedSize = 4;
		List<Product> result = productRepository.findByEnabled(true);
		assertThat(result.size()).isEqualTo(expectedSize);

	}

	@Test
	public void testFindByApproved() {
		int expectedSize = 3;
		List<Product> result = productRepository.findByApproved("APPROVED");
		assertThat(result.size()).isEqualTo(expectedSize);

	}

	@Test
	public void testFindAll() {
		int expectedSize = 4;
		List<Product> result = productRepository.findAll();
		assertThat(result.size()).isEqualTo(expectedSize);

	}

	@Test
	public void testSaveProduct() {
		Seller seller = new Seller();
		seller.setId((long) 2);
		Product product = new Product();
		product.setSeller(seller);
		Product result = productRepository.save(product);
		assertNotNull(result);
	}
	
	@Test
	public void testSaveProductA() {
		Seller seller = new Seller();
		seller.setId((long) -2);
		Product product = new Product();
		product.setSeller(seller);
		Product result = productRepository.save(product);
		assertNotNull(result);
	}
	
	
	@Test
	public void testFindBySellerIdAndApproved() {
		Seller seller = new Seller();
		seller.setId(2L);
		int expectedSize = 3;
		List<Product> results = productRepository.findBySellerIdAndApproved(seller.getId(),"APPROVED");
		assertThat(results.size()).isEqualTo(expectedSize);

	}
	
	@Test
	public void testFindBySellerIdAndApprovedA() {
		Seller seller = new Seller();
		seller.setId(2L);
		int expectedSize = 0;
		List<Product> results = productRepository.findBySellerIdAndApproved(seller.getId(),"APPROVEDD");
		assertThat(results.size()).isEqualTo(expectedSize);

	}
}
