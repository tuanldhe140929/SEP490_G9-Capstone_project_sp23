package com.SEP490_G9.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.exception.StorageException;
import com.SEP490_G9.service.serviceImpls.FileIOServiceImpl;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@RunWith(SpringRunner.class)
@Import(TestConfig.class)
class FileIOServiceTest {

	private FileIOServiceImpl fileIOService;

	@Mock
	private InputStream inputStream;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		fileIOService = new FileIOServiceImpl();
	}

	@AfterEach
	void tearDown() throws IOException {
		inputStream.close();
	}

	@Test
	void testStoreV2(@TempDir Path tempDir) throws IOException {
		// Given
		String originalFilename = UUID.randomUUID().toString() + ".txt";
		String path = tempDir.toString();
		MockMultipartFile file = new MockMultipartFile("file", originalFilename, "text/plain", "Test data".getBytes());

		// When
		String storedPath = fileIOService.storeV2(file, path);

		// Then
		File storedFile = new File(storedPath);
		assertEquals(originalFilename, storedFile.getName());
		assertEquals(path, storedFile.getParent());

		// Cleanup
		Files.delete(storedFile.toPath());
	}

	@Test
	void testStoreV2_ioException() throws IOException {
		// Given
		MultipartFile file = mock(MultipartFile.class);
		String path = "invalid_path";

		InputStream inputStream = mock(InputStream.class);
		when(file.getInputStream()).thenReturn(inputStream);

		// act
		assertThrows(NullPointerException.class,()-> fileIOService.storeV2(file, path));
	}

}
