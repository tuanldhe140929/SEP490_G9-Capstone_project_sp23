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
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.repository.ViolationTypeRepository;
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class ViolationTypeRepositoryTest {

	@Autowired
	ViolationTypeRepository vioTypeRepo;
	
	@Test
	void testFindById() {
		ViolationType viotype = new ViolationType();
		viotype.setId(1L);
		viotype.setName("Spam");
		ViolationType result = vioTypeRepo.findById(1L);
		assertEquals(viotype, result);
	}

}
