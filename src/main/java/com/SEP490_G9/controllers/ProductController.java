package com.SEP490_G9.controllers;

import java.io.File;
import java.io.FileNotFoundException;
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
import com.SEP490_G9.dto.TagDTO;
import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.Category;
import com.SEP490_G9.entity.License;
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
import com.SEP490_G9.exception.StorageException;
import com.SEP490_G9.repository.PreviewRepository;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.PreviewService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductFileService;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.SellerService;
import com.SEP490_G9.util.ClamAVUtil;
import com.SEP490_G9.util.StorageUtil;

import fi.solita.clamav.ClamAVClient;
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
	SellerService sellerService;

	@Autowired
	ProductService productService;

	@Autowired
	ProductFileService productFileService;

	@Autowired
	LicenseService licenseService;

	@Autowired
	ProductDetailsRepository productDetailsRepo;

	@GetMapping("getLicense")
	public ResponseEntity<?> getAllLicense() {
		List<License> licenses = licenseService.getAllLicense();
		return ResponseEntity.ok(licenses);
	}
	// create new Product
	@PostMapping(value = "createNewProduct")
	public ResponseEntity<?> createNewProduct() {
		Seller seller = getCurrentSeller();
		Product product = new Product();
		product.setEnabled(true);
		product.setSeller(seller);
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
	@PostMapping(value = "updateProduct")
	public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductDetailsDTO productDetailsDTO,
			@RequestParam(name = "instruction") String instructionDetails) {
		ProductDetailsDTO ret = null;
		ProductDetails notEdited = productDetailsRepo
				.findByProductIdAndProductVersionKeyVersion(productDetailsDTO.getId(), productDetailsDTO.getVersion());
		notEdited.setLastModified(new Date());
		Product product = notEdited.getProduct();

		notEdited.setTags(productDetailsDTO.getTags());
		notEdited.setCategory(productDetailsDTO.getCategory());
		if (productDetailsDTO.getDescription() != null)
			notEdited.setDescription(productDetailsDTO.getDescription().trim());
		else
			notEdited.setDescription(null);

		if (productDetailsDTO.getDetails() != null)
			notEdited.setDetailDescription(productDetailsDTO.getDetails().trim());
		else
			notEdited.setDetailDescription("");

		if (productDetailsDTO.getLicense() != null)
			notEdited.setLicense(new License(productDetailsDTO.getLicense()));
		else
			notEdited.setLicense(null);

		notEdited.setName(productDetailsDTO.getName().trim());
		notEdited.setPrice(productDetailsDTO.getPrice());
		notEdited.setDraft(productDetailsDTO.isDraft());
		notEdited.setInstruction(instructionDetails.trim());

		productService.updateProduct(product);
		ProductDetails updatedPd = productDetailsRepo.save(notEdited);

		ret = new ProductDetailsDTO(updatedPd);

		return ResponseEntity.ok(ret);
	}

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

	private Seller getCurrentSeller() {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		Seller seller = sellerService.getSellerById(account.getId());
		return seller;
	}

	@GetMapping(value = "getProductDetails")
	public ResponseEntity<?> getProductDetails(
			@RequestParam(name = "sellerId", required = true) Long sellerId){
		List<Product> p = new ArrayList<>();
		p = this.productService.getProductsBySellerId(sellerId);
		List<ProductDetails> pd = new ArrayList<>();
		for(int i = 0; i < p.size(); i++) {
			p.get(i).getActiveVersion();
			ProductDetails pde = null;
			pde = this.productDetailsRepo.findByProductIdAndProductVersionKeyVersion(p.get(i).getId(),
					p.get(i).getActiveVersion());
			pd.add(pde);
		}
		List<ProductDetailsDTO> ret = new ArrayList<>();
		for(int i = 0; i< pd.size(); i++) {
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

//	@GetMapping(value = "getProductsByKeyword/{keyword}")
//	public ResponseEntity<?> getProductsByKeyword(@PathVariable(name = "keyword") String keyword) {
//		List<ProductDetails> searchResult = this.productDetailsService.getByKeyword(keyword);
//		List<ProductDetailsDTO> searchResultDto = new ArrayList<>();
//		for (ProductDetails result : searchResult) {
//			searchResultDto.add(new ProductDetailsDTO(result));
//		}
//		return ResponseEntity.ok(searchResultDto);
//	}
//
//	@GetMapping(value = "getAllProducts")
//	public ResponseEntity<?> getAllProducts() {
//		List<ProductDetails> allProducts = this.productDetailsService.getAll();
//		List<ProductDetailsDTO> allProductsDto = new ArrayList<>();
//		for (ProductDetails product : allProducts) {
//			allProductsDto.add(new ProductDetailsDTO(product));
//		}
//		return ResponseEntity.ok(allProductsDto);
//	}

//	@PostMapping(value = "createNewVersion")
//	public ResponseEntity<?> createNewVersion(@RequestBody ProductDetailsDTO productDetailsDTO,
//			@RequestParam(name = "newVersion", required = true) String newVersion) throws IOException {
//		List<ProductDetails> productDetailss = productDetailsService.getAllByProductId(productDetailsDTO.getId());
//		List<String> versions = new ArrayList<>();
//		for (ProductDetails pd : productDetailss) {
//			versions.add(pd.getVersion());
//		}
//
//		ProductDetailsDTO ret = null;
//		if (versions.contains(newVersion) || newVersion.endsWith(".") || newVersion.length() > 6) {
//			throw new DuplicateFieldException("version", newVersion);
//		} else {
//			ProductDetails productDetails = productDetailsService.getActiveVersion(productDetailsDTO.getId());
//			ProductDetails newPD = new ProductDetails();
//			newPD = createProductDetails(productDetails.getProduct(), newVersion);
//			String source = getProductVersionDataLocation(productDetails);
//			String dest = getProductVersionDataLocation(newPD);
//
//			File coppyFrom = new File(ROOT_LOCATION + source);
//			File coppyTo = new File(ROOT_LOCATION + dest);
//			FileUtils.copyDirectory(coppyFrom, coppyTo);
//
//			String newPDCoverImage = productDetails.getCoverImage().replace(source, dest);
//			newPD.setCoverImage(newPDCoverImage);
//
//			newPD.setCategory(
//					new Category(productDetails.getCategory().getId(), productDetails.getCategory().getName()));
//
//			List<Tag> tags = new ArrayList<>();
//			for (Tag tag : productDetails.getTags()) {
//				tags.add(new Tag(tag.getId(), tag.getName()));
//			}
//			newPD.setTags(tags);
//
//			newPD.setCreatedDate(new Date());
//			newPD.setDescription(productDetails.getDescription());
//			newPD.setDetailDescription(productDetails.getDetailDescription());
//			newPD.setName(productDetails.getName());
//			newPD.setPrice(productDetails.getPrice());
//			newPD.setInstruction(productDetails.getInstruction());
//			List<ProductFile> newPDFiles = new ArrayList<>();
//			for (ProductFile pfile : productDetails.getFiles()) {
//				ProductFile pf = new ProductFile();
//				pf.setName(pfile.getName());
//				pf.setProductDetails(newPD);
//				pf.setSize(pfile.getSize());
//				pf.setType(pfile.getType());
//				pf.setSource(pfile.getSource().replace(source, dest));
//				newPDFiles.add(pf);
//			}
//
//			List<Preview> newPDPreviews = new ArrayList<>();
//
//			for (Preview preview : productDetails.getPreviews()) {
//				Preview p = new Preview();
//				p.setProductDetails(newPD);
//				p.setSource(preview.getSource());
//				p.setType(preview.getType());
//				p.setSource(preview.getSource().replace(source, dest));
//				newPDPreviews.add(p);
//			}
//
//			newPD.setFiles(newPDFiles);
//			newPD.setPreviews(newPDPreviews);
//
//			previewService.createPreviews(newPDPreviews);
//			productFileService.createProductFiles(newPDFiles);
//			newPD = productDetailsService.getActiveVersion(productDetailsDTO.getId());
//			ret = new ProductDetailsDTO(newPD);
//		}
//		return ResponseEntity.ok(ret);
//	}

//	private ProductDetails createProductDetails(Product product, String version) {
//	ProductDetails productDetails = new ProductDetails();
//	productDetails.setProduct(product);
//	productDetails.setVersion(version);
//	productDetails.setCreatedDate(new Date());
//	productDetails.setLastModified(new Date());
//	productDetails.setDraft(true);
//	ProductDetails savedProductDetails = productDetailsService.createProductDetails(productDetails);
//
//	String coverImageDestination = getCoverImageLocation(productDetails);
//	String filesDestination = getProductFilesLocation(productDetails);
//	String previewsDestinations = getPreviewsLocation(productDetails);
//	File folder = new File(ROOT_LOCATION + coverImageDestination);
//	folder.mkdirs();
//	folder = new File(ROOT_LOCATION + filesDestination);
//	folder.mkdirs();
//	folder = new File(ROOT_LOCATION + previewsDestinations);
//	folder.mkdirs();
//	return savedProductDetails;
//}

//	private String getCoverImageLocation(ProductDetails productDetails) {
////		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
////				+ productDetails.getProduct().getId() + "\\" + productDetails.getVersion() + "\\"
////				+ PRODUCT_COVER_IMAGE_FOLDER_NAME + "\\";
//		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
//				+ productDetails.getProduct().getId() + "\\";
//	}
//
//	private String getProductFilesLocation(ProductDetails productDetails) {
////		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
////				+ productDetails.getProduct().getId() + "\\" + productDetails.getVersion() + "\\"
////				+ PRODUCT_FILES_FOLDER_NAME + "\\";
//		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
//				+ productDetails.getProduct().getId() + "\\";
//	}
//
//	private String getPreviewsLocation(ProductDetails productDetails) {
////		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
////				+ productDetails.getProduct().getId() + "\\" + productDetails.getVersion() + "\\"
////				+ PRODUCT_PREVIEWS_FOLDER_NAME + "\\";
//		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
//				+ productDetails.getProduct().getId() + "\\";
//	}
//
//	private String getSellerProductsDataLocation(Seller seller) {
//		return "account_id_" + seller.getId() + "\\" + PRODUCT_FOLDER_NAME;
//	}
//
//	private String getProductVersionDataLocation(ProductDetails pd) {
////		return getSellerProductsDataLocation(pd.getProduct().getSeller()) + "\\" + pd.getProduct().getId() + "\\"
////				+ pd.getVersion() + "\\";
//		return getSellerProductsDataLocation(pd.getProduct().getSeller()) + "\\" + pd.getProduct().getId() + "\\";
//	}
}
