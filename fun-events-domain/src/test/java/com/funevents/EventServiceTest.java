package com.funevents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.funevents.model.Event;

public class EventServiceTest {

	private static EasyRandom random = new EasyRandom();

	private final EventRepository eventRepository = mock(EventRepository.class);

	private final LocalDateTime from = LocalDateTime.now();
	private final LocalDateTime to = this.from.plusMonths(5L);
	private final List<Event> eventListFromRepository = getEventList(8);

	private final EventService eventService = new EventService(this.eventRepository);

	@BeforeEach
	void resetAll() {
		reset(this.eventRepository);
	}

	@Test
	void getEvents_is_successful() {
		when(this.eventRepository.findByStartAfterAndEndBefore(this.from, this.to)).thenReturn(this.eventListFromRepository);

		final List<Event> result = this.eventService.getEvents(this.from, this.to);

		assertNotNull(result);
		assertEquals(8, result.size());
	}

	@Test
	void updateEvents_is_successful() {

		this.eventService.updateEvents(getEventList(8));

		verifyInsertedOrUpdated();
	}

	@Test
	void updateEvents_is_successful_when_event_list_is_empty() {

		this.eventService.updateEvents(List.of());

		verify(this.eventRepository, never()).update(anyList());
		verify(this.eventRepository, never()).insertOrUpdate(anyList());
	}

	@Test
	void updateEvents_is_successful_when_repository_update_fails() {
		final Event event = random.nextObject(Event.class);
		event.setOnlineSale(true);
		final List<Event> list = getEventList(4);
		list.add(event);

		doThrow(RuntimeException.class).when(this.eventRepository).update(anyList());
		when(this.eventRepository.findByStartAfterAndEndBefore(this.from, this.to)).thenReturn(this.eventListFromRepository);

		this.eventService.updateEvents(list);

		verify(this.eventRepository).update(anyList());
	}

	@Test
	void updateEvents_is_successful_when_repository_updateOrUpdate_fails() {
		final Event event = random.nextObject(Event.class);
		event.setOnlineSale(false);
		final List<Event> list = getEventList(4);
		list.add(event);

		doThrow(RuntimeException.class).when(this.eventRepository).insertOrUpdate(anyList());

		this.eventService.updateEvents(list);

		verify(this.eventRepository).insertOrUpdate(anyList());
	}

	private boolean verifyInsertedOrUpdated() {
		boolean updateIsExecuted = false;
		boolean insertOrUpdateIsExecuted = false;
		try {
			verify(this.eventRepository).update(anyList());
			updateIsExecuted = true;
			verify(this.eventRepository).insertOrUpdate(anyList());
			insertOrUpdateIsExecuted = true;
		} catch (final Exception e) {
		}
		return updateIsExecuted || insertOrUpdateIsExecuted;
	}

	private static List<Event> getEventList(final int size) {
		final List<Event> list = random.objects(Event.class, size).toList();
		return new LinkedList<>(list);
	}
}
