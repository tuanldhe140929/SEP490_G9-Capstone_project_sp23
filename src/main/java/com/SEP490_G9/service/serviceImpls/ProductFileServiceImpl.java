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
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.common.Md5Hash;
import com.SEP490_G9.dto.ProductFileDTO;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductFile;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.ProductFileRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductFileService;
import com.SEP490_G9.service.ProductService;

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

	@Override
	public ProductFile createProductFile(ProductFile productFile) {
		if (productFileRepo.existsByNameAndProductDetails(productFile.getName(), productFile.getProductDetails())) {
			throw new DuplicateFieldException("file name", productFile.getName());
		}

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
	public ProductFileDTO deleteById(Long fileId) {
		if (!productFileRepo.existsById(fileId)) {
			throw new ResourceNotFoundException("product file", "id", fileId);
		}

		ProductFile pf = getById(fileId);
		String version = pf.getProductDetails().getVersion();
		long id = pf.getProductDetails().getProduct().getId();
		ProductDetails pd = productDetailsService.getByProductIdAndVersion(id, version);
		pd.getFiles().remove(pf);
		ProductFileDTO dto = new ProductFileDTO(pf, false);

		productFileRepo.deleteById(fileId);

		if (pd.getFiles().size() <= 0) {
			pd.setDraft(true);
			dto.setLastFile(true);
			productDetailsService.updateProductDetails(pd);
		}
		return dto;
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

		if (productFile.getSize() == 0) {
			throw new FileUploadException("File size:" + productFile.getSize());
		}
		for (ProductFile file : productDetails.getFiles()) {
			if (file.getName().equalsIgnoreCase(productFile.getName())) {
				throw new FileUploadException("File exist:" + file.getName());
			}
		}
		if ((productDetails.getFiles().size() + 1) >= 10) {
			throw new FileUploadException("Exeeded max file count");
		}

		Path tempFilePath = createTempFile(productFile);

		boolean isSafe;
		File file = new File(tempFilePath.toString());
//		isSafe = VirusTotalAPI.scanFile(file);
//		System.out.println("isSafe?" + isSafe);

//		if (isSafe) {
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
			ProductFile pf = new ProductFile(storedPath.replace(ROOT_LOCATION, ""), productFile, productDetails);
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

	private Path createTempFile(MultipartFile productFile) {
		InputStream is = null;
		OutputStream os = null;
		Path tempFilePath = null;
		try {
			new File(ROOT_LOCATION + "\\" + "temp").mkdirs();
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
		System.out.println("download");
		if (!downloadTokenUtil.validateToken(userId, productId, token)) {
			System.out.println("not pass");
		}
		System.out.println("pass");

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
			System.out.println(pf.getSource());
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
		ByteArrayResource resource = new ByteArrayResource(baos.toByteArray()){
		    @Override
		    public String getFilename() {
		        return "product.zip";
		    }
		};
		return resource;
	}

	@Override
	public String generateDownloadToken(Long userId, Long productId) {
		String token = "";
		boolean isPurchased = checkIfPurchased(userId, productId);
		if (isPurchased) {
			token = downloadTokenUtil.generateToken(userId, productId);
		}
		return token;
	}

	private boolean checkIfPurchased(Long userId, Long productId) {
//		List<Transaction> transactions = transactionRepo.findByUserIdAndStatus(userId,"purchased");
//		for(Transaction transaction:transactions) {
//			for(CartItem item: transaction.getCart().getItems()) {
//				if(item.getCartItemKey().getProductVersionKey().getProductId() == productId) {
//					return true;
//				}
//			}
//		}
		return true;
	}
}
