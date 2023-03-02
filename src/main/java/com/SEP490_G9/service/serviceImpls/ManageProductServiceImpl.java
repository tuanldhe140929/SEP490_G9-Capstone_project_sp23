//package com.SEP490_G9.service.serviceImpls;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Scanner;
//
//import javax.security.sasl.AuthenticationException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.core.io.support.ResourceRegion;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.SEP490_G9.dto.ProductDetailsDTO;
//import com.SEP490_G9.dto.ProductFileDTO;
//import com.SEP490_G9.entity.Account;
//import com.SEP490_G9.entity.Category;
//import com.SEP490_G9.entity.Preview;
//import com.SEP490_G9.entity.Product;
//import com.SEP490_G9.entity.ProductDetails;
//import com.SEP490_G9.entity.ProductFile;
//import com.SEP490_G9.entity.Seller;
//import com.SEP490_G9.entity.Tag;
//import com.SEP490_G9.entity.UserDetailsImpl;
//import com.SEP490_G9.exception.DuplicateFieldException;
//import com.SEP490_G9.exception.FileUploadException;
//import com.SEP490_G9.exception.ResourceNotFoundException;
//import com.SEP490_G9.repository.CategoryRepository;
//import com.SEP490_G9.repository.PreviewRepository;
//import com.SEP490_G9.repository.ProductDetailsRepository;
//import com.SEP490_G9.repository.ProductFileRepository;
//import com.SEP490_G9.repository.ProductRepository;
//import com.SEP490_G9.repository.SellerRepository;
//import com.SEP490_G9.repository.TagRepository;
//import com.SEP490_G9.repository.UserRepository;
//import com.SEP490_G9.service.FileIOService;
//import com.SEP490_G9.service.ManageProductService;
//import com.SEP490_G9.util.ClamAVUtil;
//import com.SEP490_G9.util.StorageUtil;
//
//import org.apache.commons.io.FileUtils;
//
//import fi.solita.clamav.ClamAVClient;
//import jakarta.transaction.Transactional;
//
//@Service
//public class ManageProductServiceImpl implements ManageProductService {
//	final String PRODUCT_FOLDER_NAME = "products";
//	final String PRODUCT_COVER_IMAGE_FOLDER_NAME = "coverImage";
//	final String PRODUCT_FILES_FOLDER_NAME = "files";
//	final String PRODUCT_PREVIEWS_FOLDER_NAME = "previews";
//	final String PRODUCT_INSTRUCTION_FILE_NAME = "instruction.txt";
//
//	final String[] VIDEO_EXTENSIONS = { "video/mp4", "video/x-matroska", "video/quicktime" };
//	final String[] IMAGE_EXTENSIONS = { "image/png", "image/jpeg", "image/svg+xml" };
//
//	final String FIRST_PRODUCT_VERSION = "1.0.0";
//	@Autowired
//	StorageUtil storageUtil;
//
//	@Autowired
//	ProductRepository productRepository;
//
//	@Autowired
//	UserRepository userRepository;
//
//	@Autowired
//	FileIOService fileStorageService;
//
//	@Autowired
//	CategoryRepository categoryRepository;
//
//	@Autowired
//	TagRepository tagRepository;
//
//	@Autowired
//	ProductFileRepository productFileRepository;
//
//	@Autowired
//	PreviewRepository previewRepository;
//
//	@Autowired
//	private ServeMediaService serveMediaService;
//
//	@Autowired
//	private SellerRepository sellerRepository;
//
//	@Autowired
//	private ProductDetailsRepository productDetailsRepo;
//
//	@Autowired
//	private ClamAVUtil clamAVUtil;
//
//	@Override
//	public List<ProductDetailsDTO> getProductDetailsDTOsBySeller() {
//		Seller seller = getCurrentSeller();
//		List<Product> products = productRepository.findBySeller(seller);
//		List<ProductDetails> latestVersionProducts = new ArrayList<>();
//		for (Product product : products) {
//			latestVersionProducts.add(productDetailsRepo.findFirstByProductIdOrderByCreatedDateDesc(product.getId()));
//		}
//		List<ProductDetailsDTO> dtos = new ArrayList<>();
//		return dtos;
//	}
//
//	@Override
//	public Product createNewProduct() {
//		Seller seller = getCurrentSeller();
//		Product product = new Product();
//		product.setDraft(true);
//		product.setEnabled(true);
//		product.setSeller(seller);
//		Product createdProduct = productRepository.save(product);
//		this.createProductDetails(createdProduct, FIRST_PRODUCT_VERSION);
//		return product;
//	}
//
//	@Override
//	public ProductDetailsDTO getLatestVersionProductDTOByIdAndSeller(Long productId) throws IOException {
//		Seller seller = getCurrentSeller();
//		ProductDetails productDetails = productDetailsRepo.findFirstByProductIdOrderByCreatedDateDesc(productId);
//		if (productDetails == null) {
//			throw new ResourceNotFoundException("product id:" + productId, "user id", seller.getId());
//		}
//		ProductDetailsDTO dto = new ProductDetailsDTO(productDetails, previewRepository);
//		
//		return dto;
//	}
//
//	@Override
//	public ProductDetailsDTO updateProductDetails(ProductDetailsDTO productDetailsDTO, String instruction)
//			throws IOException {
//		ProductDetailsDTO ret = null;
//		Seller seller = getCurrentSeller();
//		try {
//
//			ProductDetails edited = getProductDetailsByProductAndVersion(productDetailsDTO);
//			if (edited.getProduct().getSeller() != seller) {
//				throw new AuthenticationException();
//			}
//			edited.setLastModified(new Date());
//			Product product = edited.getProduct();
//			edited.setTags(productDetailsDTO.getTags());
//			edited.setCategory(productDetailsDTO.getCategory());
//			edited.setDescription(productDetailsDTO.getDescription());
//			edited.setDetailDescription(productDetailsDTO.getDetails());
//			edited.setLicense(productDetailsDTO.getLicense());
//			edited.setName(productDetailsDTO.getName());
//			edited.setPrice(productDetailsDTO.getPrice());
//			product.setDraft(productDetailsDTO.isDraft());
//
//			String instructionFileDir = getProductFilesLocation(edited) + PRODUCT_INSTRUCTION_FILE_NAME;
//
//			FileOutputStream fos = new FileOutputStream(storageUtil.getLocation() + instructionFileDir);
//			fos.write(instruction.getBytes());
//			fos.flush();
//			fos.close();
//			edited.setInstruction(instruction);
//			productRepository.save(product);
//			productDetailsRepo.save(edited);
//			ret = new ProductDetailsDTO(edited, previewRepository);
//			ret.setInstruction(instruction);
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//		return ret;
//	}
//
//	@Override
//	public boolean deleteProduct(Long id) {
//		Product ret = null;
//		try {
//			Product found = getProductByIdAndSeller(id);
//			found.setEnabled(false);
//		} catch (Exception e) {
//			return false;
//		}
//		return true;
//	}
//
//	@Transactional
//	@Override
//	public ProductFileDTO deleteProductFile(Long productId, Long fileId) throws IOException {
//		ProductFile productFile = productFileRepository.findById(fileId).get();
//		if (productFile == null) {
//			throw new ResourceNotFoundException("file", productId.toString(), "");
//		}
////		ProductDetails productDetails = productFile.getProductDetails();
////		productDetails.getFiles().remove(productFile);
//		productFileRepository.deleteById(fileId);
//		File fileDir = new File(storageUtil.getLocation() + productFile.getSource());
//		fileDir.delete();
//		ProductFileDTO dto = new ProductFileDTO(productFile);
//		return dto;
//	}
//
//	@Override
//	public boolean deleteProductPreviewVideo(Long previewId) {
//		Preview preview = previewRepository.findById(previewId).get();
//		if (preview == null) {
//			throw new ResourceNotFoundException("Preview video id:", previewId.toString(), "");
//		}
//		previewRepository.delete(preview);
//		File fileDir = new File(storageUtil.getLocation() + preview.getSource());
//		fileDir.delete();
//		return true;
//	}
//
//	@Override
//	public String uploadCoverImage(MultipartFile coverImage, Long productId, String version) throws IOException {
//
//		if (!checkFileType(coverImage, IMAGE_EXTENSIONS)) {
//			throw new FileUploadException(coverImage.getContentType() + " file not accept");
//
//		} else {
//			Product product = getProductByIdAndSeller(productId);
//			ProductDetails productDetails = checkVersion(product, version);
//			String coverImageLocation = getCoverImageLocation(productDetails);
//			File coverImageDir = new File(storageUtil.getLocation() + coverImageLocation);
//			coverImageDir.mkdirs();
//
//			fileStorageService.store(coverImage, coverImageLocation);
//			productDetails.setCoverImage(coverImageLocation + coverImage.getOriginalFilename());
//			productRepository.save(product);
//			return productDetails.getCoverImage();
//		}
//	}
//
//	@Transactional
//	@Override
//	public ProductFileDTO uploadProductFile(Long productId, MultipartFile productFile, String version)
//			throws IOException {
//		ProductFileDTO dto = new ProductFileDTO();
//		dto.setId((long) -1);
//		dto.setSize(productFile.getSize());
//		dto.setName(productFile.getOriginalFilename());
//		dto.setFileState(ProductFileDTO.FileState.UPLOADING);
//
//		
//		
//
//		if (productFile.getSize() == 0) {
//
//		}
//		if (true) {
//
//			Product product = getProductByIdAndSeller(productId);
//			ProductDetails productDetails = checkVersion(product, version);
//			if (productFile.getSize() == 0) {
//				throw new FileUploadException("File size:" + productFile.getSize());
//			}
//			for (ProductFile file : productDetails.getFiles()) {
//				if (file.getName().equalsIgnoreCase(productFile.getName())) {
//					throw new FileUploadException("File exist:" + file.getName());
//				}
//			}
//			if ((productDetails.getFiles().size() + 1) >= 10) {
//				throw new FileUploadException("Exeeded max file count");
//			}
//
//			String fileLocation = getProductFilesLocation(productDetails);
//			ProductFile file = new ProductFile(productFile, fileLocation, productDetails);
//			if (productFileRepository.existsByNameAndProductDetails(file.getName(), productDetails)) {
//				throw new DuplicateFieldException("file name", file.getName());
//			}
//			File fileDir = new File(storageUtil.getLocation() + fileLocation);
//			fileDir.mkdirs();
//			fileStorageService.store(productFile, fileLocation);
//			productFileRepository.save(file);
//			productDetails.getFiles().add(file);
//			dto = new ProductFileDTO(file);
//		} else {
//			dto.setFileState(ProductFileDTO.FileState.ERROR);
//		}
//		return dto;
//	}
//
//	private boolean scanForViruses(MultipartFile file) throws IOException {
//		byte[] r = new byte[0];
//		ClamAVClient a = clamAVUtil.newClient();
//		System.out.println(a.ping());
//		try {
//			r = a.scan(file.getInputStream());
//		} catch (Exception e) {
//
//		} finally {
//			file.getInputStream().close();
//		}
//		System.out.println(new String(r,"ASCII"));
//		return ClamAVClient.isCleanReply(r);
//	}
//
//	@Override
//	public Preview uploadPreviewVideo(MultipartFile previewVideo, Long productId, String version) {
//
//		if (!checkFileType(previewVideo, VIDEO_EXTENSIONS)) {
//			throw new FileUploadException(previewVideo.getContentType() + " file not accept");
//		} else {
//			Product product = getProductByIdAndSeller(productId);
//
//			ProductDetails productDetails = checkVersion(product, version);
//
//			String previewVideoLocation = getPreviewsLocation(productDetails);
//			File previewVideoDir = new File(storageUtil.getLocation() + previewVideoLocation);
//			previewVideoDir.mkdirs();
//
//			fileStorageService.store(previewVideo, previewVideoLocation);
//			Preview preview = null;
//			List<Preview> previews = previewRepository.findByProductDetailsAndType(productDetails, "video");
//			if (previews.size() == 0) {
//				preview = new Preview();
//			} else {
//				preview = previews.get(0);
//			}
//			preview.setSource(previewVideoLocation + previewVideo.getOriginalFilename());
//			preview.setType("video");
//			preview.setProductDetails(productDetails);
//			previewRepository.save(preview);
////			pd.getPreviews().add(preview);
////			productRepository.save(product);
//			ProductDetailsDTO dto = new ProductDetailsDTO(productDetails, previewRepository);
//			return dto.getPreviewVideo();
//		}
//	}
//
//	private ProductDetails checkVersion(Product product, String version) {
//		ProductDetails pd = null;
//		for (ProductDetails productDetails : product.getProductDetails()) {
//			if (productDetails.getVersion().equalsIgnoreCase(version)) {
//				pd = productDetails;
//			}
//		}
//		if (pd == null) {
//			pd = createProductDetails(product, version);
//		}
//		return pd;
//	}
//
//	@Override
//	public List<Preview> uploadPreviewPicture(MultipartFile previewPicture, Long productId, String version) {
//
//		if (!checkFileType(previewPicture, IMAGE_EXTENSIONS)) {
//
//		} else {
//			Product product = getProductByIdAndSeller(productId);
//			ProductDetails productDetails = checkVersion(product, version);
//
//			String previewPictureLocation = getPreviewsLocation(productDetails);
//			File previewPicturesDir = new File(storageUtil.getLocation() + previewPictureLocation);
//			previewPicturesDir.mkdirs();
//
//			fileStorageService.store(previewPicture, previewPictureLocation);
//			Preview preview = new Preview();
//			preview.setSource(previewPictureLocation + previewPicture.getOriginalFilename());
//			preview.setType("picture");
//			preview.setProductDetails(productDetails);
//			previewRepository.save(preview);
////			product.getPreviews().add(preview);
////			productRepository.save(product);
//			ProductDetailsDTO dto = new ProductDetailsDTO(productDetails, previewRepository);
//			return dto.getPreviewPictures();
//
//		}
//		return null;
//	}
//
//	@Override
//	public List<Preview> deletePreviewPicture(Long previewId) {
//		Preview preview = previewRepository.findById(previewId).get();
//		if (preview == null) {
//			throw new ResourceNotFoundException("Preview picture id", previewId.toString(), "");
//		}
//		previewRepository.delete(preview);
//		List<Preview> previews = previewRepository.findByProductDetailsAndType(preview.getProductDetails(), "picture");
//
//		return previews;
//	}
//
//	@Override
//	public File serveCoverImage(Long productId) {
//		ProductDetails productDetails = productDetailsRepo.findFirstByProductIdOrderByCreatedDateDesc(productId);
//		if (productDetails == null) {
//			throw new ResourceNotFoundException("Product id:", productId.toString(), "");
//		}
//		return serveMediaService.serveImage(storageUtil.getLocation() + productDetails.getCoverImage());
//	}
//
//	@Override
//	public ResponseEntity<ResourceRegion> servePreviewVideo(Long previewId, String rangeHeader) throws IOException {
//		Preview preview = previewRepository.getReferenceById(previewId);
//		if (preview == null) {
//			throw new ResourceNotFoundException("Product id:", previewId.toString(), "");
//		}
//		return serveMediaService.serveVideo(storageUtil.getLocation() + preview.getSource(), rangeHeader);
//	}
//
//	public File servePreviewImage(Long previewId) {
//
//		Preview preview = previewRepository.getReferenceById(previewId);
//		if (preview == null) {
//			throw new ResourceNotFoundException("Preview", "previewId", previewId);
//		}
//		return serveMediaService.serveImage(storageUtil.getLocation() + preview.getSource());
//	}
//
//	private ProductDetails createProductDetails(Product product, String version) {
//		ProductDetails productDetails = new ProductDetails();
//		productDetails.setProduct(product);
//		productDetails.setVersion(version);
//		productDetails.setCreatedDate(new Date());
//		productDetails.setLastModified(null);
//		productDetails = productDetailsRepo.save(productDetails);
//		String coverImageDestination = getCoverImageLocation(productDetails);
//		String filesDestination = getProductFilesLocation(productDetails);
//		String previewsDestinations = getPreviewsLocation(productDetails);
//		File folder = new File(storageUtil.getLocation() + coverImageDestination);
//		folder.mkdirs();
//		folder = new File(storageUtil.getLocation() + filesDestination);
//		folder.mkdirs();
//		folder = new File(storageUtil.getLocation() + previewsDestinations);
//		folder.mkdirs();
//		return productDetails;
//	}
//
//	private Product getProductByIdAndSeller(Long productId) {
//		Seller seller = getCurrentSeller();
//		Product product = productRepository.findBySellerAndId(seller, productId);
//
//		if (product == null || !product.isEnabled()) {
//			throw new ResourceNotFoundException("product id:" + productId, "seller id", seller.getId());
//		}
//		return product;
//	}
//
//	private ProductDetails getProductDetailsByProductAndVersion(ProductDetailsDTO productDetailsDTO) {
//		Seller seller = getCurrentSeller();
//		ProductDetails ret = productDetailsRepo.findByProductIdAndProductVersionKeyVersion(productDetailsDTO.getId(),
//				productDetailsDTO.getVersion());
//		return ret;
//	}
//
//	private ProductDetails getLastestVersionProductDetailsByIdAndSeller(Long productId) {
//		Seller seller = getCurrentSeller();
//		ProductDetails productDetails = productDetailsRepo.findFirstByProductIdOrderByCreatedDateDesc(productId);
//		if (productDetails == null || !productDetails.getProduct().isEnabled()) {
//			throw new ResourceNotFoundException("product id:" + productId, "seller id", seller.getId());
//		}
//		return productDetails;
//	}
//
//	private String getCoverImageLocation(ProductDetails productDetails) {
//		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
//				+ productDetails.getProduct().getId() + "\\" + productDetails.getVersion() + "\\"
//				+ PRODUCT_COVER_IMAGE_FOLDER_NAME + "\\";
//	}
//
//	private String getProductFilesLocation(ProductDetails productDetails) {
//		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
//				+ productDetails.getProduct().getId() + "\\" + productDetails.getVersion() + "\\"
//				+ PRODUCT_FILES_FOLDER_NAME + "\\";
//	}
//
//	private String getPreviewsLocation(ProductDetails productDetails) {
//		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
//				+ productDetails.getProduct().getId() + "\\" + productDetails.getVersion() + "\\"
//				+ PRODUCT_PREVIEWS_FOLDER_NAME + "\\";
//	}
//
//	private String getSellerProductsDataLocation(Seller seller) {
//		return "account_id_" + seller.getId() + "\\" + PRODUCT_FOLDER_NAME;
//	}
//
//	private boolean checkFileType(MultipartFile file, String... extensions) {
//		for (String extension : extensions) {
//			if (file.getContentType().endsWith(extension)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	private Seller getCurrentSeller() {
//		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
//				.getAccount();
//		Seller seller = sellerRepository.findById(account.getId()).get();
//		if (seller == null) {
//			throw new ResourceNotFoundException("Seller", "seller_id", account.getId());
//		}
//		File productsDatafolder = new File(storageUtil.getLocation() + getSellerProductsDataLocation(seller));
//		productsDatafolder.mkdirs();
//		return seller;
//	}
//
//	@Override
//	public List<Category> getCategoryList() {
//		List<Category> typeList = categoryRepository.findAll();
//		return typeList;
//	}
//
//	@Override
//	public List<Tag> getTagList() {
//		List<Tag> tagList = tagRepository.findAll();
//		return tagList;
//	}
//
//	@Override
//	public Product createNewProductDetails(Long productId, String version) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}
