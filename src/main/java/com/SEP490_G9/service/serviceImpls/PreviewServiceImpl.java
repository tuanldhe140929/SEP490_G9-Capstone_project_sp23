package com.SEP490_G9.service.serviceImpls;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.dto.PreviewDTO;
import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entities.Preview;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.PreviewRepository;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.PreviewService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductService;

@Service
public class PreviewServiceImpl implements PreviewService {

	private String ROOT_LOCATION = "C:/Users/ADMN/eclipse-workspace/SEP490_G9_Datas/";
	final String PRODUCT_PREVIEWS_FOLDER_NAME = "previews";
	final String[] VIDEO_EXTENSIONS = { "video/mp4", "video/x-matroska", "video/quicktime" };
	final String[] IMAGE_EXTENSIONS = { "image/png", "image/jpeg", "image/svg+xml" };
	final String PRODUCT_FOLDER_NAME = "products";

	PreviewRepository previewRepository;

	ProductDetailsRepository productDetailsRepo;

	FileIOService fileIOService;

	ProductRepository productRepo;

	@Autowired
	public PreviewServiceImpl(FileIOService fileIOService, PreviewRepository previewRepository,
			ProductDetailsRepository productDetailsRepo, ProductRepository productRepo,
			@Value("${root.location}") String ROOT_LOCATION) {
		this.fileIOService = fileIOService;
		this.previewRepository = previewRepository;
		this.productDetailsRepo = productDetailsRepo;
		this.productRepo = productRepo;
		this.ROOT_LOCATION = "C:/Users/ADMN/eclipse-workspace/SEP490_G9_Datas/";
	}

	@Override
	public List<Preview> getByProductDetailsAndType(ProductDetails pd, String type) {
		List<Preview> ret = previewRepository.findByProductDetailsAndType(pd, type);
		if (ret == null) {
			throw new ResourceNotFoundException("preview video product id, version ",
					pd.getProduct().getId() + " " + pd.getVersion(), "");
		}
		return ret;
	}

	@Override
	public boolean deleteById(Long previewId) {
		if (!previewRepository.existsById(previewId)) {
			throw new ResourceNotFoundException("preview id ", "id", previewId);
		}
		previewRepository.deleteById(previewId);
		return true;
	}

	@Override
	public Preview getById(Long previewId) {
		Preview preview = previewRepository.findById(previewId).get();
		if (preview == null) {
			throw new ResourceNotFoundException("Preview picture id", previewId.toString(), "");
		}
		return preview;
	}

	@Override
	public boolean deletePreview(Preview preview) {
		if (!previewRepository.existsById(preview.getId())) {
			throw new ResourceNotFoundException("Preview video id", preview.getId().toString(), "");
		}
		previewRepository.deleteById(preview.getId());
		return false;
	}

	@Override
	public List<Preview> createPreviews(List<Preview> previews) {
		List<Preview> ret = null;
		if (previews.size() > 0) {
			ret = previewRepository.saveAll(previews);
		}
		return ret;
	}

	@Override
	public Preview createPreview(Preview preview) {

		return previewRepository.save(preview);
	}

	@Override
	public boolean deleteVideoPreview(Long productId, String version) {
		ProductDetails pd = productDetailsRepo.findByProductIdAndProductVersionKeyVersion(productId, version);
		Preview preview = getByProductDetailsAndType(pd, "video").get(0);
		deleteById(preview.getId());
		return true;
	}

	@Override
	public List<PreviewDTO> uploadPreviewPicture(Long productId, String version, MultipartFile previewPicture) {
		List<PreviewDTO> ret = null;
		if (!checkFileType(previewPicture, IMAGE_EXTENSIONS)) {
			throw new FileUploadException(previewPicture.getContentType() + " file not accept");
		} else {
			Preview preview = new Preview();
			ProductDetails productDetails = productDetailsRepo.findByProductIdAndProductVersionKeyVersion(productId,
					version);

			String previewPictureLocation = getPreviewsLocation(productDetails);
			File previewPicturesDir = new File(ROOT_LOCATION + previewPictureLocation);
			previewPicturesDir.mkdirs();

			String storedPath = fileIOService.storeV2(previewPicture, ROOT_LOCATION + previewPictureLocation);
			preview.setSource(storedPath.replace(ROOT_LOCATION, ""));
			preview.setType("picture");
			preview.setProductDetails(productDetails);
			preview = createPreview(preview);
			ProductDetailsDTO dto = new ProductDetailsDTO(productDetails);
			ret = dto.getPreviewPictures();
		}
		return ret;
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
	public Preview uploadPreviewVideo(Long productId, String version, MultipartFile previewVideo) {
		Preview preview = null;
		if (!checkFileType(previewVideo, VIDEO_EXTENSIONS)) {
			throw new FileUploadException(previewVideo.getContentType() + " file not accept");
		} else {
			Product product = productRepo.findById(productId).orElseThrow();

			ProductDetails productDetails = productDetailsRepo.findByProductIdAndProductVersionKeyVersion(productId,
					version);

			String previewVideoLocation = getPreviewsLocation(productDetails);
			File previewVideoDir = new File("C:/Users/ADMN/eclipse-workspace/SEP490_G9_Datas/" + previewVideoLocation);
			previewVideoDir.mkdirs();

			String savedPath = fileIOService.storeV2(previewVideo,
					"C:/Users/ADMN/eclipse-workspace/SEP490_G9_Datas/" + previewVideoLocation);

			preview = new Preview();
//			preview.setSource(previewVideoLocation + previewVideo.getOriginalFilename());
			preview.setSource(savedPath.replace("C:/Users/ADMN/eclipse-workspace/SEP490_G9_Datas/", ""));
			preview.setType("video");
			preview.setProductDetails(productDetails);
			return previewRepository.save(preview);

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
