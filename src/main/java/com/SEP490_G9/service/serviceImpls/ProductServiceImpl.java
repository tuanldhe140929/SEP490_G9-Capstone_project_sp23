package com.SEP490_G9.service.serviceImpls;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.FileUploadException;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.SellerService;

@Service
public class ProductServiceImpl implements ProductService {
	final String[] IMAGE_EXTENSIONS = { "image/png", "image/jpeg", "image/svg+xml" };
	final String FIRST_PRODUCT_VERSION = "1.0.0";
	final String PRODUCT_FOLDER_NAME = "products";

	@Value("${root.location}")
	private String ROOT_LOCATION;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductDetailsRepository productDetailsRepo;

	@Autowired
	FileIOService fileIOService;

	@Autowired
	SellerService sellerService;

	@Override
	public Product createProduct(Product product) {
		Product createdProduct = productRepository.save(product);
		createdProduct.getProductDetails().add(createProductDetails(createdProduct, FIRST_PRODUCT_VERSION));
		return createdProduct;

	}

	private ProductDetails createProductDetails(Product product, String version) {
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);
		productDetails.setCreatedDate(new Date());
		productDetails.setLastModified(new Date());
		productDetails.setDraft(true);
		ProductDetails savedProductDetails = productDetailsRepo.save(productDetails);

		String coverImageDestination = getCoverImageLocation(productDetails);
		String filesDestination = getProductFilesLocation(productDetails);
		String previewsDestinations = getPreviewsLocation(productDetails);
		File folder = new File(ROOT_LOCATION + coverImageDestination);
		folder.mkdirs();
		folder = new File(ROOT_LOCATION + filesDestination);
		folder.mkdirs();
		folder = new File(ROOT_LOCATION + previewsDestinations);
		folder.mkdirs();
		return savedProductDetails;
	}

	@Override
	public Product updateProduct(Product product) {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		if (product.getSeller().getId() != account.getId()) {
			// throw new AuthorizationServiceException(FIRST_PRODUCT_VERSION);
		}
		return productRepository.save(product);
	}

	@Override
	public boolean deleteProductById(Long id) {
		Seller seller = getCurrentSeller();
		Product product = productRepository.findById(id).get();
		if (product == null || product.getSeller() != seller) {
			throw new ResourceNotFoundException("product id", id.toString(), seller);
		}
		return true;
	}

	private Seller getCurrentSeller() {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		Seller seller = sellerService.getSellerById(account.getId());
		return seller;
	}

	@Override
	public Product getProductById(Long productId) {
		Product product = productRepository.findById(productId).get();
		if (product == null) {
			throw new ResourceNotFoundException("product", "id", productId);
		}
		return product;
	}

	@Override
	public List<Product> getProductsBySellerId(Long sellerId) {
		return productRepository.findBySellerId(sellerId);
	}

	@Override
	public boolean setActiveVersion(Long productId, String version) {
		Product product = productRepository.findById(productId).orElseThrow();
		if (productDetailsRepo.existsByProductIdAndProductVersionKeyVersion(productId, version))
			product.setActiveVersion(version);
		productRepository.save(product);
		return true;

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

	// danh cho V2
//	private String getProductVersionDataLocation(ProductDetails pd) {
////		return getSellerProductsDataLocation(pd.getProduct().getSeller()) + "\\" + pd.getProduct().getId() + "\\"
////				+ pd.getVersion() + "\\";
//		return getSellerProductsDataLocation(pd.getProduct().getSeller()) + "\\" + pd.getProduct().getId() + "\\";
//	}

	@Override
	public String uploadCoverImage(MultipartFile coverImage, Long productId, String version) {
		String src = "";
		if (!checkFileType(coverImage, IMAGE_EXTENSIONS)) {
			throw new FileUploadException(coverImage.getContentType() + " file not accept");
		} else {
			Product product = productRepository.findById(productId).orElseThrow();

			ProductDetails productDetails = null;
			for (ProductDetails pd : product.getProductDetails()) {
				if (pd.getVersion().equalsIgnoreCase(product.getActiveVersion())) {
					productDetails = pd;
					break;
				}
			}

			if (productDetails == null) {
				throw new ResourceNotFoundException("Product's", "active version", productId);
			}
			String coverImageLocation = getCoverImageLocation(productDetails);
			File coverImageDir = new File(ROOT_LOCATION + coverImageLocation);
			coverImageDir.mkdirs();
			String storedPath = fileIOService.storeV2(coverImage, ROOT_LOCATION + coverImageLocation);
			productDetails.setCoverImage(storedPath.replace(ROOT_LOCATION, ""));
			productDetailsRepo.save(productDetails);
			src = productDetails.getCoverImage();
			return src;
		}
	}

	private boolean checkFileType(MultipartFile file, String[] extensions) {
		for (String extension : extensions) {
			if (file.getContentType().endsWith(extension)) {
				return true;
			}
		}
		return false;
	}
}
