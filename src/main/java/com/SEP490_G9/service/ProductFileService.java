package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entity.ProductFile;

public interface ProductFileService {
	public ProductFile createProductFile(ProductFile productFile);

	public List<ProductFile> createProductFiles(List<ProductFile> productFiles);

	public ProductFile getById(Long id);

	public boolean deleteById(Long fileId);
}
