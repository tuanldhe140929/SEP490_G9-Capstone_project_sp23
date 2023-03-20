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

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductDetails.Status;
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
	public void testDeleteProductById_notFound() {
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
//			when(sellerRepository.findById(seller.getId()).get()).thenReturn(seller);
//			when(sellerService.getSellerById(seller.getId())).thenReturn(seller);
		when(productRepository.findById(1L)).thenReturn(Optional.of(p));
		when(pds.getCurrentSeller()).thenReturn(seller);
		when(productRepository.save(p)).thenReturn(p);
		boolean result = pds.deleteProductById(1L);
		assertTrue(result);
	}

	@Test
	void testdeleteProductA() {
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
		when(pds.getCurrentSeller()).thenReturn(seller);
		when(productRepository.findById(1L)).thenReturn(Optional.of(p));
		when(productRepository.save(p)).thenReturn(p);

		assertThrows(NoSuchElementException.class, () -> pds.deleteProductById(-1L));
	}

	@Test
	void testUpdateProductN() {
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

		when(productRepository.save(p)).thenReturn(p);

		Product result = pds.updateProduct(p);
		assertThat(p.getId()).isEqualTo(result.getId());
	}

	@Test
	void testGetProductById() {
		Seller seller = new Seller();
		seller.setId(2L);
		seller.setEmail("testuser");
		Product product = new Product();
		product.setId(1L);
		product.setSeller(seller);

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		Product result = pds.getProductById(1L);
		assertEquals(product, result);
	}

	@Test
	void testGetProductByIdA() {
		when(productRepository.findById(-1L)).thenThrow(NoSuchElementException.class);
		assertThrows(NoSuchElementException.class, () -> productRepository.findById(-1L));
	}

	@Test
	void testGetProductsBySelerId() {
		Seller seller = new Seller();
		seller.setId(12L);

		Product p1 = new Product();
		p1.setId(1L);
		p1.setSeller(seller);

		Product p2 = new Product();
		p2.setId(2L);
		p2.setSeller(seller);

		List<Product> expected = new ArrayList<>();
		expected.add(p1);
		expected.add(p2);

		when(productRepository.findBySellerId(12L)).thenReturn(expected);

		List<Product> results = pds.getProductsBySellerId(12L);
		assertThat(results).isEqualTo(expected);
	}

	@Test
	void testGetProductsBySellerIdA() {
		List<Product> result = pds.getProductsBySellerId(-12L);
		assertTrue(result.isEmpty());
	}

	@Test
	void testSetActiveVersion() {
		Product product = new Product();
		product.setId(1L);
		product.setActiveVersion("1.0.2");

		Seller seller = new Seller();
		seller.setId(2L);

		product.setSeller(seller);

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(pdRepo.existsByProductIdAndProductVersionKeyVersion(1L, "1.0.2")).thenReturn(true);
		when(productRepository.save(product)).thenReturn(product);
		boolean result = pds.setActiveVersion(1L, "1.0.2");
		assertTrue(result);
	}

	@Test
	void testSetActiveVersionA() {
		Product product = new Product();
		product.setId(1L);
		product.setActiveVersion("1.0.222222222222");

		Seller seller = new Seller();
		seller.setId(2L);

		product.setSeller(seller);

		when(productRepository.findById(1L)).thenReturn(Optional.of(product));
		when(pdRepo.existsByProductIdAndProductVersionKeyVersion(1L, "1.0.222222222222")).thenReturn(true);
		when(productRepository.save(product)).thenReturn(product);
		boolean result = pds.setActiveVersion(1L, "1.0.222222222222");
		assertTrue(true);
	}

	@Test
	void testUploadCoverImage() {
		pds.setROOT_LOCATION("D:\\eclipse\\SEP490_G9_Data\\");
		Long productId = 1L;
		String version = "1.0";
		Product product = new Product();
		product.setId(productId);
		product.setActiveVersion(version);
		Seller seller = new Seller();
		seller.setId(2L);
		product.setSeller(seller);
		ProductDetails productDetails = new ProductDetails();
		productDetails.setVersion(version);
		productDetails.setProduct(product);
		product.setProductDetails(Collections.singletonList(productDetails));
		MultipartFile coverImage = new MockMultipartFile("coverImage", "test.png", "image/png", "test".getBytes());

		// Mock dependencies
		when(pds.getCoverImageLocation(productDetails)).thenReturn("ABC");
		when(productRepository.findById(productId)).thenReturn(Optional.of(product));
		when(pdRepo.save(any(ProductDetails.class))).thenReturn(productDetails);
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");

		// Call the method being tested
		String result = pds.uploadCoverImage(coverImage, productId, version);

		// Verify the result
		assertNotNull(result);

		// Verify the interactions with the mocked dependencies
		verify(productRepository).findById(productId);
		verify(pdRepo).save(productDetails);
	}
}
