package com.SEP490_G9.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("storage")
@Component
public class StorageUtil {

	/**
	 * Folder location for storing files
	 */


	private String location = "C:\\Users\\ADMN\\eclipse-workspace\\SEP490_G9_Datas\\";


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}