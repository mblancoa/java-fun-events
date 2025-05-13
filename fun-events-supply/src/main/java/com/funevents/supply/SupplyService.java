package com.funevents.supply;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;

import com.funevents.EventService;
import com.funevents.model.Event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SupplyService {

	private final EventService eventService;
	private final EventProvider eventProvider;

	@Scheduled(fixedRateString = "${supply.feed-interval:60000}")
	public void fetchEventsFromProvider() {
		final List<Event> events = this.eventProvider.getEvents();
		this.eventService.updateEvents(events);
	}

}
