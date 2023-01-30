package com.SEP490_G9.services.serviceImpls;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FileUtils;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.helpers.StorageProperties;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.FileStorageService;
import com.SEP490_G9.services.ManageProductService;

@Service
public class ManageProductServiceImpl implements ManageProductService {

	@Autowired
	StorageProperties storageProperties;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	FileStorageService fileStorageService;

	@Override
	public User getCurrentUserInfo() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		return user;
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
	public Product updateProduct(Product product) {
		Product ret = null;
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		try {
			Product found = productRepository.findByUserAndId(user, product.getId());
			ret = productRepository.save(product);
		} catch (Exception e) {
			// throw new CustomException("Cap nhat san pham khong thanh cong");
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
	public Product newProduct() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Product newProduct = new Product(user);
		productRepository.save(newProduct);
		return newProduct;
	}

	@Override
	public Product uploadCoverImage(MultipartFile coverImage, Long productId) throws IOException {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Product product = productRepository.findByUserAndId(user, productId);
		String destination = user.getUsername() + "\\products\\" + productId + "\\coverImage\\";
		File folder = new File(storageProperties.getLocation()+destination);
		System.out.println(folder.mkdirs());
		fileStorageService.store(coverImage, destination);
		product.setCoverImage(destination + coverImage.getOriginalFilename());
		productRepository.save(product);
		return product;
	}

	@Override
	public File getCoverImage(Long productId) {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Product product = productRepository.findByUserAndId(user, productId);
		File file = null;
        file = new File(storageProperties.getLocation()+product.getCoverImage());
		return file;
	}
	
	@Override
	public Product createNewProduct() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Product product = new Product();
		product.setUser(user);
		
		Product createdProduct = productRepository.save(product);
		String destination = user.getUsername() + "\\products\\" + createdProduct.getId() + "\\coverImage\\";
		File folder = new File(storageProperties.getLocation()+destination);
		folder.mkdir();
		return product;
	}

}
