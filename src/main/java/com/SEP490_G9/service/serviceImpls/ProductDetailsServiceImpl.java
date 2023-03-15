package com.SEP490_G9.service.serviceImpls;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.SEP490_G9.dto.ProductDetailsDTO;

import com.SEP490_G9.entities.Category;
import com.SEP490_G9.entities.Preview;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductFile;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductFileRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.service.PreviewService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductService;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {
	final String PRODUCT_FOLDER_NAME = "products";
	@Value("${root.location}")
	private String ROOT_LOCATION;

	@Autowired
	ProductDetailsRepository productDetailsRepo;

	@Autowired
	ProductRepository productRepo;

	@Autowired
	SellerRepository sellerRepo;

	@Autowired
	ProductService productService;

	@Autowired
	PreviewService previewService;

	@Autowired
	ProductFileRepository productFileRepo;

	@Override
	public ProductDetails getActiveVersion(Long productId) {
		Product product = productRepo.findById(productId).orElseThrow();
		ProductDetails ret = productDetailsRepo.findByProductIdAndProductVersionKeyVersion(productId,
				product.getActiveVersion());
		if (ret == null) {
			throw new ResourceNotFoundException("Product details", "version", product.getActiveVersion());
		}
		return ret;
	}

	@Override
	public ProductDetails createProductDetails(ProductDetails productDetails) {
		if (productDetailsRepo.existsByProductIdAndProductVersionKeyVersion(productDetails.getProduct().getId(),
				productDetails.getVersion())) {
			throw new DuplicateFieldException("version ", productDetails.getVersion());
		}
		return productDetailsRepo.save(productDetails);
	}

	@Override
	public ProductDetails getByProductIdAndVersion(Long productId, String version) {
		ProductDetails ret = productDetailsRepo.findByProductIdAndProductVersionKeyVersion(productId, version);
		if (ret == null) {
			throw new ResourceNotFoundException("product", "id and version", productId + " " + version);
		}
		return ret;
	}

	@Override
	public boolean existByProductIdAndVersion(Long productId, String version) {
		return productDetailsRepo.existsByProductIdAndProductVersionKeyVersion(productId, version);
	}

	@Override
	public ProductDetails updateProductDetails(ProductDetails edited) {

		if (edited.getName() == null || edited.getName() == "") {
			throw new DuplicateFieldException("name must not be blank", null);
		}
		if (edited.getCategory() == null) {
			throw new DuplicateFieldException("category must not be blank", null);
		}

		return productDetailsRepo.save(edited);
	}

	@Override
	public List<ProductDetails> getAllByProductId(Long id) {
		List<ProductDetails> ret = null;
		return productDetailsRepo.findByProductId(id);
	}

	@Override
	public List<ProductDetails> getByKeyword(String keyword) {
		List<ProductDetails> allProductDetails = productDetailsRepo.findAll();
		List<ProductDetails> searchResult = new ArrayList<>();
		for (ProductDetails pd : allProductDetails) {
			if (pd.getName().trim().toLowerCase().contains(keyword.trim().toLowerCase())) {
				searchResult.add(pd);
			}
		}
		return searchResult;
	}

	@Override
	public List<ProductDetails> getAll() {
		List<ProductDetails> allProductDetails = productDetailsRepo.findAll();
		return allProductDetails;
	}

	@Override
	public List<ProductDetails> getByKeywordCategoryTags(String keyword, int categoryid, int min, int max) {
		List<ProductDetails> allProductDetails = productDetailsRepo.findAll();
		List<ProductDetails> searchResult = new ArrayList<>();
		List<ProductDetails> searchResultLatestVersion = new ArrayList<>();
		for (ProductDetails pd : allProductDetails) {
			if (categoryid == 0) {
				if (pd.getName().trim().toLowerCase().contains(keyword.trim().toLowerCase().replaceAll("\\s+", " "))
						&& pd.getPrice() >= min && pd.getPrice() <= max) {
					searchResult.add(pd);
				}
			} else {
				if (pd.getName().trim().toLowerCase().contains(keyword.trim().toLowerCase().replaceAll("\\s+", " "))
						&& pd.getCategory().getId() == categoryid && pd.getPrice() >= min && pd.getPrice() <= max) {
					searchResult.add(pd);
				}
			}
		}
		for (ProductDetails pd : searchResult) {
			Product product = pd.getProduct();
			String activeVersion = product.getActiveVersion();
			searchResultLatestVersion.add(getByProductIdAndVersion(product.getId(), activeVersion));
		}
		List<ProductDetails> finalResult = searchResultLatestVersion.stream().distinct().collect(Collectors.toList());
		return finalResult;
	}

	@Override
	public List<ProductDetails> getProductBySeller(long sellerid, String keyword, int categoryid, int min, int max) {
		Seller seller = sellerRepo.findById(sellerid);
		List<Product> productList = productRepo.findBySeller(seller);
		List<ProductDetails> productDetailsList = new ArrayList<>();
		List<ProductDetails> filteredList = new ArrayList<>();
		for (Product pro : productList) {
			String activeVersion = pro.getActiveVersion();
			productDetailsList.add(getByProductIdAndVersion(pro.getId(), activeVersion));
		}
		List<ProductDetails> latestVersionWithoutDupList = productDetailsList.stream().distinct()
				.collect(Collectors.toList());
		for (ProductDetails pd : latestVersionWithoutDupList) {
			if (categoryid == 0) {
				if (pd.getName() != null) {
					if (pd.getName().trim().toLowerCase().replaceAll("\\s+", " ")
							.contains(keyword.trim().toLowerCase().replaceAll("\\s+", " ")) && pd.getPrice() >= min
							&& pd.getPrice() <= max) {
						filteredList.add(pd);
					}
				}
			} else {
				if (pd.getName().trim().toLowerCase().replaceAll("\\s+", " ")
						.contains(keyword.trim().toLowerCase().replaceAll("\\s+", " "))
						&& pd.getCategory().getId() == categoryid && pd.getPrice() >= min && pd.getPrice() <= max) {
					filteredList.add(pd);
				}
			}
		}
		return filteredList;
	}

	public ProductDetails createNewVersion(Long id, String newVersion) {
		if (productDetailsRepo.existsByProductIdAndProductVersionKeyVersion(id, newVersion)) {
			throw new DuplicateFieldException("version", newVersion);
		}

		ProductDetails productDetails = getActiveVersion(id);
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
		productFileRepo.saveAll(newPDFiles);
		return getActiveVersion(id);
	}

	private ProductDetails createProductDetails(Product product, String version) {
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion(version);
		productDetails.setCreatedDate(new Date());
		productDetails.setLastModified(new Date());
		productDetails.setDraft(true);
		ProductDetails savedProductDetails = createProductDetails(productDetails);

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
	public List<ProductDetails> getBySellerIdAndIsDraft(Long sellerId, boolean isDraft) {
		List<Product> ps = productRepo.findBySellerId(sellerId);
		for (Product p : ps) {
			if (!p.isEnabled()) {
				ps.remove(p);
			}
		}
		List<ProductDetails> pds = new ArrayList<>();
		for (Product ep : ps) {
			ProductDetails apd = findActiveProductDetails(ep);
			if (apd.isDraft() == isDraft) {
				pds.add(apd);
			}
		}
		return pds;
	}

	private ProductDetails findActiveProductDetails(Product ep) {
		for (ProductDetails pd : ep.getProductDetails()) {
			if (pd.getVersion().equalsIgnoreCase(ep.getActiveVersion())) {
				return pd;
			}
		}
		return null;
	}

	// v1
//	private String getProductVersionDataLocation(ProductDetails pd) {
////		return getSellerProductsDataLocation(pd.getProduct().getSeller()) + "\\" + pd.getProduct().getId() + "\\"
////				+ pd.getVersion() + "\\";
//		return getSellerProductsDataLocation(pd.getProduct().getSeller()) + "\\" + pd.getProduct().getId() + "\\";
//	}

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

	@Override
	public List<ProductDetails> getAllProducts() {
		List<ProductDetails> allProductsWithDuplication = productDetailsRepo.findAll();
		List<ProductDetails> allProductsWithoutDuplication = new ArrayList<>();
		for(ProductDetails pd: allProductsWithDuplication) {
			Product product = pd.getProduct();
			String activeVersion = product.getActiveVersion();
			allProductsWithoutDuplication.add(getByProductIdAndVersion(product.getId(), activeVersion));
		}
		List<ProductDetails> allDistinctProducts = allProductsWithoutDuplication.stream().distinct().collect(Collectors.toList());
		return allDistinctProducts;
	}

	@Override
	public List<ProductDetails> getAllPendingProducts() {
		List<ProductDetails> allProducts = getAllProducts();
		List<ProductDetails> allPendingProducts = new ArrayList<>();
		for(ProductDetails pd: allProducts) {
			Product product = pd.getProduct();
			if(product.isApproved().equalsIgnoreCase("PENDING")) {
				allPendingProducts.add(pd);
			}
		}
		return allPendingProducts;
	}

}
