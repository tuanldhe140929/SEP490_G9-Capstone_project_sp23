package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.embeddable.ProductVersionKey;
import com.SEP490_G9.repository.ProductDetailsRepository;
import com.SEP490_G9.repository.ProductRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class ProductDetailsRepositoryTest {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductDetailsRepository pdRepo;

	@Test
	void testFindByProductIdAndVersion() {
		Seller seller = new Seller();
		seller.setId((long) 2);

		Product product = new Product();
		product.setId(1L);
		product.setSeller(seller);
		product.setActiveVersion("1.0.0");
		product= productRepository.save(product);
		System.out.println(product);
		
		ProductDetails pd = new ProductDetails();
		ProductVersionKey key = new ProductVersionKey();
		key.setProductId(1L);
		key.setVersion("1.0.0");
		pd.setProductVersionKey(key);
		pd.setProduct(product);
		pd.setVersion("1.0.0");
		pd = pdRepo.save(pd);

		System.out.println(pdRepo.findAll());
		ProductDetails result = pdRepo.findByProductIdAndProductVersionKeyVersion(product.getId(), "1.0.0");

		assertThat(result).isEqualTo(pd);
	}

}
