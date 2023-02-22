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

import com.SEP490_G9.helpers.Constant;
import com.SEP490_G9.models.DTOS.ProductDetailsDTO;
import com.SEP490_G9.models.DTOS.ProductFileDTO;
import com.SEP490_G9.models.Entities.Category;
import com.SEP490_G9.models.Entities.Preview;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.ProductFile;
import com.SEP490_G9.models.Entities.Tag;
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

	@GetMapping(value = "getEnabledProductsBySeller")
	public ResponseEntity<?> getProductsByUser() {
		List<ProductDetailsDTO> products = manageProductService.getProductDetailsDTOsBySeller();
		return ResponseEntity.ok(products);
	}

	@GetMapping("getLatestVersionProductByIdAndSeller")
	public ResponseEntity<?> getProductByIdAndUser(@RequestParam(name = "productId") Long productId)
			throws IOException {
		ProductDetailsDTO productDTO = null;
		productDTO = manageProductService.getLatestVersionProductDTOByIdAndSeller(productId);
		return ResponseEntity.ok(productDTO);
	}

	@PostMapping(value = "createNewProduct")
	public ResponseEntity<?> createNewProductRequest() {
		Product product = manageProductService.createNewProduct();

		return ResponseEntity.ok(product);
	}

	@PostMapping(value = "updateProduct")
	public ResponseEntity<?> updateProduct(@RequestBody ProductDetailsDTO productDetailsDTO,
			@RequestParam(name = "instruction") String instructionDetails) throws IOException {
		ProductDetailsDTO ret = manageProductService.updateProductDetails(productDetailsDTO, instructionDetails);
		return ResponseEntity.ok(ret);
	}

	@DeleteMapping(value = "deleteProduct/{id}")
	public ResponseEntity<?> deleteProducts(@PathVariable(name = "id") Long id) {
		return ResponseEntity.ok(manageProductService.deleteProduct(id));
	}

	@PostMapping(value = "uploadCoverImage")
	public ResponseEntity<?> uploadCoverImage(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "coverImage") MultipartFile coverImage, @RequestParam(name = "version") String version)
			throws IOException {
		String src = manageProductService.uploadCoverImage(coverImage, productId, version);
		return ResponseEntity.ok(src);
	}

	@PostMapping(value = "uploadPreviewVideo")
	public ResponseEntity<?> uploadPreviewVideo(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "previewVideo") MultipartFile previewVideo,
			@RequestParam(name = "version") String version) throws IOException {
		Preview src = manageProductService.uploadPreviewVideo(previewVideo, productId, version);
		return ResponseEntity.ok(src);
	}

	@PostMapping(value = "uploadPreviewPicture")
	public ResponseEntity<?> uploadPreviewPicture(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "previewPicture") MultipartFile previewPicture,
			@RequestParam(name = "version") String version) {
		List<Preview> previews = manageProductService.uploadPreviewPicture(previewPicture, productId, version);
		return ResponseEntity.ok(previews);
	}

	@PostMapping("uploadProductFile")
	public ResponseEntity<?> uploadProductFiles(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "productFile", required = true) MultipartFile productFile,
			@RequestParam(name = "version") String version) throws IOException {
		ProductFileDTO dto = manageProductService.uploadProductFile(productId, productFile, version);
		return ResponseEntity.ok(dto);
	}

	@PostMapping("deleteProductFile")
	public ResponseEntity<?> deleteProductFile(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "fileId", required = true) Long fileId) throws IOException {
		ProductFileDTO productFileDTO = manageProductService.deleteProductFile(productId, fileId);
		return ResponseEntity.ok(productFileDTO);
	}

	@DeleteMapping("removePreviewVideo")
	public ResponseEntity<?> removePreviewVideo(@RequestParam(name = "productId", required = true) Long productId)
			throws IOException {
		boolean ret = manageProductService.deleteProductPreviewVideo(productId);
		return ResponseEntity.ok(ret);
	}

	@DeleteMapping("removePreviewPicture")
	public ResponseEntity<?> removePreviewPicture(@RequestParam(name = "previewId", required = true) Long previewId)
			throws IOException {
		List<Preview> previews = manageProductService.deletePreviewPicture(previewId);
		return ResponseEntity.ok(previews);
	}

	@GetMapping("getTypeList")
	public ResponseEntity<?> getTypeList() {
		List<Category> typeList = manageProductService.getCategoryList();
		return ResponseEntity.ok(typeList);
	}

	@GetMapping("getTagList")
	public ResponseEntity<?> getTagList() {
		List<Tag> tagList = manageProductService.getTagList();
		return ResponseEntity.ok(tagList);
	}

	@GetMapping("getVirusTotalKey")
	public ResponseEntity<?> getVirusTotalKey() {
		return ResponseEntity.ok(Constant.VIRUS_TOTAL_KEY);
	}
}
