package com.SEP490_G9.dto;

import java.util.ArrayList;
import java.util.List;

import com.SEP490_G9.entity.Product;
import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.entity.Seller;
import com.SEP490_G9.repository.PreviewRepository;

public class ProductDTO {

	private Long id;

	private boolean draft = true;

	private boolean enabled = true;

	private Seller seller;

	private List<ProductDetailsDTO> productDetails = new ArrayList<>();

	public ProductDTO(Product product, PreviewRepository previewRepository) {
		this.id = product.getId();
		this.draft = product.isDraft();
		this.enabled = product.isEnabled();
		this.seller = product.getSeller();
		if (product.getProductDetails()!=null & product.getProductDetails().size() >0) {
			for (ProductDetails pd : product.getProductDetails()) {
				this.productDetails.add(new ProductDetailsDTO(pd, previewRepository));
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public List<ProductDetailsDTO> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetailsDTO> productDetails) {
		this.productDetails = productDetails;
	}

}
