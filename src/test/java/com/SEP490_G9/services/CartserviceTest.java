package com.SEP490_G9.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.SEP490_G9.dto.CartDTO;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.repository.CartItemRepository;
import com.SEP490_G9.repository.CartRepository;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.service.serviceImpls.AccountServiceImpl;
import com.SEP490_G9.service.serviceImpls.CartServiceImplement;

class CartserviceTest {
	@Mock
	CartRepository cartRepo;
	@MockBean
	ProductDetailsRepository productDetailsRepo;
	@MockBean
	 @Mock
	    private CartItemRepository cartItemRepo;
	@InjectMocks
	CartServiceImplement cartServiceImpl;

	@Test
	void testaddProduct() {
		Product product = new Product();
		product.setId(1L);

		ProductDetails pd = new ProductDetails();
		when(productDetailsRepo.findFirstByProductIdOrderByCreatedDateDesc(product.getId())).thenReturn(pd);
		
		
		Cart cart = new Cart();
		CartItem item = new CartItem(cart, pd);
		
		
		CartDTO result = cartServiceImpl.addProduct(product.getId());
		
	}
	@Test
	void testRemoveproduct() {
		
	}

}
