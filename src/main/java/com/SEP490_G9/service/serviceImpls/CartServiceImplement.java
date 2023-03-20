package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.dto.CartDTO;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Cart;
import com.SEP490_G9.entities.CartItem;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Transaction;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.entities.embeddable.CartItemKey;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.CartItemRepository;
import com.SEP490_G9.repository.CartRepository;
import com.SEP490_G9.repository.PreviewRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.TransactionRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.service.CartService;

import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.repository.ProductDetailsRepository;

@Service
public class CartServiceImplement implements CartService {
	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	PreviewRepository previewRepo;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	ProductDetailsRepository productDetailsRepository;
	@Autowired
	CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	TransactionRepository transactionRepo;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	UserRepository userRepo;

	@Override

	public CartDTO addProduct(Long productId) {
		Cart cart = getCurrentCart();
		boolean userOwnProduct = isUserOwnProduct(cart.getUser(), productId);
		if (userOwnProduct) {
			throw new IllegalArgumentException("User had already own product");
		}

		for (CartItem item : cart.getItems()) {
			if (item.getProductDetails().getProduct().getId() == productId) {
				throw new IllegalArgumentException("Cart already has item");
			}
		}

		Product product = productRepository.findById(productId).orElseThrow();
		ProductDetails activeVersion = null;
		for (ProductDetails pd : product.getProductDetails()) {
			if (pd.getVersion().equalsIgnoreCase(product.getActiveVersion()))
				activeVersion = pd;
			break;
		}
		if (activeVersion == null) {
			throw new ResourceNotFoundException("Product", "active version", productId);
		}

		CartItem item = new CartItem(cart, activeVersion);
		cart.addItem(item);
		cartItemRepository.save(item);
		CartDTO cartDTO = new CartDTO(getCurrentCart());
		return cartDTO;
	}

	public boolean isUserOwnProduct(User user, Long productId) {
		boolean ret = false;
		List<Cart> carts = cartRepository.findByUser(user);
		List<Cart> purchasedCart = new ArrayList<>();
		for (Cart cart : carts) {
			if (isCartHadPurchased(cart))
				purchasedCart.add(cart);
		}
		for (Cart cart : purchasedCart) {
			for (CartItem item : cart.getItems()) {
				if (item.getProductDetails().getProduct().getId() == productId)
					ret = true;
			}
		}
		return ret;
	}

	private boolean isCartHadPurchased(Cart cart) {
		for (Transaction transaction : cart.getTransactions()) {
			if (transaction.getStatus().equals(Transaction.Status.COMPLETED)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public CartDTO removeProduct(Long productId) {
		Cart cart = getCurrentCart();
		CartItem itemToRemove = null;
		for (CartItem item : cart.getItems()) {
			if (item.getProductDetails().getProductVersionKey().getProductId() == productId) {
				itemToRemove = item;
				break;
			}
		}
		if (itemToRemove != null) {
			cart.getItems().remove(itemToRemove);
			cartItemRepository.delete(itemToRemove);
		} else {
			throw new ResourceNotFoundException("item", "id", productId);
		}
		CartDTO cartDto = new CartDTO(getCurrentCart());
		return cartDto;

	}

	public CartDTO removeAllProduct(Long productId) {
		Cart cart = getCurrentCart();
		CartItem itemToRemove = null;

		// Find the CartItem in the cart with the given productId
		for (CartItem item : cart.getItems()) {
			if (item.getProductDetails().getProductVersionKey().getProductId().equals(productId)) {
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
		return new CartDTO(getCurrentCart());

	}

	@Override
	public Cart getCurrentCart() {
		Cart ret = null;
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getAccount();
		User user = userRepo.findById(account.getId()).get();

		boolean userOwnCart = cartRepository.existsByUser(user);
		if (!userOwnCart) {
			Cart newCart = new Cart();
			newCart.setUser(user);
			ret = cartRepository.save(newCart);
		} else {
			Cart cart = cartRepository.findFirstByUserOrderByIdDesc(user);
			boolean cartIsPurchased = isCartHadPurchased(cart);
			if (cartIsPurchased) {
				Cart newCart = new Cart();
				newCart.setUser(user);
				ret = cartRepository.save(newCart);
			} else {
				List<CartItem> updatedItems = new ArrayList<>();
				for (CartItem item : cart.getItems()) {
					CartItem updatedItem = new CartItem();

					Long productId = item.getProductDetails().getProductVersionKey().getProductId();
					String activeVersion = item.getProductDetails().getProduct().getActiveVersion();

					ProductDetails pd = productDetailsRepository.findByProductIdAndProductVersionKeyVersion(productId,
							activeVersion);
					updatedItem.setProductDetails(pd);

					CartItemKey cartItemKey = new CartItemKey();
					cartItemKey.setCartId(cart.getId());
					cartItemKey.setProductVersionKey(pd.getProductVersionKey());
					updatedItem.setCartItemKey(cartItemKey);
					updatedItems.add(updatedItem);
				}
				cart.setItems(updatedItems);
				ret = cartRepository.save(cart);
			}
		}
		return ret;
	}

	@Override
	public Cart getCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId).get();
		return cart;
	}

	@Override
	public List<Cart> getByUser(User user) {
		List<Cart> carts = cartRepository.findByUser(user);
		return carts;
	}
}
