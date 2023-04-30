package com.SEP490_G9.service.serviceImpls;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.SEP490_G9.entities.Product;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.ProductFile;
import com.SEP490_G9.repository.ProductFileRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.service.CartService;

@Configuration
@EnableScheduling
public class FileDeleteCronJob {

	@Value("${root.location}")
	private String ROOT_LOCATION;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductFileRepository productFileRepository;

	@Autowired
	CartService cartService;
	@Scheduled(cron = "0 0 20 * * SUN") // Execute at 8pm every Sunday
	public void scheduledDeleteFile() {
System.out.println("current date");
		List<Product> deleteProducts = new ArrayList<>();
		List<Product> disabledProduct = productRepository.findByEnabled(false);
		for (Product product : disabledProduct) {
			if (cartService.isProductPurchased(product.getId()) && checkDate(product)) {
				deleteProducts.add(product);
			}
		}

		for (Product product : deleteProducts) {
			ArrayList<ProductFile> deleteFiles = new ArrayList<>();
			for (ProductDetails pd : product.getProductDetails()) {
				deleteFiles.addAll(pd.getFiles());
			}

			for (ProductFile file : deleteFiles) {
				File physicFile = new File(this.ROOT_LOCATION + file.getSource());
				if (physicFile.exists()) {
					if (physicFile.delete()) {
						System.out.println("File deleted successfully.");
						file.setDeleted(true);
					} else {
						System.out.println("Failed to delete the file.");
					}
				} else {
					System.out.println("The file does not exist.");
				}
			}
			productFileRepository.saveAll(deleteFiles);
		}
	}

	private boolean checkDate(Product product) {
		boolean ret = false;
		Long currentDate = new Date().getTime();
		Long targetDate = product.getLastModified().getTime();
		Long result = currentDate - targetDate;
		if (result < 1000 * 60 * 60 * 24 * 15) {
			ret = true;
		}
		return ret;
	}

}
