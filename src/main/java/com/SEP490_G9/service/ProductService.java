package com.SEP490_G9.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.entities.License;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Seller;

public interface ProductService {
	Product createProduct(Product product);

	Product updateProduct(Product product);

	boolean deleteProductById(Long id);

	Product getProductById(Long productId);

	List<Product> getProductsBySellerId(Long sellerId);

	boolean setActiveVersion(Long productId, String version);

	String uploadCoverImage(MultipartFile coverImage, Long productId, String version);

	String updateProductApprovalStatus(long productId, boolean status);

	List<License> getAllLicense();
	
	List<Product> getAllProductsByReportStatus(String reportStatus);

	Product createNewProduct();
	
	boolean isProductDisabled(long productId);

	License getLicenceByProductId(Long productId);

	boolean applyLicense(Long productId, int licenseId);
}
