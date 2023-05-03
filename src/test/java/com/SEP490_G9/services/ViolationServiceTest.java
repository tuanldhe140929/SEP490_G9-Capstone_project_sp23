package com.SEP490_G9.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.repository.TagRepository;
import com.SEP490_G9.repository.ViolationRepository;
import com.SEP490_G9.service.serviceImpls.TagServiceImpl;
import com.SEP490_G9.service.serviceImpls.ViolationServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class ViolationServiceTest {

	@Mock
	ViolationRepository vioRepo;

	@InjectMocks
	ViolationServiceImpl vioService;

	@Test
	void testAddViolation_1() {
		boolean expected = true;
		Date datecreate = new Date();
		Account acc = new Account();
		Violation vio = new Violation((long) 1, "Người bán này lấy cắp ý tưởng của tôi", datecreate, acc);

		Mockito.when(vioRepo.save(vio)).thenReturn(vio);
		Violation result = vioService.addViolation(vio);
		assertEquals(expected, result);
	}

	@Test
	void testAddViolation_2() {
		boolean expected = true;
		Date datecreate = new Date();
		Account acc = new Account();
		Violation vio = new Violation((long) 1, "Người bán này lấy cắp ý tưởng của tôi", datecreate, acc);

		Mockito.when(vioRepo.save(vio)).thenReturn(vio);
		Violation result = vioService.addViolation(vio);
		assertEquals(expected, result);
	}

	@Test
	void testAddViolation_3() {
		boolean expected = true;
		Date datecreate = new Date();
		Account acc = new Account();
		Violation vio = new Violation((long) 1, "Người bán này lấy cắp ý tưởng của tôi", datecreate, acc);

		Mockito.when(vioRepo.save(vio)).thenReturn(vio);
		Violation result = vioService.addViolation(vio);
		assertEquals(expected, result);
	}

	@Test
	void testAddViolation_4() {
		boolean expected = true;
		Date datecreate = new Date();
		Account acc = new Account();
		Violation vio = new Violation((long) 1, "Người bán này lấy cắp ý tưởng của tôi", datecreate, acc);

		Mockito.when(vioRepo.save(vio)).thenReturn(vio);
		Violation result = vioService.addViolation(vio);
		assertEquals(expected, result);
	}

	@Test
	void testAddViolation_5() {
		boolean expected = true;
		Date datecreate = new Date();
		Account acc = new Account();
		Violation vio = new Violation((long) 1, "Người bán này lấy cắp ý tưởng của tôi", datecreate, acc);

		Mockito.when(vioRepo.save(vio)).thenReturn(vio);
		Violation result = vioService.addViolation(vio);
		assertEquals(expected, result);
	}

	@Test
	void testAddViolation_6() {
		boolean expected = true;
		Date datecreate = new Date();
		Account acc = new Account();
		Violation vio = new Violation((long) 1, "Người bán này lấy cắp ý tưởng của tôi", datecreate, acc);

		Mockito.when(vioRepo.save(vio)).thenReturn(vio);
		Violation result = vioService.addViolation(vio);
		assertEquals(expected, result);
	}

	@Test
	void testAddViolation_7() {
		boolean expected = true;
		Date datecreate = new Date();
		Account acc = new Account();
		Violation vio = new Violation((long) 1, "Người bán này lấy cắp ý tưởng của tôi", datecreate, acc);

		Mockito.when(vioRepo.save(vio)).thenReturn(vio);
		Violation result = vioService.addViolation(vio);
		assertEquals(expected, result);
	}

	@Test
	void testAddViolation_8() {
		boolean expected = true;
		Date datecreate = new Date();
		Account acc = new Account();
		Violation vio = new Violation((long) 1, "Người bán này lấy cắp ý tưởng của tôi", datecreate, acc);

		Mockito.when(vioRepo.save(vio)).thenReturn(vio);
		Violation result = vioService.addViolation(vio);
		assertEquals(expected, result);
	}
}
