package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
import com.SEP490_G9.entities.Category;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.exception.NumberException;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.serviceImpls.ProductDetailsServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class ProductDetailsServiceTest {

	@Mock
	ProductDetailsRepository productDetailsRepo;
	
	@Mock
	ProductRepository productRepo;

	@InjectMocks
	ProductDetailsServiceImpl pdsi;

	@Test
	void testGetAll() {
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
		pd2.setProduct(product);
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
		pd4.setName("abcd");

		List<ProductDetails> allProduct = new ArrayList<>();
		allProduct.add(pd1);
		allProduct.add(pd2);
		allProduct.add(pd3);
		allProduct.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allProduct);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		expected.add(pd2);
		expected.add(pd3);
		expected.add(pd4);
		
		List<ProductDetails> actual = pdsi.getAll();
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByLatestVer() {
		String v1 = "1.0.0";
		String v2 = "1.0.1";
		
		Product p1 = new Product();
		p1.setActiveVersion(v1);
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setActiveVersion(v2);
		p2.setId(2L);
		
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
		pd1.setVersion(v1);
		pd1.setProduct(p1);
		pd1.setCategory(cat4);
		pd1.setName("asfsf");
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setVersion(v2);
		pd2.setProduct(p1);
		pd2.setCategory(cat1);
		pd2.setName("adasf");
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setVersion(v1);
		pd3.setProduct(p2);
		pd3.setCategory(cat3);
		pd3.setName("osidf");
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setVersion(v2);
		pd4.setProduct(p2);
		pd4.setCategory(cat2);
		pd4.setName("asfas");
		
		List<ProductDetails> allProduct = new ArrayList<>();
		allProduct.add(pd1);
		allProduct.add(pd2);
		allProduct.add(pd3);
		allProduct.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allProduct);
		when(productRepo.findById(1L).get()).thenReturn(p1);
		when(productRepo.findById(2L).get()).thenReturn(p2);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		expected.add(pd4);
		
		List<ProductDetails> allOfIt = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByLatestVer(allOfIt);
		
		assertEquals(expected, actual);

	}
	
	@Test
	void testGetBySeller() {
		Seller seller = new Seller();
		seller.setId(1L);
		seller.setEmail("seller@gmail.com");
		seller.setPassword("password");
		
		Seller seller1 = new Seller();
		seller1.setId(2L);
		seller1.setEmail("seller@gmail.com");
		seller1.setPassword("password");
		
		Product p1 = new Product();
		p1.setId(1L);
		p1.setSeller(seller);
		
		Product p2 = new Product();
		p2.setId(2L);
		p2.setSeller(seller1);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setVersion("1.0.0");
		pd1.setName("adc");
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setVersion("1.0.1");
		pd2.setName("xyz");
	
		List<ProductDetails> allProduct = new ArrayList<>();
		allProduct.add(pd1);

		when(productDetailsRepo.findAll()).thenReturn(allProduct);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		
		List<ProductDetails> unexpected = new ArrayList<>();
		unexpected.add(pd2);
		
		List<ProductDetails> allOfIt = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getBySeller(allOfIt, seller.getId());
		
		assertEquals(expected, actual);
		assertNotEquals(unexpected, actual);
	}
	
	@Test
	void testGetByPending() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setApproved("APPROVED");
		
		Product p2 = new Product();
		p2.setId(2L);
		p2.setApproved("REJECTED");
		
		Product p3 = new Product();
		p3.setId(3L);
		p3.setApproved("PENDING");
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd3);
		
		List<ProductDetails> allProducts = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByPending(allProducts);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByApproved() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setApproved("APPROVED");
		
		Product p2 = new Product();
		p2.setId(2L);
		p2.setApproved("REJECTED");
		
		Product p3 = new Product();
		p3.setId(3L);
		p3.setApproved("PENDING");
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		
		List<ProductDetails> allProducts = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByApproved(allProducts);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByRejected() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setApproved("APPROVED");
		
		Product p2 = new Product();
		p2.setId(2L);
		p2.setApproved("REJECTED");
		
		Product p3 = new Product();
		p3.setId(3L);
		p3.setApproved("PENDING");
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd2);
		
		List<ProductDetails> allProducts = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByRejected(allProducts);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByDrafted() {
		Product p1 = new Product();
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setDraft(false);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);
		pd2.setDraft(true);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);
		pd3.setDraft(false);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);
		pd4.setDraft(false);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd2);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> allProducts = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByDrafted(allProducts);
		
		assertEquals(expected, actual);
		
	}
	
	@Test
	void testGetByPublished() {
		Product p1 = new Product();
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setDraft(false);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);
		pd2.setDraft(true);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);
		pd3.setDraft(false);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);
		pd4.setDraft(false);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		expected.add(pd3);
		expected.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> allProducts = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByPublished(allProducts);
		
		assertEquals(expected, actual);
		
	}
	
	@Test
	void testGetByEnabled() {
		Product p1 = new Product();
		p1.setEnabled(true);
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setEnabled(false);
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setDraft(false);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);
		pd2.setDraft(true);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);
		pd3.setDraft(false);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);
		pd4.setDraft(false);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		expected.add(pd2);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> allProducts = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByEnabled(allProducts);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByDisabled() {
		Product p1 = new Product();
		p1.setEnabled(true);
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setEnabled(false);
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setDraft(false);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);
		pd2.setDraft(true);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);
		pd3.setDraft(false);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);
		pd4.setDraft(false);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd3);
		expected.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> allProducts = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByDisabled(allProducts);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testByKeywordNormal() {
		Product p1 = new Product();
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setName("One");
		pd1.setProduct(p1);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setName("Two");
		pd2.setProduct(p1);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setName("Three");
		pd3.setProduct(p2);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setName("Four");
		pd4.setProduct(p2);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd3);
		
		List<ProductDetails> allProducts = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByKeyword(allProducts, "three");
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testByKeywordSpacing() {
		Product p1 = new Product();
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setName("One lovely cat");
		pd1.setProduct(p1);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setName("Two lovely cats");
		pd2.setProduct(p1);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setName("Three bad dogs");
		pd3.setProduct(p2);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setName("Four bad dogs");
		pd4.setProduct(p2);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		expected.add(pd2);
		
		List<ProductDetails> allProducts = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByKeyword(allProducts, "      lOveLy        cAt      ");
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testByKeywordEmpty() {
		Product p1 = new Product();
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setName("One");
		pd1.setProduct(p1);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setName("Two");
		pd2.setProduct(p1);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setName("Three");
		pd3.setProduct(p2);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setName("Four");
		pd4.setProduct(p2);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		expected.add(pd2);
		expected.add(pd3);
		expected.add(pd4);
		
		List<ProductDetails> allProducts = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByKeyword(allProducts, "  ");
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByCategoryNormal() {
		
		Category c1 = new Category();
		c1.setId(1);
		c1.setName("Cat1");
		
		Category c2 = new Category();
		c2.setId(2);
		c2.setName("Cat2");
		
		Category c3 = new Category();
		c3.setId(3);
		c3.setName("Cat3");
		
		Product p1 = new Product();
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setCategory(c3);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setCategory(c3);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setCategory(c1);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setCategory(c2);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		expected.add(pd2);
		
		List<ProductDetails> allOfIt = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByCategory(allOfIt, 3);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByCategoryZero() {
		
		Category c1 = new Category();
		c1.setId(1);
		c1.setName("Cat1");
		
		Category c2 = new Category();
		c2.setId(2);
		c2.setName("Cat2");
		
		Category c3 = new Category();
		c3.setId(3);
		c3.setName("Cat3");
		
		Product p1 = new Product();
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setCategory(c3);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setCategory(c3);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setCategory(c1);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setCategory(c2);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		expected.add(pd2);
		expected.add(pd3);
		expected.add(pd4);
		
		List<ProductDetails> allOfIt = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByCategory(allOfIt, 0);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByPriceRangeNormal() {
		Product p1 = new Product();
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setPrice(10000);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setPrice(1000);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setPrice(8000);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setPrice(6000);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd2);
		expected.add(pd4);
		
		List<ProductDetails> allOfIt = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getByPriceRange(allOfIt, 0, 7000);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetByPriceRangeMinGreater() {
		Product p1 = new Product();
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setPrice(10000);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setPrice(1000);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setPrice(8000);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setPrice(6000);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd2);
		expected.add(pd4);
		
		List<ProductDetails> allOfIt = pdsi.getAll();
		
		assertThrows(NumberException.class, () -> {
			List<ProductDetails> actual = pdsi.getByPriceRange(allOfIt, 10000, 7000);
		});
	}
	
	@Test
	void testGetByPriceRangeNegative() {
		Product p1 = new Product();
		p1.setId(1L);
		
		Product p2 = new Product();
		p2.setId(2L);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setPrice(10000);
		
		ProductDetails pd2 = new ProductDetails();
		pd2.setPrice(1000);
		
		ProductDetails pd3 = new ProductDetails();
		pd3.setPrice(8000);
		
		ProductDetails pd4 = new ProductDetails();
		pd4.setPrice(6000);
		
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);
		
		when(productDetailsRepo.findAll()).thenReturn(allPd);
		
		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd2);
		expected.add(pd4);
		
		List<ProductDetails> allOfIt = pdsi.getAll();
		
		assertThrows(NumberException.class, () -> {
			List<ProductDetails> actual = pdsi.getByPriceRange(allOfIt, -10, 7000);
		});
	}
	
//	@Test
//	void testGetProductForSearching() {
//
//		String version = "1.0.0";
//		String version1 = "1.0.1";
//		String version2 = "1.0.2";
//		String version3 = "1.0.3";
//
//		Product product = new Product();
//		product.setActiveVersion(version);
//		product.setId(1L);
//
//		Product product1 = new Product();
//		product1.setId(2L);
//		product1.setActiveVersion(version1);
//
//		Product product2 = new Product();
//		product2.setId(3L);
//		product2.setActiveVersion(version2);
//
//		Product product3 = new Product();
//		product3.setId(4L);
//		product3.setActiveVersion(version3);
//
//		Category cat1 = new Category();
//		cat1.setId(1);
//		cat1.setName("Cat1");
//
//		Category cat2 = new Category();
//		cat2.setId(2);
//		cat2.setName("Cat2");
//
//		Category cat3 = new Category();
//		cat3.setId(3);
//		cat3.setName("Cat3");
//
//		Category cat4 = new Category();
//		cat4.setId(4);
//		cat4.setName("Cat4");
//
//		ProductDetails pd1 = new ProductDetails();
//		pd1.setProduct(product);
//		pd1.setVersion(version);
//		pd1.setCategory(cat1);
//		pd1.setPrice(100);
//		pd1.setName("abcd");
//
//		ProductDetails pd2 = new ProductDetails();
//		pd2.setProduct(product1);
//		pd2.setVersion(version1);
//		pd2.setCategory(cat2);
//		pd2.setPrice(1000);
//		pd2.setName("abcd");
//
//		ProductDetails pd3 = new ProductDetails();
//		pd3.setProduct(product2);
//		pd3.setVersion(version2);
//		pd3.setCategory(cat3);
//		pd3.setPrice(10000);
//		pd3.setName("abcd");
//
//		ProductDetails pd4 = new ProductDetails();
//		pd4.setProduct(product3);
//		pd4.setVersion(version3);
//		pd4.setCategory(cat4);
//		pd4.setPrice(100000);
//		pd4.setName("abcd");
//
//		List<ProductDetails> allProduct = new ArrayList<>();
//		allProduct.add(pd1);
//		allProduct.add(pd2);
//		allProduct.add(pd3);
//		allProduct.add(pd4);
//		
//		List<ProductDetails> expected = new ArrayList<>();
//		expected.add(pd2);
//		expected.add(pd3);
//		expected.add(pd4);
//
//		when(productDetailsRepo.findAll()).thenReturn(allProduct);
//		
//		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product.getId(), version)).thenReturn(pd1);
//		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product1.getId(), version1)).thenReturn(pd2);
//		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product2.getId(), version2)).thenReturn(pd3);
//		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product3.getId(), version3)).thenReturn(pd4);
//		
//		List<ProductDetails> result = pdsi.getProductForSearching("a", 0, 200, 1000000);
//		assertThat(result).isEqualTo(expected);
//	}
//	
//	@Test
//	void testGetAllPendingProducts() {
//		String version = "1.0.0";
//		String version1 = "1.0.1";
//		String version2 = "1.0.2";
//		String version3 = "1.0.3";
//
//		Product product = new Product();
//		product.setActiveVersion(version);
//		product.setId(1L);
//		product.setApproved("PENDING");
//
//		Product product1 = new Product();
//		product1.setId(2L);
//		product1.setActiveVersion(version1);
//		product.setApproved("APPROVED");
//
//		Product product2 = new Product();
//		product2.setId(3L);
//		product2.setActiveVersion(version2);
//		product.setApproved("REJECTED");
//
//		Product product3 = new Product();
//		product3.setId(4L);
//		product3.setActiveVersion(version3);
//		product.setApproved("PENDING");
//
//		Category cat1 = new Category();
//		cat1.setId(1);
//		cat1.setName("Cat1");
//
//		Category cat2 = new Category();
//		cat2.setId(2);
//		cat2.setName("Cat2");
//
//		Category cat3 = new Category();
//		cat3.setId(3);
//		cat3.setName("Cat3");
//
//		Category cat4 = new Category();
//		cat4.setId(4);
//		cat4.setName("Cat4");
//
//		ProductDetails pd1 = new ProductDetails();
//		pd1.setProduct(product);
//		pd1.setVersion(version);
//		pd1.setCategory(cat1);
//		pd1.setPrice(100);
//		pd1.setName("abcd");
//
//		ProductDetails pd2 = new ProductDetails();
//		pd2.setProduct(product1);
//		pd2.setVersion(version1);
//		pd2.setCategory(cat2);
//		pd2.setPrice(1000);
//		pd2.setName("abcd");
//
//		ProductDetails pd3 = new ProductDetails();
//		pd3.setProduct(product2);
//		pd3.setVersion(version2);
//		pd3.setCategory(cat3);
//		pd3.setPrice(10000);
//		pd3.setName("abcd");
//
//		ProductDetails pd4 = new ProductDetails();
//		pd4.setProduct(product3);
//		pd4.setVersion(version3);
//		pd4.setCategory(cat4);
//		pd4.setPrice(100000);
//		pd4.setName("abcd");
//
//		List<ProductDetails> allProduct = new ArrayList<>();
//		allProduct.add(pd1);
//		allProduct.add(pd2);
//		allProduct.add(pd3);
//		allProduct.add(pd4);
//		
//		when(productDetailsRepo.findAll()).thenReturn(allProduct);
//		
//		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product.getId(), version)).thenReturn(pd1);
//		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product1.getId(), version1)).thenReturn(pd2);
//		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product2.getId(), version2)).thenReturn(pd3);
//		when(productDetailsRepo.findByProductIdAndProductVersionKeyVersion(product3.getId(), version3)).thenReturn(pd4);
//		
//		List<ProductDetails> allPendingProducts = pdsi.getAllPendingProducts();
//		assertEquals(2, allPendingProducts.size());
//	}
}
