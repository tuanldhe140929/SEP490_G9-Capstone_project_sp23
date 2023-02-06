package com.SEP490_G9.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.models.DTOS.ProductDTO;
import com.SEP490_G9.models.Entities.Preview;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.Tag;
import com.SEP490_G9.models.Entities.Type;
import com.SEP490_G9.models.Entities.User;

public interface ManageProductService {
	
	public User getCurrentUserInfo();
	
	public Product addProduct(Product product);
	
	public ProductDTO updateProduct(Product product,String instruction) throws IOException;
	
	public boolean deleteProduct(Long id);
	
	public List<Product> getProductsByUser();

	public String uploadCoverImage(MultipartFile coverImage, Long productId) throws IOException;

	public Product createNewProduct();

	public ProductDTO getProductDTOByIdAndUser(Long productId) throws IOException;

	public List<Type> getTypeList();

	public List<Tag> getTagList();

	public ProductDTO uploadProductFile(Long productId, MultipartFile productFile) throws IOException;
	
	public ProductDTO deleteProductFile(Long productId, Long fileId) throws IOException;

	public Preview uploadPreviewVideo(MultipartFile previewVideo, Long productId);

	public File serveCoverImage(Long productId);

	public ResponseEntity<ResourceRegion> servePreviewVideo(Long previewId, String rangeHeader) throws IOException;

	public boolean deleteProductPreviewVideo(Long productId);

	public List<Preview> uploadPreviewPicture(MultipartFile previewPicture, Long productId);

	public File servePreviewImage(Long previewId);

	public List<Preview> deletePreviewPicture(Long previewId);

	
}
