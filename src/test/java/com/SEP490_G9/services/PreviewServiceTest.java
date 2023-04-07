package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.dto.PreviewDTO;
import com.SEP490_G9.entities.Preview;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.PreviewRepository;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.PreviewService;
import com.SEP490_G9.service.serviceImpls.PreviewServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class PreviewServiceTest {

	@Mock
	PreviewRepository previewRepo;

	@InjectMocks
	PreviewServiceImpl ps;
	
	@Mock
	FileIOService fileIOService;

	@Mock
	ProductDetailsRepository pdRepo;
	
	@Test
	void testUploadPreviewPicture() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L,"1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("picture");
		pd.getPreviews().add(preview);
		MultipartFile coverImage = new MockMultipartFile("coverImage", "test.png", "image/png", "test".getBytes());
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);
		
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		List<PreviewDTO> result = ps.uploadPreviewPicture(1L, "1.0.0",coverImage);
		System.out.println(result.get(0).getId());
		// Verify the result
		assertThat(result.get(0).getId()).isEqualTo(1L);
	}
	
	@Test
	void testUploadPreviewPictureA() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L,"1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("picture");
		pd.getPreviews().add(preview);
		MultipartFile coverImage = new MockMultipartFile("coverImage", "test.png", "asdasd", "test".getBytes());
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);
		
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		assertThrows(FileUploadException.class, ()->ps.uploadPreviewPicture(1L, "1.0.0",coverImage));
	}
	
	@Test
	void testUploadPreviewVideo() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L,"1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("video");
		pd.getPreviews().add(preview);
		MultipartFile coverImage = new MockMultipartFile("video", "test.png", "video/mp4", "test".getBytes());
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);
		when(previewRepo.save(any(Preview.class))).thenReturn(preview);
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		Preview result = ps.uploadPreviewVideo(1L, "1.0.0",coverImage);
		// Verify the result
		System.out.println(result.getId());
		assertThat(result.getId()).isEqualTo(1L);
	}
	
	
	@Test
	void testUploadPreviewVideoA() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L,"1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("video");
		pd.getPreviews().add(preview);
		MultipartFile coverImage = new MockMultipartFile("video", "test.png", "img/mp4", "test".getBytes());
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);
		when(previewRepo.save(any(Preview.class))).thenReturn(preview);
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		assertThrows(FileUploadException.class, ()->ps.uploadPreviewVideo(1L, "1.0.0",coverImage));
	}
}
