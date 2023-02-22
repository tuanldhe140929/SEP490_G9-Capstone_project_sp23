package com.SEP490_G9.service.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.dto.CartDTO;
import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.Cart;
import com.SEP490_G9.entity.CartItem;
import com.SEP490_G9.entity.Product;
import com.SEP490_G9.entity.Transaction;
import com.SEP490_G9.entity.User;
import com.SEP490_G9.entity.UserDetailsImpl;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.CartItemRepository;
import com.SEP490_G9.repository.CartRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.CartService;


@Service
public class CartServiceImplement implements CartService {
	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	UserRepository userRepo;


	@Override
	public CartDTO addProduct(Long productId) {

		Product product = productRepository.findById(productId).get();
		Cart cart = getCurrentCart();
		CartItem item = new CartItem(cart, product);
		cart.addItem(item);
		cartItemRepository.save(item);
		CartDTO cartDTO = new CartDTO(cart);
		return cartDTO;
	}

	@Override
	public CartDTO removeProduct(Long productId) {
		Cart cart = getCurrentCart();
		CartItem itemToRemove = null;
		for (CartItem item : cart.getItems()) {
			if (item.getProduct().getId() == productId) {
				itemToRemove = item;
				break;
			}
		}
		if (itemToRemove != null) {
			cart.getItems().remove(itemToRemove);
			cartItemRepository.delete(itemToRemove);
		} else {
			throw new ResourceNotFoundException("Product with id " + productId + " not found in cart.", null,
					itemToRemove);
		}
		CartDTO cartDto = new CartDTO(cart);

		return cartDto;
	}

	public CartDTO removeAllProduct(Long productId) {
		Cart cart = getCurrentCart();
		CartItem itemToRemove = null;

		// Find the CartItem in the cart with the given productId
		for (CartItem item : cart.getItems()) {
			if (item.getProduct().getId().equals(productId)) {
				itemToRemove = item;
				break;
			}
		}

		if (itemToRemove != null) {
			// Remove the CartItem from the cart
			cart.getItems().remove(itemToRemove);

			// Update the cart in the database
			cartRepository.save(cart);
		}

		// Convert the updated cart to a CartDTO and return it
		return new CartDTO(cart);

	}

	private Cart getCurrentCart() {

		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		User user = userRepo.getReferenceById(account.getId());
		Cart cart = cartRepository.findCurrentCart(account.getId());

		Cart retCart = null;
		if (cart == null) {
			retCart = cartRepository.save(new Cart(user));

		} else {
			retCart = checkTransactionAndReturnCart(cart, user);
		}
		return retCart;

	}

	@Override
	public CartDTO getCurrentCartDTO() {
		Cart cart = getCurrentCart();
		CartDTO cartDTO = new CartDTO(cart);
		return cartDTO;
	}

	@Override
	public Cart getCart(Long cartId) {
		Cart cart = cartRepository.getReferenceById(cartId);
		return cart;
	}

	private Cart checkTransactionAndReturnCart(Cart cart, Account account) {
		Transaction transactions = transactionRepository.findByCartId(cart.getId());
		if (transactions == null) {
			return cart;
		} else {
			Cart newCart = cartRepository.save(new Cart(account));
			return newCart;
		}

	}

	@Override
	public CartDTO checkOut(Cart cart, Account account) {
		// Validate that the user exists
		account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		User user = userRepo.getReferenceById(account.getId());
		if (user == null) {
			// Error: User not found
			return null;
		}

		// Validate that the cart is not empty
		cart = getCurrentCart();
		if (cart == null) {
			// Error: Cart is empty
			return null;
		}

		// Create a new transaction
		Transaction transaction = new Transaction();

		// Set the transaction details

		// Save the transaction
		transactionRepository.save(transaction);

		// Clear the cart
		cart = new Cart();

		// Return the transaction details
		CartDTO cartDto = new CartDTO();
		// ....
		return cartDto;
	}

}
