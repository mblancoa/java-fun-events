package com.funevents.start;

import static com.funevents.configuration.DomainConfiguration.CONSUMER_PROFILE;
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
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.funevents.api.model.Error;
import com.funevents.api.model.EventList;
import com.funevents.api.model.EventResponse;
import com.funevents.api.model.EventSummary;
import com.funevents.model.Event;
import com.funevents.mongodbrepository.model.EventDB;

@ActiveProfiles({ "test", CONSUMER_PROFILE })
@SpringBootTest(classes = { MongoDBTestConetionConfiguration.class,
		EventsStartAppApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EventServiceIntegrationTest extends BaseApiTest {

	@Autowired
	private MongoDBTestUtils mongodb;

	private final LocalDateTime today = LocalDateTime.now();
	private final LocalDateTime tomorrow = this.today.plusDays(1L);

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
			this.restTemplate.exchange(uri, HttpMethod.GET, null, EventResponse.class);
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
	void getEvents_return400whenStartsAtIsNotPresent() throws JsonMappingException, JsonProcessingException {
		final String uri = UriComponentsBuilder.fromUriString(getSearchServiceUrl()).queryParam("ends_at", this.tomorrow)
				.toUriString();

		try {
			this.restTemplate.exchange(uri, HttpMethod.GET, null, EventResponse.class);
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

	private static List<Event> getEventList(final int size) {
		final EasyRandom random = new EasyRandom();
		final List<Event> list = random.objects(Event.class, size).toList();
		return new LinkedList<>(list);
	}

}
