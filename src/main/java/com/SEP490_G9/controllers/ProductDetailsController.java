package com.SEP490_G9.controllers;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.dto.ProductDetailsDTO;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductDetails.Status;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/productDetails")
public class ProductDetailsController {

	@Autowired
	ProductDetailsService productDetailsService;

	@Autowired
	ProductDetailsRepository productDetailsRepo;

	@Autowired
	ProductService productService;

	@GetMapping(value = "/getProductsForSearching")
	public ResponseEntity<?> getFilteredProducts(@RequestParam("keyword") String keyword,
			@RequestParam("categoryid") int categoryid, @RequestParam("tagidlist") List<Integer> tagidlist,
			@RequestParam("min") int min, @RequestParam("max") int max) {
		List<ProductDetails> filteredProducts = productDetailsService.getProductForSearching(keyword, categoryid,
				tagidlist, min, max);
		List<ProductDetailsDTO> filteredProductsDto = new ArrayList<>();
		for (ProductDetails result : filteredProducts) {
			filteredProductsDto.add(new ProductDetailsDTO(result));
		}
		return ResponseEntity.ok(filteredProductsDto);
	}

	@GetMapping(value = "/getAllProducts")
	public ResponseEntity<?> getAllProducts() {
		List<ProductDetails> allProducts = productDetailsService.getAll();
		List<ProductDetailsDTO> allProductsDto = new ArrayList<>();
		for (ProductDetails product : allProducts) {
			allProductsDto.add(new ProductDetailsDTO(product));
		}
		return ResponseEntity.ok(allProductsDto);
	}

	@GetMapping(value = "/getProductsBySellerForSeller")
	public ResponseEntity<?> getProductsBySellerForSeller(@RequestParam("sellerid") Long sellerid,
			@RequestParam("keyword") String keyword, @RequestParam("categoryid") int categoryid,
			@RequestParam("tagidlist") List<Integer> tagidlist, @RequestParam("min") int min,
			@RequestParam("max") int max) {
		List<ProductDetails> finalList = productDetailsService.getProductBySellerForSeller(sellerid, keyword,
				categoryid, tagidlist, min, max);
		List<ProductDetailsDTO> finalListDto = new ArrayList<>();
		for (ProductDetails result : finalList) {
			finalListDto.add(new ProductDetailsDTO(result));
		}
		return ResponseEntity.ok(finalListDto);
	}

	@GetMapping(value = "/getProductsBySellerForUser")
	public ResponseEntity<?> getProductsBySellerForUser(@RequestParam("sellerid") Long sellerid,
			@RequestParam("keyword") String keyword, @RequestParam("categoryid") int categoryid,
			@RequestParam("tagidlist") List<Integer> tagidlist, @RequestParam("min") int min,
			@RequestParam("max") int max) {
		List<ProductDetails> finalList = productDetailsService.getProductBySellerForUser(sellerid, keyword, categoryid,
				tagidlist, min, max);
		List<ProductDetailsDTO> finalListDto = new ArrayList<>();
		for (ProductDetails result : finalList) {
			finalListDto.add(new ProductDetailsDTO(result));
		}
		return ResponseEntity.ok(finalListDto);
	}

	@GetMapping(value = "getPublishedProductsBySeller")
	public ResponseEntity<?> getProductsBySeller(@RequestParam(name = "sellerId") Long sellerId) {
		List<ProductDetails> publishedProducts = productDetailsService.getBySellerIdAndIsDraft(sellerId, false);
		List<ProductDetailsDTO> dtos = new ArrayList<>();
		for (ProductDetails pd : publishedProducts) {
			dtos.add(new ProductDetailsDTO(pd));
		}

		return ResponseEntity.ok(dtos);
	}

	@GetMapping(value = "getActiveVersion")
	public ResponseEntity<?> getActiveVersion(@RequestParam(name = "productId") Long productId) {
		ProductDetails pd = productDetailsService.getActiveVersion(productId);
		ProductDetailsDTO dto = new ProductDetailsDTO(pd);
		return ResponseEntity.ok(dto);
	}

