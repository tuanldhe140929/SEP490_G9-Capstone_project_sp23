package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
import com.SEP490_G9.entities.Category;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.serviceImpls.ProductDetailsServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class ProductDetailsServiceTest {

	@Mock
	ProductDetailsRepository productDetailsRepo;

	@InjectMocks
	ProductDetailsServiceImpl pdsi;

	@Test
	void testGetProductForSearching() {

		String version = "1.0.0";
		String version1 = "1.0.1";
		String version2 = "1.0.2";
		String version3 = "1.0.3";

		Product product = new Product();
		product.setActiveVersion(version);
		product.setId(1L);

		Product product1 = new Product();
		product1.setId(2L);
		product1.setActiveVersion(version1);

		Product product2 = new Product();
		product2.setId(3L);
		product2.setActiveVersion(version2);

		Product product3 = new Product();
		product3.setId(4L);
		product3.setActiveVersion(version3);

		Category cat1 = new Category();
		cat1.setId(1);
		cat1.setName("Cat1");

		Category cat2 = new Category();
		cat2.setId(2);
		cat2.setName("Cat2");

		Category cat3 = new Category();
		cat3.setId(3);
		cat3.setName("Cat3");

		Category cat4 = new Category();
		cat4.setId(4);
		cat4.setName("Cat4");

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(product);
		pd1.setVersion(version);
		pd1.setCategory(cat1);
		pd1.setPrice(100);
		pd1.setName("abcd");

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(product1);
		pd2.setVersion(version1);
		pd2.setCategory(cat2);
		pd2.setPrice(1000);
		pd2.setName("abcd");

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(product2);
		pd3.setVersion(version2);
		pd3.setCategory(cat3);
		pd3.setPrice(10000);
		pd3.setName("abcd");

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(product3);
		pd4.setVersion(version3);
		pd4.setCategory(cat4);
		pd4.setPrice(100000);
		pd4.setName("abcd");

		List<ProductDetails> allProduct = new ArrayList<>();
		allProduct.add(pd1);
		allProduct.add(pd2);
		allProduct.add(pd3);
		allProduct.add(pd4);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd2);
		expected.add(pd3);
		expected.add(pd4);

		when(productDetailsRepo.findAll()).thenReturn(allProduct);
		
		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product.getId(), version)).thenReturn(pd1);
		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product1.getId(), version1)).thenReturn(pd2);
		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product2.getId(), version2)).thenReturn(pd3);
		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product3.getId(), version3)).thenReturn(pd4);
		
		List<ProductDetails> result = pdsi.getProductForSearching("a", 0, 200, 1000000);
		assertThat(result).isEqualTo(expected);
	}
	
	@Test
	void testGetAllPendingProducts() {
		String version = "1.0.0";
		String version1 = "1.0.1";
		String version2 = "1.0.2";
		String version3 = "1.0.3";

		Product product = new Product();
		product.setActiveVersion(version);
		product.setId(1L);
		product.setApproved("PENDING");

		Product product1 = new Product();
		product1.setId(2L);
		product1.setActiveVersion(version1);
		product.setApproved("APPROVED");

		Product product2 = new Product();
		product2.setId(3L);
		product2.setActiveVersion(version2);
		product.setApproved("REJECTED");

		Product product3 = new Product();
		product3.setId(4L);
		product3.setActiveVersion(version3);
		product.setApproved("PENDING");

		Category cat1 = new Category();
		cat1.setId(1);
		cat1.setName("Cat1");

		Category cat2 = new Category();
		cat2.setId(2);
		cat2.setName("Cat2");

		Category cat3 = new Category();
		cat3.setId(3);
		cat3.setName("Cat3");

		Category cat4 = new Category();
		cat4.setId(4);
		cat4.setName("Cat4");

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(product);
		pd1.setVersion(version);
		pd1.setCategory(cat1);
		pd1.setPrice(100);
		pd1.setName("abcd");

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(product1);
		pd2.setVersion(version1);
		pd2.setCategory(cat2);
		pd2.setPrice(1000);
		pd2.setName("abcd");

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(product2);
		pd3.setVersion(version2);
		pd3.setCategory(cat3);
		pd3.setPrice(10000);
		pd3.setName("abcd");

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(product3);
		pd4.setVersion(version3);
		pd4.setCategory(cat4);
		pd4.setPrice(100000);
		pd4.setName("abcd");

		List<ProductDetails> allProduct = new ArrayList<>();
		allProduct.add(pd1);
		allProduct.add(pd2);
		allProduct.add(pd3);
		allProduct.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allProduct);
		
		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product.getId(), version)).thenReturn(pd1);
		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product1.getId(), version1)).thenReturn(pd2);
		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product2.getId(), version2)).thenReturn(pd3);
		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product3.getId(), version3)).thenReturn(pd4);
		
		List<ProductDetails> allPendingProducts = pdsi.getAllPendingProducts();
		assertEquals(2, allPendingProducts.size());
	}
}
