package com.SEP490_G9.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.models.DTOS.ProductDTO;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.Tag;
import com.SEP490_G9.models.Entities.Type;
import com.SEP490_G9.models.Entities.User;

public interface ManageProductService {
	
	public User getCurrentUserInfo();
	
	public Product addProduct(Product product);
	
	public ProductDTO updateProduct(Product product,String instruction) throws IOException;
	
	public Product deleteProduct(Long id);
	
	public List<Product> getProductsByUser();

	public Product uploadCoverImage(MultipartFile coverImage, Long productId) throws IOException;

	public File getCoverImage(Long productId);

	public Product createNewProduct();

	public ProductDTO getProductDTOByIdAndUser(Long productId) throws IOException;

	public List<Type> getTypeList();

	public List<Tag> getTagList();

	public Product uploadProductFile(Long productId, MultipartFile productFile) throws IOException;
	
	public Product deleteProductFile(Long productId, Long fileId) throws IOException;

}
