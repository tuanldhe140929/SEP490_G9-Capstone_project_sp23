package com.SEP490_G9.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.dto.ProductFileDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductFile;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductFileService;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.SellerService;
import com.SEP490_G9.service.VirusTotalService;

@RequestMapping(value = "/productFile")
@RestController
public class ProductFileController {

	final String PRODUCT_FOLDER_NAME = "products";
	final String PRODUCT_COVER_IMAGE_FOLDER_NAME = "coverImage";
	final String PRODUCT_FILES_FOLDER_NAME = "files";
	final String PRODUCT_PREVIEWS_FOLDER_NAME = "previews";
	final String PRODUCT_INSTRUCTION_FILE_NAME = "instruction.txt";

	@Autowired
	VirusTotalService virusTotalService;

	@Autowired
	ProductService productService;

	@Autowired
	ProductDetailsService productDetailsService;

	@Autowired
	SellerService sellerService;

	@Value("${root.location}")
	String ROOT_LOCATION;

	@Autowired
	ProductFileService productFileService;

	@Autowired
	FileIOService fileStorageService;

	@PostMapping(value = "uploadProductFile")
	public ResponseEntity<?> uploadProductFile(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "productFile", required = true) MultipartFile productFile,
			@RequestParam(name = "version") String version)
			throws IOException, InterruptedException, ExecutionException {

		ProductFileDTO ret = new ProductFileDTO();
		ret = productFileService.uploadFile(productId, version, productFile);
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value = "deleteProductFile")
	public ResponseEntity<?> deleteProductFile(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "fileId", required = true) Long fileId) throws IOException {
		ProductFileDTO dto = null;
		dto = productFileService.deleteById(fileId);
		return ResponseEntity.ok(dto);
	}

	@PostMapping("generateDownloadToken/{productId}")
	public ResponseEntity<String> generateDownloadToken(@PathVariable Long productId) {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		String token = productFileService.generateDownloadToken(account.getId(), productId);
		System.out.println(token);
		return ResponseEntity.ok(token);
		// redirect to download page, display a download button, list of files,
		// click the download button call the below endpoint to serve file for download
	}

	@GetMapping("download")
	public ResponseEntity<ByteArrayResource> downloadProduct(@RequestParam(name="productId",required=true) Long productId, @RequestParam(name="token",required = true) String token) {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		System.out.println("recieving"+token);
		ByteArrayResource resource = productFileService.downloadFile(account.getId(), productId, token);
		byte[] data = resource.getByteArray();
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=product.zip")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(data.length).body(resource);
	}

}
