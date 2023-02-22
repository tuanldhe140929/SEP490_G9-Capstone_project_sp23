package com.SEP490_G9.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.service.ManageAccountInfoService;
import com.SEP490_G9.service.ManageProductService;


@RequestMapping(value = "public/serveMedia")
@RestController
public class ServeMediaController {
	@Autowired
	ManageProductService manageProductService;
	
	@Autowired
	ManageAccountInfoService manageAccountInfoService;

	@GetMapping("serveCoverImage")
	public ResponseEntity<byte[]> serveCoverImage(@RequestParam(name = "productId") Long productId) {
		File file = manageProductService.serveCoverImage(productId);
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		byte[] image = new byte[0];
		try {
			image = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().contentType(MediaType.valueOf(mimeType)).body(image);
	}

	@GetMapping(value = "servePreviewVideo/{previewId}", produces = "video/mp4")
	public ResponseEntity<ResourceRegion> getVideo(@RequestHeader(value = "Range", required = false) String rangeHeader,
			@PathVariable(name = "previewId") Long previewId) throws IOException {
		return manageProductService.servePreviewVideo(previewId, rangeHeader);

	}

	@GetMapping(value = "servePreviewPicture/{previewId}")
	public ResponseEntity<byte[]> getVideo(@PathVariable(name = "previewId") Long previewId)
			throws IOException {
		File file = manageProductService.servePreviewImage(previewId);
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		byte[] image = new byte[0];
		try {
			image = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok().contentType(MediaType.valueOf(mimeType)).body(image);
	}
	
	@GetMapping("serveProfileImage")
	public ResponseEntity<byte[]> serveProfileImage(@RequestParam(name = "userId") Long userId) {
		File file = manageAccountInfoService.serveProfileImage(userId);
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		byte[] image = new byte[0];
		try {
			image = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().contentType(MediaType.valueOf(mimeType)).body(image);
	}
}
