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
	void testPS2_1() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("picture");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[1024 * 1024 * 10];
		MultipartFile coverImage = new MockMultipartFile("coverImage", "test.jpg", "image/jpeg", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		List<PreviewDTO> result = ps.uploadPreviewPicture(1L, "1.0.0", coverImage);
		// Verify the result
		assertThat(result.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void testPS2_2() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("picture");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[1024 * 1024 * 10];
		MultipartFile coverImage = new MockMultipartFile("coverImage", "test.png", "image/png", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		List<PreviewDTO> result = ps.uploadPreviewPicture(1L, "1.0.0", coverImage);
		// Verify the result
		assertThat(result.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void testtestPS2_3() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("picture");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[1024 * 1024 * 200];
		MultipartFile coverImage = new MockMultipartFile("coverImage", "test.svg", "image/svg+xml", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		List<PreviewDTO> result = ps.uploadPreviewPicture(1L, "1.0.0", coverImage);
		// Verify the result
		assertThat(result.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void testPS2_4() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("picture");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[1];
		MultipartFile coverImage = new MockMultipartFile("coverImage", "test.jpg", "image/jpeg", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		List<PreviewDTO> result = ps.uploadPreviewPicture(1L, "1.0.0", coverImage);
		// Verify the result
		assertThat(result.get(0).getId()).isEqualTo(1L);
	}

	@Test
	void testPS2_5() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("picture");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[0];
		MultipartFile coverImage = new MockMultipartFile("coverImage", "test.jpg", "image/jpg", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");

		// Verify the result
		assertThrows(FileUploadException.class, () -> {
			ps.uploadPreviewPicture(1L, "1.0.0", coverImage);
		});
	}

	@Test
	void testPS2_6() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("picture");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[1024*1024*500];
		MultipartFile coverImage = new MockMultipartFile("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111", ""
				+ "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111", "image/jpg", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");

		// Verify the result
		assertThrows(FileUploadException.class, () -> {
			ps.uploadPreviewPicture(1L, "1.0.0", coverImage);
		});
	}
	
	@Test
	void testtestPS2_7() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("picture");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[1024*1024*500];
		MultipartFile coverImage = new MockMultipartFile("video","video.mov", "video/quicktime", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);

		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");

		// Verify the result
		assertThrows(FileUploadException.class, () -> {
			ps.uploadPreviewPicture(1L, "1.0.0", coverImage);
		});
	}
	@Test
	void testPS1_1() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("video");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[1024*1024*10];
		MultipartFile coverImage = new MockMultipartFile("video", "test.png", "video/mp4", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);
		when(previewRepo.save(any(Preview.class))).thenReturn(preview);
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		Preview result = ps.uploadPreviewVideo(1L, "1.0.0", coverImage);
		// Verify the result
		System.out.println(result.getId());
		assertThat(result.getId()).isEqualTo(1L);
	}

	@Test
	void testPS1_2() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("video");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[1];
		MultipartFile coverImage = new MockMultipartFile("video", "test.png", "video/x-matroska", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);
		when(previewRepo.save(any(Preview.class))).thenReturn(preview);
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		Preview result = ps.uploadPreviewVideo(1L, "1.0.0", coverImage);
		// Verify the result
		System.out.println(result.getId());
		assertThat(result.getId()).isEqualTo(1L);
	}
	
	@Test
	void testPS1_3() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("video");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[1024 * 1024 * 200];
		MultipartFile coverImage = new MockMultipartFile("video", "test.png", "video/quicktime", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);
		when(previewRepo.save(any(Preview.class))).thenReturn(preview);
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		Preview result = ps.uploadPreviewVideo(1L, "1.0.0", coverImage);
		// Verify the result
		System.out.println(result.getId());
		assertThat(result.getId()).isEqualTo(1L);
	}
	
	@Test
	void testPS1_4() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("video");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[1];
		MultipartFile coverImage = new MockMultipartFile("video", "test.png", "video/x-matroska", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);
		when(previewRepo.save(any(Preview.class))).thenReturn(preview);
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
		Preview result = ps.uploadPreviewVideo(1L, "1.0.0", coverImage);
		// Verify the result
		System.out.println(result.getId());
		assertThat(result.getId()).isEqualTo(1L);
	}
	
	@Test
	void testPS1_5() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("video");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[0];
		MultipartFile coverImage = new MockMultipartFile("video", "test.png", "video/x-matroska", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);
		when(previewRepo.save(any(Preview.class))).thenReturn(preview);
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
	
		assertThrows(FileUploadException.class, () -> {
			ps.uploadPreviewVideo(1L, "1.0.0", coverImage);
		});
	}
	
	@Test
	void testPS1_6() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("video");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[10];
		MultipartFile coverImage = new MockMultipartFile("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
				"11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111", "video/x-matroska", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);
		when(previewRepo.save(any(Preview.class))).thenReturn(preview);
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
	
		assertThrows(FileUploadException.class, () -> {
			ps.uploadPreviewVideo(1L, "1.0.0", coverImage);
		});
	}
	
	@Test
	void testPS1_7() {
		Product product = new Product();
		product.setId(1L);
		Seller seller = new Seller();
		seller.setId(1L);
		product.setSeller(seller);
		ProductDetails pd = new ProductDetails();
		pd.setProductVersionKey(new ProductVersionKey(1L, "1.0.0"));
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		Preview preview = new Preview();
		preview.setId(1L);
		preview.setProductDetails(pd);
		preview.setType("video");
		pd.getPreviews().add(preview);
		byte[] bytes = new byte[10];
		MultipartFile coverImage = new MockMultipartFile("name",
				"name.svg", "image/svg+xml", bytes);
		when(pdRepo.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(pd);
		when(previewRepo.save(any(Preview.class))).thenReturn(preview);
		when(fileIOService.storeV2(any(MultipartFile.class), anyString())).thenReturn("path/to/image.png");
	
		assertThrows(FileUploadException.class, () -> {
			ps.uploadPreviewVideo(1L, "1.0.0", coverImage);
		});
	}
}
