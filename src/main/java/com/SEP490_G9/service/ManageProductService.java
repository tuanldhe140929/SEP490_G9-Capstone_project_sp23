package com.SEP490_G9.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.dto.ProductFileDTO;
import com.SEP490_G9.entity.Category;
import com.SEP490_G9.entity.Preview;
import com.SEP490_G9.entity.Product;
import com.SEP490_G9.entity.Tag;


public interface ManageProductService {
	public List<ProductDetailsDTO> getProductDetailsDTOsBySeller();
	
	public Product createNewProduct();
	
	public Product createNewProductDetails(Long productId, String version);
	
	public ProductDetailsDTO updateProductDetails(ProductDetailsDTO productDetailsDTO,String instruction) throws IOException;
	
	public List<Category> getCategoryList();

	public List<Tag> getTagList();
	
	public boolean deleteProduct(Long id);

	public ProductFileDTO uploadProductFile(Long productId, MultipartFile productFile, String version) throws IOException;
	
	public ProductFileDTO deleteProductFile(Long productId, Long fileId) throws IOException;
	
	public String uploadCoverImage(MultipartFile coverImage, Long productId, String version) throws IOException;

	public List<Preview> uploadPreviewPicture(MultipartFile previewPicture, Long productId, String version);

	public Preview uploadPreviewVideo(MultipartFile previewVideo, Long productId, String version);
	
	public boolean deleteProductPreviewVideo(Long previewId);

	public List<Preview> deletePreviewPicture(Long previewId);

	public File serveCoverImage(Long productId);
	
	public ResponseEntity<ResourceRegion> servePreviewVideo(Long previewId, String rangeHeader) throws IOException;

	public File servePreviewImage(Long previewId);

	public ProductDetailsDTO getLatestVersionProductDTOByIdAndSeller(Long productId) throws IOException;
	
	
}
