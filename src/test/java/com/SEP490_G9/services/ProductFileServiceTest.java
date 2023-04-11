package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.common.Md5Hash;
import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.dto.ProductFileDTO;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductDetails.Status;
import com.SEP490_G9.entities.ProductFile;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.ProductFileRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.service.CartService;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.VirusTotalService;
import com.SEP490_G9.service.serviceImpls.ProductFileServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@RunWith(PowerMockRunner.class)
@Import(TestConfig.class)
@SpringBootTest
class ProductFileServiceTest {

	@Mock
	ProductFileRepository pfr;

	@InjectMocks
	ProductFileServiceImpl pfs;

	@Mock
	ProductDetailsService productDetailsService;

	@Mock
	FileIOService fileIOService;

	@Mock
	TransactionRepository transactionRepo;

	@Mock
	ProductRepository productRepo;
	
	@Mock 
	VirusTotalService virusTotalService;
	
	@Mock
	CartService cartService;
	
	@Mock
	Md5Hash md5Hash;

	@Test
	void testPFS21() {
		Product product = new Product();
		product.setId(1L);
		ProductDetails pd = new ProductDetails();
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		pd.setProductVersionKey(new ProductVersionKey(2L, "1.0.0"));
		pd.setApproved(Status.NEW);
		ProductFile file = new ProductFile();
		file.setId(1L);
		pd.getFiles().add(file);
		file.setProductDetails(pd);
		when(productDetailsService.getByProductIdAndVersion(1L, "1.0.0")).thenReturn(pd);
		when(pfr.existsById(1L)).thenReturn(true);
		when(pfr.findById(1L)).thenReturn(Optional.of(file));
		ProductFile result = pfs.deleteById(1L);
		assertNotNull(result);
	}

	@Test
	void testPFS22() {
		Product product = new Product();
		product.setId(1L);
		ProductDetails pd = new ProductDetails();
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		pd.setProductVersionKey(new ProductVersionKey(2L, "1.0.0"));
		pd.setApproved(Status.NEW);
		ProductFile file = new ProductFile();
		file.setId(1L);
		pd.getFiles().add(file);
		file.setProductDetails(pd);
		when(productDetailsService.getByProductIdAndVersion(1L, "1.0.0")).thenReturn(pd);
		when(pfr.existsById(2L)).thenReturn(false);
		when(pfr.findById(1L)).thenReturn(Optional.of(file));
		assertThrows(ResourceNotFoundException.class, () -> {
			pfs.deleteById(2L);
		});

	}
	
	@Test
	void testPFS23() {
		Product product = new Product();
		product.setId(1L);
		ProductDetails pd = new ProductDetails();
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		pd.setProductVersionKey(new ProductVersionKey(2L, "1.0.0"));
		pd.setApproved(Status.NEW);
		ProductFile file = new ProductFile();
		file.setId(1L);
		pd.getFiles().add(file);
		file.setProductDetails(pd);
		when(productDetailsService.getByProductIdAndVersion(1L, "1.0.0")).thenReturn(pd);
		when(pfr.existsById(-1L)).thenReturn(false);
		when(pfr.findById(1L)).thenReturn(Optional.of(file));
		assertThrows(ResourceNotFoundException.class, () -> {
			pfs.deleteById(-1L);
		});

	}
	

	@Test
	void testPFS24() {
		Product product = new Product();
		product.setId(1L);
		ProductDetails pd = new ProductDetails();
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		pd.setProductVersionKey(new ProductVersionKey(2L, "1.0.0"));
		pd.setApproved(Status.PENDING);
		ProductFile file = new ProductFile();
		file.setId(1L);
		pd.getFiles().add(file);
		file.setProductDetails(pd);
		when(productDetailsService.getByProductIdAndVersion(1L, "1.0.0")).thenReturn(pd);
		when(pfr.existsById(1L)).thenReturn(true);
		when(pfr.findById(1L)).thenReturn(Optional.of(file));

		assertThrows(IllegalArgumentException.class, () -> {
			pfs.deleteById(1L);
		});
	}
	
