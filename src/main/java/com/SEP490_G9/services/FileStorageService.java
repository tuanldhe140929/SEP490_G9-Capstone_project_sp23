package com.SEP490_G9.services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {

	void init();

	void store(MultipartFile file, String path);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

	MultipartFile loadAsMultipartFile(String filename);
}
