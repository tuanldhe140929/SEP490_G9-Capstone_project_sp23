package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.service.VirusTotalService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class VirusTotalServiceImpl implements VirusTotalService {

	@Value("${virustotal.key}")
	private String virusTotalKey;

	private final String UPLOAD_ENDPOINT = "https://www.virustotal.com/api/v3/files";
	private final String ANALYSIS_ENDPOINT = "https://www.virustotal.com/api/v3/analyses/";

	@Override
	public boolean scanFile(File file) throws IOException {
		List<byte[]> chunks = splitFileIntoChunks(file.getPath().toString());

		List<Boolean> isChunksSafe = new ArrayList<>();
		List<Future<Boolean>> futures = new ArrayList<>();

		ExecutorService executor = Executors.newFixedThreadPool(chunks.size());
		for (byte[] chunk : chunks) {
			Future<Boolean> future = executor.submit(() -> {
				String analysisId = uploadChunk(UPLOAD_ENDPOINT, chunk);
				boolean isChunkSafe = getAnalysis(analysisId, chunks.indexOf(chunk));
				return isChunkSafe;
			});

			futures.add(future);
		}
		executor.shutdown();

		for (Future<Boolean> future : futures) {
			try {
				boolean safe = future.get();
				isChunksSafe.add(safe);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		boolean isMalicious = true;
		for (boolean response : isChunksSafe) {
			System.out.println("compare");
			isMalicious = isMalicious && response;
		}

		return isMalicious;
	}

	private boolean getAnalysis(String analysisId, int index) {
		System.out.println("gettin analysis");
		boolean isSafe = false;
		OkHttpClient client = new OkHttpClient();
		boolean notCompleted = true;
		while (notCompleted) {
			Request request = new Request.Builder().url(ANALYSIS_ENDPOINT + analysisId).get()
					.addHeader("accept", "application/json")
					.addHeader("x-apikey", virusTotalKey).build();

			try {
				Response response = client.newCall(request).execute();
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readTree(response.body().string());
				System.out.println(rootNode.toPrettyString());
				// Check if the analysis is completed
				
				
//				{
//					  "error" : {
//					    "message" : "Quota exceeded",
//					    "code" : "QuotaExceededError"
//					  }
//					}
//					gettin analysis
//					{
//					  "error" : {
//					    "message" : "Quota exceeded",
//					    "code" : "QuotaExceededError"
//					  }
//					}
				
				String status = rootNode.get("data").get("attributes").get("status").asText();
				if (status.equals("completed")) {
					// Print out the JSON object
					notCompleted = false;
					System.out.println(index);
					
					int malicious = rootNode.get("data").get("attributes").get("stats").get("malicious").asInt();
					if (malicious == 0) {
						isSafe = true;
					}
					break;
				}
				// Wait for a few seconds before sending the next request
				TimeUnit.SECONDS.sleep(2);
			} catch (IOException | InterruptedException e) {
				throw new FileUploadException("Error when get analysis result" + e.getMessage());
			}
		}
		return isSafe;
	}

	public String uploadChunk(String url, byte[] data) {
		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.connectTimeout(100, TimeUnit.SECONDS); 
		builder.readTimeout(100, TimeUnit.SECONDS); 
		builder.writeTimeout(100, TimeUnit.SECONDS); 
		
		
		OkHttpClient client = new OkHttpClient();
		client = builder.build();
		
		System.out.println("create request");
		
		
		MediaType mediaType = MediaType.parse("multipart/form-data");
		
		
		RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("file", "temp-file-" + UUID.randomUUID(), RequestBody.create(data, mediaType)).build();

		Request request = new Request.Builder().url(url).post(requestBody)
				.addHeader("accept", "application/json")
				.addHeader("x-apikey", virusTotalKey).build();
		String id = "";
		try {
			Response response = client.newCall(request).execute();

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(response.body().string());

			// Get the value of the "id" attribute
			id = rootNode.get("data").get("id").asText();
		} catch (IOException e) {
			e.printStackTrace();
			//throw new FileUploadException("Error when upload chunks \n" + e.getMessage());
		}
		return id;

	}

	public static List<byte[]> splitFileIntoChunks(String filePath) throws IOException {
		int chunkSize = 5 * 1024 * 1024;
		List<byte[]> chunks = new ArrayList<>();

		try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath))) {
			while (true) {
				byte[] buffer = new byte[chunkSize];
				int bytesRead = inputStream.read(buffer);
				if (bytesRead == -1) {
					break;
				}

				byte[] chunk = Arrays.copyOf(buffer, bytesRead);
				chunks.add(chunk);
			}
		}
		System.out.println(chunks.size());
		return chunks;
	}

}
