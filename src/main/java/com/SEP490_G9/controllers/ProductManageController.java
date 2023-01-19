package com.SEP490_G9.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.models.DTOS.Product;
import com.SEP490_G9.services.ManageProductService;

@RequestMapping(value = "private/productManage")
@RestController
public class ProductManageController {

	@Autowired
	ManageProductService manageProductService;
	
	@GetMapping(value="getProductsByUser")
	public ResponseEntity<?> getProductsByUser() {
		List<Product> products = manageProductService.getProductsByUser();
		return ResponseEntity.ok(products);
	}

	@PostMapping(value="addProduct")
	public ResponseEntity<?> addProduct(@RequestBody Product product) {
		Product ret =  manageProductService.addProduct(product);
		return ResponseEntity.ok(ret);
	}

	@DeleteMapping(value="deleteProduct/{id}")
	public ResponseEntity<?> deleteProducts(@PathVariable(name="id") Long id) {
		Product ret = manageProductService.deleteProduct(id);
		return ResponseEntity.ok(ret);
	}

	@PostMapping(value="updateProduct")
	public ResponseEntity<?> updateProduct(@RequestBody Product product) {
		Product ret = manageProductService.updateProduct(product);
		return ResponseEntity.ok(ret);
	}
}
