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

import com.SEP490_G9.entity.Preview;
import com.SEP490_G9.entity.ProductDetails;
import com.SEP490_G9.entity.User;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.ManageAccountInfoService;
import com.SEP490_G9.service.ManageProductService;
import com.SEP490_G9.service.PreviewService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.UserService;
import com.SEP490_G9.service.serviceImpls.ServeMediaService;
import com.SEP490_G9.util.StorageUtil;

@RequestMapping(value = "public/serveMedia")
@RestController
public class ServeMediaController {

	@Autowired
	ManageAccountInfoService manageAccountInfoService;

	@Autowired
	ProductDetailsService productDetailsService;

	@Autowired
	StorageUtil storageUtil;

	@Autowired
	PreviewService previewService;

	@Autowired
	ServeMediaService serveMediaService;

	@Autowired
	UserService userService;

	
	@GetMapping("serveCoverImage")
	public ResponseEntity<byte[]> serveCoverImage(@RequestParam(name = "productId") Long productId) throws IOException {
		ProductDetails pd = productDetailsService.getProductDetailsByProductId(productId);
		File file = new File(pd.getCoverImage());
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		byte[] image = new byte[0];
		image = FileUtils.readFileToByteArray(file);
		return ResponseEntity.ok().contentType(MediaType.valueOf(mimeType)).body(image);
	}

	@GetMapping(value = "servePreviewVideo/{previewId}", produces = "video/mp4")
	public ResponseEntity<ResourceRegion> getVideo(@RequestHeader(value = "Range", required = false) String rangeHeader,
			@PathVariable(name = "previewId") Long previewId) throws IOException {

		Preview preview = previewService.getById(previewId);
		
		return serveMediaService.serveVideo(preview.getSource(), rangeHeader);
	}

	@GetMapping(value = "servePreviewPicture/{previewId}")
	public ResponseEntity<byte[]> getVideo(@PathVariable(name = "previewId") Long previewId) throws IOException {
		Preview preview = previewService.getById(previewId);
		File file = new File(preview.getSource());
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		byte[] image = new byte[0];
		image = FileUtils.readFileToByteArray(file);
		return ResponseEntity.ok().contentType(MediaType.valueOf(mimeType)).body(image);
	}

	@GetMapping("serveProfileImage")
	public ResponseEntity<byte[]> serveProfileImage(@RequestParam(name = "userId") Long userId) throws IOException {
		User user = userService.getById(userId);
		File file = new File(storageUtil.getLocation() + user.getAvatar());
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		byte[] image = new byte[0];
		image = FileUtils.readFileToByteArray(file);
		return ResponseEntity.ok().contentType(MediaType.valueOf(mimeType)).body(image);
	}
}
