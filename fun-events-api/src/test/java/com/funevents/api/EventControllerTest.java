package com.funevents.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.funevents.EventService;
import com.funevents.api.EventController;
import com.funevents.api.model.EventList;
import com.funevents.api.model.EventResponse;
import com.funevents.api.model.EventSummary;
import com.funevents.model.Event;

public class EventControllerTest {

	private MockMvc mockMvc;

	private final EventService eventService = mock(EventService.class);

	private EventController eventController = new EventController(this.eventService);

	private final LocalDateTime today = LocalDateTime.now();
	private final LocalDateTime tomorrow = this.today.plusDays(1L);
	private final EasyRandom random = new EasyRandom();
	private final ObjectMapper objectMapper = objectMapper();

	private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.eventController).build();
	}

	@Test
	public void testSearch() throws Exception {
		final List<Event> list = this.random.objects(Event.class, 2).toList();
		when(this.eventService.getEvents(this.today, this.tomorrow)).thenReturn(list);

		final MvcResult result = this.mockMvc
				.perform(get("/search").param("starts_at", this.today.format(this.formatter))
						.param("ends_at", this.tomorrow.format(this.formatter)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		final String jsonResponse = result.getResponse().getContentAsString();
		final EventResponse eventResponse = this.objectMapper.readValue(jsonResponse, EventResponse.class);

		assertNotNull(eventResponse);
		assertNull(eventResponse.getError());
		final EventList data = eventResponse.getData();
		assertNotNull(data);
		final List<EventSummary> events = data.getEvents();
		assertNotNull(events);
		assertEquals(2, events.size());
	}

	private ObjectMapper objectMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}
}