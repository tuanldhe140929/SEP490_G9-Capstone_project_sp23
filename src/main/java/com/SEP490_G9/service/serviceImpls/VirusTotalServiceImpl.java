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
import java.util.zip.ZipException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exception.FileUploadException;
import com.SEP490_G9.exception.InternalServerException;
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
	public boolean scanFile(File file) {

		if (fileHasPassword(file)) {
			throw new FileUploadException("CompressedFile can not have password");
		}
		long startTime = System.currentTimeMillis();
		List<byte[]> chunks;
		chunks.si
		try {
			chunks = splitFileIntoChunks(file.getPath().toString());
		} catch (IOException e1) {
			throw new InternalServerException("Error when split file");
		}

		List<Boolean> isChunksSafe = new ArrayList<>();
		List<Future<Boolean>> futures = new ArrayList<>();

		ExecutorService executor = Executors.newFixedThreadPool(chunks.size());
		for (byte[] chunk : chunks) {
			Future<Boolean> future = executor.submit(() -> {
				long uploadChunkStart = System.currentTimeMillis();
				String analysisId = uploadChunk(UPLOAD_ENDPOINT, chunk);
				long uploadChunkEnd = System.currentTimeMillis();
				// System.out.println("upload chunk " + (chunks.indexOf(chunk) + 1) + " takes "
				// + (uploadChunkEnd - uploadChunkStart) / 1000 + "s");

				boolean isChunkSafe = getAnalysis(analysisId, chunks.indexOf(chunk));
				long analysisEnd = System.currentTimeMillis();
				System.out.println("analysis chunk " + (chunks.indexOf(chunk) + 1) + " takes "
						+ (analysisEnd - uploadChunkEnd) / 1000 + "s");

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
			isMalicious = isMalicious && response;
		}

		long finishTime = System.currentTimeMillis();
		// System.out.println("total: " + (finishTime - startTime) / 1000);
		return isMalicious;
	}


	private boolean fileHasPassword(File productFile) {
		char[] password = "password".toCharArray(); // replace with the password to test
		try (FileInputStream fis = new FileInputStream(productFile);
				ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(fis)) {
			ArchiveEntry entry;
			while ((entry = ais.getNextEntry()) != null) {
				IOUtils.toByteArray(ais);
			}
		} catch (IOException e) {
			throw new InternalServerException("Error occur when tryin to extract compressed file");
		} catch (ArchiveException e) {
			if (e.getCause() instanceof ZipException && e.getCause().getMessage().contains("password")) {
				return true; // password-protected file
			}
		}
		return false; // file is not password-protected
	}

	private boolean getAnalysis(String analysisId, int index) {
		boolean isSafe = false;
		OkHttpClient client = new OkHttpClient();
		boolean notCompleted = true;
		while (notCompleted) {
			Request request = new Request.Builder().url(ANALYSIS_ENDPOINT + analysisId).get()
					.addHeader("accept", "application/json").addHeader("x-apikey", virusTotalKey).build();
			System.out.println("Request URL: " + request.url());
			System.out.println("Request method: " + request.method());
			System.out.println("Request headers: " + request.headers());
			System.out.println("Request body: " + request.body());
			try {
				Response response = client.newCall(request).execute();
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode rootNode = objectMapper.readTree(response.body().string());
				System.out.println(rootNode.toPrettyString());
				// Check if the analysis is completed
				String status = rootNode.get("data").get("attributes").get("status").asText();
				if (status.equals("completed")) {
					// Print out the JSON object
					notCompleted = false;
					// System.out.println(index);

					int malicious = rootNode.get("data").get("attributes").get("stats").get("malicious").asInt();
					if (malicious == 0) {
						isSafe = true;
					}
					break;
				}
				// Wait for a few seconds before sending the next request
				TimeUnit.SECONDS.sleep(40);
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

		MediaType mediaType = MediaType.parse("multipart/form-data");

		RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("file", "temp-file-" + UUID.randomUUID(), RequestBody.create(data, mediaType)).build();

		Request request = new Request.Builder().url(url).post(requestBody).addHeader("accept", "application/json")
				.addHeader("x-apikey", virusTotalKey).build();
		String id = "";

		// Print out the response information
		try {
			Response response = client.newCall(request).execute();

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(response.body().string());

			// Get the value of the "id" attribute
			id = rootNode.get("data").get("id").asText();
		} catch (IOException e) {
			e.printStackTrace();
			// throw new FileUploadException("Error when upload chunks \n" +
			// e.getMessage());
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
		// System.out.println("chunk: " + chunks.size());
		return chunks;
	}
}
