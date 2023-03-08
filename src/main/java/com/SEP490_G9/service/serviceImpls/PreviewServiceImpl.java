package com.SEP490_G9.service.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entity.Preview;
import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.PreviewRepository;
import com.SEP490_G9.service.PreviewService;

@Service
public class PreviewServiceImpl implements PreviewService {

	@Autowired
	PreviewRepository previewRepository;

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
		if(!previewRepository.existsById(preview.getId())) {
			throw new ResourceNotFoundException("Preview video id", preview.getId().toString(), "");
		}
		previewRepository.deleteById(preview.getId());
		return false;
	}

	@Override
	public List<Preview> createPreviews(List<Preview> previews) {
		List<Preview> ret = null;
		if(previews.size()>0) {
			ret = previewRepository.saveAll(previews);
		}
		return ret;
	}

	@Override
	public Preview createPreview(Preview preview) {
		return previewRepository.save(preview);
	}

}
