package com.SEP490_G9.service.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entity.ProductFile;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.ProductFileRepository;
import com.SEP490_G9.service.ProductFileService;

@Service
public class ProductFileServiceImpl implements ProductFileService {

	@Autowired
	ProductFileRepository productFileRepo;

	@Override
	public ProductFile createProductFile(ProductFile productFile) {
		if (productFileRepo.existsByNameAndProductDetails(productFile.getName(), productFile.getProductDetails())) {
			throw new DuplicateFieldException("file name", productFile.getName());
		}

		return productFileRepo.save(productFile);
	}

	@Override
	public List<ProductFile> createProductFiles(List<ProductFile> productFiles) {
		List<ProductFile> ret = null;
		if (productFiles.size() >= 0) {
			for (ProductFile productFile : productFiles) {
				if (productFileRepo.existsByNameAndProductDetails(productFile.getName(),
						productFile.getProductDetails())) {
					throw new DuplicateFieldException("file name", productFile.getName());
				}
			}
			ret = productFileRepo.saveAll(productFiles);
		}
		return ret;
	}

	@Override
	public ProductFile getById(Long id) {
		ProductFile ret = productFileRepo.findById(id).get();
		if (ret == null) {
			throw new ResourceNotFoundException("product file", "id", id);
		}
		return ret;
	}

	@Override
	public boolean deleteById(Long fileId) {
		if (!productFileRepo.existsById(fileId)) {
			throw new ResourceNotFoundException("product file", "id", fileId);
		}
		productFileRepo.deleteById(fileId);
		return true;
	}

}
