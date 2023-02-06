package com.SEP490_G9.services.serviceImpls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.FileUtils;

import com.SEP490_G9.models.Entities.Preview;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.ProductFile;
import com.SEP490_G9.models.Entities.Tag;
import com.SEP490_G9.models.Entities.Type;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.exceptions.DuplicateFieldException;
import com.SEP490_G9.exceptions.FileFormatNotAccept;
import com.SEP490_G9.exceptions.ResourceNotFoundException;
import com.SEP490_G9.helpers.StorageProperties;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.models.DTOS.ProductDTO;
import com.SEP490_G9.repositories.PreviewRepository;
import com.SEP490_G9.repositories.ProductFileRepository;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.repositories.TagRepository;
import com.SEP490_G9.repositories.TypeRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.FileStorageService;
import com.SEP490_G9.services.ManageProductService;

import jakarta.transaction.Transactional;

@Service
public class ManageProductServiceImpl implements ManageProductService {
	final String PRODUCT_FOLDER_NAME = "products";
	final String PRODUCT_COVER_IMAGE_FOLDER_NAME = "coverImage";
	final String PRODUCT_FILES_FOLDER_NAME = "files";
	final String PRODUCT_PREVIEWS_FOLDER_NAME = "previews";
	final String PRODUCT_INSTRUCTION_FILE_NAME = "instruction.txt";

	final String[] VIDEO_EXTENSIONS = { "video/mp4", "video/x-matroska", "video/quicktime" };
	final String[] IMAGE_EXTENSIONS = { "image/png", "image/jpeg", "image/svg+xml" };
	@Autowired
	StorageProperties storageProperties;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	TypeRepository typeRepository;

	@Autowired
	TagRepository tagRepository;

	@Autowired
	ProductFileRepository productFileRepository;

	@Autowired
	PreviewRepository previewRepository;

	@Autowired
	private ServeMediaService serveMediaService;

	@Override
	public User getCurrentUserInfo() {
		User user = userRepository.getReferenceById((long) 1);
		return user;
	}

	@Override
	public List<Type> getTypeList() {
		List<Type> typeList = typeRepository.findAll();
		return typeList;
	}

	@Override
	public List<Tag> getTagList() {
		List<Tag> tagList = tagRepository.findAll();
		return tagList;
	}

	@Override
	public ProductDTO getProductDTOByIdAndUser(Long productId) throws IOException {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Product product = productRepository.getReferenceById(productId);

		if (product == null) {
			throw new ResourceNotFoundException("product id:" + productId, "user id", user.getId());
		}
		ProductDTO productDTO = new ProductDTO(product, previewRepository);
		File file = new File(storageProperties.getLocation() + product.getInstructionSource());
		if (file.exists()) {
			String instruction = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
			productDTO.setInstruction(instruction);
		}
		return productDTO;
	}

	private Product getProductByIdAndUser(Long productId) {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Product product = productRepository.getReferenceById(productId);

		if (product == null || !product.isActive()) {
			throw new ResourceNotFoundException("product id:" + productId, "user id", user.getId());
		}
		return product;
	}

