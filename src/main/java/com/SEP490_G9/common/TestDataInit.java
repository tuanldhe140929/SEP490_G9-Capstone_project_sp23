package com.SEP490_G9.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Category;
import com.SEP490_G9.entities.License;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.entities.Seller;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.CategoryRepository;
import com.SEP490_G9.repository.LicenseRepository;
import com.SEP490_G9.repository.ProductRepository;
import com.SEP490_G9.repository.RoleRepository;
import com.SEP490_G9.repository.SellerRepository;
import com.SEP490_G9.repository.TagRepository;
import com.SEP490_G9.repository.UserRepository;
import com.SEP490_G9.repository.ViolationTypeRepository;

@Component
public class TestDataInit implements ApplicationRunner {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private ViolationTypeRepository violationTypeRepository;

	@Autowired
	private LicenseRepository licenseRepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		roleRepository.save(new Role(Constant.ADMIN_ROLE_ID, "ROLE_ADMIN"));
		roleRepository.save(new Role(Constant.STAFF_ROLE_ID, "ROLE_STAFF"));
		roleRepository.save(new Role(Constant.USER_ROLE_ID, "ROLE_USER"));
		roleRepository.save(new Role(Constant.SELLER_ROLE_ID, "ROLE_SELLER"));

		List<Role> userRoles = new ArrayList<>();
		userRoles.add(roleRepository.getReferenceById(Constant.USER_ROLE_ID));

		List<Role> staffRoles = new ArrayList<>();
		staffRoles.add(roleRepository.getReferenceById(Constant.STAFF_ROLE_ID));

		List<Role> adminRoles = new ArrayList<>();
		adminRoles.add(roleRepository.getReferenceById(Constant.ADMIN_ROLE_ID));

		List<Role> sellerRoles = new ArrayList<>();
		sellerRoles.add(roleRepository.getReferenceById(Constant.USER_ROLE_ID));
		sellerRoles.add(roleRepository.getReferenceById(Constant.SELLER_ROLE_ID));

//		Account userAccount = new Account();
//		userAccount.setId((long) 1);
//		userAccount.setEmail("user1@gmail.com");
//		userAccount.setPassword(new BCryptPasswordEncoder().encode("user1234"));
//		userAccount.setRoles(userRoles);
//		userAccount.setAccountCreatedDate(new Date());
//		userAccount.setAccountLastModifed(null);
//		accountRepository.save(userAccount);

		User user = new User();

		user.setId((long) 1);
		user.setEmail("user1@gmail.com");
		user.setPassword(new BCryptPasswordEncoder().encode("user1234"));
		user.setUsername("user1");
		user.setRoles(userRoles);
		user.setCreatedDate(new Date());
		user.setFirstName("John");
		user.setLastName("Doe");
		userRepository.save(user);

//		userAccount.setId((long) 2);
//		userAccount.setEmail("user2@gmail.com");
//		userAccount.setRoles(userRoles);
//		accountRepository.save(userAccount);

		Seller seller = new Seller();
		seller.setId((long) 2);
		seller.setEmail("seller1@gmail.com");
		seller.setPassword(new BCryptPasswordEncoder().encode("seller1234"));
		seller.setUsername("seller1");
		seller.setRoles(sellerRoles);
		seller.setCreatedDate(new Date());
		seller.setFirstName("John");
		seller.setLastName("Doe");
		seller.setSellerEnabled(true);
		sellerRepository.save(seller);

		Account staffAccount = new Account();
		staffAccount.setId((long) 3);
		staffAccount.setEmail("staff1@gmail.com");
		staffAccount.setPassword(new BCryptPasswordEncoder().encode("staff1234"));
		staffAccount.setRoles(staffRoles);
		staffAccount.setCreatedDate(new Date());
		accountRepository.save(staffAccount);

		Account adminAccount = new Account();
		adminAccount.setId((long) 4);
		adminAccount.setEmail("admin@gmail.com");
		adminAccount.setPassword(new BCryptPasswordEncoder().encode("admin1234"));
		adminAccount.setRoles(adminRoles);
		adminAccount.setCreatedDate(new Date());
		accountRepository.save(adminAccount);

		tagRepository.save(new Tag(1, "2D"));
		tagRepository.save(new Tag(2, "3D"));
		tagRepository.save(new Tag(3, "adventure"));
		tagRepository.save(new Tag(4, "sci-fi"));
		tagRepository.save(new Tag(5, "sport"));

		categoryRepository.save(new Category(1, "Sprites"));
		categoryRepository.save(new Category(2, "Sound effects"));
		categoryRepository.save(new Category(3, "Music"));
		categoryRepository.save(new Category(4, "Textures"));
		categoryRepository.save(new Category(5, "Characters"));
		categoryRepository.save(new Category(6, "Tileset"));
		categoryRepository.save(new Category(7, "Backgrounds"));
		categoryRepository.save(new Category(8, "Fonts"));
		categoryRepository.save(new Category(9, "Icons"));
		categoryRepository.save(new Category(10, "Tileset"));
		categoryRepository.save(new Category(11, "User interfaces"));
		categoryRepository.save(new Category(12, "Lore"));
		categoryRepository.save(new Category(13, "Others"));

		violationTypeRepository.save(new ViolationType(1, "Spam"));
		violationTypeRepository.save(new ViolationType(2, "Lừa đảo"));
		violationTypeRepository.save(new ViolationType(3, "Đạo nhái sản phẩm"));
		violationTypeRepository.save(new ViolationType(4, "Nội dung không lành mạnh"));
		violationTypeRepository.save(new ViolationType(5, "Kích động thù ghét"));

		License license2 = new License();
		license2.setId(1);
		license2.setName("All Rights Reserved");
		license2.setAcrynosm("");
		license2.setDetails(
				"The copyright owner exclusive rights to control the use, distribution, and modification of their work, and anyone who wishes to use the work in any way must obtain permission from the copyright owner. This license is the most restrictive and does not allow for any form of sharing, copying, or modification without explicit permission from the copyright holder.");
		licenseRepo.save(license2);

		License license = new License();
		license.setId(2);
		license.setName("Attribution-NonCommercial");
		license.setAcrynosm("CC BY-NC");
		license.setDetails(
				"Others are allowed to use, distribute, and build upon the licensed work for non-commercial purposes, as long as they give appropriate credit to the original creator. However, if someone wants to use the work for commercial purposes, they must first obtain permission from the copyright holder");
		licenseRepo.save(license);

	}
}