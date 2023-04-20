package com.SEP490_G9.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.dto.CartDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.entities.embeddable.CartItemKey;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.CartItemRepository;
import com.SEP490_G9.repository.CartRepository;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.serviceImpls.AccountServiceImpl;
import com.SEP490_G9.service.serviceImpls.CartServiceImplement;
import com.SEP490_G9.service.serviceImpls.ProductDetailsServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class CartserviceTest {
	@Mock
	CartRepository cartRepo;
	@Mock
	ProductDetailsServiceImpl productDetailsService;
	@Mock
	private CartItemRepository cartItemRepo;
	@InjectMocks
	CartServiceImplement cartServiceImpl;
	@Mock
	UserRepository userRepo;

	@Test
	void testaddItem_1() {
		User user = new User();
		user.setId(1L);

		Seller seller = new Seller(user);
		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");
		product.setSeller(seller);

		Cart cart = new Cart();
		cart.setId(1L);
		cart.setUser(user);
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
		when(cartRepo.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productDetailsService.getActiveVersion(1l)).thenReturn(productDetails);
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);

		assertThrows(IllegalArgumentException.class, () -> {
			cartServiceImpl.addItem(cart.getId(), product.getId());
		});
	}

	@Test
	void testaddItem_2() {
		User user = new User();
		user.setId(1L);
		Seller seller = new Seller(user);

		User user1 = new User();
		user1.setId(2L);

		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");
		product.setSeller(seller);

		Cart cart = new Cart();
		cart.setId(2L);
		cart.setUser(user1);
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
		when(cartRepo.findById(cart.getId())).thenReturn(Optional.of(cart));
		when(productDetailsService.getActiveVersion(anyLong())).thenReturn(productDetails);
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
		when(userRepo.findById(anyLong())).thenReturn(Optional.of(user1));

		assertThrows(NoSuchElementException.class, () -> {
			cartServiceImpl.addItem(cart.getId(), product.getId());
		});
	}

	@Test
	void testaddItem_3() {
		User user = new User();
		user.setId(2L);

		Seller seller = new Seller(user);

		User user2 = new User();
		user2.setId(3L);
		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");
		product.setSeller(seller);

		Cart cart = new Cart();
		cart.setId(3L);
		cart.setUser(user2);
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
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(key);
		productDetails.setPrice(1000);

		CartDTO cartDTO = new CartDTO();
		cartDTO.setId(1l);
		cartDTO.setUser(user);
		when(cartRepo.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productDetailsService.getActiveVersion(anyLong())).thenReturn(productDetails);
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
		Cart result = cartServiceImpl.addItem(cart.getId(), product.getId());
		assertEquals(1, result.getItems().size());
	}

	@Test
	void testaddItem_4() {
		User user = new User();
		user.setId(2L);

		Seller seller = new Seller(user);

		User user2 = new User();
		user2.setId(3L);
		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");
		product.setSeller(seller);

		Cart cart = new Cart();
		cart.setId(3L);
		cart.setUser(user2);
		ProductVersionKey key = new ProductVersionKey();
		key.setProductId(1l);
		key.setVersion("1.0.0");

		CartItemKey cartItemKey = new CartItemKey();
		cartItemKey.setCartId(1l);
		cartItemKey.setProductVersionKey(key);

		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setCartItemKey(cartItemKey);
		cart.addItem(cartItem);
		cart.addItem(cartItem);
		cart.addItem(cartItem);
		cart.addItem(cartItem);
		cart.addItem(cartItem);
		cart.addItem(cartItem);
		cart.addItem(cartItem);
		cart.addItem(cartItem);
		cart.addItem(cartItem);
		cart.addItem(cartItem);

		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(key);
		productDetails.setPrice(1000);

		CartDTO cartDTO = new CartDTO();
		cartDTO.setId(1l);
		cartDTO.setUser(user);
		when(cartRepo.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productDetailsService.getActiveVersion(anyLong())).thenReturn(productDetails);
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
		assertThrows(IllegalArgumentException.class, () -> {
			cartServiceImpl.addItem(cart.getId(), product.getId());
		});
	}

	@Test
	void testaddItem_5() {
		User user = new User();
		user.setId(2L);

		Seller seller = new Seller(user);

		User user2 = new User();
		user2.setId(3L);
		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");
		product.setSeller(seller);

		Cart cart = new Cart();
		cart.setId(3L);
		cart.setUser(user2);
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
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(key);
		productDetails.setPrice(1000);
		cartItem.setProductDetails(productDetails);
//
		Product product2 = new Product();
		product2.setId(2l);
		product2.setActiveVersion("1.0.0");
		product2.setSeller(seller);

		ProductVersionKey key2 = new ProductVersionKey();
		key2.setProductId(2l);
		key2.setVersion("1.0.0");

		CartItemKey cartItemKey2 = new CartItemKey();
		cartItemKey2.setCartId(1l);
		cartItemKey2.setProductVersionKey(key2);

		CartItem cartItem2 = new CartItem();
		cartItem2.setCart(cart);
		cartItem2.setCartItemKey(cartItemKey2);

		ProductDetails productDetails2 = new ProductDetails();
		productDetails2.setProduct(product2);
		productDetails2.setVersion("1.0.0");
		productDetails2.setProductVersionKey(key2);
		productDetails2.setPrice(1000);
		cartItem2.setProductDetails(productDetails2);
		cart.addItem(cartItem);

		when(cartRepo.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productDetailsService.getActiveVersion(anyLong())).thenReturn(productDetails2);
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
		Cart result = cartServiceImpl.addItem(cart.getId(), product2.getId());
		assertEquals(2, result.getItems().size());
	}

	@Test
	void testaddItem_6() {
		User user = new User();
		user.setId(2L);

		Seller seller = new Seller(user);

		User user2 = new User();
		user2.setId(3L);
		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");
		product.setSeller(seller);

		Cart cart = new Cart();
		cart.setId(3L);
		cart.setUser(user2);
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
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(key);
		productDetails.setPrice(1000);
		cartItem.setProductDetails(productDetails);
//
		Product product2 = new Product();
		product2.setId(2l);
		product2.setActiveVersion("1.0.0");
		product2.setSeller(seller);

		ProductVersionKey key2 = new ProductVersionKey();
		key2.setProductId(2l);
		key2.setVersion("1.0.0");

		CartItemKey cartItemKey2 = new CartItemKey();
		cartItemKey2.setCartId(1l);
		cartItemKey2.setProductVersionKey(key2);

		CartItem cartItem2 = new CartItem();
		cartItem2.setCart(cart);
		cartItem2.setCartItemKey(cartItemKey2);

		ProductDetails productDetails2 = new ProductDetails();
		productDetails2.setProduct(product2);
		productDetails2.setVersion("1.0.0");
		productDetails2.setProductVersionKey(key2);
		productDetails2.setPrice(1000);
		cartItem2.setProductDetails(productDetails2);

		when(cartRepo.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productDetailsService.getActiveVersion(anyLong())).thenReturn(productDetails2);
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
		Cart result = cartServiceImpl.addItem(cart.getId(), product2.getId());
		assertEquals(1, result.getItems().size());
	}

	@Test
	void testaddItem_7() {
		User user = new User();
		user.setId(2L);

		Seller seller = new Seller(user);

		User user2 = new User();
		user2.setId(3L);
		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");
		product.setSeller(seller);

		Cart cart = new Cart();
		cart.setId(3L);
		cart.setUser(user2);
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
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(key);
		productDetails.setPrice(1000);
		cartItem.setProductDetails(productDetails);
		when(cartRepo.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productDetailsService.getActiveVersion(anyLong())).thenReturn(null);
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
		assertThrows(NullPointerException.class, () -> {
			cartServiceImpl.addItem(cart.getId(), product.getId());
		});
	}

	@Test
	void testaddItem_8() {
		User user = new User();
		user.setId(2L);

		Seller seller = new Seller(user);

		User user2 = new User();
		user2.setId(3L);
		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");
		product.setSeller(seller);

		Cart cart = new Cart();
		cart.setId(3L);
		cart.setUser(user2);
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
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(key);
		productDetails.setPrice(1000);
		cartItem.setProductDetails(productDetails);

		when(cartRepo.findById(anyLong())).thenReturn(null);
		when(productDetailsService.getActiveVersion(anyLong())).thenReturn(null);
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
		assertThrows(NullPointerException.class, () -> {
			cartServiceImpl.addItem(cart.getId(), product.getId());
		});
	}

	@Test
	void testaddItem_9() {
		User user = new User();
		user.setId(2L);

		Seller seller = new Seller(user);

		User user2 = new User();
		user2.setId(3L);
		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");
		product.setSeller(seller);

		Cart cart = new Cart();
		cart.setId(3L);
		cart.setUser(user2);
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
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(key);
		productDetails.setPrice(1000);
		cartItem.setProductDetails(productDetails);
//
		Product product2 = new Product();
		product2.setId(2l);
		product2.setActiveVersion("1.0.0");
		product2.setSeller(seller);

		ProductVersionKey key2 = new ProductVersionKey();
		key2.setProductId(2l);
		key2.setVersion("1.0.0");

		CartItemKey cartItemKey2 = new CartItemKey();
		cartItemKey2.setCartId(1l);
		cartItemKey2.setProductVersionKey(key2);

		CartItem cartItem2 = new CartItem();
		cartItem2.setCart(cart);
		cartItem2.setCartItemKey(cartItemKey2);

		ProductDetails productDetails2 = new ProductDetails();
		productDetails2.setProduct(product2);
		productDetails2.setVersion("1.0.0");
		productDetails2.setProductVersionKey(key2);
		productDetails2.setPrice(1000);
		cartItem2.setProductDetails(productDetails2);

		when(cartRepo.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productDetailsService.getActiveVersion(anyLong())).thenReturn(null);
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
		assertThrows(NullPointerException.class, () -> {
			cartServiceImpl.addItem(cart.getId(), product.getId());
		});
	}

	@Test
	void testaddItem_10() {
		User user = new User();
		user.setId(2L);

		Seller seller = new Seller(user);

		User user2 = new User();
		user2.setId(3L);
		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");
		product.setSeller(seller);

		Cart cart = new Cart();
		cart.setId(3L);
		cart.setUser(user2);
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
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(key);
		productDetails.setPrice(1000);
		cartItem.setProductDetails(productDetails);
//
		Product product2 = new Product();
		product2.setId(2l);
		product2.setActiveVersion("1.0.0");
		product2.setSeller(seller);

		ProductVersionKey key2 = new ProductVersionKey();
		key2.setProductId(2l);
		key2.setVersion("1.0.0");

		CartItemKey cartItemKey2 = new CartItemKey();
		cartItemKey2.setCartId(1l);
		cartItemKey2.setProductVersionKey(key2);

		CartItem cartItem2 = new CartItem();
		cartItem2.setCart(cart);
		cartItem2.setCartItemKey(cartItemKey2);

		ProductDetails productDetails2 = new ProductDetails();
		productDetails2.setProduct(product2);
		productDetails2.setVersion("1.0.0");
		productDetails2.setProductVersionKey(key2);
		productDetails2.setPrice(1000);
		cartItem2.setProductDetails(productDetails2);
		cart.addItem(cartItem);

		when(cartRepo.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productDetailsService.getActiveVersion(anyLong())).thenReturn(productDetails2);
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
		Cart result = cartServiceImpl.addItem(cart.getId(), product2.getId());
		assertEquals(2, result.getItems().size());
	}

	@Test
	void testaddItem_11() {
		User user = new User();
		user.setId(2L);

		Seller seller = new Seller(user);

		User user2 = new User();
		user2.setId(3L);
		Product product = new Product();
		product.setId(1l);
		product.setActiveVersion("1.0.0");
		product.setSeller(seller);

		Cart cart = new Cart();
		cart.setId(3L);
		cart.setUser(user2);
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
		productDetails.setVersion("1.0.0");
		productDetails.setProductVersionKey(key);
		productDetails.setPrice(1000);
		cartItem.setProductDetails(productDetails);
//
		Product product2 = new Product();
		product2.setId(2l);
		product2.setActiveVersion("1.0.0");
		product2.setSeller(seller);

		ProductVersionKey key2 = new ProductVersionKey();
		key2.setProductId(2l);
		key2.setVersion("1.0.0");

		CartItemKey cartItemKey2 = new CartItemKey();
		cartItemKey2.setCartId(1l);
		cartItemKey2.setProductVersionKey(key2);

		CartItem cartItem2 = new CartItem();
		cartItem2.setCart(cart);
		cartItem2.setCartItemKey(cartItemKey2);

		ProductDetails productDetails2 = new ProductDetails();
		productDetails2.setProduct(product2);
		productDetails2.setVersion("1.0.0");
		productDetails2.setProductVersionKey(key2);
		productDetails2.setPrice(1000);
		cartItem2.setProductDetails(productDetails2);
		cart.addItem(cartItem);

		when(cartRepo.findById(anyLong())).thenReturn(Optional.of(cart));
		when(productDetailsService.getActiveVersion(anyLong())).thenReturn(productDetails2);
		when(cartRepo.save(cart)).thenReturn(cart);
		when(cartItemRepo.save(cartItem)).thenReturn(cartItem);
		Cart result = cartServiceImpl.addItem(cart.getId(), product2.getId());
		assertEquals(2, result.getItems().size());
	}

	private Cart testCart;

	@Before
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		testCart = new Cart();
		testCart.setId(1L);
	}

	@Test
	void testRemoveItem_1() {
		Long productIdToRemove = 1L;
		CartItem itemToRemove = new CartItem();
		ProductDetails productDetails = new ProductDetails();
		ProductVersionKey productVersionKey = new ProductVersionKey();
		productVersionKey.setProductId(productIdToRemove);
		productDetails.setProductVersionKey(productVersionKey);
		itemToRemove.setProductDetails(productDetails);
		testCart = new Cart();
		testCart.setId(1L);
		testCart.getItems().add(itemToRemove);

		Mockito.when(cartRepo.findById(testCart.getId())).thenReturn(Optional.of(testCart));
		when(cartRepo.save(testCart)).thenReturn(testCart);
		// Execution
		Cart returnedCart = cartServiceImpl.removeItem(productIdToRemove, testCart.getId());
		assertTrue(returnedCart.getItems().isEmpty());
	}

	@Test
	void testRemoveItem_2() {
		Long productIdToRemove = 1L;
		testCart = new Cart();
		testCart.setId(1L);
		Mockito.when(cartRepo.findById(testCart.getId())).thenReturn(Optional.of(testCart));

		// Execution
		assertThrows(IllegalArgumentException.class, () -> {
			cartServiceImpl.removeItem(productIdToRemove, testCart.getId());
		});

	}

	@Test
	void testRemoveItem_3() {
		Long productIdToRemove = 2L;
		testCart = new Cart();
		testCart.setId(1L);
		Mockito.when(cartRepo.findById(testCart.getId())).thenReturn(Optional.of(testCart));

		// Execution
		assertThrows(IllegalArgumentException.class, () -> {
			cartServiceImpl.removeItem(productIdToRemove, testCart.getId());
		});

	}

	@Test
	void testRemoveItem_4() {
		Long productIdToRemove = 3L;
		testCart = new Cart();
		testCart.setId(1L);
		Mockito.when(cartRepo.findById(testCart.getId())).thenReturn(Optional.of(testCart));

		// Execution
		assertThrows(IllegalArgumentException.class, () -> {
			cartServiceImpl.removeItem(productIdToRemove, testCart.getId());
		});

	}

	@Test
	void testGetCurrentCart_1() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepo.findById(account.getId())).thenReturn(Optional.of(user));

		when(cartRepo.existsByUserId(user.getId())).thenReturn(false);

		Cart newCart = new Cart();
		newCart.setId(2L);
		newCart.setUser(user);
		when(cartRepo.save(any(Cart.class))).thenReturn(newCart);

		// Act
		Cart result = cartServiceImpl.getCurrentCart();

		// Assert
		assertNotNull(result);
		assertEquals(newCart.getId(), result.getId());
		assertEquals(user, result.getUser());
		assertEquals(0, result.getItems().size());

	}

	@Test
	public void testGetCurrentCart_2() {
		// Create a user account
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		// Create a product
		Product product = new Product();
		product.setActiveVersion("1.0");
		product.setEnabled(true);

		// Create a product detail
		ProductDetails productDetails = new ProductDetails();
		productDetails.setProduct(product);
		productDetails.setVersion("1.0");
		productDetails.setPrice(2);
		productDetails.setApproved(ProductDetails.Status.APPROVED);

		// Create a cart item
		CartItem cartItem = new CartItem();
		cartItem.setProductDetails(productDetails);
		cartItem.setPrice(1);

		// Create a cart
		Cart cart = new Cart();
		cart.setUser(user);
		cart.setItems(Collections.singletonList(cartItem));
		when(cartRepo.findFirstByUserOrderByIdDesc(user)).thenReturn(cart);
		when(userRepo.findById(account.getId())).thenReturn(Optional.of(user));
		when(cartRepo.findById(anyLong())).thenReturn(Optional.of(cart));
		when(cartRepo.existsByUserId(user.getId())).thenReturn(true);
		when(pdr.findByProductIdAndProductVersionKeyVersion(1L, "1.0.0")).thenReturn(productDetails);
		// Modify the price of the product detail
		// Call the getCurrentCart() method

		// Check that the cart item price has been updated
		assertThrows(NoSuchElementException.class, () -> {
			cartServiceImpl.getCurrentCart();
		});
	}

	@Test
	void testGetCurrentCart_4() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepo.findById(account.getId())).thenReturn(Optional.of(user));

		when(cartRepo.existsByUserId(user.getId())).thenReturn(false);

		Cart newCart = new Cart();
		newCart.setId(2L);
		newCart.setUser(user);
		when(cartRepo.save(any(Cart.class))).thenReturn(newCart);

		// Act
		Cart result = cartServiceImpl.getCurrentCart();

		// Assert
		assertNotNull(result);
		assertEquals(newCart.getId(), result.getId());
		assertEquals(user, result.getUser());
		assertEquals(0, result.getItems().size());

	}

	@Test
	void testGetCurrentCart_5() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepo.findById(account.getId())).thenReturn(Optional.of(user));

		when(cartRepo.existsByUserId(user.getId())).thenReturn(false);

		Cart newCart = new Cart();
		newCart.setId(2L);
		newCart.setUser(user);
		when(cartRepo.save(any(Cart.class))).thenReturn(newCart);

		// Act
		Cart result = cartServiceImpl.getCurrentCart();

		// Assert
		assertNotNull(result);
		assertEquals(newCart.getId(), result.getId());
		assertEquals(user, result.getUser());
		assertEquals(0, result.getItems().size());

	}

	@Test
	void testGetCurrentCart_6() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepo.findById(account.getId())).thenReturn(Optional.of(user));

		when(cartRepo.existsByUserId(user.getId())).thenReturn(false);

		Cart newCart = new Cart();
		newCart.setId(2L);
		newCart.setUser(user);
		when(cartRepo.save(any(Cart.class))).thenReturn(newCart);

		// Act
		Cart result = cartServiceImpl.getCurrentCart();

		// Assert
		assertNotNull(result);
		assertEquals(newCart.getId(), result.getId());
		assertEquals(user, result.getUser());
		assertEquals(0, result.getItems().size());

	}

	@Test
	void testGetCurrentCart_7() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepo.findById(account.getId())).thenReturn(Optional.of(user));

		when(cartRepo.existsByUserId(user.getId())).thenReturn(false);

		Cart newCart = new Cart();
		newCart.setId(2L);
		newCart.setUser(user);
		when(cartRepo.save(any(Cart.class))).thenReturn(newCart);

		// Act
		Cart result = cartServiceImpl.getCurrentCart();

		// Assert
		assertNotNull(result);
		assertEquals(newCart.getId(), result.getId());
		assertEquals(user, result.getUser());
		assertEquals(0, result.getItems().size());

	}

	@Test
	void testGetCurrentCart_3() {
		Account account = new Account();
		account.setEmail("johndoe@gmail.com");
		UserDetailsImpl userDetails = new UserDetailsImpl();
		userDetails.setAccount(account);
		account.setId(1L);
		User user = new User(account);
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
		SecurityContextHolder.setContext(securityContext);

		when(userRepo.findById(account.getId())).thenReturn(Optional.of(user));

		when(cartRepo.existsByUserId(user.getId())).thenReturn(false);

		Cart newCart = new Cart();
		newCart.setId(2L);
		newCart.setUser(user);
		when(cartRepo.save(any(Cart.class))).thenReturn(newCart);

		// Act
		Cart result = cartServiceImpl.getCurrentCart();

		// Assert
		assertNotNull(result);
		assertEquals(newCart.getId(), result.getId());
		assertEquals(user, result.getUser());
		assertEquals(0, result.getItems().size());

	}

	@Mock
	ProductDetailsRepository pdr;
}
