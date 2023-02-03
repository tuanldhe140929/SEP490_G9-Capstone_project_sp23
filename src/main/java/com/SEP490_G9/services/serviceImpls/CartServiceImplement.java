package com.SEP490_G9.services.serviceImpls;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.ResourceNotFoundException;
import com.SEP490_G9.models.UserDetailsImpl;
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
	public Cart addProduct(Long productId) {
		CartItem item = new CartItem();
		Product product = productRepository.getReferenceById(productId);
		item.setProduct(product);
		Cart cart = getCurrentCart();
		cart.addItem(item);
		cartItemRepository.saveAll(cart.getItems());
		return cart;
	}

	@Override
	public Cart removeProduct(Long productId) {
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
	        throw new ResourceNotFoundException("Product with id " + productId + " not found in cart.", null, itemToRemove);
	    }
	    return cart;
	}

	@Override
	public Cart getCurrentCart() {
		
		//co roi thi check bang transaction xem co cart id chua -> co roi -> thanh toan roi -> phai tao cart moi
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		 return cartRepository.findByUserId(user.getId());
	}

	@Override
	public Cart getCart(Long cartId) {
		Cart cart = cartRepository.getReferenceById(cartId);
		return cart;
	}
	public Cart createCart(Long cartId) {
	    List<Transaction> transactions = transactionRepository.findByCartId(cartId);
	    if (transactions.isEmpty()) {
	        Cart cart = new Cart();
	        cartRepository.save(cart);
	        return cart;
	    } else {
	        Cart cart = new Cart();
	        cartRepository.save(cart);
	        return cart;
	    }
	}
   
}