package com.funevents.startapp;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseApiTest {
	private static final String SEARCH_SERVICE_URL_PATTERN = "http://localhost:%d/search";
	protected final ObjectMapper mapper = new ObjectMapper();
	protected final RestTemplate restTemplate = new RestTemplate();

	@LocalServerPort
	private int port;

	protected String getSearchServiceUrl() {
		return String.format(SEARCH_SERVICE_URL_PATTERN, this.port);
	}

}
