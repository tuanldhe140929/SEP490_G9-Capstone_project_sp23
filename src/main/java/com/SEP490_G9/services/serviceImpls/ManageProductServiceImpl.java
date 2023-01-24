package com.SEP490_G9.services.serviceImpls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.ManageProductService;

@Service
public class ManageProductServiceImpl implements ManageProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public User getCurrentUserInfo() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		return user;
	}

	@Override
	public Product addProduct(Product product) {
		Product ret = null;
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		product.setUser(user);
		try {
			ret = productRepository.save(product);
		} catch (Exception e) {
			// throw new CustomException("Them san pham khong thanh cong");
		}
		return ret;
	}

	@Override
	public Product updateProduct(Product product) {
		Product ret = null;
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		try {
			Product found = productRepository.findByUserAndId(user, product.getId());
			ret = productRepository.save(product);
		} catch (Exception e) {
			// throw new CustomException("Cap nhat san pham khong thanh cong");
		}
		return ret;
	}

	@Override
	public Product deleteProduct(Long id) {
		Product ret = null;
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		try {
			Product found = productRepository.getReferenceById(id);
			ret = productRepository.findByUserAndId(user, id);
			productRepository.deleteById(id);
		} catch (Exception e) {
			// throw new CustomException("Xoa san pham khong thanh cong");
		}
		return ret;
	}

	@Override
	public List<Product> getProductsByUser() {
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		return productRepository.findByUser(user);
	}

}
