package com.SEP490_G9.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.Tag;
import com.SEP490_G9.entities.Violation;
import com.SEP490_G9.repository.TagRepository;
import com.SEP490_G9.repository.ViolationRepository;
import com.SEP490_G9.service.serviceImpls.TagServiceImpl;
import com.SEP490_G9.service.serviceImpls.ViolationServiceImpl;

class ViolationServiceTest {

	@Mock
	ViolationRepository vioRepo;
	
	@InjectMocks
	ViolationServiceImpl vioService;

	@Test
	void testAddViolation() {
		boolean expected = true;
		Date datecreate = new Date();
		Account acc = new Account();
		Violation vio = new Violation((long)1, "Người bán này lấy cắp ý tưởng của tôi",datecreate,acc);
		
		Mockito.when(vioRepo.save(vio)).thenReturn(vio);
		boolean result = vioService.addViolation(vio);
		assertEquals(expected, result);
	}
	
}
