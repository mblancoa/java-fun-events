package com.funevents.startapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.funevents.api.model.Error;
import com.funevents.api.model.EventList;
import com.funevents.api.model.EventResponse;
import com.funevents.api.model.EventSummary;
import com.funevents.model.Event;
import com.funevents.mongodbrepository.model.EventDB;

@ActiveProfiles({ "test" })
@SpringBootTest(classes = { MongoDBTestConetionConfiguration.class,
		EventsStartAppApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventServiceIntegrationTest {

	private static final String GET_EVENTS_PATH = "/events";
	private static final int PROVIDER_URL_PORT = 9090;
	private static final String SEARCH_SERVICE_URL_PATTERN = "http://localhost:%d/search";
	private static EasyRandom random = new EasyRandom();

	@LocalServerPort
	private int port;
	@Autowired
	private MongoDBTestUtils mongodb;

	private final RestTemplate restTemplate = new RestTemplate();
	private final LocalDateTime today = LocalDateTime.now();
	private final LocalDateTime tomorrow = this.today.plusDays(1L);

	private final ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void beforeEach() {
		this.mongodb.cleanCollection(EventDB.class);
	}

	@Test
	void getEvents_successfully() {
		final List<Event> list = getEventList(4);
		this.mongodb.insertMultipleOnes(list);

		final LocalDateTime from = list.stream().map(Event::getStartsAt).min(LocalDateTime::compareTo).get();
		final LocalDateTime to = list.stream().map(Event::getEndsAt).max(LocalDateTime::compareTo).get();

		final String uri = UriComponentsBuilder.fromUriString(getSearchServiceUrl()).queryParam("starts_at", from)
				.queryParam("ends_at", to).toUriString();

		final EventResponse response = this.restTemplate.getForObject(uri, EventResponse.class);

		assertNotNull(response);
		assertNull(response.getError());
		final EventList data = response.getData();
		assertNotNull(data);
		final List<EventSummary> events = data.getEvents();
		assertNotNull(events);
		assertEquals(4, events.size());

	}

	@Test
	void getEvents_return400whenEndsAtIsNotPresent() throws JsonMappingException, JsonProcessingException {
		final String uri = UriComponentsBuilder.fromUriString(getSearchServiceUrl()).queryParam("starts_at", this.today)
				.toUriString();

		try {
			final ResponseEntity<EventResponse> responseEntity = this.restTemplate.exchange(uri, HttpMethod.GET, null,
					EventResponse.class);
		} catch (final HttpClientErrorException e) {
			final String responseBody = e.getResponseBodyAsString();
			final EventResponse body = this.mapper.readValue(responseBody, EventResponse.class);
			assertNotNull(body);
			assertNull(body.getData());
			assertNotNull(body.getError());
			final Error error = body.getError();
			assertEquals(String.valueOf(400), error.getCode());
			assertEquals("Required request parameter 'ends_at' for method parameter type LocalDateTime is not present",
					error.getMessage());
			assertEquals(String.valueOf(400), error.getCode());
		}
	}

	@Test
	void getEvents_return400whenStartsAtIsNOtPresent() throws JsonMappingException, JsonProcessingException {
		final String uri = UriComponentsBuilder.fromHttpUrl(getSearchServiceUrl()).queryParam("ends_at", this.tomorrow)
				.toUriString();

		try {
			final ResponseEntity<EventResponse> responseEntity = this.restTemplate.exchange(uri, HttpMethod.GET, null,
					EventResponse.class);
		} catch (final HttpClientErrorException e) {
			final String responseBody = e.getResponseBodyAsString();
			final EventResponse body = this.mapper.readValue(responseBody, EventResponse.class);
			assertNotNull(body);
			assertNull(body.getData());
			assertNotNull(body.getError());
			final Error error = body.getError();
			assertEquals(String.valueOf(400), error.getCode());
			assertEquals("Required request parameter 'starts_at' for method parameter type LocalDateTime is not present",
					error.getMessage());
			assertEquals(String.valueOf(400), error.getCode());
		}

	}

	private String getSearchServiceUrl() {
		return String.format(SEARCH_SERVICE_URL_PATTERN, this.port);
	}

	private static List<Event> getEventList(final int size) {
		final List<Event> list = random.objects(Event.class, size).toList();
		return new LinkedList<>(list);
	}

}