	@Override
	public Product createNewProduct() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Product product = new Product();
		product.setUser(user);
		Product createdProduct = productRepository.save(product);
		String coverImageDestination = user.getUsername() + "\\products\\" + createdProduct.getId() + "\\coverImage\\";
		String filesDestination = user.getUsername() + "\\products\\" + createdProduct.getId() + "\\files\\";
		File folder = new File(storageProperties.getLocation() + coverImageDestination);
		folder.mkdir();
		folder = new File(storageProperties.getLocation() + filesDestination);
		folder.mkdir();
		return product;
	}

	@Override
	public Product addProduct(Product product) {
		Product ret = null;
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		product.setUser(user);
		try {
			ret = productRepository.save(product);
		} catch (Exception e) {
			// throw new CustomException("Them san pham khong thanh cong");
		}
		return ret;
	}

	@Override
	public ProductDTO updateProduct(Product product, String instruction) throws IOException {

		ProductDTO ret = null;
		try {

			Product found = getProductByIdAndUser(product.getId());
			String instructionFileDir = getProductFilesLocation(found) + PRODUCT_INSTRUCTION_FILE_NAME;

			FileOutputStream fos = new FileOutputStream(storageProperties.getLocation() + instructionFileDir);
			fos.write(instruction.getBytes());
			fos.flush();
			fos.close();
			found.setDraft(product.isDraft());
			found.setName(product.getName());
			found.setDetails(product.getDetails());
			found.setPrice(product.getPrice());
			found.setDescription(product.getDescription());
			found.setTags(product.getTags());
			found.setType(product.getType());
			found.setLastUpdate(new Date());
			found.setInstructionSource(instructionFileDir);
			productRepository.save(found);
			ret = new ProductDTO(found, previewRepository);
		} catch (Exception e) {
			System.out.println(e);
		}
		return ret;
	}

	@Override
	public boolean deleteProduct(Long id) {
		Product ret = null;
		try {
			Product found = getProductByIdAndUser(id);
			found.setActive(false);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<Product> getProductsByUser() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		return productRepository.findByUser(user);
	}

	@Override
	public String uploadCoverImage(MultipartFile coverImage, Long productId) throws IOException {

		if (!checkFileType(coverImage, IMAGE_EXTENSIONS)) {
			throw new FileFormatNotAccept(coverImage.getContentType() + " file not accept");
		} else {
			Product product = getProductByIdAndUser(productId);
			String coverImageLocation = getCoverImageLocation(product);
			File coverImageDir = new File(storageProperties.getLocation() + coverImageLocation);
			coverImageDir.mkdirs();
			fileStorageService.store(coverImage, coverImageLocation);
			product.setCoverImage(coverImageLocation + coverImage.getOriginalFilename());
			productRepository.save(product);
			return product.getCoverImage();
		}
	}

	private boolean checkFileType(MultipartFile file, String... extensions) {
		for (String extension : extensions) {
			if (file.getContentType().endsWith(extension)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Preview uploadPreviewVideo(MultipartFile previewVideo, Long productId) {

		if (!checkFileType(previewVideo, VIDEO_EXTENSIONS)) {
			throw new FileFormatNotAccept(previewVideo.getContentType() + " file not accept");
		} else {
			Product product = getProductByIdAndUser(productId);
			String previewVideoLocation = getPreviewsLocation(product);
			File previewVideoDir = new File(storageProperties.getLocation() + previewVideoLocation);
			previewVideoDir.mkdirs();

			fileStorageService.store(previewVideo, previewVideoLocation);
			Preview preview = null;
			List<Preview> previews = previewRepository.findByProductAndType(product, "video");
			if (previews.size() == 0) {
				preview = new Preview();
			} else {
				preview = previews.get(0);
			}
			preview.setSource(previewVideoLocation + previewVideo.getOriginalFilename());
			preview.setType("video");
			preview.setProduct(product);
			previewRepository.save(preview);
			product.getPreviews().add(preview);
			productRepository.save(product);
			ProductDTO dto = new ProductDTO(product, previewRepository);
			return dto.getPreviewVideo();
		}
	}

	@Transactional
	@Override
	public ProductDTO uploadProductFile(Long productId, MultipartFile productFile) throws IOException {
		Product product = getProductByIdAndUser(productId);
		String fileLocation = getProductFilesLocation(product);
		ProductFile file = new ProductFile(productFile, fileLocation, product);
		if (productFileRepository.existsByName(file.getName())) {
			throw new DuplicateFieldException("file name", file.getName());
		}
		File fileDir = new File(storageProperties.getLocation() + fileLocation);
		fileDir.mkdirs();
		fileStorageService.store(productFile, fileLocation);
		productFileRepository.save(file);
		product.getFiles().add(file);
		productRepository.save(product);
		ProductDTO dto = new ProductDTO(product, previewRepository);
		return dto;
	}

	@Transactional
	@Override
	public ProductDTO deleteProductFile(Long productId, Long fileId) throws IOException {
		Product product = getProductByIdAndUser(productId);

		ProductFile productFile = productFileRepository.getReferenceById(fileId);
		if (productFile == null) {
			throw new ResourceNotFoundException("file", productId.toString(), "");
		}
		product.getFiles().remove(productFile);
		productFileRepository.deleteById(fileId);
		File fileDir = new File(storageProperties.getLocation() + productFile.getSource());
		fileDir.delete();
		ProductDTO dto = new ProductDTO(product,previewRepository);
		return dto;
	}

	private String getCoverImageLocation(Product product) {
		return getUserProductsDataLocation(product.getUser()) + "\\" + product.getId() + "\\"
				+ PRODUCT_COVER_IMAGE_FOLDER_NAME + "\\";
	}

	private String getProductFilesLocation(Product product) {
		return getUserProductsDataLocation(product.getUser()) + "\\" + product.getId() + "\\"
				+ PRODUCT_FILES_FOLDER_NAME + "\\";
	}

	private String getPreviewsLocation(Product product) {
		return getUserProductsDataLocation(product.getUser()) + "\\" + product.getId() + "\\"
				+ PRODUCT_PREVIEWS_FOLDER_NAME + "\\";
	}

	private String getUserProductsDataLocation(User user) {
		return user.getUsername() + "\\" + PRODUCT_FOLDER_NAME;
	}

	@Override
	public File serveCoverImage(Long productId) {
		Product product = productRepository.getReferenceById(productId);

		if (product == null) {
			throw new ResourceNotFoundException("Product id:", productId.toString(), "");
		}
		return serveMediaService.serveImage(storageProperties.getLocation() + product.getCoverImage());
	}

	@Override
	public ResponseEntity<ResourceRegion> servePreviewVideo(Long previewId, String rangeHeader) throws IOException {
		Preview preview = previewRepository.getReferenceById(previewId);
		if (preview == null) {
			throw new ResourceNotFoundException("Product id:", previewId.toString(), "");
		}
		return serveMediaService.serveVideo(storageProperties.getLocation() + preview.getSource(), rangeHeader);
	}

	@Override
	public boolean deleteProductPreviewVideo(Long productId) {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Product product = productRepository.findByUserAndId(user, productId);
		if (product == null) {
			throw new ResourceNotFoundException("Product id:", productId.toString(), "");
		}
		List<Preview> preview = previewRepository.findByProductAndType(product, "video");
		if (preview == null) {
			throw new ResourceNotFoundException("Preview video for product id:", productId.toString(), "");
		}
		File fileDir = new File(storageProperties.getLocation() + preview.get(0).getSource());
		fileDir.delete();
		previewRepository.delete(preview.get(0));
		return true;
	}

	@Override
	public List<Preview> uploadPreviewPicture(MultipartFile previewPicture, Long productId) {

		if (!checkFileType(previewPicture, IMAGE_EXTENSIONS)) {

		} else {
			Product product = getProductByIdAndUser(productId);
			String previewPictureLocation = getPreviewsLocation(product);
			File previewPicturesDir = new File(storageProperties.getLocation() + previewPictureLocation);
			previewPicturesDir.mkdirs();

			fileStorageService.store(previewPicture, previewPictureLocation);
			Preview preview = new Preview();
			preview.setSource(previewPictureLocation + previewPicture.getOriginalFilename());
			preview.setType("picture");
			preview.setProduct(product);
			previewRepository.save(preview);
			product.getPreviews().add(preview);
			productRepository.save(product);
			ProductDTO dto = new ProductDTO(product, previewRepository);
			return dto.getPreviewPictures();

		}
		return null;
	}

	@Override
	public File servePreviewImage(Long previewId) {
		Preview preview = previewRepository.getReferenceById(previewId);
		if(preview==null) {
			throw new ResourceNotFoundException("Preview", "previewId", previewId);
		}
		return serveMediaService.serveImage(storageProperties.getLocation()+preview.getSource());
	}

	@Override
	public List<Preview> deletePreviewPicture(Long previewId) {
		Preview preview = previewRepository.getReferenceById(previewId);
		previewRepository.delete(preview);
		List<Preview> previews = previewRepository.findByProductAndType(preview.getProduct(), "picture");
		
		return previews;
	}

}
