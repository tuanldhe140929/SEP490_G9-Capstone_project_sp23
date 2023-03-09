package com.SEP490_G9.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	SellerService sellerService;

	@Autowired
	StorageUtil storageUtil;

	@Autowired
	ProductService productService;

	@Autowired
	FileIOService fileStorageService;

	@DeleteMapping(value = "deletePreviewVideo")
	public ResponseEntity<?> removePreviewVideo(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "version", required = true) String version) throws IOException {
		ProductDetailsDTO dto = new ProductDetailsDTO();
		dto.setId(productId);
		dto.setVersion(version);
		ProductDetails pd = productDetailsService.getByIdAndVersion(productId,version);
		Preview preview = previewService.getByProductDetailsAndType(pd, "video").get(0);
		boolean ret = previewService.deletePreview(preview);
		return ResponseEntity.ok(ret);
	}

	@DeleteMapping(value = "deletePreviewPicture")
	public ResponseEntity<?> removePreviewPicture(@RequestParam(name = "previewId", required = true) Long previewId)
			throws IOException {
		Preview preview = previewService.getById(previewId);
		boolean ret = previewService.deleteById(previewId);
		List<Preview> returnData = previewService.getByProductDetailsAndType(preview.getProductDetails(),
				"picture");
		return ResponseEntity.ok(returnData);
	}

	@PostMapping(value = "uploadPreviewPicture")
	public ResponseEntity<?> uploadPreviewPicture(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "previewPicture") MultipartFile previewPicture,
			@RequestParam(name = "version") String version) {
		List<PreviewDTO> previews = null;

		if (!checkFileType(previewPicture, IMAGE_EXTENSIONS)) {
			throw new FileUploadException(previewPicture.getContentType() + " file not accept");
		} else {
			Product product = productService.getProductByIdAndSeller(productId, getCurrentSeller());
			ProductDetails productDetails = checkVersion(product, version);

			String previewPictureLocation = getPreviewsLocation(productDetails);
			File previewPicturesDir = new File(storageUtil.getLocation() + previewPictureLocation);
			previewPicturesDir.mkdirs();

			String storedPath = fileStorageService.storeV2(previewPicture,
					storageUtil.getLocation() + previewPictureLocation);
			Preview preview = new Preview();
			preview.setSource(storedPath.replace(storageUtil.getLocation(), ""));
			preview.setType("picture");
			preview.setProductDetails(productDetails);
			preview = previewService.createPreview(preview);
//			product.getPreviews().add(preview);
//			productRepository.save(product);
			//productDetails.getPreviews().add(preview);
			ProductDetailsDTO dto = new ProductDetailsDTO(productDetails);
			previews = dto.getPreviewPictures();

		}

		return ResponseEntity.ok(previews);
	}

	@PostMapping(value = "uploadPreviewVideo")
	public ResponseEntity<?> uploadPreviewVideo(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "previewVideo") MultipartFile previewVideo,
			@RequestParam(name = "version") String version) throws IOException {
		PreviewDTO src = null;
		if (!checkFileType(previewVideo, VIDEO_EXTENSIONS)) {
			throw new FileUploadException(previewVideo.getContentType() + " file not accept");
		} else {
			Product product = productService.getProductByIdAndSeller(productId, getCurrentSeller());

			ProductDetails productDetails = checkVersion(product, version);

			String previewVideoLocation = getPreviewsLocation(productDetails);
			File previewVideoDir = new File(storageUtil.getLocation() + previewVideoLocation);
			previewVideoDir.mkdirs();

			String savedPath = fileStorageService.storeV2(previewVideo,
					storageUtil.getLocation() + previewVideoLocation);
			Preview preview = null;
			List<Preview> previews = previewService.getByProductDetailsAndType(productDetails, "video");
			if (previews.size() == 0) {
				preview = new Preview();
			} else {
				preview = previews.get(0);
			}
//			preview.setSource(previewVideoLocation + previewVideo.getOriginalFilename());
			preview.setSource(previewVideoLocation + previewVideo.getOriginalFilename());
			preview.setType("video");
			preview.setProductDetails(productDetails);
			preview = previewService.createPreview(preview);
			productDetails.getPreviews().add(preview);
			ProductDetailsDTO dto = new ProductDetailsDTO(productDetails);
			src = dto.getPreviewVideo();
		}
		return ResponseEntity.ok(src);
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

	private boolean checkFileType(MultipartFile file, String[] extensions) {
		for (String extension : extensions) {
			if (file.getContentType().endsWith(extension)) {
				return true;
			}
		}
		return false;
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
