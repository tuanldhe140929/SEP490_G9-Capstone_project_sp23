package com.SEP490_G9.helpers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.SEP490_G9.models.Entities.Tag;
import com.SEP490_G9.models.Entities.Type;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.Role;
import com.SEP490_G9.repositories.TagRepository;
import com.SEP490_G9.repositories.TypeRepository;
import com.SEP490_G9.repositories.UserRepository;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.repositories.RoleRepository;

@Component
public class TestDataInit implements ApplicationRunner {

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

		roleRepository.save(new Role((long) 1, "ROLE_ADMIN"));
		roleRepository.save(new Role((long) 2, "ROLE_USER"));
		roleRepository.save(new Role((long) 3, "ROLE_INSPECTOR"));

		User user = new User();
		user.setId((long)1);

		user.setEmail("namdinh@gmail.com");
		user.setPassword(new BCryptPasswordEncoder().encode("admin1234"));
		user.setUsername("namdinh");
		user.setRole(roleRepository.getReferenceById((long) 2));
		userRepository.save(user);


		user.setId((long) 2);
		user.setRole(roleRepository.getReferenceById((long) 2));
		user.setUsername("abcd");
		user.setEmail("toanpk@gmail.com");
		user.setPassword(new BCryptPasswordEncoder().encode("12345678"));
		userRepository.save(user);
    

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

		Product product = new Product();

		product.setId((long)1);
		product.setUser(userRepository.getReferenceById((long) 1));

		product.setName("TEST PRODUCT");
		product.setType(typeRepository.getReferenceById(7));
		List<Tag> tags = new ArrayList<>();
		tags.add(tagRepository.getReferenceById(1));
		tags.add(tagRepository.getReferenceById(2));
		product.setTags(tags);
		productRepository.save(product);

	}
}