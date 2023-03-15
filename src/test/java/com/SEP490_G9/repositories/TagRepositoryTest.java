package com.SEP490_G9.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.repository.TagRepository;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class TagRepositoryTest {

	@Autowired
	TagRepository tagRepo;
	
	@Test
	void testFindAll() {
		Tag tag1 = new Tag(1, "Horror");
		Tag tag2 = new Tag(2, "Adventure");
		Tag tag3 = new Tag(3, "Action");
		tagRepo.save(tag1);
		tagRepo.save(tag2);
		tagRepo.save(tag3);
		List<Tag> expected = tagRepo.findAll();
		assertThat(expected.size()).isEqualTo(3);
	}
	
	@Test
	void testExistByName() {
		Tag tag1 = new Tag(1, "Family");
		tagRepo.save(tag1);
		boolean existed = tagRepo.existsByName("Horror");
		assertThat(existed).isEqualTo(false);
	}
	
	@Test
	void testFindById() {
		Tag tag1 = new Tag(1, "Fighting");
		tagRepo.save(tag1);
		Tag result = tagRepo.findById(1);
		System.out.println(tag1);
		assertThat(result).isEqualTo(tag1);
	}
}
