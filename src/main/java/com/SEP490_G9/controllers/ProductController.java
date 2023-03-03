package com.SEP490_G9.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.SEP490_G9.dto.ProductFileDTO;
import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.Category;
import com.SEP490_G9.entity.Preview;
import com.SEP490_G9.entity.Product;
import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.entity.ProductFile;
import com.SEP490_G9.entity.Seller;
import com.SEP490_G9.entity.Tag;
import com.SEP490_G9.entity.UserDetailsImpl;
import com.SEP490_G9.entity.embeddable.ProductVersionKey;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.PreviewRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.ManageProductService;
import com.SEP490_G9.service.PreviewService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductFileService;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.SellerService;
import com.SEP490_G9.util.ClamAVUtil;
import com.SEP490_G9.util.StorageUtil;

import fi.solita.clamav.ClamAVClient;

@RestController
@RequestMapping("/product")
public class ProductController {

	final String PRODUCT_FOLDER_NAME = "products";
	final String PRODUCT_COVER_IMAGE_FOLDER_NAME = "coverImage";
	final String PRODUCT_FILES_FOLDER_NAME = "files";
	final String PRODUCT_PREVIEWS_FOLDER_NAME = "previews";
	final String PRODUCT_INSTRUCTION_FILE_NAME = "instruction.txt";

	final String[] IMAGE_EXTENSIONS = { "image/png", "image/jpeg", "image/svg+xml" };

	final String FIRST_PRODUCT_VERSION = "1.0.0";

	@Autowired
	FileIOService fileStorageService;

	@Autowired
	SellerService sellerService;

	@Autowired
	ProductService productService;

	@Autowired
	ProductDetailsService productDetailsService;

	@Autowired
	PreviewRepository previewRepository;

	@Autowired
	StorageUtil storageUtil;

	@Autowired
	ProductFileService productFileService;

	@Autowired
	private ClamAVUtil clamAVUtil;

	@Autowired
	PreviewService previewService;

	@GetMapping(value = "getPublishedProductsBySeller")
	public ResponseEntity<?> getProductsBySeller() {

		Seller seller = getCurrentSeller();
		List<Product> products = productService.getProductsBySeller(seller);

		List<ProductDetails> latestVersionProducts = new ArrayList<>();
		for (Product product : products) {
			latestVersionProducts.add(productDetailsService.getProductDetailsByProductId(product.getId()));
		}
		List<ProductDetailsDTO> dtos = new ArrayList<>();
		for (ProductDetails pd : latestVersionProducts) {
			dtos.add(new ProductDetailsDTO(pd, previewRepository));
		}
		return ResponseEntity.ok(dtos);
	}

	@GetMapping("getProductById")
	public ResponseEntity<?> getProductById(@RequestParam(name = "productId") Long productId) throws IOException {
		ProductDetails pd = productDetailsService.getProductDetailsByProductId(productId);
		ProductDetailsDTO dto = new ProductDetailsDTO(pd, previewRepository);
		return ResponseEntity.ok(dto);
	}

