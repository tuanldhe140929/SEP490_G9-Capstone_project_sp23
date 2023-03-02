package com.SEP490_G9.service;

import java.io.File;
import java.io.IOException;

public interface VirusTotalService {
	
	public boolean scanFile(File file) throws IOException, InterruptedException;

}
