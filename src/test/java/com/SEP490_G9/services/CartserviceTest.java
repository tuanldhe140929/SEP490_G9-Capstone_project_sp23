package com.SEP490_G9.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.dto.CartDTO;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.embeddable.CartItemKey;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.repository.CartItemRepository;
import com.SEP490_G9.repository.CartRepository;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.service.serviceImpls.AccountServiceImpl;
import com.SEP490_G9.service.serviceImpls.CartServiceImplement;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class CartserviceTest {
	@Mock
	CartRepository cartRepo;
	@MockBean
	ProductDetailsRepository productDetailsRepo;
	@MockBean
	private CartItemRepository cartItemRepo;
	@InjectMocks
	CartServiceImplement cartServiceImpl;

	@Test
	void testaddProduct() {
		User user = new User();
		user.setId(1L);

		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");

		Cart cart = new Cart();
		cart.setId(1L);

		ProductVersionKey key = new ProductVersionKey();
		key.setProductId(1l);
		key.setVersion("1.0.0");

		CartItemKey cartItemKey = new CartItemKey();
		cartItemKey.setCartId(1l);
		cartItemKey.setProductVersionKey(key);

		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setCartItemKey(cartItemKey);

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setProductVersionKey(key);
		productDetails.setPrice(1000);
		
		CartDTO cartDTO = new CartDTO();
		cartDTO.setId(1l);
		cartDTO.setUser(user);

		when(productDetailsRepo.findFirstByProductIdOrderByCreatedDateDesc(product.getId()))
	    .thenReturn(new ProductDetails());
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
		
		 cartDTO = cartServiceImpl.addProduct(product.getId());
//		assertEquals(1, cartDTO.getItems().size());
	}

	

}
