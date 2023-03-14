package com.SEP490_G9.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.entities.License;
import com.SEP490_G9.repository.LicenseRepository;
import com.SEP490_G9.service.LicenseService;
import com.SEP490_G9.service.serviceImpls.LicenseServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class LicenseServiceTest {

	@Mock
	LicenseRepository licenseRepository;

	@InjectMocks
	LicenseServiceImpl licenseServiceImpl;

	@Test
	void testGetAllLicense() {
		License license1 = new License();
		license1.setId(1);
		license1.setName("Name123");

		License license2 = new License();
		license1.setId(2);
		license1.setName("Name123333");

		List<License> expected = new ArrayList<>();
		expected.add(license1);
		expected.add(license2);

		when(licenseRepository.findAll()).thenReturn(expected);
	
		List<License> result = licenseServiceImpl.getAllLicense();
		assertThat(result).isEqualTo(expected);
	}

}
