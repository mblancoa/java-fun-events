package com.funevents;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;

import com.funevents.model.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SupplyService {

	private final EventService eventService;
	private final EventProvider eventProvider;

	@Scheduled(fixedRateString = "${supply.feed-interval:60000}")
	public void fetchEventsFromProvider() {

		log.debug("Fetching events from provider...");
		final List<Event> events = this.eventProvider.getEvents();
		this.eventService.updateEvents(events);
		final int size = events != null ? events.size() : 0;
		log.debug("{} events have been fetched", size);
	}

}
