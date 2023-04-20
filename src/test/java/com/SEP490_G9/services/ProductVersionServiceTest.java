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
	void PDS1test1() {

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
	void PDS1test2() {

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
		expected.add(pd3);

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("3d", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS1test3() {

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
		pd1.setName("samurai sword");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("sword", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS1test4() {

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
		pd1.setName("samurai sword");
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
		pd4.setName("ninja sword");
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
		List<ProductDetails> actual = pdsi.getProductForSearching("", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS1test5() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("     shIELD      ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS1test6() {

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
		pd1.setName("God of war shield");
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
		pd3.setName("Gears of war gun");
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
		pd4.setName("ninja sword");
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
		List<ProductDetails> actual = pdsi.getProductForSearching("        oF   WaR    ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS1test7() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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
		List<ProductDetails> actual = pdsi.getProductForSearching("            ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS1test8() {

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
	void PDS1test9() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("     shIELD      ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS1test10() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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
		
		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("    NUINU8989@$%$NJKA     SAJDASDNJASDN     &*&%^*&*", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS1test11() {

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
		pd1.setName("Súng phun lửa");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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
		
		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("Sung", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS2test1() {

		Category c1 = new Category();
		c1.setId(1);
		c1.setName("Script");

		Category c2 = new Category();
		c2.setId(2);
		c2.setName("Model");

		Category c3 = new Category();
		c3.setId(3);
		c3.setName("Soundtrack");

		Tag t1 = new Tag();
		t1.setId(1);
		t1.setName("Action");

		Tag t2 = new Tag();
		t2.setId(2);
		t2.setName("Sport");

		Tag t3 = new Tag();
		t3.setId(3);
		t3.setName("Horror");

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
		pd1.setName("Dragon wing");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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
		
		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("   wing ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS2test2() {

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
	void PDS2test3() {

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
		expected.add(pd3);

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("3d", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS2test4() {

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
		pd1.setName("samurai sword");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("sword", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS2test5() {

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
		pd1.setName("samurai sword");
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
		pd4.setName("ninja sword");
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
		List<ProductDetails> actual = pdsi.getProductForSearching("", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS2test6() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("     shIELD      ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS2test7() {

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
		pd1.setName("God of war shield");
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
		pd3.setName("Gears of war gun");
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
		pd4.setName("ninja sword");
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
		List<ProductDetails> actual = pdsi.getProductForSearching("        oF   WaR    ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS2test8() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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
		List<ProductDetails> actual = pdsi.getProductForSearching("            ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS2test9() {

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
	void PDS2Test10() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("     shIELD      ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS3test1() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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
		
		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("    NUINU8989@$%$NJKA     SAJDASDNJASDN     &*&%^*&*", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS3test2() {

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
	void PDS3test3() {

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
		expected.add(pd3);

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("3d", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS3test4() {

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
		pd1.setName("Bản đồ");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("Ban do", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS3test5() {

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
		pd1.setName("samurai sword");
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
		pd4.setName("ninja sword");
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
		List<ProductDetails> actual = pdsi.getProductForSearching("", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS3test6() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("     shIELD      ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS3test7() {

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
		pd1.setName("God of war shield");
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
		pd3.setName("Gears of war gun");
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
		pd4.setName("ninja sword");
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
		List<ProductDetails> actual = pdsi.getProductForSearching("        oF   WaR    ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS3test8() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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
		List<ProductDetails> actual = pdsi.getProductForSearching("            ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS3test9() {

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
	void PDS3test10() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("     shIELD      ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS4test1() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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
		
		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("    NUINU8989@$%$NJKA     SAJDASDNJASDN     &*&%^*&*", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS4test2() {

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
	void PDS4test3() {

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
		expected.add(pd3);

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("3d", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS4test4() {

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
		pd1.setName("samurai sword");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("sword", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS4test5() {

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
		pd1.setName("samurai sword");
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
		pd4.setName("ninja sword");
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
		List<ProductDetails> actual = pdsi.getProductForSearching("", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS4test6() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("     shIELD      ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS4test7() {

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
		pd1.setName("God of war shield");
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
		pd3.setName("Gears of war gun");
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
		pd4.setName("ninja sword");
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
		List<ProductDetails> actual = pdsi.getProductForSearching("        oF   WaR    ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS4test8() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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
		List<ProductDetails> actual = pdsi.getProductForSearching("            ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS4test9() {

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
	void PDS4test10() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("     shIELD      ", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS5test1() {

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
		pd1.setName("SHIELD symbol");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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
		
		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("    NUINU8989@$%$NJKA     SAJDASDNJASDN     &*&%^*&*", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS5test2() {

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
	void PDS5test3() {

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
		expected.add(pd3);

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("3d", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}
	
	@Test
	void PDS5test4() {

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
		pd1.setName("samurai sword");
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
		pd4.setName("ninja sword");
		pd4.setApproved(Status.APPROVED);
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

		List<Integer> actualTagList = new ArrayList<>();
		actualTagList.add(1);
		List<ProductDetails> actual = pdsi.getProductForSearching("sword", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

	@Test
	void PDS5test5() {

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
		pd1.setName("samurai sword");
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
		pd4.setName("ninja sword");
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
		List<ProductDetails> actual = pdsi.getProductForSearching("", 0, tagIdList, 0, 1000);
		assertEquals(expected, actual);
	}

}
