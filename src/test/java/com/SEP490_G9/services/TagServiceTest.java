package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.repository.TagRepository;
import com.SEP490_G9.service.TagService;
import com.SEP490_G9.service.serviceImpls.TagServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class TagServiceTest {

	@Mock
	TagRepository tagRepo;
	
	@InjectMocks
	TagServiceImpl tagService;
	
	@Test
	void testGetAllTags() {
		Tag tag1 = new Tag(1, "Family");
		Tag tag2 = new Tag(2, "Horror");
		Tag tag3 = new Tag(3, "Action");
		List<Tag> expected = new ArrayList<>();
		expected.add(tag1);
		expected.add(tag2);
		expected.add(tag3);
		List<Tag> result = tagService.getAllTags();
		assertThat(result).isEqualTo(expected);
	}
	
	@Test
	void testAddTag() {
		boolean expected = true;
		Tag tag = new Tag(50, "Action");
		Mockito.when(tagRepo.save(tag)).thenReturn(tag);
		boolean result = tagService.addTag(tag);
		assertEquals(expected, result);
	}
	
	@Test
	void testUpdateTag() {
		boolean expected = true;
		Tag tag1 = new Tag(100, "RPG");
		tagRepo.save(tag1);
		Tag tag2 = new Tag(100, "Sport");
		Mockito.when(tagRepo.save(tag2)).thenReturn(tag2);
		boolean result = tagService.addTag(tag2);
		assertEquals(expected, result);
	}

}
