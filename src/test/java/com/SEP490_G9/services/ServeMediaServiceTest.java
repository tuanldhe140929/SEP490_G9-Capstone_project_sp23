package com.SEP490_G9.services;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;

import com.SEP490_G9.configs.TestConfig;
import com.SEP490_G9.service.serviceImpls.ServeMediaServiceImpl;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ URL.class })
class ServeMediaServiceTest {

	private ServeMediaServiceImpl videoService;


	public void testServeVideo() throws IOException {
		// arrange
		String dir = "src/test/resources/sample.mp4";
		String rangeHeader = "bytes=0-";

		URLConnection mockConnection = mock(URLConnection.class);
		when(mockConnection.getContentLengthLong()).thenReturn(1000L);

		URL mockUrl = PowerMockito.mock(URL.class);
		when(mockUrl.openConnection()).thenReturn(mockConnection);

		ResourceRegion mockResourceRegion = mock(ResourceRegion.class);
		when(mockResourceRegion.getResource()).thenReturn(new UrlResource(mockUrl));

		when(videoService.getResourceRegion(any(), any())).thenReturn(mockResourceRegion);

		// act
		ResponseEntity<ResourceRegion> responseEntity = videoService.serveVideo(dir, rangeHeader);

		// assert
		MediaType mediaType = MediaTypeFactory.getMediaType(responseEntity.getBody().getResource()).orElse(MediaType.APPLICATION_OCTET_STREAM);
		List<String> contentRangeHeader = responseEntity.getHeaders().get("Content-Range");
		List<String> contentLengthHeader = responseEntity.getHeaders().get("Content-Length");

		// check response status
		assertThat(HttpStatus.PARTIAL_CONTENT).isEqualTo(responseEntity.getStatusCode());

		// check response headers
		assertNotNull(contentRangeHeader);
		assertEquals(1, contentRangeHeader.size());
		assertEquals("bytes 0-999/1000", contentRangeHeader.get(0));
		assertNotNull(contentLengthHeader);
		assertEquals(1, contentLengthHeader.size());
		assertEquals("1000", contentLengthHeader.get(0));
		assertEquals(MediaTypeFactory.getMediaType(dir).orElse(MediaType.APPLICATION_OCTET_STREAM), mediaType);

		// check response body
		assertNotNull(responseEntity.getBody());
		assertEquals(mockResourceRegion.getResource(), responseEntity.getBody().getResource());
		assertEquals(mockResourceRegion.getPosition(), responseEntity.getBody().getPosition());
		assertEquals(mockResourceRegion.getCount(), responseEntity.getBody().getCount());
	}
}