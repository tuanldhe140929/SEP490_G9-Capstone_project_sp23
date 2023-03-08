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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.dto.ProductFileDTO;
import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.Product;
import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.entity.ProductFile;
import com.SEP490_G9.entity.Seller;
import com.SEP490_G9.entity.UserDetailsImpl;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductFileService;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.SellerService;
import com.SEP490_G9.service.VirusTotalService;
import com.SEP490_G9.util.ClamAVUtil;
import com.SEP490_G9.util.StorageUtil;

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
	ClamAVUtil clamAVUtil;

	@Autowired
	SellerService sellerService;

	@Autowired
	StorageUtil storageUtil;

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
		ret.setId((long) -1);
		ret.setSize(productFile.getSize());
		ret.setName(productFile.getOriginalFilename());
		ret.setFileState(ProductFileDTO.FileState.UPLOADING);

		Product product = productService.getProductByIdAndSeller(productId, getCurrentSeller());
		ProductDetails productDetails = checkVersion(product, version);

		if (productFile.getSize() == 0) {
			throw new FileUploadException("File size:" + productFile.getSize());
		}
		for (ProductFile file : productDetails.getFiles()) {
			if (file.getName().equalsIgnoreCase(productFile.getName())) {
				throw new FileUploadException("File exist:" + file.getName());
			}
		}
		if ((productDetails.getFiles().size() + 1) >= 10) {
			throw new FileUploadException("Exeeded max file count");
		}

		Path tempFilePath = createTempFile(productFile);

		boolean isSafe;
		File file = new File(tempFilePath.toString());
		isSafe = virusTotalService.scanFile(file);
		System.out.println("isSafe?" + isSafe);

		if (isSafe) {
//		if (true) {
			Files.deleteIfExists(tempFilePath);
			String fileLocation = getProductFilesLocation(productDetails);
			File fileDir = new File(storageUtil.getLocation() + fileLocation);
			fileDir.mkdirs();
			String storedPath = fileStorageService.storeV2(productFile, storageUtil.getLocation() + fileLocation);
			ProductFile pf = new ProductFile(storedPath.replace(storageUtil.getLocation(), ""), productFile, productDetails);
			ProductFile savedFile = productFileService.createProductFile(pf);
			ret = new ProductFileDTO(savedFile);
		} else {
			Files.deleteIfExists(tempFilePath);
			ret.setFileState(ProductFileDTO.FileState.MALICIOUS);
		}
		return ResponseEntity.ok(ret);
	}

	private Path createTempFile(MultipartFile productFile) throws IOException {
		new File(storageUtil.getLocation() + "\\" + "temp").mkdirs();
		String fileName = UUID.randomUUID().toString();
		Path tempFilePath = Paths.get(this.storageUtil.getLocation() + "/temp/" + fileName);
		Files.createDirectories(tempFilePath.getParent());
		Files.createFile(tempFilePath);

		InputStream is = null;
		OutputStream os = null;
		try {
			is = productFile.getInputStream();
			os = new FileOutputStream(this.storageUtil.getLocation() + "/temp/" + fileName);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} catch (Exception e) {

		} finally {
			is.close();
			os.close();
		}
		return tempFilePath;
	}

	@PostMapping(value = "deleteProductFile")
	public ResponseEntity<?> deleteProductFile(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "fileId", required = true) Long fileId) throws IOException {

		ProductFile pf = productFileService.getById(fileId);
		String version = pf.getProductDetails().getVersion();
		long id = pf.getProductDetails().getProduct().getId();
		ProductDetails pd = productDetailsService.getByIdAndVersion(id, version);
		pd.getFiles().remove(pf);
		ProductFileDTO dto = new ProductFileDTO(pf, false);

		productFileService.deleteById(fileId);

		if (pd.getFiles().size()<= 0) {
			pd.setDraft(true);
			dto.setLastFile(true);

			productDetailsService.updateProductDetailsStatus(pd);
		}

		return ResponseEntity.ok(dto);
	}

	private ProductDetails checkVersion(Product product, String version) {
		ProductDetails pd = null;
		for (ProductDetails productDetails : product.getProductDetails()) {
			if (productDetails.getVersion().equalsIgnoreCase(version)) {
				pd = productDetails;
			}
		}
		if (pd == null) {
			pd = createProductDetails(product, version);
		}
		return pd;
	}

	private ProductDetails createProductDetails(Product product, String version) {
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);
		productDetails.setCreatedDate(new Date());
		productDetails.setLastModified(null);
		ProductDetails savedProductDetails = productDetailsService.createProductDetails(productDetails);

		String coverImageDestination = getCoverImageLocation(productDetails);
		String filesDestination = getProductFilesLocation(productDetails);
		String previewsDestinations = getPreviewsLocation(productDetails);
		File folder = new File(storageUtil.getLocation() + coverImageDestination);
		folder.mkdirs();
		folder = new File(storageUtil.getLocation() + filesDestination);
		folder.mkdirs();
		folder = new File(storageUtil.getLocation() + previewsDestinations);
		folder.mkdirs();
		return savedProductDetails;
	}

	private Seller getCurrentSeller() {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		Seller seller = sellerService.getSellerById(account.getId());
		return seller;
	}

	private String getCoverImageLocation(ProductDetails productDetails) {
//		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
//				+ productDetails.getProduct().getId() + "\\" + productDetails.getVersion() + "\\"
//				+ PRODUCT_COVER_IMAGE_FOLDER_NAME + "\\";
		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
				+ productDetails.getProduct().getId() + "\\";
	}

	private String getProductFilesLocation(ProductDetails productDetails) {
//		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
//				+ productDetails.getProduct().getId() + "\\" + productDetails.getVersion() + "\\"
//				+ PRODUCT_FILES_FOLDER_NAME + "\\";
		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
				+ productDetails.getProduct().getId() + "\\";
	}

	private String getPreviewsLocation(ProductDetails productDetails) {
//		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
//				+ productDetails.getProduct().getId() + "\\" + productDetails.getVersion() + "\\"
//				+ PRODUCT_PREVIEWS_FOLDER_NAME + "\\";
		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
				+ productDetails.getProduct().getId() + "\\";
	}

	private String getSellerProductsDataLocation(Seller seller) {
		return "account_id_" + seller.getId() + "\\" + PRODUCT_FOLDER_NAME;
	}
}
