package com.SEP490_G9.services.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.ResourceNotFoundException;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.models.DTOS.CartDTO;
import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.CartItem;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.Transaction;
import com.SEP490_G9.models.Entities.User;

import com.SEP490_G9.repositories.CartItemRepository;
import com.SEP490_G9.repositories.CartRepository;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.repositories.TransactionRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.CartService;

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

	private Cart getCurrentCart() {

		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		Cart cart =cartRepository.findCurrentCart(user.getId());
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

	private Cart checkTransactionAndReturnCart(Cart cart, User user) {
		Transaction transactions = transactionRepository.findByCartId(cart.getId());
		if (transactions == null) {
			return cart;
		} else {
			Cart newCart = cartRepository.save(new Cart(user));
			return newCart;
		}

	}

	@Override
	public CartDTO checkOut(Cart cart, User user) {
	    // Validate that the user exists
	    user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
	    if(user == null) {
	        // Error: User not found
	        return null;
	    }

	    // Validate that the cart is not empty
	    cart = getCurrentCart();
	    if(cart == null) {
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
	    //....
	    return cartDto;
	}

    
        
    
		
	
	}
	
