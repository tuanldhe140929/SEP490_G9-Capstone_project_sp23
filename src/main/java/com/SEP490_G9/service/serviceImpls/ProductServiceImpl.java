package com.SEP490_G9.service.serviceImpls;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.License;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductDetails.Status;
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.FileUploadException;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.LicenseRepository;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.ReportRepository;
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
	
	@Autowired
	LicenseRepository licenseRepository;
	
	@Autowired
	ReportRepository reportRepository;

	@Override
	public Product createProduct(Product product) {
		Seller seller = getCurrentSeller();
		product.setSeller(seller);
		Product createdProduct = productRepository.save(product);
		List<ProductDetails> ps = productDetailsRepo.findByApproved(Status.NEW);
		System.out.println(ps.size());
		createdProduct.getProductDetails().add(createProductDetails(createdProduct, FIRST_PRODUCT_VERSION));
		return createdProduct;

	}

	private ProductDetails createProductDetails(Product product, String version) {
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);
		productDetails.setCreatedDate(new Date());
		productDetails.setLastModified(new Date());
		productDetails.setApproved(Status.NEW);
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
		Product product = productRepository.findById(id).orElseThrow();
		product.setEnabled(false);
		productRepository.save(product);
		if (product == null || product.getSeller() != seller) {
			throw new ResourceNotFoundException("product id", id.toString(), seller);
		}
		return true;
	}

	public Seller getCurrentSeller() {
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		Seller seller = sellerService.getSellerById(account.getId());
		return seller;
	}

	@Override
	public Product getProductById(Long productId) {
		Product product = productRepository.findById(productId).orElseThrow();
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
		ProductDetails pd = productDetailsRepo.findByProductIdAndProductVersionKeyVersion(productId, version);
		if(pd==null){
			throw new ResourceNotFoundException("product details","id and version",productId + " "+ version);
		}
		if(pd.getFiles().size()>0) {
			product.setActiveVersion(version);
			productRepository.save(product);
			return true;
		}else {
			return false;
		}

	}

	public String getCoverImageLocation(ProductDetails productDetails) {
		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
				+ productDetails.getProduct().getId() + "\\";
	}

	public String getProductFilesLocation(ProductDetails productDetails) {
		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
				+ productDetails.getProduct().getId() + "\\";
	}

	public String getPreviewsLocation(ProductDetails productDetails) {
		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
				+ productDetails.getProduct().getId() + "\\";
	}

	public String getSellerProductsDataLocation(Seller seller) {
		return "account_id_" + seller.getId() + "\\" + PRODUCT_FOLDER_NAME;
	}

	@Override
	public String uploadCoverImage(MultipartFile coverImage, Long productId, String version) {
		String src = "";
		if (!checkFileType(coverImage, IMAGE_EXTENSIONS)) {
			throw new FileUploadException(coverImage.getContentType() + " file not accept");
		} else {
			Product product = productRepository.findById(productId).orElseThrow();

			ProductDetails productDetails = null;
			for (ProductDetails pd : product.getProductDetails()) {
				if (pd.getVersion().equalsIgnoreCase(version)) {
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

	@Override
	public String updateProductApprovalStatus(long productId, boolean status) {
//		Product product = productRepository.findById(productId).get();
//		if(status) {
//			product.setApproved("APPROVED");
//			productRepository.save(product);
//			return "APPROVED";
//		}else {
//			product.setApproved("REJECTED");
//			productRepository.save(product);
//			return "REJECTED";
//		}
		return "";
	}

	@Override
	public List<License> getAllLicense() {
	return licenseRepository.findAll();
	}

	public String getROOT_LOCATION() {
		return ROOT_LOCATION;
	}

	public void setROOT_LOCATION(String rOOT_LOCATION) {
		ROOT_LOCATION = rOOT_LOCATION;
	}

	@Override
	public List<Product> getAllProductsByReportStatus(String reportStatus) {
		List<Report> allReports = reportRepository.findAll();
		List<Product> allReportProducts = new ArrayList<>();
		for(Report report: allReports) {
			Product product = report.getProduct();
			if(report.getStatus().equalsIgnoreCase(reportStatus)) {
				allReportProducts.add(product);
			}
		}
		List<Product> finalResult = allReportProducts.stream().distinct().toList();
		return finalResult;
	}

}
