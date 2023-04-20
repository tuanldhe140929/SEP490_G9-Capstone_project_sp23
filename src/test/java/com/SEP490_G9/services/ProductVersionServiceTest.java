package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
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
import com.SEP490_G9.entities.ProductDetails.Status;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.exception.NumberException;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.serviceImpls.ProductDetailsServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class ProductVersionServiceTest {

	@Mock
	ProductDetailsRepository productDetailsRepo;

	@Mock
	ProductRepository productRepo;

	@InjectMocks
	ProductDetailsServiceImpl pdsi;

	@Test
	void test1PDS1() {
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
	void test2PDS1() {
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

		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		expected.add(pd2);
		expected.add(pd3);
		expected.add(pd4);

		List<ProductDetails> allOfIt = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getAll();

		assertEquals(expected, actual);

	}

	@Test
	void test3PDS1() {
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
	void test4PDS1() {
		Product p1 = new Product();
		p1.setId(1L);

		Product p2 = new Product();
		p2.setId(2L);

		Product p3 = new Product();
		p3.setId(3L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setApproved(Status.APPROVED);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setApproved(Status.REJECTED);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setApproved(Status.PENDING);

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
	void test5PDS1() {
		Product p1 = new Product();
		p1.setId(1L);

		Product p2 = new Product();
		p2.setId(2L);

		Product p3 = new Product();
		p3.setId(3L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setApproved(Status.APPROVED);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setApproved(Status.REJECTED);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setApproved(Status.PENDING);

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
	void test6PDS1() {
		Product p1 = new Product();
		p1.setId(1L);

		Product p2 = new Product();
		p2.setId(2L);

		Product p3 = new Product();
		p3.setId(3L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setApproved(Status.APPROVED);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setApproved(Status.REJECTED);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setApproved(Status.PENDING);

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
	void test7PDS1() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setDraft(true);

		Product p2 = new Product();
		p2.setId(2L);
		p2.setDraft(false);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
		List<ProductDetails> actual = pdsi.getByDrafted(allProducts);

		assertEquals(expected, actual);

	}

	@Test
	void test8PDS1() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setDraft(true);

		Product p2 = new Product();
		p2.setId(2L);
		p2.setDraft(false);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
		List<ProductDetails> actual = pdsi.getByPublished(allProducts);

		assertEquals(expected, actual);

	}

	@Test
	void test9PDS1() {
		Product p1 = new Product();
		p1.setEnabled(true);
		p1.setId(1L);

		Product p2 = new Product();
		p2.setEnabled(false);
		p2.setId(2L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
	void test10PDS1() {
		Product p1 = new Product();
		p1.setEnabled(true);
		p1.setId(1L);

		Product p2 = new Product();
		p2.setEnabled(false);
		p2.setId(2L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
	void test1PDS2() {
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
	void test2PDS2() {
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
	void test3PDS2() {
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
	void test4PDS2() {

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
	void test5PDS2() {

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
	void test6PDS2() {
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
			List<ProductDetails> actual = pdsi.getByPriceRange(allOfIt, 0, 7000);
		});

	}

	@Test
	void test7PDS2() {
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
	void test8PDS2() {
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

	@Test
	void test9PDS2() {

		Category c1 = new Category();
		c1.setId(1);
		c1.setName("Cat1");

		Category c2 = new Category();
		c2.setId(2);
		c2.setName("Cat2");

		Category c3 = new Category();
		c3.setId(3);
		c3.setName("Cat3");

		Tag t1 = new Tag();
		t1.setId(1);
		t1.setName("Tag1");

		Tag t2 = new Tag();
		t2.setId(2);
		t2.setName("Tag2");

		Tag t3 = new Tag();
		t3.setId(3);
		t3.setName("Tag3");

		Product p1 = new Product();
		p1.setId(1L);
		p1.setEnabled(true);
		p1.setActiveVersion("1.0.0");
		ArrayList<ProductDetails> pds1 = new ArrayList<>();
		p1.setProductDetails(pds1);
		
		
		Product p2 = new Product();
		p2.setId(2L);
		p2.setEnabled(true);
		//vua sua cho nay
		p2.setActiveVersion("1.0.0");
		ArrayList<ProductDetails> pds2 = new ArrayList<>();
		//vua sua cho nay
		p2.setProductDetails(pds2);
		
		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		//vua sua cho nay
		pd1.setVersion("1.0.0");
		pd1.setName("RPG asset1");
		pd1.setApproved(Status.APPROVED);
		pd1.setCategory(c1);
		List<Tag> tagList1 = new ArrayList<>();
		tagList1.add(t1);
		pd1.setTags(tagList1);
		pd1.setPrice(30);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);
		pd2.setVersion("1.0.1");
		pd2.setName("RPG asset2");
		pd2.setApproved(Status.PENDING);
		pd2.setCategory(c3);
		List<Tag> tagList2 = new ArrayList<>();
		tagList2.add(t1);
		tagList2.add(t3);
		pd2.setTags(tagList2);
		pd2.setPrice(60);
		
		//vua sua cho nay, tuong tu voi pd3, 4
		pds1.add(pd1);
		pds1.add(pd2);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);
		pd3.setVersion("1.0.0");
		pd3.setName("3d asset1");
		pd3.setApproved(Status.APPROVED);
		pd3.setCategory(c2);
		List<Tag> tagList3 = new ArrayList<>();
		tagList3.add(t2);
		tagList3.add(t3);
		pd3.setTags(tagList3);
		pd3.setPrice(40);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);
		pd4.setVersion("1.0.1");
		pd4.setName("3d asset2");
		pd4.setApproved(Status.PENDING);
		pd4.setCategory(c3);
		List<Tag> tagList4 = new ArrayList<>();
		tagList4.add(t1);
		tagList4.add(t3);
		pd4.setTags(tagList4);
		pd4.setPrice(50);

		pds2.add(pd3);
		pds2.add(pd4);
		List<ProductDetails> allPd = new ArrayList<>();
		allPd.add(pd1);
		allPd.add(pd2);
		allPd.add(pd3);
		allPd.add(pd4);

		when(productDetailsRepo.findAll()).thenReturn(allPd);
		List<ProductDetails> allOfIt = pdsi.getAll();

		List<Integer> tagIdList = new ArrayList<>();

		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		expected.add(pd3);

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("asset", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void test10PDS2() {
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
	void test1PDS3() {
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

		List<ProductDetails> expected = new ArrayList<>();
		expected.add(pd1);
		expected.add(pd2);
		expected.add(pd3);
		expected.add(pd4);

		List<ProductDetails> allOfIt = pdsi.getAll();
		List<ProductDetails> actual = pdsi.getAll();

		assertEquals(expected, actual);

	}

	@Test
	void test2PDS3() {
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
	void test3PDS3() {
		Product p1 = new Product();
		p1.setId(1L);

		Product p2 = new Product();
		p2.setId(2L);

		Product p3 = new Product();
		p3.setId(3L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setApproved(Status.APPROVED);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setApproved(Status.REJECTED);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setApproved(Status.PENDING);

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
	void test4PDS3() {
		Product p1 = new Product();
		p1.setId(1L);

		Product p2 = new Product();
		p2.setId(2L);

		Product p3 = new Product();
		p3.setId(3L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setApproved(Status.APPROVED);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setApproved(Status.REJECTED);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setApproved(Status.PENDING);

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
	void test5PDS3() {
		Product p1 = new Product();
		p1.setId(1L);

		Product p2 = new Product();
		p2.setId(2L);

		Product p3 = new Product();
		p3.setId(3L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setApproved(Status.APPROVED);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setApproved(Status.REJECTED);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setApproved(Status.PENDING);

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
	void test6PDS3() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setDraft(true);

		Product p2 = new Product();
		p2.setId(2L);
		p2.setDraft(false);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
		List<ProductDetails> actual = pdsi.getByDrafted(allProducts);

		assertEquals(expected, actual);

	}

	@Test
	void test7PDS3() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setDraft(true);

		Product p2 = new Product();
		p2.setId(2L);
		p2.setDraft(false);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
		List<ProductDetails> actual = pdsi.getByPublished(allProducts);

		assertEquals(expected, actual);

	}
	
	@Test
	void test8PDS3() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setDraft(true);

		Product p2 = new Product();
		p2.setId(2L);
		p2.setDraft(false);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
		List<ProductDetails> actual = pdsi.getByPublished(allProducts);

		assertEquals(expected, actual);

	}

	@Test
	void test9PDS3() {
		Product p1 = new Product();
		p1.setEnabled(true);
		p1.setId(1L);

		Product p2 = new Product();
		p2.setEnabled(false);
		p2.setId(2L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
	void test1PDS4() {
		Product p1 = new Product();
		p1.setEnabled(true);
		p1.setId(1L);

		Product p2 = new Product();
		p2.setEnabled(false);
		p2.setId(2L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
	void test2PDS4() {
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
	void test3PDS4() {
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
	void test4PDS4() {
		Product p1 = new Product();
		p1.setId(1L);

		Product p2 = new Product();
		p2.setId(2L);

		Product p3 = new Product();
		p3.setId(3L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setApproved(Status.APPROVED);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setApproved(Status.REJECTED);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setApproved(Status.PENDING);

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
	void test5PDS4() {
		Product p1 = new Product();
		p1.setId(1L);

		Product p2 = new Product();
		p2.setId(2L);

		Product p3 = new Product();
		p3.setId(3L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);
		pd1.setApproved(Status.APPROVED);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p2);
		pd2.setApproved(Status.REJECTED);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p3);
		pd3.setApproved(Status.PENDING);

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
	void test6PDS4() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setDraft(true);

		Product p2 = new Product();
		p2.setId(2L);
		p2.setDraft(false);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
		List<ProductDetails> actual = pdsi.getByDrafted(allProducts);

		assertEquals(expected, actual);

	}

	@Test
	void test7PDS4() {
		Product p1 = new Product();
		p1.setId(1L);
		p1.setDraft(true);

		Product p2 = new Product();
		p2.setId(2L);
		p2.setDraft(false);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
		List<ProductDetails> actual = pdsi.getByPublished(allProducts);

		assertEquals(expected, actual);

	}

	@Test
	void test8PDS4() {
		Product p1 = new Product();
		p1.setEnabled(true);
		p1.setId(1L);

		Product p2 = new Product();
		p2.setEnabled(false);
		p2.setId(2L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
	void test9PDS4() {
		Product p1 = new Product();
		p1.setEnabled(true);
		p1.setId(1L);

		Product p2 = new Product();
		p2.setEnabled(false);
		p2.setId(2L);

		ProductDetails pd1 = new ProductDetails();
		pd1.setProduct(p1);

		ProductDetails pd2 = new ProductDetails();
		pd2.setProduct(p1);

		ProductDetails pd3 = new ProductDetails();
		pd3.setProduct(p2);

		ProductDetails pd4 = new ProductDetails();
		pd4.setProduct(p2);

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
	void test10PDS4() {
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
	void test1PDS5() {
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
	void test2PDS5() {
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
	void test3PDS5() {

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
	void test4PDS5() {

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
	void test5PDS5() {
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
			List<ProductDetails> actual = pdsi.getByPriceRange(allOfIt, 0, 7000);
		});

	}
}
