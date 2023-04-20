package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.common.Constant;
import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductDetails.Status;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.SellerService;
import com.SEP490_G9.service.serviceImpls.ProductServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(MockitoJUnitRunner.class)
@Import(TestConfig.class)
@TestPropertySource(properties = { "root.location=D://" })
class ProductServiceTest {

	@Mock
	ProductRepository productRepository;

	@Mock
	ProductDetailsRepository pdRepo;

	@Mock
	SellerService sellerService;

	@Mock
	SellerRepository sellerRepository;

	@InjectMocks
	ProductServiceImpl pds;

	@Mock
	FileIOService fileIOService;

//	@Test
//	@WithMockUser(username = "testuser", roles = { "SELLER,USER" })
//	void testCreateNewProduct() {
//		ProductDetails productDetails = new ProductDetails();
//		Product product = new Product();
//		product.setId(1L);
//		Seller seller = new Seller();
//		seller.setId(1L);
//		seller.setEmail("testuser");
//
//		product.setSeller(seller);
//		productDetails.setProduct(product);
//		productDetails.setVersion("1.0.0");
//		productDetails.setCreatedDate(new Date());
//		productDetails.setLastModified(new Date());
//		productDetails.setDraft(true);
//		when(pdRepo.save(productDetails)).thenReturn(productDetails);
//		when(productRepository.save(product)).thenReturn(product);
//		Product result = pds.createProduct(product);
//
//		assertThat(result.getId()).isEqualTo(product.getId());
//	}

	@Test
	@WithMockUser(username = "testuser", roles = { "SELLER" })
	public void testPRDS2_1() {
		// Set up mock data
		Seller seller = new Seller();
		seller.setId(2L);
		seller.setEmail("testuser");
		List<Role> sellerRoles = new ArrayList<>();
		sellerRoles.add(new Role(Constant.SELLER_ROLE_ID, "ROLE_SELLER"));
		seller.setRoles(sellerRoles);
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(seller);
		Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContext securityContext = org.mockito.Mockito.mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Product p = new Product();
		p.setId(1L);
		p.setEnabled(false);
		p.setSeller((Seller) seller);
			when(sellerRepository.findById(seller.getId())).thenReturn(Optional.of(seller));
			when(sellerService.getSellerById(seller.getId())).thenReturn(seller);
		when(productRepository.findById(1L)).thenReturn(Optional.of(p));
		when(productRepository.save(p)).thenReturn(p);
		
		assertThrows(ResourceNotFoundException.class, () -> {
			pds.deleteProductById(1L);
		});
	}

	@Test
	void testPRDS2_2() {
		// Set up mock data
		Seller seller = new Seller();
		seller.setId(2L);
		seller.setEmail("testuser");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(seller);
		Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContext securityContext = org.mockito.Mockito.mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Product p = new Product();
		p.setId(1L);
		p.setEnabled(false);
		p.setSeller((Seller) seller);
//		when(sellerRepository.findById(seller.getId()).get()).thenReturn(seller);
//		when(sellerService.getSellerById(seller.getId())).thenReturn(seller);
		when(productRepository.findById(1L)).thenReturn(Optional.of(p));
		when(productRepository.save(p)).thenReturn(p);
		assertThrows(IllegalAccessError.class, () -> {
			pds.deleteProductById(1L);
		});
	}

	@Test
	void testPRDS2_3() {
		// Set up mock data
		Seller seller = new Seller();
		seller.setId(2L);
		seller.setEmail("testuser");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(seller);
		Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContext securityContext = org.mockito.Mockito.mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Product p = new Product();
		p.setId(1L);
		p.setEnabled(false);
		p.setSeller((Seller) seller);
//		when(sellerRepository.findById(seller.getId()).get()).thenReturn(seller);
//		when(sellerService.getSellerById(seller.getId())).thenReturn(seller);
		when(productRepository.save(p)).thenReturn(p);
		assertThrows(IllegalAccessError.class, () -> {
			pds.deleteProductById(1L);
		});
	}

	@Test
	@WithMockUser(username = "testuser", roles = { "SELLER" })
	void testPRDS3_1() {
		Product product = new Product();
		product.setId(1L);
		product.setActiveVersion("1.0.2");

		Seller seller = new Seller();
		seller.setId(2L);

		product.setSeller(seller);

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(pdRepo.existsByProductIdAndProductVersionKeyVersion(1L, "1.0.2")).thenReturn(true);
		when(productRepository.save(product)).thenReturn(product);
		
		assertThrows(ClassCastException.class, () -> {
			pds.setActiveVersion(1L, "1.0.2");
		});
	}

	@Test
	@WithMockUser(username = "testuser", roles = { "SELLER" })
	void testPRDS3_2() {
		Product product = new Product();
		product.setId(1L);
		product.setActiveVersion("1.0.222222222222");

		Seller seller = new Seller();
		seller.setId(2L);

		product.setSeller(seller);

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(pdRepo.existsByProductIdAndProductVersionKeyVersion(1L, "1.0.222222222222")).thenReturn(true);
		when(productRepository.save(product)).thenReturn(product);
		assertThrows(ClassCastException.class, () -> {
			pds.setActiveVersion(1L, "1.0.2");
		});
	}

	@Test
	@WithMockUser(username = "testuser", roles = { "SELLER" })
	void testPRDS1_1() {
		Product product = new Product();
		when(productRepository.save(product)).thenReturn(product);
		assertThrows(ClassCastException.class, () -> {
			pds.createProduct(product);
		});
	}

//	
	@Test
	@WithMockUser(username = "testuser", roles = { "USER" })
	void testPRDS1_2() {
		Product product = new Product();
		when(productRepository.save(product)).thenReturn(product);
		assertThrows(ClassCastException.class, () -> {
			pds.createProduct(product);
		});
	}

}
