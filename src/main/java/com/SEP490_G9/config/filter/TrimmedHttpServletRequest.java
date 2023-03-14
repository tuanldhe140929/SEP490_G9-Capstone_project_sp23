package com.SEP490_G9.config.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class TrimmedHttpServletRequest extends HttpServletRequestWrapper {
	private static final String CONTENT_TYPE_JSON = "application/json";

	public TrimmedHttpServletRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value != null) {
			value = value.trim();
		}
		return value;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				values[i] = values[i].trim();
			}
		}
		System.out.println("value");
		return values;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	private String getRequestJson(BufferedReader reader) throws IOException {
		StringBuilder json = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			json.append(line);
		}
		return json.toString();
	}

	private static class CustomBufferedReader extends BufferedReader {
		private final String json;

		public CustomBufferedReader(BufferedReader reader, String json) {
			super(reader);
			this.json = json;
		}

		@Override
		public String readLine() throws IOException {
			String line = super.readLine();
			if (line != null) {
				ObjectMapper mapper = new ObjectMapper();
				Object value = mapper.readValue(json, Object.class);
				String jsonString = mapper.writeValueAsString(value);
				line = jsonString.replaceAll("\\s+", "");
			}
			return line;
		}
	}
}