	@Test
	void testPFS25() {
		Product product = new Product();
		product.setId(1L);
		ProductDetails pd = new ProductDetails();
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		pd.setProductVersionKey(new ProductVersionKey(2L, "1.0.0"));
		pd.setApproved(Status.APPROVED);
		ProductFile file = new ProductFile();
		file.setId(1L);
		pd.getFiles().add(file);
		file.setProductDetails(pd);
		when(productDetailsService.getByProductIdAndVersion(1L, "1.0.0")).thenReturn(pd);
		when(pfr.existsById(1L)).thenReturn(true);
		when(pfr.findById(1L)).thenReturn(Optional.of(file));

		assertThrows(IllegalArgumentException.class, () -> {
			pfs.deleteById(1L);
		});
	}
	@Test
	void testPFS26() {
		Product product = new Product();
		product.setId(1L);
		ProductDetails pd = new ProductDetails();
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		pd.setProductVersionKey(new ProductVersionKey(2L, "1.0.0"));
		pd.setApproved(Status.REJECTED);
		ProductFile file = new ProductFile();
		file.setId(1L);
		pd.getFiles().add(file);
		file.setProductDetails(pd);
		when(productDetailsService.getByProductIdAndVersion(1L, "1.0.0")).thenReturn(pd);
		when(pfr.existsById(1L)).thenReturn(true);
		when(pfr.findById(1L)).thenReturn(Optional.of(file));

		assertThrows(IllegalArgumentException.class, () -> {
			pfs.deleteById(1L);
		});
	}
	@Test
	void testPFS11() throws IOException, InterruptedException {
		pfs.setROOT_LOCATION("C:\\Users\\ADMN\\Desktop\\Test Data\\");
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		Long productId = 1L;
		String version = "1.0.0";
		MultipartFile coverImage = new MockMultipartFile("c", "test.png", "image/png", "test".getBytes());

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);

	
		ProductFile file1 = new ProductFile();
		file1.setId(1L);
		file1.setName(coverImage.getName());
		file1.setProductDetails(productDetails);
		Mockito.when(productDetailsService.getByProductIdAndVersion(productId, version)).thenReturn(productDetails);

//		PowerMockito.mockStatic(VirusTotalAPI.class);
		try {
			when(virusTotalService.scanFile(any(File.class))).thenReturn(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		when(pfr.existsByNameAndProductDetails(file1.getName(), productDetails)).thenReturn(false);
		when(pfr.save(any(ProductFile.class))).thenReturn(file1);
		
		// Call the service method
		ProductFileDTO result = pfs.uploadFile(productId, version, coverImage);

		// Verify the result
		assertThat(result.getId()).isEqualTo(file1.getId());
	}
	@Test
	void testPFS12() throws IOException, InterruptedException {
		pfs.setROOT_LOCATION("C:\\Users\\ADMN\\Desktop\\Test Data\\");
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		Long productId = 1L;
		String version = "1.0.0";
		MultipartFile coverImage = new MockMultipartFile("c", "test.png", "image/png", "test".getBytes());

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);

	
		ProductFile file1 = new ProductFile();
		file1.setId(1L);
		file1.setName(coverImage.getName());
		file1.setProductDetails(productDetails);
		Mockito.when(productDetailsService.getByProductIdAndVersion(2L, "0")).thenReturn(null);

//		PowerMockito.mockStatic(VirusTotalAPI.class);
		try {
			when(virusTotalService.scanFile(any(File.class))).thenReturn(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		when(pfr.existsByNameAndProductDetails(file1.getName(), productDetails)).thenReturn(false);
		when(pfr.save(any(ProductFile.class))).thenReturn(file1);
		
		// Call the service method
		
		assertThrows(IllegalArgumentException.class, () -> {
			pfs.uploadFile(productId, version, coverImage);
		});
	}
	@Test
	void testPFS13() throws IOException, InterruptedException {
		pfs.setROOT_LOCATION("C:\\Users\\ADMN\\Desktop\\Test Data\\");
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		Long productId = 1L;
		String version = "1.0.0";
		byte[] bytes = new byte[1024 * 1024 * 500];
		MultipartFile coverImage =
				new MockMultipartFile("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
						"1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111", "image/png", bytes);
		

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);

	
		ProductFile file1 = new ProductFile();
		file1.setId(1L);
		file1.setName(coverImage.getName());
		file1.setProductDetails(productDetails);
		Mockito.when(productDetailsService.getByProductIdAndVersion(productId, version)).thenReturn(productDetails);

//		PowerMockito.mockStatic(VirusTotalAPI.class);
		try {
			when(virusTotalService.scanFile(any(File.class))).thenReturn(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		when(pfr.existsByNameAndProductDetails(file1.getName(), productDetails)).thenReturn(false);
		when(pfr.save(any(ProductFile.class))).thenReturn(file1);
		
		// Call the service method
		ProductFileDTO result = pfs.uploadFile(productId, version, coverImage);

		// Verify the result
		assertThat(result.getId()).isEqualTo(file1.getId());
	}
	@Test
	void testPFS14() throws IOException, InterruptedException {
		pfs.setROOT_LOCATION("C:\\Users\\ADMN\\Desktop\\Test Data\\");
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		Long productId = 1L;
		String version = "1.0.0";
		byte[] bytes = new byte[1024 * 1024 * 500];
		MultipartFile coverImage =
				new MockMultipartFile("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
						"11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111", "image/png", bytes);
		

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);

	
		ProductFile file1 = new ProductFile();
		file1.setId(1L);
		file1.setName(coverImage.getName());
		file1.setProductDetails(productDetails);
		Mockito.when(productDetailsService.getByProductIdAndVersion(productId, version)).thenReturn(productDetails);

//		PowerMockito.mockStatic(VirusTotalAPI.class);
		try {
			when(virusTotalService.scanFile(any(File.class))).thenReturn(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		when(pfr.existsByNameAndProductDetails(file1.getName(), productDetails)).thenReturn(false);
		when(pfr.save(any(ProductFile.class))).thenReturn(file1);
		
		// Call the service method
	
		assertThrows(FileUploadException.class, () -> {
			pfs.uploadFile(productId, version, coverImage);
		});
	}
	@Test
	void testPFS15() throws IOException, InterruptedException {
		pfs.setROOT_LOCATION("C:\\Users\\ADMN\\Desktop\\Test Data\\");
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		Long productId = 1L;
		String version = "1.0.0";
		//501Mb
		byte[] bytes = new byte[1024 * 1024 * 501];
		MultipartFile coverImage =
				new MockMultipartFile("normalName",
						"normalName", "image/png", bytes);
		

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);

	
		ProductFile file1 = new ProductFile();
		file1.setId(1L);
		file1.setName(coverImage.getName());
		file1.setProductDetails(productDetails);
		Mockito.when(productDetailsService.getByProductIdAndVersion(productId, version)).thenReturn(productDetails);

//		PowerMockito.mockStatic(VirusTotalAPI.class);
		try {
			when(virusTotalService.scanFile(any(File.class))).thenReturn(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		when(pfr.existsByNameAndProductDetails(file1.getName(), productDetails)).thenReturn(false);
		when(pfr.save(any(ProductFile.class))).thenReturn(file1);
		
		// Call the service method
	
		assertThrows(FileUploadException.class, () -> {
			pfs.uploadFile(productId, version, coverImage);
		});
	}
	@Test
	void testPFS16() throws IOException, InterruptedException {
		pfs.setROOT_LOCATION("C:\\Users\\ADMN\\Desktop\\Test Data\\");
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		Long productId = 1L;
		String version = "1.0.0";
		//0
		byte[] bytes = new byte[1024 * 1024 * 0];
		MultipartFile coverImage =
				new MockMultipartFile("normalName",
						"normalName", "image/png", bytes);
		

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);

	
		ProductFile file1 = new ProductFile();
		file1.setId(1L);
		file1.setName(coverImage.getName());
		file1.setProductDetails(productDetails);
		Mockito.when(productDetailsService.getByProductIdAndVersion(productId, version)).thenReturn(productDetails);

//		PowerMockito.mockStatic(VirusTotalAPI.class);
		try {
			when(virusTotalService.scanFile(any(File.class))).thenReturn(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		when(pfr.existsByNameAndProductDetails(file1.getName(), productDetails)).thenReturn(false);
		when(pfr.save(any(ProductFile.class))).thenReturn(file1);
		
		// Call the service method
	
		assertThrows(FileUploadException.class, () -> {
			pfs.uploadFile(productId, version, coverImage);
		});
	}
	@Test
	void testPFS17() throws IOException, InterruptedException {
		pfs.setROOT_LOCATION("C:\\Users\\ADMN\\Desktop\\Test Data\\");
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		Long productId = 1L;
		String version = "1.0.0";
		//0
		byte[] bytes = new byte[1];
		MultipartFile coverImage =
				new MockMultipartFile("normalName",
						"normalName", "image/png", bytes);
		

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);
		productDetails.getFiles().add(new ProductFile());
		productDetails.getFiles().add(new ProductFile());
		productDetails.getFiles().add(new ProductFile());
		productDetails.getFiles().add(new ProductFile());
		productDetails.getFiles().add(new ProductFile());
		productDetails.getFiles().add(new ProductFile());
		productDetails.getFiles().add(new ProductFile());
		productDetails.getFiles().add(new ProductFile());
		productDetails.getFiles().add(new ProductFile());
		productDetails.getFiles().add(new ProductFile());
	
		ProductFile file1 = new ProductFile();
		file1.setId(1L);
		file1.setName(coverImage.getName());
		file1.setProductDetails(productDetails);
		Mockito.when(productDetailsService.getByProductIdAndVersion(productId, version)).thenReturn(productDetails);

//		PowerMockito.mockStatic(VirusTotalAPI.class);
		try {
			when(virusTotalService.scanFile(any(File.class))).thenReturn(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		when(pfr.existsByNameAndProductDetails(file1.getName(), productDetails)).thenReturn(false);
		when(pfr.save(any(ProductFile.class))).thenReturn(file1);
		
		// Call the service method
	
		assertThrows(FileUploadException.class, () -> {
			pfs.uploadFile(productId, version, coverImage);
		});
	}
	@Test
	void testPFS18() throws IOException, InterruptedException {
		pfs.setROOT_LOCATION("C:\\Users\\ADMN\\Desktop\\Test Data\\");
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		Long productId = 1L;
		String version = "1.0.0";
		MultipartFile coverImage = new MockMultipartFile("c", "test.png", "image/png", "test".getBytes());

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);

	
		ProductFile file1 = new ProductFile();
		file1.setId(1L);
		file1.setName(coverImage.getName());
		file1.setProductDetails(productDetails);
		Mockito.when(productDetailsService.getByProductIdAndVersion(productId, version)).thenReturn(productDetails);

//		PowerMockito.mockStatic(VirusTotalAPI.class);
		try {
			when(virusTotalService.scanFile(any(File.class))).thenReturn(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		when(pfr.existsByNameAndProductDetails(file1.getName(), productDetails)).thenReturn(false);
		when(pfr.save(any(ProductFile.class))).thenReturn(file1);
		
		// Call the service method
		ProductFileDTO result = pfs.uploadFile(productId, version, coverImage);

		// Verify the result
		assertThat(result.getId()).isEqualTo(-1L);
	}
	@Test
	void testPFS19() throws IOException, InterruptedException {
		pfs.setROOT_LOCATION("C:\\Users\\ADMN\\Desktop\\Test Data\\");
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		Long productId = 1L;
		String version = "1.0.0";
		byte[] bytes = new byte[1024 * 1024 * 100];
		MultipartFile coverImage = new MockMultipartFile("111111111111111111111111111111111111111111111111111111111111111111111111111111111",
				"111111111111111111111111111111111111111111111111111111111111111111111111111111111", "image/png", bytes);

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);

	
		ProductFile file1 = new ProductFile();
		file1.setId(1L);
		file1.setName(coverImage.getName());
		file1.setProductDetails(productDetails);
		Mockito.when(productDetailsService.getByProductIdAndVersion(productId, version)).thenReturn(productDetails);

//		PowerMockito.mockStatic(VirusTotalAPI.class);
		try {
			when(virusTotalService.scanFile(any(File.class))).thenReturn(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		when(pfr.existsByNameAndProductDetails(file1.getName(), productDetails)).thenReturn(false);
		when(pfr.save(any(ProductFile.class))).thenReturn(file1);
		
		// Call the service method
		ProductFileDTO result = pfs.uploadFile(productId, version, coverImage);

		// Verify the result
		assertThat(result.getId()).isEqualTo(file1.getId());
	}
	
	@Test
	void testPFS31() {
		when(cartService.isUserPurchasedProduct(1L, 1L)).thenReturn(true);
		when(md5Hash.generateToken(1L, 1L)).thenReturn("downloadToken");
		String result = pfs.generateDownloadToken(1L, 1L);
		
		assertThat(result.length()).isGreaterThan(0);
	}
	
	@Test
	void testPFS32() {
		when(cartService.isUserPurchasedProduct(1L, 1L)).thenReturn(false);
		when(md5Hash.generateToken(1L, 1L)).thenReturn("downloadToken");

		
		assertThrows(IllegalAccessError.class, () -> {
			pfs.generateDownloadToken(1L, 1L);
			
		});
	}
	
	@Test
	void testPFS33() {
		when(cartService.isUserPurchasedProduct(-1L, -1L)).thenReturn(false);
		when(md5Hash.generateToken(-1L, -1L)).thenReturn("downloadToken");

		
		assertThrows(IllegalAccessError.class, () -> {
			pfs.generateDownloadToken(-1L, -1L);
			
		});
	}

	@Test
	@WithMockUser(username = "testuser", roles = { "USER" })
	void testPFS41() {
		Product product = new Product();
		product.setActiveVersion("1.0.0");
		
		ProductDetails pd = new ProductDetails();
		pd.setVersion("1.0.0");
		pd.getFiles().add(new ProductFile());
		pd.setName("ABC");
		product.getProductDetails().add(pd);
		when(productRepo.findById(1L)).thenReturn(Optional.of(product));
		when(md5Hash.validateToken(1L, 1L, "downloadToken")).thenReturn(true);
		
		ByteArrayResource result = pfs.downloadFile(1L, 1L, "downloadToken");
		assertNotNull(result);
	}
	
	@Test
	@WithMockUser(username = "testuser", roles = { "STAFF" })
	void testPFS45() {
		Product product = new Product();
		product.setActiveVersion("1.0.0");
		
		ProductDetails pd = new ProductDetails();
		pd.setVersion("1.0.0");
		pd.getFiles().add(new ProductFile());
		pd.setName("ABC");
		product.getProductDetails().add(pd);
		
		when(productRepo.findById(1L)).thenReturn(Optional.of(product));
		when(md5Hash.validateToken(1L, 1L, "")).thenReturn(false);
		ByteArrayResource result = pfs.downloadFile(1L, 1L, "");
		assertNotNull(result);
	}
	
	@Test
	@WithMockUser(username = "testuser", roles = { "USER" })
	void testPFS43() {
		Product product = new Product();
		product.setActiveVersion("1.0.0");
		
		ProductDetails pd = new ProductDetails();
		pd.setVersion("1.0.0");
		pd.getFiles().add(new ProductFile());
		pd.setName("ABC");
		product.getProductDetails().add(pd);
		
		when(productRepo.findById(1L)).thenReturn(Optional.of(product));
		when(md5Hash.validateToken(1L, 1L, "")).thenReturn(false);
		
		assertThrows(IllegalAccessError.class, () -> {
			pfs.downloadFile(1L, 1L,"");
			
		});
	}
	
	@Test
	@WithMockUser(username = "testuser", roles = { "USER" })
	void testPFS44() {
		Product product = new Product();
		product.setActiveVersion("1.0.0");
		
		ProductDetails pd = new ProductDetails();
		pd.setVersion("1.0.0");
		pd.getFiles().add(new ProductFile());
		pd.setName("ABC");
		product.getProductDetails().add(pd);
		
		when(productRepo.findById(-1L)).thenThrow(NoSuchElementException.class);
		when(md5Hash.validateToken(-1L, -1L, "")).thenReturn(false);
		
		assertThrows(IllegalAccessError.class, () -> {
			pfs.downloadFile(-1L, -1L,"");
			
		});
	}
	
	@Test
	void testPFS42() {
		Product product = new Product();
		product.setActiveVersion("1.0.0");
		
		ProductDetails pd = new ProductDetails();
		pd.setVersion("1.0.0");
		pd.getFiles().add(new ProductFile());
		pd.setName("ABC");
		product.getProductDetails().add(pd);
		
		when(productRepo.findById(1L)).thenReturn(Optional.of(product));
		when(md5Hash.validateToken(1L, 1L, "downloadToken")).thenReturn(false);
		
		assertThrows(IllegalAccessError.class, () -> {
			pfs.downloadFile(1L, 1L,"downloadToken");
			
		});
	}
}
