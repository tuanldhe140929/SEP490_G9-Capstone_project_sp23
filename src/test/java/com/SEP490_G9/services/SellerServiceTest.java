package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.common.Constant;
import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.RoleRepository;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.SellerService;
import com.SEP490_G9.service.serviceImpls.AccountServiceImpl;
import com.SEP490_G9.service.serviceImpls.SellerServiceImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class SellerServiceTest {
	@Mock
	AccountRepository accountRepo;

	@Mock
	SellerRepository sellerRepo;

	@Mock
	UserRepository userRepo;

	@Mock
	ProductRepository productRepo;

	@InjectMocks
	SellerServiceImpl sellerImpl;

	@Mock
	RoleRepository roleRepo;
	
	@Mock
	EntityManager entityManager;

	@Test
	void testGetAllSeller_1() {
		Role sellerRoleInDB = new Role(4, "ROLE_SELLER");
		when(roleRepo.findById(Constant.SELLER_ROLE_ID)).thenReturn(sellerRoleInDB);

		List<Role> sellerRole = new ArrayList<>();
		Role role = roleRepo.findById(Constant.SELLER_ROLE_ID);
		sellerRole.add(role);

		List<Seller> expected = new ArrayList<>();

		Account sellerAccount = new Account();

		sellerAccount.setRoles(sellerRole);
		sellerAccount.setId((long) 1);
		
		User u = new User(sellerAccount);
		Seller s = new Seller(u);
		expected.add(s);
		
		Account sellerAccount2 = new Account();
		sellerAccount2.setRoles(sellerRole);
		sellerAccount2.setId((long) 2);
		
		User u1 = new User(sellerAccount2);
		Seller s1 = new Seller(u1);
		expected.add(s1);
		
		when(sellerRepo.findAll()).thenReturn(expected);

		int expectedSize = 1;
		List<Seller> result = sellerImpl.getAllSellers();
		assertThat(result).isEqualTo(expected);
		assertThat(result.size()).isEqualTo(2);
	}

	@Test
	void testGetAllSeller_2() {
		Role sellerRoleInDB = new Role(4, "ROLE_SELLER");
		when(roleRepo.findById(Constant.SELLER_ROLE_ID)).thenReturn(sellerRoleInDB);

		List<Role> sellerRole = new ArrayList<>();
		Role role = roleRepo.findById(Constant.SELLER_ROLE_ID);
		sellerRole.add(role);

		List<Seller> expected = new ArrayList<>();

		Account sellerAccount = new Account();

		sellerAccount.setRoles(sellerRole);
		sellerAccount.setId((long) 1);
		
		User u = new User(sellerAccount);
		Seller s = new Seller(u);
		expected.add(s);
		
		
		when(sellerRepo.findAll()).thenReturn(expected);

		int expectedSize = 1;
		List<Seller> result = sellerImpl.getAllSellers();
		assertThat(result).isEqualTo(expected);
		assertThat(result.size()).isEqualTo(1);
	}
	@Test
	void testGetAllSeller_3() {
		Role sellerRoleInDB = new Role(4, "ROLE_SELLER");
		when(roleRepo.findById(Constant.SELLER_ROLE_ID)).thenReturn(sellerRoleInDB);

		List<Role> sellerRole = new ArrayList<>();
		Role role = roleRepo.findById(Constant.SELLER_ROLE_ID);
		sellerRole.add(role);

		List<Seller> expected = new ArrayList<>();
		
		when(sellerRepo.findAll()).thenReturn(expected);

		int expectedSize = 1;
		List<Seller> result = sellerImpl.getAllSellers();
		assertThat(result).isEqualTo(expected);
		assertThat(result.size()).isEqualTo(0);
	}

	@Test
	public void testCreateNewSeller_1() {
		// Arrange
		String paypalEmail = "1";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(null);
		
		// Act
		assertThrows(IllegalArgumentException.class, () -> {
			sellerImpl.createNewSeller(paypalEmail);
		});
	}
	@Test
	public void testCreateNewSeller_11() {
		// Arrange
		String paypalEmail = " ";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> {
			sellerImpl.createNewSeller(paypalEmail);
		});
	}
	
	@Test
	public void testCreateNewSeller_2() {
		// Arrange
		String paypalEmail = "r             @gmail.com";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> {
			sellerImpl.createNewSeller(paypalEmail);
		});
	}
	@Test
	public void testCreateNewSeller_3() {
		// Arrange
		String paypalEmail = "paypal@gmail.com";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(null);
		
		// Act
		boolean result = sellerImpl.createNewSeller(paypalEmail);
		assertTrue(result);
	}
	
	@Test
	public void testCreateNewSeller_4() {
		// Arrange
		String paypalEmail = "paypal@gmail.com";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(null);
		
		// Act
		boolean result = sellerImpl.createNewSeller(paypalEmail);
		assertTrue(result);
	}
	
	@Test
	public void testCreateNewSeller_5() {
		// Arrange
		String paypalEmail = "r            @gmail.com";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> {
			sellerImpl.createNewSeller(paypalEmail);
		});
	}
	
	@Test
	public void testCreateNewSeller_6() {
		// Arrange
		String paypalEmail = "paypal@gmail.com";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(any(Seller.class));
		
		assertThrows(InvalidUseOfMatchersException.class, () -> {
			sellerImpl.createNewSeller(paypalEmail);
		});
	}
	
	@Test
	public void testCreateNewSeller_7() {
		// Arrange
		String paypalEmail = "paypal@gmail.com";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(any(Seller.class));
		
		assertThrows(InvalidUseOfMatchersException.class, () -> {
			sellerImpl.createNewSeller(paypalEmail);
		});
	}
	
	
	@Test
	public void testCreateNewSeller_8() {
		// Arrange
		String paypalEmail = "paypal@gmail.com";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(null);
		
		boolean result = sellerImpl.createNewSeller(paypalEmail);
		assertTrue(result);
	}
	
	
	@Test
	public void testCreateNewSeller_9() {
		// Arrange
		String paypalEmail = "paypal@gmail.com";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(any(Seller.class));
		
		assertThrows(InvalidUseOfMatchersException.class, () -> {
			sellerImpl.createNewSeller(paypalEmail);
		});
	}
	
	@Test
	public void testCreateNewSeller_10() {
		// Arrange
		String paypalEmail = "paypal@gmail.com";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(null);
		
		boolean result = sellerImpl.createNewSeller(paypalEmail);
		assertTrue(result);
	}
	
	@Test
	public void testCreateNewSeller_12() {
		// Arrange
		String paypalEmail = " ";
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
		
		when(userRepo.findById(1L)).thenReturn(Optional.of(user));
		when(sellerRepo.findByPaypalEmail(paypalEmail)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> {
			sellerImpl.createNewSeller(paypalEmail);
		});
	}
}
