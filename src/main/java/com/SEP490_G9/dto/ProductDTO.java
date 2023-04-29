package com.SEP490_G9.dto;

import java.util.ArrayList;
import java.util.List;

import com.SEP490_G9.entities.License;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.repository.PreviewRepository;

public class ProductDTO {

	private Long id;

	private boolean enabled = true;

	private Seller seller;

	private List<ProductDetailsDTO> productDetails = new ArrayList<>();

	private License license;

	private boolean draft;

	public ProductDTO(Product product) {
		this.id = product.getId();
		this.enabled = product.isEnabled();
		this.seller = product.getSeller();
		this.draft = product.isDraft();
		if (product.getProductDetails() != null & product.getProductDetails().size() > 0) {
			for (ProductDetails pd : product.getProductDetails()) {
				this.productDetails.add(new ProductDetailsDTO(pd));
			}
		}
		this.license = product.getLicense();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	public List<ProductDetailsDTO> getProductDetails() {
		return productDetails;
	}

	public boolean isDraft() {
		return draft;
	}

	public void setDraft(boolean draft) {
		this.draft = draft;
	}

	public void setProductDetails(List<ProductDetailsDTO> productDetails) {
		this.productDetails = productDetails;
	}

}
