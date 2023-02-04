package com.SEP490_G9.services.serviceImpls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FileUtils;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.ProductFile;
import com.SEP490_G9.models.Entities.Tag;
import com.SEP490_G9.models.Entities.Type;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.exceptions.DuplicateFieldException;
import com.SEP490_G9.exceptions.ResourceNotFoundException;
import com.SEP490_G9.helpers.StorageProperties;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.repositories.ProductFileRepository;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.repositories.TagRepository;
import com.SEP490_G9.repositories.TypeRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.FileStorageService;
import com.SEP490_G9.services.ManageProductService;

import jakarta.transaction.Transactional;

@Service
public class ManageProductServiceImpl implements ManageProductService {
	final String PRODUCT_FOLDER_NAME = "products";
	final String PRODUCT_COVER_IMAGE_FOLDER_NAME = "coverImage";
	final String PRODUCT_FILES_FOLDER_NAME = "files";
	final String PRODUCT_PREVIEWS_FOLDER_NAME = "previews";
	final String PRODUCT_INSTRUCTION_FILE_NAME = "instruction.txt";

	
	@Autowired
	StorageProperties storageProperties;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	TypeRepository typeRepository;

	@Autowired
	TagRepository tagRepository;
	
	@Autowired 
	ProductFileRepository productFileRepository;

	@Override
	public User getCurrentUserInfo() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		return user;
	}

	@Override
	public List<Type> getTypeList() {
		List<Type> typeList = typeRepository.findAll();
		return typeList;
	}

	@Override
	public List<Tag> getTagList() {
		List<Tag> tagList = tagRepository.findAll();
		return tagList;
	}

	@Override
	public Product getProductByIdAndUser(Long productId) {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Product product = productRepository.findByUserAndId(user, productId);
		System.out.println(productId+" "+user.getId());
		if (product == null) {
			throw new ResourceNotFoundException("product id:" + productId, "user id", user.getId());
		}
		return product;
	}

	@Override
	public Product createNewProduct() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Product product = new Product();
		product.setUser(user);
		Product createdProduct = productRepository.save(product);
		String coverImageDestination = user.getUsername() + "\\products\\" + createdProduct.getId() + "\\coverImage\\";
		String filesDestination = user.getUsername() + "\\products\\" + createdProduct.getId() + "\\files\\";
		File folder = new File(storageProperties.getLocation() + coverImageDestination);
		folder.mkdir();
		folder = new File(storageProperties.getLocation() + filesDestination);
		folder.mkdir();
		return product;
	}

	@Override
	public Product addProduct(Product product) {
		Product ret = null;
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		product.setUser(user);
		try {
			ret = productRepository.save(product);
		} catch (Exception e) {
			// throw new CustomException("Them san pham khong thanh cong");
		}
		return ret;
	}

	@Override
	public Product updateProduct(Product product,String instruction) throws IOException {
		
		
		Product ret = null;
		try {

			Product found = getProductByIdAndUser(product.getId());
			String instructionFileDir = getProductFilesLocation(found)+PRODUCT_INSTRUCTION_FILE_NAME;

			FileOutputStream fos = new FileOutputStream(storageProperties.getLocation() + instructionFileDir);
			fos.write(instruction.getBytes());
			fos.flush();
			fos.close();
			
			found.setName(product.getName());
			found.setDetails(product.getDetails());
			found.setPrice(product.getPrice());
			found.setDescription(product.getDescription());
			found.setTags(product.getTags());
			found.setType(product.getType());
			found.setUrl(product.getUrl());
			found.setLastUpdate(new Date());
			found.setInstructionSource(instructionFileDir);
			ret = productRepository.save(found);
		} catch (Exception e) {
			System.out.println(e);
		}
		return ret;
	}

	@Override
	public Product deleteProduct(Long id) {
		Product ret = null;
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		try {
			Product found = productRepository.getReferenceById(id);
			ret = productRepository.findByUserAndId(user, id);
			productRepository.deleteById(id);
		} catch (Exception e) {
			// throw new CustomException("Xoa san pham khong thanh cong");
		}
		return ret;
	}

	@Override
	public List<Product> getProductsByUser() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		return productRepository.findByUser(user);
	}

	@Override
	public Product uploadCoverImage(MultipartFile coverImage, Long productId) throws IOException {
		Product product = getProductByIdAndUser(productId);
		String coverImageLocation = getCoverImageLocation(product);
		File coverImageDir = new File(storageProperties.getLocation() + coverImageLocation);
		coverImageDir.mkdirs();
		fileStorageService.store(coverImage, coverImageLocation);
		product.setCoverImage(coverImageLocation + coverImage.getOriginalFilename());
		productRepository.save(product);
		return product;
	}

	@Override
	public File getCoverImage(Long productId) {
		Product product = getProductByIdAndUser(productId);
		File file = new File(storageProperties.getLocation() + product.getCoverImage());
		return file;
	}

	@Transactional
	@Override
	public Product uploadProductFile(Long productId, MultipartFile productFile) throws IOException {
		Product product = getProductByIdAndUser(productId);
		String fileLocation = getProductFilesLocation(product);
		File fileDir = new File(storageProperties.getLocation() + fileLocation);
		fileDir.mkdirs();
		fileStorageService.store(productFile, fileLocation);
		ProductFile file = new ProductFile(productFile, fileLocation, product);
		
		if(productFileRepository.existsByName(file.getName())) {
			throw new DuplicateFieldException("file name",file.getName());
		}
		productFileRepository.save(file);
		product.getFiles().add(file);
		return product;
	}
	
	@Transactional
	@Override
	public Product deleteProductFile(Long productId, Long fileId) throws IOException {
		Product product = getProductByIdAndUser(productId);
		
		ProductFile productFile = productFileRepository.getReferenceById(fileId);
		product.getFiles().remove(productFile);
		productFileRepository.deleteById(fileId);
		File fileDir = new File(storageProperties.getLocation() + productFile.getSource());
		fileDir.delete();	
		return product;
	}

	private String getCoverImageLocation(Product product) {
		return getUserProductsDataLocation(product.getUser()) + product.getId() + "\\" + PRODUCT_COVER_IMAGE_FOLDER_NAME
				+ "\\";
	}

	private String getProductFilesLocation(Product product) {
		return getUserProductsDataLocation(product.getUser()) + product.getId() + "\\" + PRODUCT_FILES_FOLDER_NAME
				+ "\\";
	}

	private String getPreviewsLocation(Product product) {
		return getUserProductsDataLocation(product.getUser()) + product.getId() + "\\" + PRODUCT_PREVIEWS_FOLDER_NAME
				+ "\\";
	}

	private String getUserProductsDataLocation(User user) {
		return user.getUsername() + "\\" + PRODUCT_FOLDER_NAME + "\\";
	}
}
