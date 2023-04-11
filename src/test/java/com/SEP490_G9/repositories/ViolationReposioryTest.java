package com.SEP490_G9.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
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
import com.SEP490_G9.entities.Report;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.entities.ViolationType;
import com.SEP490_G9.repository.ViolationTypeRepository;
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class ViolationReposioryTest {

	@Autowired
	Violation vioTypeRepo;
	
	
	@Test
	void test() {
		fail("Not yet implemented");
	}
	
	@Test
	void testFindById() {
		Violation viotype = new Violation();
		viotype.setId(1L);
		viotype.setCreated_date(null);
		viotype.setDescription(null);
		viotype.setAccount(null);
		Violation result = vioTypeRepo.findById(1L);
		assertEquals(viotype, result);
	}

}