	@PostMapping(value = "createNewVersion")
	public ResponseEntity<?> createNewVersion(@RequestBody ProductDetailsDTO productDetailsDTO,
			@RequestParam(name = "newVersion", required = true) String newVersion) throws IOException {
		List<ProductDetails> productDetailss = productDetailsService.getAllByProductId(productDetailsDTO.getId());
		List<String> versions = new ArrayList<>();
		for (ProductDetails pd : productDetailss) {
			versions.add(pd.getVersion());
		}

		ProductDetailsDTO ret = null;
		if (versions.contains(newVersion) || newVersion.endsWith(".") || newVersion.length() > 6) {
			throw new DuplicateFieldException("version", newVersion);
		} else {
			ProductDetails productDetails = productDetailsService
					.getProductDetailsByProductId(productDetailsDTO.getId());
			ProductDetails newPD = new ProductDetails();
			newPD = createProductDetails(productDetails.getProduct(), newVersion);
			String source = getProductVersionDataLocation(productDetails);
			String dest = getProductVersionDataLocation(newPD);

			File coppyFrom = new File(storageUtil.getLocation() + source);
			File coppyTo = new File(storageUtil.getLocation() + dest);
			FileUtils.copyDirectory(coppyFrom, coppyTo);

			String newPDCoverImage = productDetails.getCoverImage().replace(source, dest);
			newPD.setCoverImage(newPDCoverImage);

			newPD.setCategory(
					new Category(productDetails.getCategory().getId(), productDetails.getCategory().getName()));

			List<Tag> tags = new ArrayList<>();
			for (Tag tag : productDetails.getTags()) {
				tags.add(new Tag(tag.getId(), tag.getName()));
			}
			newPD.setTags(tags);

			newPD.setCreatedDate(new Date());
			newPD.setDescription(productDetails.getDescription());
			newPD.setDetailDescription(productDetails.getDetailDescription());
			newPD.setName(productDetails.getName());
			newPD.setPrice(productDetails.getPrice());
			newPD.setInstruction(productDetails.getInstruction());
			List<ProductFile> newPDFiles = new ArrayList<>();
			for (ProductFile pfile : productDetails.getFiles()) {
				ProductFile pf = new ProductFile();
				pf.setName(pfile.getName());
				pf.setProductDetails(newPD);
				pf.setSize(pfile.getSize());
				pf.setType(pfile.getType());
				pf.setSource(pfile.getSource().replace(source, dest));
				newPDFiles.add(pf);
			}

			List<Preview> newPDPreviews = new ArrayList<>();

			for (Preview preview : productDetails.getPreviews()) {
				Preview p = new Preview();
				p.setProductDetails(newPD);
				p.setSource(preview.getSource());
				p.setType(preview.getType());
				p.setSource(preview.getSource().replace(source, dest));
				newPDPreviews.add(p);
			}

			newPD.setFiles(newPDFiles);
			newPD.setPreviews(newPDPreviews);

			previewService.createPreviews(newPDPreviews);
			productFileService.createProductFiles(newPDFiles);
			newPD = productDetailsService.getProductDetailsByProductId(productDetailsDTO.getId());
			ret = new ProductDetailsDTO(newPD, previewRepository);
		}
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value = "chooseVersion")
	public ResponseEntity<?> createNewVersionV2(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "version", required = true) String newVersion) throws IOException {
		ProductDetailsDTO dto = new ProductDetailsDTO();
		dto.setId(productId);
		dto.setVersion(newVersion);
		ProductDetails productDetails = productDetailsService.getProductDetailsByProductIdAndVersionAndSeller(dto,
				getCurrentSeller());
		ProductDetailsDTO ret = new ProductDetailsDTO(productDetails, previewRepository);
		System.out.println(ret.getName());
		return ResponseEntity.ok(ret);

	}

	@GetMapping(value = "getAllVersion")
	public ResponseEntity<?> getAllVersion(@RequestParam(name = "productId", required = true) Long productId) {
		List<ProductDetails> productDetailss = productDetailsService.getAllByProductId(productId);
		List<String> versions = new ArrayList<>();
		for (ProductDetails pd : productDetailss) {
			versions.add(pd.getVersion());
		}
		return ResponseEntity.ok(versions);
	}

	@PostMapping(value = "createNewVersionV2")
	public ResponseEntity<?> createNewVersionV2(@RequestBody ProductDetailsDTO productDetailsDTO,
			@RequestParam(name = "newVersion", required = true) String newVersion) throws IOException {
		List<ProductDetails> productDetailss = productDetailsService.getAllByProductId(productDetailsDTO.getId());

		for (ProductDetails pd : productDetailss) {
			if (newVersion == pd.getVersion()) {
				throw new DuplicateFieldException("version", newVersion);
			}
		}

		ProductDetails productDetails = productDetailsService.getProductDetailsByProductId(productDetailsDTO.getId());
		ProductDetails newPD = new ProductDetails();
		newPD = createProductDetails(productDetails.getProduct(), newVersion);
		newPD.setCoverImage(productDetails.getCoverImage());
		if (productDetails.getCategory() != null) {
			newPD.setCategory(
					new Category(productDetails.getCategory().getId(), productDetails.getCategory().getName()));
		}
		if (productDetails.getTags() != null) {
			List<Tag> tags = new ArrayList<>();
			for (Tag tag : productDetails.getTags()) {
				tags.add(new Tag(tag.getId(), tag.getName()));
			}
			newPD.setTags(tags);
		}

		newPD.setCreatedDate(new Date());
		newPD.setDescription(productDetails.getDescription());
		newPD.setDetailDescription(productDetails.getDetailDescription());
		newPD.setName(productDetails.getName());
		newPD.setPrice(productDetails.getPrice());
		newPD.setInstruction(productDetails.getInstruction());

		List<ProductFile> newPDFiles = new ArrayList<>();
		for (ProductFile pfile : productDetails.getFiles()) {
			ProductFile pf = new ProductFile();
			pf.setName(pfile.getName());
			pf.setProductDetails(newPD);
			pf.setSize(pfile.getSize());
			pf.setType(pfile.getType());
			// pf.setSource(pfile.getSource().replace(source, dest));
			pf.setSource(pfile.getSource());
			newPDFiles.add(pf);
		}

		List<Preview> newPDPreviews = new ArrayList<>();

		for (Preview preview : productDetails.getPreviews()) {
			Preview p = new Preview();
			p.setProductDetails(newPD);
			p.setSource(preview.getSource());
			p.setType(preview.getType());
			// p.setSource(preview.getSource().replace(source, dest));
			newPDPreviews.add(p);
		}
		previewService.createPreviews(newPDPreviews);
		productFileService.createProductFiles(newPDFiles);
		newPD = productDetailsService.getProductDetailsByProductId(productDetailsDTO.getId());
		ProductDetailsDTO dto = new ProductDetailsDTO(newPD, previewRepository);
		return ResponseEntity.ok(dto);
	}

	@PostMapping(value = "createNewProduct")
	public ResponseEntity<?> createNewProduct() {
		Seller seller = getCurrentSeller();
		Product product = new Product();
		product.setDraft(true);
		product.setEnabled(true);
		product.setSeller(seller);
		Product createdProduct = productService.createProduct(product);
		ProductDTO productDTO = new ProductDTO(createdProduct, previewRepository);
		ProductDetails productDetails = createProductDetails(createdProduct, FIRST_PRODUCT_VERSION);
		ProductDetailsDTO dto = new ProductDetailsDTO(productDetails, previewRepository);

		return ResponseEntity.ok(productDTO);
	}

	@PostMapping(value = "updateProduct")
	public ResponseEntity<?> updateProduct(@RequestBody ProductDetailsDTO productDetailsDTO,
			@RequestParam(name = "instruction") String instructionDetails) throws IOException {
		ProductDetailsDTO ret = null;

		Seller seller = getCurrentSeller();
		ProductDetails notEdited = productDetailsService
				.getProductDetailsByProductIdAndVersionAndSeller(productDetailsDTO, seller);
		notEdited.setLastModified(new Date());
		Product product = notEdited.getProduct();
		notEdited.setTags(productDetailsDTO.getTags());
		notEdited.setCategory(productDetailsDTO.getCategory());
		notEdited.setDescription(productDetailsDTO.getDescription());
		notEdited.setDetailDescription(productDetailsDTO.getDetails());
		notEdited.setLicense(productDetailsDTO.getLicense());
		notEdited.setName(productDetailsDTO.getName());
		notEdited.setPrice(productDetailsDTO.getPrice());
		product.setDraft(productDetailsDTO.isDraft());

		String instructionFileDir = getProductFilesLocation(notEdited) + PRODUCT_INSTRUCTION_FILE_NAME;

		FileOutputStream fos = new FileOutputStream(storageUtil.getLocation() + instructionFileDir);
		fos.write(instructionDetails.getBytes());
		fos.flush();
		fos.close();
		notEdited.setInstruction(instructionDetails);
		Product updatedProduct = productService.updateProduct(product);
		ProductDetails updatedPd = productDetailsService.updateProductDetails(notEdited);

		ret = new ProductDetailsDTO(updatedPd, previewRepository);

		return ResponseEntity.ok(ret);
	}

	@DeleteMapping(value = "deleteProduct/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable(name = "id") Long id) {
		boolean isDeleted = productService.deleteProductByIdAndSeller(id, getCurrentSeller());
		return ResponseEntity.ok(isDeleted);
	}

	@PostMapping(value = "uploadCoverImage")
	public ResponseEntity<?> uploadCoverImage(@RequestParam(name = "productId") Long productId,
			@RequestParam(name = "coverImage") MultipartFile coverImage, @RequestParam(name = "version") String version)
			throws IOException {
		String src = "";
		if (!checkFileType(coverImage, IMAGE_EXTENSIONS)) {
			throw new FileUploadException(coverImage.getContentType() + " file not accept");

		} else {
			Product product = productService.getProductByIdAndSeller(productId, getCurrentSeller());
			ProductDetails productDetails = checkVersion(product, version);
			String coverImageLocation = getCoverImageLocation(productDetails);
			File coverImageDir = new File(storageUtil.getLocation() + coverImageLocation);
			coverImageDir.mkdirs();

			String storedPath = fileStorageService.storeV2(coverImage, storageUtil.getLocation() + coverImageLocation);
			productDetails.setCoverImage(storedPath);
			productService.updateProduct(product);
			src = productDetails.getCoverImage();
		}

		return ResponseEntity.ok(src);
	}

	@PostMapping(value = "deleteProductFile")
	public ResponseEntity<?> deleteProductFile(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "fileId", required = true) Long fileId) throws IOException {
		ProductFile pf = productFileService.getById(fileId);
		productFileService.deleteById(fileId);
		File fileDir = new File(storageUtil.getLocation() + pf.getSource());
		fileDir.delete();
		ProductFileDTO dto = new ProductFileDTO(pf);
		return ResponseEntity.ok(dto);
	}

	private Seller getCurrentSeller() {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		Seller seller = sellerService.getSellerById(account.getId());
		return seller;
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

	private boolean checkFileType(MultipartFile file, String[] extensions) {
		for (String extension : extensions) {
			if (file.getContentType().endsWith(extension)) {
				return true;
			}
		}
		return false;
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

	private String getProductVersionDataLocation(ProductDetails pd) {
//		return getSellerProductsDataLocation(pd.getProduct().getSeller()) + "\\" + pd.getProduct().getId() + "\\"
//				+ pd.getVersion() + "\\";
		return getSellerProductsDataLocation(pd.getProduct().getSeller()) + "\\" + pd.getProduct().getId() + "\\";
	}

	private ProductDetails createProductDetails(Product product, String version) {
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);
		productDetails.setCreatedDate(new Date());
		productDetails.setLastModified(new Date());
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

}