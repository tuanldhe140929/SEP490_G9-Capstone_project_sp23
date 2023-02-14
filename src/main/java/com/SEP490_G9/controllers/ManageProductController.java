package com.SEP490_G9.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.models.DTOS.ProductDTO;
import com.SEP490_G9.models.Entities.Preview;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.Tag;
import com.SEP490_G9.models.Entities.Type;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.services.FileStorageService;
import com.SEP490_G9.services.ManageProductService;

@RequestMapping(value = "private/manageProduct")
@RestController
public class ManageProductController {

	@Autowired
	ManageProductService manageProductService;

	@Autowired
	FileStorageService fileStorageService;

	@GetMapping(value = "getCurrentUserInfo")
	public ResponseEntity<?> getCurrentUserInfo() {
		User user = manageProductService.getCurrentUserInfo();
		User user2 = new User();
		user2.setId(user.getId());
		user2.setEnabled(user.isEnabled());
		user2.setEmail(user.getEmail());
		user2.setPassword(null);
		user2.setUsername(user.getUsername());
		user2.setRefreshToken(user.getRefreshToken());
		return ResponseEntity.ok(user2);
	}

	@GetMapping(value = "getProductsByUser")
	public ResponseEntity<?> getProductsByUser() {
		List<Product> products = manageProductService.getProductsByUser();
		return ResponseEntity.ok(products);
	}

	@PostMapping(value = "addProduct")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		Product ret = manageProductService.addProduct(product);
		return ResponseEntity.ok(ret);
	}

	@DeleteMapping(value = "deleteProduct/{id}")
	public ResponseEntity<?> deleteProducts(@PathVariable(name = "id") Long id) {
		return ResponseEntity.ok(manageProductService.deleteProduct(id));
	}

	@PostMapping(value = "updateProduct")
	public ResponseEntity<?> updateProduct(@RequestBody Product product, @RequestParam(name="instruction") String instructionDetails) throws IOException {
		ProductDTO ret = manageProductService.updateProduct(product,instructionDetails);
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value="createNewProductRequest")
	public ResponseEntity<?> createNewProductRequest(){
		Product product = manageProductService.createNewProduct();
		return ResponseEntity.ok(product);
	}
	
	
	
	
	
	
	@PostMapping(value = "uploadCoverImage")
	public ResponseEntity<?> uploadCoverImage(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "coverImage") MultipartFile coverImage) throws IOException {
		String src = manageProductService.uploadCoverImage(coverImage, productId);
		return ResponseEntity.ok(src);
	}
	
	
	
	
	
	
	@PostMapping(value = "uploadPreviewVideo")
	public ResponseEntity<?> uploadPreviewVideo(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "previewVideo") MultipartFile previewVideo) throws IOException {
		Preview src = manageProductService.uploadPreviewVideo(previewVideo, productId);
		System.out.println(previewVideo.getInputStream().available());
		return ResponseEntity.ok(src);
	}
	
	@PostMapping(value="uploadPreviewPicture")
	public ResponseEntity<?> uploadPreviewPicture(@RequestParam(name="productId") Long productId,
			@RequestParam(name="previewPicture") MultipartFile previewPicture){
		List<Preview> previews = manageProductService.uploadPreviewPicture(previewPicture, productId);
		return ResponseEntity.ok(previews);
	}
	
	
	@GetMapping("getProductByIdAndUser")
	public ResponseEntity<?> getProductByIdAndUser(@RequestParam(name = "productId") Long productId) throws IOException{
		ProductDTO productDTO = null;
		productDTO = manageProductService.getProductDTOByIdAndUser(productId);
		return ResponseEntity.ok(productDTO);		
	}

	@GetMapping("getTypeList")
	public ResponseEntity<?> getTypeList(){
		List<Type> typeList = manageProductService.getTypeList();
		return ResponseEntity.ok(typeList);		
	}
	
	@GetMapping("getTagList")
	public ResponseEntity<?> getTagList(){
		List<Tag> tagList = manageProductService.getTagList();
		return ResponseEntity.ok(tagList);		
	}
	
	@PostMapping("uploadProductFile")
	public ResponseEntity<?> uploadProductFiles(@RequestParam(name = "productId", required=true) Long productId,
			@RequestParam(name = "productFile", required=true) MultipartFile productFile) throws IOException{
		ProductDTO product = manageProductService.uploadProductFile(productId,productFile);
		return ResponseEntity.ok(product);
	}
	
	@PostMapping("deleteProductFile")
	public ResponseEntity<?> deleteProductFile(@RequestParam(name = "productId", required=true) Long productId,
			@RequestParam(name = "fileId", required=true) Long fileId) throws IOException{
		ProductDTO product = manageProductService.deleteProductFile(productId, fileId);
		return ResponseEntity.ok(product);
	}
	
	@DeleteMapping("removePreviewVideo")
	public ResponseEntity<?> removePreviewVideo(@RequestParam(name = "productId", required=true) Long productId) throws IOException{
		boolean ret = manageProductService.deleteProductPreviewVideo(productId);
		return ResponseEntity.ok(ret);
	}
	
	@DeleteMapping("removePreviewPicture")
	public ResponseEntity<?> removePreviewPicture(@RequestParam(name = "previewId", required=true) Long previewId) throws IOException{
		List<Preview> previews = manageProductService.deletePreviewPicture(previewId);
		return ResponseEntity.ok(previews);
	}
}
