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

import com.SEP490_G9.exception.InternalServerException;
import com.SEP490_G9.service.FileIOService;

@Service
public class FileIOServiceImpl implements FileIOService {

	@Value("${root.location}")
	String ROOT_LOCATION;

	@Override
	public void init() {
		try {
			System.out.println(ROOT_LOCATION);
			Path p = Paths.get(ROOT_LOCATION);
			Files.createDirectories(p);
		} catch (IOException e) {
			throw new InternalServerException("Could not initialize storage", e);
		}
	}

	@Override
	public String storeV2(MultipartFile file, String path) {
		InputStream is = null;
		OutputStream os = null;
		String fileName = file.getOriginalFilename().replace("+", "_");
		String dest = path + "/" + fileName;
		File destFile = new File(dest);
		int count = 0;
		System.out.println(dest);
		while (destFile.exists()) {
			count++;
			dest = path + "/(" + count + ") " + file.getOriginalFilename();
			System.out.println(count);
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
			throw new InternalServerException("Failed to store file.", e);
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				throw new InternalServerException("Failed to store file.", e);
			}
		}
	}
}
