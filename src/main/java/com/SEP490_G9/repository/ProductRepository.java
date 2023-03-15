package com.SEP490_G9.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.Seller;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	public List<Product> findBySeller(Seller user);

	public List<Product> findBySellerId(Long sellerId);

	public List<Product> findBySellerIdAndEnabled(Long id, boolean enabled);

	public List<Product> findByEnabled(boolean enabled);

	public List<Product> findByApproved(boolean b);

	public List<Product> findBySellerIdAndApproved(Long id, boolean b);

}
