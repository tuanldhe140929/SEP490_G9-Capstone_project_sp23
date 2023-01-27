package com.SEP490_G9.services.serviceImpls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.ResourceNotFoundException;
import com.SEP490_G9.models.Entities.Cart;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.services.CartService;

@Service
public class CartServiceImplement implements CartService {

	@Override
	public Cart addProduct(Long productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cart removeProduct(Long productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cart getCurrentCart() {
		// TODO Auto-generated method stub
		return null;
	}

   
}