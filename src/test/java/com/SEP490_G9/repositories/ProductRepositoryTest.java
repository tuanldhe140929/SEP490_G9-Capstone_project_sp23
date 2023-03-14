package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.SellerRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	public void testFindBySellerId() {
		Seller seller = new Seller();
		seller.setId((long) 2);

		Product product = new Product();
		product.setId((long) 1);
		product.setSeller(seller);
		productRepository.save(product);

		List<Product> products = productRepository.findBySellerId(seller.getId());
		assertThat(products.size()).isEqualTo(1);
	}

	@Test
	public void testFindById() {

		Seller seller = new Seller();
		seller.setId(2L);
		Product product = new Product();
		product.setId(1L);
		product.setSeller(seller);
		productRepository.save(product);
		Product result = productRepository.findById(product.getId()).orElseThrow();
		assertThat(result.getId()).isEqualTo(product.getId());
		assertThat(result.getSeller().getId()).isEqualTo(product.getSeller().getId());
	}

	@Test
	public void testFindByEnabled() {
		Seller seller = new Seller();
		seller.setId((long) 2);

		Product product = new Product();
		product.setId((long) 1);
		product.setEnabled(false);
		product.setSeller(seller);

		productRepository.save(product);

		Product product2 = new Product();
		product2.setId((long) 2);
		product2.setEnabled(true);
		product2.setSeller(seller);
		productRepository.save(product2);

		List<Product> result = productRepository.findByEnabled(false);
		assertThat(result.size()).isEqualTo(1);

	}

	@Test
	public void testSaveProduct() {
		Seller seller = new Seller();
		seller.setId((long) 2);
		Product product = new Product();
		product.setSeller(seller);
		product.setId((long) 403);

		Product result = productRepository.save(product);
		assertThat(result).isEqualTo(product);
	}
}
