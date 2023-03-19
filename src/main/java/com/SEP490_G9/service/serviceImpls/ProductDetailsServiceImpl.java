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
import com.SEP490_G9.entities.ProductDetails.Status;
import com.SEP490_G9.entities.ProductFile;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.NumberException;
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

	//Supporting methods
	
	@Override
	public List<ProductDetails> getAll() {
		List<ProductDetails> allProductDetails = productDetailsRepo.findAll();
		return allProductDetails;
	}
	
	@Override
	public List<ProductDetails> getByLatestVer(List<ProductDetails> listPd) {
		List<ProductDetails> latestVerPd = new ArrayList<>();
		for(ProductDetails pd: listPd) {
			Product product = pd.getProduct();
			ProductDetails latestVer = getActiveVersion(product.getId());
			latestVerPd.add(latestVer);
		}
		return latestVerPd.stream().distinct().toList();
	}

	@Override
	public List<ProductDetails> getBySeller(List<ProductDetails> listPd, long sellerId) {
		List<ProductDetails> PdBySeller = new ArrayList<>();
		for(ProductDetails pd: listPd) {
			Product product = pd.getProduct();
			if(product.getSeller().getId().equals(sellerId)) {
				PdBySeller.add(pd);
			}
		}
		return PdBySeller;
	}

	@Override
	public List<ProductDetails> getByPending(List<ProductDetails> listPd) {
		List<ProductDetails> pendingPd = new ArrayList<>();
		for(ProductDetails pd: listPd) {
			if(pd.getApproved() == Status.PENDING) {
				pendingPd.add(pd);
			}
		}
		return pendingPd;
	}

	@Override
	public List<ProductDetails> getByApproved(List<ProductDetails> listPd) {
		List<ProductDetails> approvedPd = new ArrayList<>();
		for(ProductDetails pd: listPd) {
			if(pd.getApproved() == Status.APPROVED) {
				approvedPd.add(pd);
			}
		}
		return approvedPd;
	}

	@Override
	public List<ProductDetails> getByRejected(List<ProductDetails> listPd) {
		List<ProductDetails> rejectedPd = new ArrayList<>();
		for(ProductDetails pd: listPd) {
			if(pd.getApproved() == Status.REJECTED) {
				rejectedPd.add(pd);
			}
		}
		return rejectedPd;
	}

	@Override
	public List<ProductDetails> getByDrafted(List<ProductDetails> listPd) {
		List<ProductDetails> draftedPd = new ArrayList<>();
		for(ProductDetails pd: listPd) {
			Product product = pd.getProduct();
			if(product.isDraft()) {
				draftedPd.add(pd);
			}
		}
		return draftedPd;
	}

	@Override
	public List<ProductDetails> getByPublished(List<ProductDetails> listPd) {
		List<ProductDetails> publishedPd = new ArrayList<>();
		for(ProductDetails pd: listPd) {
			Product product = pd.getProduct();
			if(!product.isDraft()) {
				publishedPd.add(pd);
			}
		}
		return publishedPd;
	}

	@Override
	public List<ProductDetails> getByEnabled(List<ProductDetails> listPd) {
		List<ProductDetails> enabledPd = new ArrayList<>();
		for(ProductDetails pd: listPd) {
			Product product = pd.getProduct();
			if(product.isEnabled()) {
				enabledPd.add(pd);
			}
		}
		return enabledPd;
	}

	@Override
	public List<ProductDetails> getByDisabled(List<ProductDetails> listPd) {
		List<ProductDetails> disabledPd = new ArrayList<>();
		for(ProductDetails pd: listPd) {
			Product product = pd.getProduct();
			if(!product.isEnabled()) {
				disabledPd.add(pd);
			}
		}
		return disabledPd;
	}

	@Override
	public List<ProductDetails> getByKeyword(List<ProductDetails> listPd, String keyword) {
		if(keyword.trim().isEmpty()) {
			return listPd;
		}else {
			List<ProductDetails> pdByKeyword = new ArrayList<>();
			for(ProductDetails pd: listPd) {
				if(pd.getName().trim().replaceAll("\\s+", " ").toLowerCase().contains(keyword.trim().toLowerCase().replaceAll("\\s+", " "))) {
					pdByKeyword.add(pd);
				}
			}
			return pdByKeyword;
		}
	}

	@Override
	public List<ProductDetails> getByCategory(List<ProductDetails> listPd, int categoryId) {
		List<ProductDetails> pdByCategory = new ArrayList<>();
		if(categoryId==0) {
			return listPd;
		}else {
			for(ProductDetails pd: listPd) {
				if(pd.getCategory().getId()==categoryId) {
					pdByCategory.add(pd);
				}
			}
			return pdByCategory;
		}
	}

	// unusable, yet
	@Override
	public List<ProductDetails> getByTag(List<ProductDetails> listPd, List<Integer> tagId) {
		return null;
	}

	@Override
	public List<ProductDetails> getByPriceRange(List<ProductDetails> listPd, int min, int max) {
		if(min > max) {
			throw new NumberException("Min cannot be greater than max");
		}
		if(min < 0 || max < 0) {
			throw new NumberException("Min or max cannot be negative");
		}
		List<ProductDetails> pdByPriceRange = new ArrayList<>();
		for(ProductDetails pd: listPd) {
			if(pd.getPrice()>=min&&pd.getPrice()<=max) {
				pdByPriceRange.add(pd);
			}
		}
		return pdByPriceRange;
	}
	
	//By Nam Dinh
	
	@Override
	public ProductDetails getActiveVersion(Long productId) {
		Product product = productRepo.findById(productId).orElseThrow();
		ProductDetails ret = null;
		for(ProductDetails pd: product.getProductDetails()) {
			if(pd.getVersion().equals(product.getActiveVersion())) {
				ret = pd;
			}
		}
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
		if(existByProductIdAndVersion(edited.getProductVersionKey().getProductId(), edited.getProductVersionKey().getVersion()))
			throw new ResourceNotFoundException("product details with id and version","",edited);
		return productDetailsRepo.save(edited);
	}

	@Override
	public List<ProductDetails> getAllByProductId(Long id) {
		List<ProductDetails> ret = null;
		return productDetailsRepo.findByProductId(id);
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
		newPD.setApproved(Status.PENDING);
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
			if (ep.isDraft()) {
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


	private String getCoverImageLocation(ProductDetails productDetails) {
		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
				+ productDetails.getProduct().getId() + "\\";
	}

	private String getProductFilesLocation(ProductDetails productDetails) {
		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
				+ productDetails.getProduct().getId() + "\\";
	}

	private String getPreviewsLocation(ProductDetails productDetails) {
		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
				+ productDetails.getProduct().getId() + "\\";
	}

	private String getSellerProductsDataLocation(Seller seller) {
		return "account_id_" + seller.getId() + "\\" + PRODUCT_FOLDER_NAME;
	}
	
	// By Quan Nguyen
	
	//hien san pham theo tu khoa
	@Override
	public List<ProductDetails> getProductForSearching(String keyword, int categoryid, int min, int max) {
		List<ProductDetails> allPd = getAll();
		List<ProductDetails> allApprovedPd = getByApproved(allPd);
		List<ProductDetails> allEnabledPd = getByEnabled(allApprovedPd);
		List<ProductDetails> allPublishedPd = getByPublished(allEnabledPd);
		List<ProductDetails> allLatestPd = getByLatestVer(allPublishedPd);
		List<ProductDetails> allKeywordPd = getByKeyword(allLatestPd, keyword);
		List<ProductDetails> allCategoryPd = getByCategory(allKeywordPd, categoryid);
		List<ProductDetails> finalResult = getByPriceRange(allCategoryPd, min, max);
		return finalResult;
	}

	//hien san pham nguoi dung cho chinh no
	@Override
	public List<ProductDetails> getProductBySellerForSeller(long sellerId, String keyword, int categoryId, int min,
			int max) {
		List<ProductDetails> allPd = getAll();
		List<ProductDetails> allEnabledPd = getByEnabled(allPd);
		List<ProductDetails> allLatestPd = getByLatestVer(allEnabledPd);
		List<ProductDetails> allKeywordPd = getByKeyword(allLatestPd, keyword);
		List<ProductDetails> allCategoryPd = getByCategory(allKeywordPd, categoryId);
		List<ProductDetails> allPricePd = getByPriceRange(allCategoryPd, min, max);
		List<ProductDetails> allSellerPd = getBySeller(allPricePd, sellerId);
		return allSellerPd;
	}

	//hien san pham nguoi dung cho nguoi khac
	@Override
	public List<ProductDetails> getProductBySellerForUser(long sellerId, String keyword, int categoryId, int min,
			int max) {
		List<ProductDetails> allPd = getAll();
		List<ProductDetails> allApprovedPd = getByApproved(allPd);
		List<ProductDetails> allEnabledPd = getByEnabled(allApprovedPd);
		List<ProductDetails> allPublishedPd = getByPublished(allEnabledPd);
		List<ProductDetails> allLatestPd = getByLatestVer(allPublishedPd);
		List<ProductDetails> allKeywordPd = getByKeyword(allLatestPd, keyword);
		List<ProductDetails> allCategoryPd = getByCategory(allKeywordPd, categoryId);
		List<ProductDetails> allPricePd = getByPriceRange(allCategoryPd, min, max);
		List<ProductDetails> allSellerPd = getBySeller(allPricePd, sellerId);
		return allSellerPd;

	}

	
}