	@GetMapping(value = "getActiveVersionForDownload")
	public ResponseEntity<?> getActiveVersionForDownload(@RequestParam(name = "productId") Long productId) {
		ProductDetails pd = productDetailsService.getActiveVersionForDownload(productId);
		ProductDetailsDTO dto = new ProductDetailsDTO(pd);
		return ResponseEntity.ok(dto);
	}

	@GetMapping(value = "getAllVersion")
	public ResponseEntity<?> getAllVersion(@RequestParam(name = "productId", required = true) Long productId) {
		List<ProductDetails> productDetailss = productDetailsService.getAllByProductId(productId);
		List<ProductDetailsDTO> ret = new ArrayList<>();
		for (ProductDetails dt : productDetailss) {
			ret.add(new ProductDetailsDTO(dt));
		}
		return ResponseEntity.ok(ret);
	}

	// get by id and
	@PostMapping(value = "getByIdAndVersion")
	public ResponseEntity<?> createNewVersionV2(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "version", required = true) String newVersion) {
		ProductDetails productDetails = productDetailsService.getByProductIdAndVersion(productId, newVersion);
		ProductDetailsDTO ret = new ProductDetailsDTO(productDetails);
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value = "verifyProduct")
	public ResponseEntity<?> verfyProduct(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "version", required = true) String version) {
		ProductDetails pd = productDetailsService.updateApprovalStatus(productId, version, "PENDING");
		ProductDetailsDTO ret = new ProductDetailsDTO(pd);
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value = "cancelVerifyProduct")
	public ResponseEntity<?> cancelVerfyProduct(@RequestParam(name = "productId", required = true) Long productId,
			@RequestParam(name = "version", required = true) String version) {
		ProductDetails pd = productDetailsService.updateApprovalStatus(productId, version, "NEW");
		ProductDetailsDTO ret = new ProductDetailsDTO(pd);
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value = "createNewVersionV2")
	public ResponseEntity<?> createNewVersionV2(@RequestBody ProductDetailsDTO productDetailsDTO,
			@RequestParam(name = "newVersion", required = true) String newVersion) {
		ProductDetails newPD = productDetailsService.createNewVersion(productDetailsDTO.getId(), newVersion);
		ProductDetailsDTO dto = new ProductDetailsDTO(newPD);
		return ResponseEntity.ok(dto);
	}

	@PostMapping(value = "updateProduct")
	public ResponseEntity<?> updateProduct(@Valid @RequestBody ProductDetailsDTO productDetailsDTO,
			@RequestParam(name = "instruction") String instructionDetails) {
		ProductDetailsDTO ret = null;
		ProductDetails notEdited = productDetailsRepo
				.findByProductIdAndProductVersionKeyVersion(productDetailsDTO.getId(), productDetailsDTO.getVersion());
		if (notEdited.getApproved() != Status.NEW) {
			throw new IllegalArgumentException("Cannot edit this version");
		}
		System.out.println(productDetailsDTO.getPrice());
		notEdited.setLastModified(new Date());
		Product product = notEdited.getProduct();
		notEdited.setTags(productDetailsDTO.getTags());
		notEdited.setCategory(productDetailsDTO.getCategory());
		if (productDetailsDTO.getDescription() != null)
			notEdited.setDescription(productDetailsDTO.getDescription().trim());
		else
			notEdited.setDescription(null);

		if (productDetailsDTO.getDetails() != null)
			notEdited.setDetailDescription(productDetailsDTO.getDetails().trim());
		else
			notEdited.setDetailDescription("");

		if (productDetailsDTO.getLicense() != null)
			notEdited.setLicense(productDetailsDTO.getLicense());
		else
			notEdited.setLicense(null);

		notEdited.setLastModified(new Date());
		notEdited.setName(productDetailsDTO.getName().trim());
		notEdited.setPrice(productDetailsDTO.getPrice());
		notEdited.setInstruction(instructionDetails.trim());

		productService.updateProduct(product);
		ProductDetails updatedPd = productDetailsRepo.save(notEdited);

		ret = new ProductDetailsDTO(updatedPd);

		return ResponseEntity.ok(ret);
	}

	@GetMapping(value = "getProductsByReportStatus")
	public ResponseEntity<?> getProductsByReportStatus(@RequestParam(name = "status") String status) {
		List<ProductDetails> reportList = productDetailsService.getProductsByReportStatus(status);
		List<ProductDetailsDTO> allDtoPd = new ArrayList<>();
		for (ProductDetails pd : reportList) {
			allDtoPd.add(new ProductDetailsDTO(pd));
		}
		return ResponseEntity.ok(allDtoPd);
	}

	@GetMapping(value = "getByApprovalStatus")
	public ResponseEntity<?> getByApprovalStatus(@RequestParam(name = "status") String status) {
		List<ProductDetails> allStatusPd = productDetailsService.getProductsByApprovalStatus(status);
		List<ProductDetailsDTO> allDtoPd = new ArrayList<>();
		for (ProductDetails pd : allStatusPd) {
			allDtoPd.add(new ProductDetailsDTO(pd));
		}
		return ResponseEntity.ok(allDtoPd);
	}

	@PutMapping(value = "updateApprovalStatus")
	public ResponseEntity<?> updateApprovalStatus(@RequestParam(name = "productId") long productId,
			@RequestParam(name = "version") String version, @RequestParam(name = "status") String status) {
		ProductDetails pd = productDetailsService.updateApprovalStatus(productId, version, status);
		return ResponseEntity.ok(pd);
	}

	@GetMapping(value = "allProductsLatestVers")
	public ResponseEntity<?> allProductsLatestVer() {
		List<ProductDetails> allProductsLatestVer = productDetailsService.getAllByLatestVersion();
		List<ProductDetailsDTO> allDtoPd = new ArrayList<>();
		for (ProductDetails pd : allProductsLatestVer) {
			allDtoPd.add(new ProductDetailsDTO(pd));
		}
		return ResponseEntity.ok(allDtoPd);
	}

	@GetMapping(value = "GetAllProductForHomePage")
	public ResponseEntity<?> GetAllProductForHomePage() {
		List<ProductDetails> allProducts = productDetailsService.getAll();
		List<ProductDetails> enabledProducts = productDetailsService.getByEnabled(allProducts);
		List<ProductDetails> approvedProducts = productDetailsService.getByApproved(enabledProducts);
		List<ProductDetails> latestProducts = productDetailsService.getByLatestVer(approvedProducts);

		List<ProductDetailsDTO> allProductsDTO = new ArrayList<>();
		for (ProductDetails p : latestProducts) {
			allProductsDTO.add(new ProductDetailsDTO(p));
		}
		return ResponseEntity.ok(allProductsDTO);
	}


	@GetMapping(value = "getLastestUpdatedProductForHomePage")
	public ResponseEntity<?> LastestUpdatedProductForHomePage() {
		List<ProductDetails> allProducts = productDetailsService.getAll();
		List<ProductDetails> enabledProducts = productDetailsService.getByEnabled(allProducts);
		List<ProductDetails> approvedProducts = productDetailsService.getByApproved(enabledProducts);
		List<ProductDetails> latestProducts = productDetailsService.getByLatestVer(approvedProducts);
		List<ProductDetails> latestUpdatedProduts = productDetailsService.getProductByTime(latestProducts);
		List<ProductDetailsDTO> allProductsDTO = new ArrayList<>();
		for (ProductDetails p : latestUpdatedProduts) {
			allProductsDTO.add(new ProductDetailsDTO(p));
		}
		return ResponseEntity.ok(allProductsDTO);
	}


	@GetMapping(value = "getTotalPurchasedCount")
	public ResponseEntity<?> getTotalPurchasedCount(@RequestParam("productId") Long productId) {
		int count = this.productDetailsService.getTotalPurchasedCount(productId);
		return ResponseEntity.ok(count);
	}


	@GetMapping(value = "getCurrentVersion")
	public String getCurrentVersion(@RequestParam("productId") Long productId) {
		String currentVer = productDetailsService.getCurrentVersion(productId);
		return currentVer;
	}




}
