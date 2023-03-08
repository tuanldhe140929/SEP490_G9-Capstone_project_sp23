package com.SEP490_G9.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fi.solita.clamav.ClamAVClient;
@Component
public class ClamAVUtil {

	 private final String hostname;
	  private final int port;
	  private final int timeout;

	  public ClamAVUtil(@Value("${clamd.host}") String hostname,
	                             @Value("${clamd.port}") int port,
	                             @Value("${clamd.timeout}") int timeout) {
	    this.hostname = hostname;
	    this.port = port;
	    this.timeout = timeout;
	  }

	  public ClamAVClient newClient() {
	        return new ClamAVClient(hostname, port, timeout);
	    }
}
