package com.funevents.supply;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.funevents.EventService;
import com.funevents.model.Event;

public class SupplyServiceTest {

	private static EasyRandom random = new EasyRandom();

	private final EventService eventService = mock(EventService.class);
	private final EventProvider eventProvider = mock(EventProvider.class);

	private final SupplyService supplyService = new SupplyService(this.eventService, this.eventProvider);

	@BeforeEach
	void resetAll() {
		reset(this.eventService, this.eventProvider);
	}

	@Test
	void fetchEventsFromProviderTest() {
		when(this.eventProvider.getEvents()).thenReturn(getEventList(4));
		
		this.supplyService.fetchEventsFromProvider();
		
		verify(this.eventProvider,times(1)).getEvents();
		verify(this.eventService,times(1)).updateEvents(anyList());
	}

	private static List<Event> getEventList(final int size) {
		final List<Event> list = random.objects(Event.class, size).toList();
		return new LinkedList<>(list);
	}

}
