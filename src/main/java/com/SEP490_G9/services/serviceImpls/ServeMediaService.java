package com.SEP490_G9.services.serviceImpls;

import static java.lang.Math.min;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.ResourceNotFoundException;
import com.SEP490_G9.helpers.StorageProperties;
import com.SEP490_G9.models.UserDetailsImpl;
import com.SEP490_G9.models.DTOS.ProductDTO;
import com.SEP490_G9.models.Entities.Product;
import com.SEP490_G9.models.Entities.User;
import com.SEP490_G9.repositories.PreviewRepository;
import com.SEP490_G9.repositories.ProductRepository;
import com.SEP490_G9.services.ServeMediaServiceImpl;

@Service
public class ServeMediaService implements ServeMediaServiceImpl {
	
	private static final long CHUNK_SIZE = 1000000L;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	StorageProperties storageProperties;
	
	@Autowired
	PreviewRepository previewRepository;
	
	@Override
	public File serveImage(String dir) {

		File file = new File(dir);
		return file;
	}
	
	@Override
	public ResponseEntity<ResourceRegion> serveVideo(String dir, String rangeHeader) throws IOException {
		
		return getVideoRegion(rangeHeader,dir);
	}


	
	
	private ResponseEntity<ResourceRegion> getVideoRegion(String rangeHeader, String directory) throws IOException {
		FileUrlResource videoResource = new FileUrlResource(directory);
		System.out.println(videoResource.getFilename());
		ResourceRegion resourceRegion = getResourceRegion(videoResource, rangeHeader);

		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.contentType(MediaTypeFactory.getMediaType(videoResource).orElse(MediaType.APPLICATION_OCTET_STREAM))
				.body(resourceRegion);
	}

	private ResourceRegion getResourceRegion(UrlResource video, String httpHeaders) throws IOException {
		ResourceRegion resourceRegion = null;
		System.out.println(video.getFilename());
		long contentLength = video.contentLength();
		int fromRange = 0;
		int toRange = 0;
		if (httpHeaders!=null && !httpHeaders.equalsIgnoreCase("")) {
			String[] ranges = httpHeaders.substring("bytes=".length()).split("-");
			fromRange = Integer.valueOf(ranges[0]);
			if (ranges.length > 1) {
				toRange = Integer.valueOf(ranges[1]);
			} else {
				toRange = (int) (contentLength - 1);
			}
		}

		if (fromRange > 0) {
			long rangeLength = min(CHUNK_SIZE, toRange - fromRange + 1);
			resourceRegion = new ResourceRegion(video, fromRange, rangeLength);
		} else {
			long rangeLength = min(CHUNK_SIZE, contentLength);
			resourceRegion = new ResourceRegion(video, 0, rangeLength);
		}

		return resourceRegion;
	}
	
}