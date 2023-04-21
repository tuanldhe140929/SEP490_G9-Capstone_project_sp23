package com.SEP490_G9.service.serviceImpls;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.common.Constant;
import com.SEP490_G9.common.Md5Hash;
import com.SEP490_G9.dto.ProductFileDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductFile;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.entities.ProductDetails.Status;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.exception.InternalServerException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.ProductFileRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.service.CartService;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductFileService;
import com.SEP490_G9.service.ProductService;
import com.SEP490_G9.service.VirusTotalService;

import jakarta.transaction.Transactional;

@Service
public class ProductFileServiceImpl implements ProductFileService {
	final String PRODUCT_FOLDER_NAME = "products";
	@Value("${root.location}")
	private String ROOT_LOCATION;

	@Autowired
	ProductFileRepository productFileRepo;

	@Autowired
	ProductDetailsService productDetailsService;

	@Autowired
	FileIOService fileIOService;

	@Autowired
	TransactionRepository transactionRepo;

	@Autowired
	ProductRepository productRepo;

	@Autowired
	Md5Hash downloadTokenUtil;

	@Autowired
	VirusTotalService virusTotalApi;

	@Autowired
	CartService cartService;

	@Override
	public ProductFile createProductFile(ProductFile productFile) {
		return productFileRepo.save(productFile);
	}

	@Override
	public List<ProductFile> createProductFiles(List<ProductFile> productFiles) {
		List<ProductFile> ret = null;
		if (productFiles.size() >= 0) {
			for (ProductFile productFile : productFiles) {
				if (productFileRepo.existsByNameAndProductDetails(productFile.getName(),
						productFile.getProductDetails())) {
					throw new DuplicateFieldException("file name", productFile.getName());
				}
			}
			ret = productFileRepo.saveAll(productFiles);
		}
		return ret;
	}

	@Override
	public ProductFile getById(Long id) {
		ProductFile ret = productFileRepo.findById(id).get();
		if (ret == null) {
			throw new ResourceNotFoundException("product file", "id", id);
		}
		return ret;
	}

	@Transactional
	@Override
	public ProductFile deleteById(Long fileId) {
		if (!productFileRepo.existsById(fileId)) {
			throw new ResourceNotFoundException("product file", "id", fileId);
		}

		ProductFile pf = getById(fileId);
		ProductDetails pd = pf.getProductDetails();
		if (pd.getApproved() != Status.NEW) {
			throw new IllegalArgumentException("Cannot edit this version");
		}
		if (pf.isNewUploaded()) {
			pf.setEnabled(false);
			pf.setNewUploaded(true);
			pf.setReviewed(false);
		} else {
			pf.setEnabled(false);
			pf.setNewUploaded(false);
			pf.setReviewed(false);
		}
		productFileRepo.save(pf);
		return pf;
	}

	@Override
	public List<ProductFile> getAllFileByIdAndVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductFileDTO uploadFile(Long productId, String version, MultipartFile productFile) {
		ProductFileDTO ret = new ProductFileDTO();
		ret.setId((long) -1);
		ret.setSize(productFile.getSize());
		ret.setName(productFile.getOriginalFilename());
		ret.setFileState(ProductFileDTO.FileState.UPLOADING);

		ProductDetails productDetails = productDetailsService.getByProductIdAndVersion(productId, version);
		if (productDetails == null) {
			throw new IllegalArgumentException("version not found");
		}
		if (productDetails.getApproved() != Status.NEW) {
			throw new IllegalArgumentException("Cannot edit this version");
		}
		if (productFile.getOriginalFilename().length() > 100) {
			throw new FileUploadException("File name too long");
		}
		if (productFile.getSize() == 0 || productFile.getSize() > 500 * 1024 * 1024) {
			throw new FileUploadException("File size:" + productFile.getSize());
		}
		int fileCount = 0;
		for (ProductFile file : productDetails.getFiles()) {
			if (file.isEnabled() == true) {
				fileCount++;
			}
		}
		if ((fileCount + 1) > 10) {
			throw new FileUploadException("Exeeded max file count");
		}

		long totalSize = 0;
		for (ProductFile file : productDetails.getFiles()) {
			totalSize += file.getSize();
		}
		totalSize += productFile.getSize();
		if (totalSize >= 2000 * 1024 * 1024) {
			throw new FileUploadException("Exeeded max storage for version");
		}

		if (productFile.getOriginalFilename().endsWith(".zip") || productFile.getOriginalFilename().endsWith(".rar")) {
			if (fileHasPassword(productFile)) {
				System.out.println("compressed");
				throw new FileUploadException("Compressed file cannot has password");
			}
		}

		Path tempFilePath = createTempFile(productFile);

		boolean isSafe = false;
		File file = new File(tempFilePath.toString());
//		try {
//			isSafe = virusTotalApi.scanFile(file);
//			System.out.println("zxczxc");
//		} catch (IOException e1) {
//			throw new FileUploadException("Cannot scan file");
//		} catch (InterruptedException e) {
//			throw new FileUploadException("Cannot scan file");
//		}
		if (true) {
			try {

				Files.deleteIfExists(tempFilePath);
			} catch (IOException e) {
				throw new FileUploadException("Error at server");
			}
			String fileLocation = getProductFilesLocation(productDetails);
			File fileDir = new File(ROOT_LOCATION + fileLocation);
			fileDir.mkdirs();
			String storedPath = fileIOService.storeV2(productFile, ROOT_LOCATION + fileLocation);
			System.out.println(storedPath);
			ProductFile pf = new ProductFile(storedPath.replace(ROOT_LOCATION, ""), productFile, productDetails);
			pf.setEnabled(true);
			pf.setNewUploaded(true);
			pf.setReviewed(false);
			pf.setCreatedDate(new Date());
			pf.setLastModified(new Date());
			System.out.println(pf.getName());
			ProductFile savedFile = createProductFile(pf);
			ret = new ProductFileDTO(savedFile);
		} else {
			try {
				Files.deleteIfExists(tempFilePath);
			} catch (IOException e) {
				throw new FileUploadException("Error at server");
			}
			ret.setFileState(ProductFileDTO.FileState.MALICIOUS);
		}
		return ret;
	}

