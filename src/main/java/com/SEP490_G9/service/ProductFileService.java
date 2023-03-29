package com.SEP490_G9.service;

import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.dto.ProductFileDTO;
import com.SEP490_G9.entities.ProductFile;

public interface ProductFileService {
	public ProductFile createProductFile(ProductFile productFile);

	public List<ProductFile> createProductFiles(List<ProductFile> productFiles);

	public ProductFile getById(Long id);

	public ProductFile deleteById(Long fileId);

	public List<ProductFile> getAllFileByIdAndVersion();

	public ProductFileDTO uploadFile(Long productId, String version, MultipartFile productFile);

	public ByteArrayResource downloadFile(Long userId, Long productId, String token);

	public String generateDownloadToken(Long userId, Long productId);
}
