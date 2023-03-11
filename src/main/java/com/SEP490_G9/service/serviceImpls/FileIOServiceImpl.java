package com.SEP490_G9.service.serviceImpls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.SEP490_G9.exception.StorageException;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.common.StorageUtil;

@Service
public class FileIOServiceImpl implements FileIOService {

	private Path rootLocation;
	
	@Value("${root.location}")
	String ROOT_LOCATION;


	@Override
	public void init() {
		try {
			System.out.println(ROOT_LOCATION);
			Path p = Paths.get(ROOT_LOCATION);
			Files.createDirectories(p);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
//
//	@Override
//	public void store(MultipartFile file, String path) {
//		try {
//			if (file.isEmpty()) {
//				throw new StorageException("Failed to store empty file.");
//			}
//			Path destinationFile = this.rootLocation.resolve(Paths.get(path + "\\" + file.getOriginalFilename()))
//					.normalize().toAbsolutePath();
//
//			try (InputStream inputStream = file.getInputStream()) {
//				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
//			}
//		} catch (IOException e) {
//			throw new StorageException("Failed to store file.", e);
//		}
//	}

	@Override
	public String storeV2(MultipartFile file, String path) {
		InputStream is = null;
		OutputStream os = null;
		String dest = path + "\\" + file.getOriginalFilename();
		File destFile = new File(dest);
		int count = 0;
		while (destFile.exists()) {
			count++;
			dest = path + "\\(" + count + ") " + file.getOriginalFilename();
			destFile = new File(dest);
		}
		try {
			is = file.getInputStream();
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			
			return dest;
		} catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				throw new StorageException("Failed to store file.", e);
			}
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation))
					.map(this.rootLocation::relativize);
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new StorageException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new StorageException("Could not read file: " + filename, e);
		}
	}

	@Override
	public MultipartFile loadAsMultipartFile(String filename) {
		try {
			Path path = load(filename);
			File file = new File("");
			MultipartFile multipartFile = null;

			ClassPathResource imgFile = new ClassPathResource("image/sid.jpg");

			return multipartFile;
		} catch (Exception e) {
			throw new StorageException("Could not read file: " + filename, e);
		}
	}
}
