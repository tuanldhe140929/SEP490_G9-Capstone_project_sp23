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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.common.VirusTotalAPI;
import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.dto.ProductFileDTO;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductFile;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.repository.ProductFileRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.serviceImpls.ProductFileServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@RunWith(PowerMockRunner.class)
@Import(TestConfig.class)
@SpringBootTest
@PrepareForTest(VirusTotalAPI.class)
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

	@Test
	void testCreateProductFile() {
		ProductFile file = new ProductFile();
		file.setId(1L);
		when(pfr.save(any(ProductFile.class))).thenReturn(file);
		when(pfr.existsByNameAndProductDetails(anyString(), any(ProductDetails.class))).thenReturn(false);
		ProductFile result = pfs.createProductFile(file);
		assertThat(result.getId()).isEqualTo(1L);
	}

	@Test
	void testCreateProductFiles1() {
		ProductFile file1 = new ProductFile();
		file1.setId(1L);

		ProductFile file2 = new ProductFile();
		file2.setId(2L);

		List<ProductFile> files = new ArrayList<>();
		files.add(file2);
		files.add(file1);

		when(pfr.saveAll(files)).thenReturn(files);
		when(pfr.existsByNameAndProductDetails(anyString(), any(ProductDetails.class))).thenReturn(false);
		List<ProductFile> results = pfs.createProductFiles(files);
		assertThat(results.size()).isGreaterThan(0);
	}

	@Test
	void testGetById() {
		ProductFile file = new ProductFile();
		file.setId(2L);
		when(pfr.findById(anyLong())).thenReturn(Optional.of(file));
		ProductFile result = pfs.getById(2L);
		assertThat(result.getId()).isEqualTo(2L);
	}

	VirusTotalAPI virusTotalAPI = mock(VirusTotalAPI.class);

	@Test
	void testDeleteById() {
		Product product = new Product();
		product.setId(2L);
		ProductDetails pd = new ProductDetails();
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		pd.setProductVersionKey(new ProductVersionKey(2L, "1.0.0"));

		ProductFile file = new ProductFile();
		file.setId(2L);
		pd.getFiles().add(file);
		file.setProductDetails(pd);
		when(productDetailsService.getByProductIdAndVersion(2L, "1.0.0")).thenReturn(pd);
		when(pfr.existsById(2L)).thenReturn(true);
		when(pfr.findById(2L)).thenReturn(Optional.of(file));
		ProductFile result = pfs.deleteById(2L);
		assertNotNull(result);
	}

	@Test
	void testGetAllFileByIdAndVersion() {
		Product product = new Product();
		product.setId(2L);
		ProductDetails pd = new ProductDetails();
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		pd.setProductVersionKey(new ProductVersionKey(2L, "1.0.0"));

		ProductFile file1 = new ProductFile();
		file1.setId(1L);
		file1.setProductDetails(pd);

		ProductFile file2 = new ProductFile();
		file2.setId(1L);
		file2.setProductDetails(pd);

		List<ProductFile> files = new ArrayList<>();
		files.add(file1);
		files.add(file2);

		when(pfr.findByProductDetails(pd)).thenReturn(files);
		List<ProductFile> results = pfs.getAllFileByIdAndVersion();

	}

	@Test
	@PrepareForTest(VirusTotalAPI.class)
	void testUploadFile() throws IOException {
		pfs.setROOT_LOCATION("ABC");
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		Long productId = 1L;
		String version = "1.0";
		MultipartFile coverImage = new MockMultipartFile("coverImage", "test.png", "image/png", "test".getBytes());
		
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);

	
		ProductFile file1 = new ProductFile();
		file1.setId(1L);
		file1.setName("test1.txt");
		file1.setProductDetails(productDetails);
		Mockito.when(productDetailsService.getByProductIdAndVersion(productId, version)).thenReturn(productDetails);

//		PowerMockito.mockStatic(VirusTotalAPI.class);
		try {
			when(virusTotalAPI.scanFile(any(File.class))).thenReturn(true);
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

	void testDownloadFile() {

	}

	void testGenerateDownloadToken() {

	}
}
