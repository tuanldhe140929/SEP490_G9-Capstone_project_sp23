package com.SEP490_G9.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.dto.ProductDTO;
import com.SEP490_G9.dto.ProductDetailsDTO;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Category;
import com.SEP490_G9.entities.License;
import com.SEP490_G9.entities.Preview;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductFile;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.PreviewRepository;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.PreviewService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductFileService;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.SellerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

	final String PRODUCT_FOLDER_NAME = "products";
	final String PRODUCT_COVER_IMAGE_FOLDER_NAME = "coverImage";
	final String PRODUCT_FILES_FOLDER_NAME = "files";
	final String PRODUCT_PREVIEWS_FOLDER_NAME = "previews";
	final String PRODUCT_INSTRUCTION_FILE_NAME = "instruction.txt";

	final String FIRST_PRODUCT_VERSION = "1.0.0";


	@Autowired
	ProductService productService;

	@Autowired
	ProductDetailsRepository productDetailsRepo;

	@GetMapping("getLicense")
	public ResponseEntity<?> getAllLicense() {
		List<License> licenses = productService.getAllLicense();
		return ResponseEntity.ok(licenses);
	}

	// create new Product
	@PostMapping(value = "createNewProduct")
	public ResponseEntity<?> createNewProduct() {
		Product product = new Product();
		product.setEnabled(true);
		product.setDraft(true);
		product.setActiveVersion(FIRST_PRODUCT_VERSION);
		Product createdProduct = productService.createProduct(product);
		ProductDTO productDTO = new ProductDTO(createdProduct);
		return ResponseEntity.ok(productDTO);
	}

	// change active version of product
	@PostMapping(value = "activeVersion")
	public ResponseEntity<?> activeVersion(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "version") String version) {
		boolean ret = false;
		ret = productService.setActiveVersion(productId, version);
		return ResponseEntity.ok(ret);
	}

	// update product
	// delete
	@DeleteMapping(value = "deleteProduct/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable(name = "id") Long id) {
		boolean isDeleted = productService.deleteProductById(id);
		return ResponseEntity.ok(isDeleted);
	}

	// upload coverImage
	@PostMapping(value = "uploadCoverImage")
	public ResponseEntity<?> uploadCoverImage(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "coverImage") MultipartFile coverImage, @RequestParam(name = "version") String version)
			throws IOException {
		String src = "";

		src = productService.uploadCoverImage(coverImage, productId, version);

		return ResponseEntity.ok(src);
	}

	@GetMapping(value = "getProductDetails")
	public ResponseEntity<?> getProductDetails(@RequestParam(name = "sellerId", required = true) Long sellerId) {
		List<Product> p = new ArrayList<>();
		p = this.productService.getProductsBySellerId(sellerId);
		List<ProductDetails> pd = new ArrayList<>();
		for (int i = 0; i < p.size(); i++) {
			p.get(i).getActiveVersion();
			ProductDetails pde = null;
			pde = this.productDetailsRepo.findByProductIdAndProductVersionKeyVersion(p.get(i).getId(),
					p.get(i).getActiveVersion());
			pd.add(pde);
		}
		List<ProductDetailsDTO> ret = new ArrayList<>();
		for (int i = 0; i < pd.size(); i++) {
			ProductDetailsDTO pdto = new ProductDetailsDTO(pd.get(i));
			ret.add(pdto);
		}
		return ResponseEntity.ok(ret);
	}

	@GetMapping(value = "getProductsCountBySellerId")
	public ResponseEntity<?> getProductsCountBySellerId(
			@RequestParam(name = "sellerId", required = true) Long sellerId) {
		List<Product> sellerProducts = this.productService.getProductsBySellerId(sellerId);
		int count = sellerProducts.size();
		return ResponseEntity.ok(count);
	}
	
	@PostMapping(value = "updateProductApprovalStatus")
	public ResponseEntity<?> updateProductApprovalStatus(@RequestParam(name = "productId") long productId, @RequestParam(name = "status") boolean status){
		productService.updateProductApprovalStatus(productId, status);
		return ResponseEntity.ok(status);
	}

//	@GetMapping(value = "getProductsByKeyword/{keyword}")
//	public ResponseEntity<?> getProductsByKeyword(@PathVariable(name = "keyword") String keyword) {
//		List<ProductDetails> searchResult = this.productDetailsService.getByKeyword(keyword);
//		List<ProductDetailsDTO> searchResultDto = new ArrayList<>();
//		for (ProductDetails result : searchResult) {
//			searchResultDto.add(new ProductDetailsDTO(result));
//		}
//		return ResponseEntity.ok(searchResultDto);
//	}

}