	private boolean fileHasPassword(MultipartFile productFile) {
		char[] password = "password".toCharArray(); // replace with the password to test
		try (InputStream fis = productFile.getInputStream();
				ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP,
						fis)) {
			ArchiveEntry entry;
			while ((entry = ais.getNextEntry()) != null) {
				// do nothing, just read through the archive
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new InternalServerException("Error occurred when trying to extract compressed file", e);
		} catch (ArchiveException e) {
			if (e.getCause() instanceof ZipException && e.getCause().getMessage().toLowerCase().contains("password")) {
				return true; // password-protected file
			}
		}
		return false; // file is not password-protected
	}

	private Path createTempFile(MultipartFile productFile) {
		InputStream is = null;
		OutputStream os = null;
		Path tempFilePath = null;
		try {
			new File(ROOT_LOCATION + "/" + "temp").mkdirs();
			String fileName = UUID.randomUUID().toString();
			tempFilePath = Paths.get(this.ROOT_LOCATION + "/temp/" + fileName);
			Files.createDirectories(tempFilePath.getParent());
			Files.createFile(tempFilePath);

			is = productFile.getInputStream();
			os = new FileOutputStream(this.ROOT_LOCATION + "/temp/" + fileName);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}

		} catch (Exception e) {
			throw new FileUploadException("Error at server");
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				throw new FileUploadException("Error at server");
			}

		}
		return tempFilePath;
	}

	private String getProductFilesLocation(ProductDetails productDetails) {
//		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
//				+ productDetails.getProduct().getId() + "\\" + productDetails.getVersion() + "\\"
//				+ PRODUCT_FILES_FOLDER_NAME + "\\";
		return getSellerProductsDataLocation(productDetails.getProduct().getSeller()) + "\\"
				+ productDetails.getProduct().getId() + "\\";
	}

	private String getSellerProductsDataLocation(Seller seller) {
		return "account_id_" + seller.getId() + "\\" + PRODUCT_FOLDER_NAME;
	}

	@Override
	public ByteArrayResource downloadFile(Long userId, Long productId, String token) {

		if (token.isEmpty()) {
			Collection<? extends GrantedAuthority> auth = SecurityContextHolder.getContext().getAuthentication()
					.getAuthorities();

			if (!auth.contains(new SimpleGrantedAuthority("ROLE_STAFF"))) {
				System.out.println("checkin");
				throw new IllegalAccessError("You don't have right to download this product");
			}
		} else {
			if (!downloadTokenUtil.validateToken(userId, productId, token)) {
				throw new IllegalAccessError("false token");
			}
		}
		List<File> productFiles = new ArrayList<>();
		ProductDetails activeVersion = null;
		Product p = productRepo.findById(productId).orElseThrow();

		for (ProductDetails pd : p.getProductDetails()) {
			if (pd.getVersion().equalsIgnoreCase(p.getActiveVersion())) {
				activeVersion = pd;
				break;
			}
		}
		List<ProductFile> pfs = activeVersion.getFiles();
		for (ProductFile pf : pfs) {
			if ((pf.isEnabled() && !pf.isNewUploaded() && pf.isReviewed())
					|| (!pf.isEnabled() && !pf.isNewUploaded() && !pf.isReviewed()))
				productFiles.add(new File(ROOT_LOCATION + pf.getSource()));
		}

		// Compress the files into a zip archive
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		try {
			ZipEntry zipEntry = new ZipEntry(activeVersion.getName());
			zos.putNextEntry(zipEntry);
			for (File file : productFiles) {
				ZipEntry entry = new ZipEntry(file.getName());
				zos.putNextEntry(entry);
				FileInputStream fis = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				fis.close();
				zos.closeEntry();
			}
		} catch (IOException e) {

		} finally {
			try {
				zos.close();
				baos.close();
			} catch (IOException e) {

			}
		}
		ByteArrayResource resource = new ByteArrayResource(baos.toByteArray()) {
			@Override
			public String getFilename() {
				return "product.zip";
			}
		};
		return resource;
	}

	boolean isStaff(Account account) {
		boolean ret = false;
		for (Role role : account.getRoles()) {
			if (role.getId() == Constant.STAFF_ROLE_ID) {
				ret = true;
			}
		}
		return ret;
	}

	@Override
	public String generateDownloadToken(Long userId, Long productId) {
		String token = "";
		boolean isPurchased = cartService.isUserPurchasedProduct(userId, productId);
		boolean productIsFree = false;
		Product p = productRepo.findById(productId).orElseThrow();
		for (ProductDetails pd : p.getProductDetails()) {
			if (pd.getVersion().equalsIgnoreCase(p.getActiveVersion())) {
				if (pd.getPrice() == 0) {
					productIsFree = true;
				}
			}
		}
		if (isPurchased || productIsFree) {
			token = downloadTokenUtil.generateToken(userId, productId);
		} else {
			throw new IllegalAccessError("You don't have right to download this product");
		}

		return token;
	}

	public String getROOT_LOCATION() {
		return ROOT_LOCATION;
	}

	public void setROOT_LOCATION(String rOOT_LOCATION) {
		ROOT_LOCATION = rOOT_LOCATION;
	}
}
