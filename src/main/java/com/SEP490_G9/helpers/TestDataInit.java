package com.SEP490_G9.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.SEP490_G9.models.DTOS.Category;
import com.SEP490_G9.models.DTOS.Role;
import com.SEP490_G9.repositories.CategoryRepository;
import com.SEP490_G9.repositories.RoleRepository;


@Component
public class TestDataInit implements ApplicationRunner {

	@Autowired
    private RoleRepository roleRepository;

	@Autowired
	private CategoryRepository categoryRepository;
	
//	@Autowired
//	private PreviewRepository previewRepository;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
	
		roleRepository.save(new Role((long) 1,"ROLE_ADMIN"));
		roleRepository.save(new Role((long) 2,"ROLE_USER"));
		roleRepository.save(new Role((long) 3,"ROLE_INSPECTOR"));
		
		categoryRepository.save(new Category((long)1,"2D"));
		categoryRepository.save(new Category((long)2,"3D"));
		
	}
}