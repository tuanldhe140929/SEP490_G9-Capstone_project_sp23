package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.serviceImpls.SellerServiceImpl;
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class SellerServiceTest {

	@Autowired
	SellerRepository sellerRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	ProductRepository productRepo;
	
	@InjectMocks
	SellerServiceImpl sellerImpl;
	
	@Test
	void testGetSellerById() {
		Long expectedId = (long)2;
		Seller expected = new Seller();
		expected.setId(expectedId);
		
		when(sellerRepo.findById(expectedId).get()).thenReturn(expected);
		Seller result = sellerRepo.findById(expectedId).get();
		assertThat(result.getId()).isEqualTo(expectedId);
	}
	@Test
	void testgetSellerByAProduct() {
		Seller seller = new Seller();
		seller.setId((long)1);
		
		Product p1 = new Product();
		p1.setId((long)1);
		p1.setSeller(seller);
		
		Product p2 = new Product();
		p2.setId((long)2);
		p2.setSeller(seller);
		
		List<Product> expected = new ArrayList<>();
		expected.add(p1);
		expected.add(p2);
		
		when(productRepo.findById((long)1)).thenReturn(expected);
		Seller result = sellerImpl.getSellerByAProduct((long)1);
		
	}

}
