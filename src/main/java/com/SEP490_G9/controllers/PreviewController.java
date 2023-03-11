package com.SEP490_G9.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.dto.PreviewDTO;
import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Preview;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.repository.PreviewRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.PreviewService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.SellerService;
import com.SEP490_G9.common.StorageUtil;

@RequestMapping(value = "/preview")
@RestController
public class PreviewController {
	@Value("${root.location}")
	private String ROOT_LOCATION;
	final String PRODUCT_FOLDER_NAME = "products";
	final String PRODUCT_COVER_IMAGE_FOLDER_NAME = "coverImage";
	final String PRODUCT_FILES_FOLDER_NAME = "files";
	final String PRODUCT_PREVIEWS_FOLDER_NAME = "previews";
	final String PRODUCT_INSTRUCTION_FILE_NAME = "instruction.txt";

	final String[] VIDEO_EXTENSIONS = { "video/mp4", "video/x-matroska", "video/quicktime" };
	final String[] IMAGE_EXTENSIONS = { "image/png", "image/jpeg", "image/svg+xml" };

	@Autowired
	PreviewService previewService;

	@Autowired
	ProductDetailsService productDetailsService;

	@DeleteMapping(value = "deletePreviewVideo")
	public ResponseEntity<?> removePreviewVideo(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "version", required = true) String version) throws IOException {
		boolean ret = previewService.deleteVideoPreview(productId, version);
		return ResponseEntity.ok(ret);

	}

	@DeleteMapping(value = "deletePreviewPicture")
	public ResponseEntity<?> removePreviewPicture(@RequestParam(name = "previewId", required = true) Long previewId)
			throws IOException {
		List<PreviewDTO> ret = new ArrayList<>();
		Preview preview = previewService.getById(previewId);
		previewService.deleteById(previewId);
		List<Preview> returnData = previewService.getByProductDetailsAndType(preview.getProductDetails(), "picture");
		for (Preview p : returnData) {
			ret.add(new PreviewDTO(p));
		}
		return ResponseEntity.ok(returnData);
	}

	@PostMapping(value = "uploadPreviewPicture")
	public ResponseEntity<?> uploadPreviewPicture(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "previewPicture") MultipartFile previewPicture,
			@RequestParam(name = "version") String version) {
		System.out.println(previewPicture.getOriginalFilename());
		List<PreviewDTO> previews = previewService.uploadPreviewPicture(productId, version, previewPicture);
		
		return ResponseEntity.ok(previews);
	}

	@PostMapping(value = "uploadPreviewVideo")
	public ResponseEntity<?> uploadPreviewVideo(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "previewVideo") MultipartFile previewVideo,
			@RequestParam(name = "version") String version) throws IOException {
		PreviewDTO src = null;
		Preview preview = previewService.uploadPreviewVideo(productId, version, previewVideo);
		if (preview != null) {
			src = new PreviewDTO(preview);
		}
		return ResponseEntity.ok(src);
	}
}
