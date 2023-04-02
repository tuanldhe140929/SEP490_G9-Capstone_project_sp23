package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.UserDetailsImpl;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.common.Constant;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.service.SellerService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Service
public class SellerServiceImpl implements SellerService {

	@Autowired
	SellerRepository sellerRepository;
	@Autowired
	EntityManager entityManager;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AccountRepository accountRepository;

	@Override
	public Seller getSellerById(Long sellerId) {
		Seller seller = sellerRepository.findById(sellerId).get();
		if (seller == null) {
			throw new ResourceNotFoundException("seller", sellerId.toString(), "null");
		}
		return seller;
	}

	@Override
	public Seller getSellerByAProduct(long productId) {
		Product product = productRepository.findById(productId).get();
		Seller seller = product.getSeller();
		return seller;
	}

	@Override
	public int getSellerNumberOfFlags(long sellerId) {
		int amountOfFlags = 0;
		Seller seller = sellerRepository.findById(sellerId);
		List<Product> sellerProducts = seller.getProducts();
		for(Product product: sellerProducts) {
			if(!product.isEnabled()) {
				amountOfFlags = amountOfFlags + 1;
			}
		}
		return amountOfFlags;
	}

	//supporting methods
	
	@Override
	public List<Seller> getAllSellers() {
		List<Seller> allSellers = sellerRepository.findAll();
		return allSellers;
	}

	@Override
	public List<Seller> getAllEnabledSellers(List<Seller> sellerList) {
		List<Seller> allEnabled = new ArrayList<>();
		List<Account> allAccount = accountRepository.findAll();
		for(Seller seller: sellerList) {
			for(Account account: allAccount) {
				if(account.getId().equals(seller.getId()) && account.isEnabled()) {
					allEnabled.add(seller);
				}
			}
		}
		return allEnabled;
	}

	@Override
	public List<Seller> getFlaggedSellers(List<Seller> sellerList) {
		List<Seller> flaggedSellers = new ArrayList<>();
		for(Seller seller: sellerList) {
			if(getSellerNumberOfFlags(seller.getId())>0) {
				flaggedSellers.add(seller);
			}
		}
		return flaggedSellers;
	}

	@Override
	public List<Seller> getSellersByKeyword(List<Seller> sellerList, String keyword) {
		List<Seller> sellerByKeyword = new ArrayList<>();
		for(Seller seller: sellerList) {
			if(seller.getUsername().trim().toLowerCase().replaceAll("\\s+", " ").contains(keyword.trim().toLowerCase().replaceAll("\\s+", " "))) {
				sellerByKeyword.add(seller);
			}
		}
		return sellerByKeyword;
	}
	
	//main method
	@Override
	public List<Seller> getSellersForSearching(String keyword) {
		List<Seller> allSellers = getAllSellers();
		List<Seller> allEnabled	= getAllEnabledSellers(allSellers);
		List<Seller> allByKeyword = getSellersByKeyword(allEnabled, keyword);
		return allByKeyword;
	}
	@Override
	public Seller getSellerByPaypalEmail(String paypalEmail) {
	    return sellerRepository.findByPaypalEmail(paypalEmail);
	}
	@Transactional 
	@Override
	public boolean createNewSeller(String paypalEmail) {		
		Account account = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
	            .getAccount();
		User user = userRepository.findById(account.getId()).get();
	   
		
		
	    if (!isValidEmail(paypalEmail)) {
	        throw new IllegalArgumentException("Invalid email format");
	    }

	    // check if email is already used by another seller
	    Seller existingSeller = sellerRepository.findByPaypalEmail(paypalEmail);
	    if (existingSeller != null) {
	        throw new IllegalArgumentException("Email is already used by another seller");
	    }
	    
	    
	    String sql = "insert into sellers(paypal_email,seller_enabled,account_id) values(?,?,?)";
	    Query query = entityManager.createNativeQuery(sql);
	    query.setParameter(1, paypalEmail);
	    query.setParameter(2, true);
	    query.setParameter(3,  account.getId());
	    
	    int c =query.executeUpdate();
	    if (c!=0) {
	    	Role role1= new Role(Constant.SELLER_ROLE_ID,"ROLE_SELLER");
	    	Role role2= new Role(Constant.USER_ROLE_ID,"ROLE_USER");
	    	List<Role> list = new ArrayList<>();
	    	list.add(role1);
	    	list.add(role2);
	    	user.setRoles(list);
	    	userRepository.save(user);
	    }
	    
	   return true;
	}
	private boolean isValidEmail(String email) {
	    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
	                        "[a-zA-Z0-9_+&*-]+)*@" +
	                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
	                        "A-Z]{2,7}$";
	    Pattern pattern = Pattern.compile(emailRegex);
	    return pattern.matcher(email).matches();
	}
	
	
}
