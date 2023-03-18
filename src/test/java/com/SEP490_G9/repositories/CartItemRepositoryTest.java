package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.embeddable.CartItemKey;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.repository.CartItemRepository;
import com.SEP490_G9.repository.CartRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class CartItemRepositoryTest {
	@Mock
	CartRepository cartRepo;
	@Mock
	CartItemRepository cartItemRepo;

	@Test
	void testSaveCartItem() {
		User user = new User();
		user.setId(1L);
		Product product = new Product();
		product.setId(1L);
		product.setActiveVersion("1.0.0");
		
		Cart cart = new Cart(user);
		cart.setId(1l);
		ProductVersionKey key = new ProductVersionKey();
		key.setProductId(1l);
		key.setVersion("1.0.0");
		
		CartItemKey cartItemKey= new CartItemKey() ;
		cartItemKey.setCartId(1l);
		cartItemKey.setProductVersionKey(key);
		
		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setCartItemKey(cartItemKey);
		

		assertThat(cartItem.getCart()).isEqualTo(cart);
		assertThat(cartItem.getCartItemKey()).isEqualTo(cartItemKey);		
	}
	@Test
	void testGetByCart() {
		User user = new User();
		user.setId(1L);
		Product product = new Product();
		product.setId(1L);
		product.setActiveVersion("1.0.0");
		
		Cart cart = new Cart(user);
		cart.setId(1l);
		ProductVersionKey key = new ProductVersionKey();
		key.setProductId(1l);
		key.setVersion("1.0.0");
		
		CartItemKey cartItemKey= new CartItemKey() ;
		cartItemKey.setCartId(1l);
		cartItemKey.setProductVersionKey(key);
		
		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setCartItemKey(cartItemKey);
		

		assertThat(cartItem.getCart()).isEqualTo(cart);
		
		
	}
}
