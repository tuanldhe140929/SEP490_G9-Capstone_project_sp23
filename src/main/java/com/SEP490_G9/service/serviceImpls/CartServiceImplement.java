package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
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
import com.SEP490_G9.service.ProductDetailsService;
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
	ProductDetailsService productDetailsService;

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
	public Cart addItem(Long productId, Long cartId) {
		Cart cart = getById(cartId);
		if (isUserPurchasedProduct(cart.getUser().getId(), productId)) {
			throw new IllegalArgumentException("User had already own product");
		}

		for (CartItem item : cart.getItems()) {
			if (item.getProductDetails().getProduct().getId() == productId) {
				throw new IllegalArgumentException("Cart already has item");
			}
		}
		ProductDetails activeVersion = productDetailsService.getActiveVersion(productId);
		CartItem item = new CartItem(cart, activeVersion);
		cartItemRepository.save(item);
		cart.addItem(item);
		Cart ret = cartRepository.save(cart);
		return ret;
	}

	@Override
	public boolean isCartHadPurchased(Long cartId) {
		Cart cart = cartRepository.findById(cartId).orElseThrow();
		for (Transaction transaction : cart.getTransactions()) {
			if (transaction.getStatus().equals(Transaction.Status.COMPLETED)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Cart removeItem(Long productId, Long cartId) {
		Cart cart = getById(cartId);
		CartItem itemToRemove = null;
		for (CartItem item : cart.getItems()) {
			if (item.getProductDetails().getProductVersionKey().getProductId() == productId) {
				itemToRemove = item;
				break;
			}
		}
		if (itemToRemove == null) {
			throw new IllegalArgumentException("Cart does not have product with id:" + productId);
		} else {
			cart.getItems().remove(itemToRemove);
			Cart ret = cartRepository.save(cart);
			return ret;
		}
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
			cartItemRepository.delete(itemToRemove);
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

		// chua co cart thi tao cart moi
		if (!cartRepository.existsByUserId(user.getId())) {
			Cart cart = new Cart();
			cart.setUser(user);
			ret = createCart(cart);
		} else {
			Cart cart = cartRepository.findFirstByUserOrderByIdDesc(user);
			boolean cartIsPurchased = isCartHadPurchased(cart.getId());
			// cart da thanh toan thi tao cart moi
			if (cartIsPurchased) {
				Cart newCart = new Cart();
				newCart.setUser(user);
				ret = createCart(cart);
				// cart chua thanh toan thi update lai active version
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
	public Cart createCart(Cart cart) {
		Cart saved = cartRepository.save(cart);
		return saved;
	}

	@Override
	public Cart getById(Long cartId) {
		Cart cart = cartRepository.findById(cartId).orElseThrow();
		return cart;
	}

	@Override
	public List<Cart> getByUserId(Long userId) {
		List<Cart> carts = cartRepository.findByUserId(userId);
		return carts;
	}

	@Override
	public boolean isUserOwnCart(Long userId, Long cartId) {
		return cartRepository.existsByIdAndUserId(cartId, userId);
	}

	@Override
	public boolean isUserPurchasedProduct(Long userId, Long productId) {
		boolean ret = false;
		List<Cart> carts = cartRepository.findByUserId(userId);
		List<Cart> purchasedCart = new ArrayList<>();
		for (Cart cart : carts) {
			if (isCartHadPurchased(cart.getId()))
				purchasedCart.add(cart);
		}
		for (Cart cart : purchasedCart) {
			for (CartItem item : cart.getItems()) {
				if (item.getProductDetails().getProduct().getId() == productId)
					;
				ret = true;
			}
		}
		return ret;
	}
}
