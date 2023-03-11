package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entities.Preview;
import com.SEP490_G9.entities.ProductDetails;

public interface PreviewService {
	List<Preview> getByProductDetailsAndType(ProductDetails productDetails, String type);

	boolean deletePreview(Preview preview);

	Preview getById(Long previewId);

	boolean deleteById(Long previewId);

	List<Preview> createPreviews(List<Preview> previews);

	Preview createPreview(Preview preview);
}
