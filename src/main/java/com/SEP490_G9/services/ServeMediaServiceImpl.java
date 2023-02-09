package com.SEP490_G9.services;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface ServeMediaServiceImpl {

	File serveImage(String dir);

	ResponseEntity<ResourceRegion> serveVideo(String dir, String rangeHeader) throws IOException;
	
	
}
