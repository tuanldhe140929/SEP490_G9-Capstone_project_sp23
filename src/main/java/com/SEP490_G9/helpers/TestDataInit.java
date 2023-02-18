package com.SEP490_G9.helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.SEP490_G9.models.Entities.Tag;
import com.SEP490_G9.models.Entities.Type;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.RefreshToken;
import com.SEP490_G9.models.Entities.Role;
import com.SEP490_G9.repositories.TagRepository;
import com.SEP490_G9.repositories.TypeRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.repositories.AccountRepository;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.repositories.RoleRepository;

@Component
public class TestDataInit implements ApplicationRunner {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private TypeRepository typeRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

//	@Autowired
//	private PreviewRepository previewRepository;

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
		user.setUserCreatedDate(new Date());
		user.setAccountCreatedDate(new Date());
		user.setFirstName("John");
		user.setLastName("Doe");
		userRepository.save(user);

//		userAccount.setId((long) 2);
//		userAccount.setEmail("user2@gmail.com");
//		userAccount.setRoles(userRoles);
//		accountRepository.save(userAccount);

		user.setId((long) 2);
		user.setEmail("user2@gmail.com");
		user.setUsername("user2");
		user.setRoles(sellerRoles);
		userRepository.save(user);


		Account staffAccount = new Account();
		staffAccount.setId((long) 3);
		staffAccount.setEmail("staff1@gmail.com");
		staffAccount.setPassword(new BCryptPasswordEncoder().encode("staff1234"));
		staffAccount.setRoles(staffRoles);
		staffAccount.setAccountCreatedDate(new Date());
		staffAccount.setAccountLastModifed(null);
		accountRepository.save(staffAccount);

		Account adminAccount = new Account();
		adminAccount.setId((long) 4);
		adminAccount.setEmail("admin@gmail.com");
		adminAccount.setPassword(new BCryptPasswordEncoder().encode("admin1234"));
		adminAccount.setRoles(adminRoles);
		adminAccount.setAccountCreatedDate(new Date());
		adminAccount.setAccountLastModifed(null);
		accountRepository.save(adminAccount);


		tagRepository.save(new Tag(1, "2D"));
		tagRepository.save(new Tag(2, "3D"));
		tagRepository.save(new Tag(3, "adventure"));
		tagRepository.save(new Tag(4, "sci-fi"));
		tagRepository.save(new Tag(5, "sport"));

		typeRepository.save(new Type(1, "Sprites"));
		typeRepository.save(new Type(2, "Sound effects"));
		typeRepository.save(new Type(3, "Music"));
		typeRepository.save(new Type(4, "Textures"));
		typeRepository.save(new Type(5, "Characters"));
		typeRepository.save(new Type(6, "Tileset"));
		typeRepository.save(new Type(7, "Backgrounds"));
		typeRepository.save(new Type(8, "Fonts"));
		typeRepository.save(new Type(9, "Icons"));
		typeRepository.save(new Type(10, "Tileset"));
		typeRepository.save(new Type(11, "User interfaces"));
		typeRepository.save(new Type(12, "Lore"));
		typeRepository.save(new Type(13, "Others"));


		



	}
}