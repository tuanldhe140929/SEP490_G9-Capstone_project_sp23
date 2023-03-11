package com.SEP490_G9.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

import com.SEP490_G9.entities.Preview;
import com.SEP490_G9.entities.ProductDetails;
import com.SEP490_G9.entities.User;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.service.FileIOService;
import com.SEP490_G9.service.ManageAccountInfoService;
import com.SEP490_G9.service.PreviewService;
import com.SEP490_G9.service.ProductDetailsService;
import com.SEP490_G9.service.UserService;
import com.SEP490_G9.service.serviceImpls.ServeMediaService;

@RequestMapping(value = "public/serveMedia")
@RestController
public class ServeMediaController {

	@Autowired
	ManageAccountInfoService manageAccountInfoService;

	@Autowired
	ProductDetailsService productDetailsService;

	@Value("${root.location}")
	String ROOT_LOCATION;

	@Autowired
	PreviewService previewService;

	@Autowired
	ServeMediaService serveMediaService;

	@Autowired
	UserService userService;

	@GetMapping("serveCoverImage")
	public ResponseEntity<byte[]> serveCoverImage(@RequestParam(name = "productId") Long productId) throws IOException {
		ProductDetails pd = productDetailsService.getActiveVersion(productId);
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
		File file = new File(ROOT_LOCATION + user.getAvatar());
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		byte[] image = new byte[0];
		image = FileUtils.readFileToByteArray(file);
		return ResponseEntity.ok().contentType(MediaType.valueOf(mimeType)).body(image);
	}

	@GetMapping(value = "/image", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> serveImage(@RequestParam(name = "source") String source) {
		String imagePath = UriUtils.decode(source, StandardCharsets.UTF_8);

		File file = new File(ROOT_LOCATION + imagePath);
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		byte[] image = new byte[0];
		try {
			image = FileUtils.readFileToByteArray(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok().contentType(MediaType.valueOf(mimeType)).body(image);
	}

	@GetMapping(value = "video", produces = "video/mp4")
	public ResponseEntity<ResourceRegion> serveVideo(
			@RequestHeader(value = "Range", required = false) String rangeHeader,
			@RequestParam(name = "source") String source) throws IOException {
		return serveMediaService.serveVideo(ROOT_LOCATION + source, rangeHeader);
	}
}
