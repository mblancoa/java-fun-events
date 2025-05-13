package com.funevents;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import com.funevents.model.Event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EventService {

	private final EventRepository eventRepository;

	public List<Event> getEvents(final LocalDateTime from, final LocalDateTime to) {
		return this.eventRepository.findByStartAfterAndEndBefore(from, to);
	}

	public String getEventListToJson(final LocalDateTime from, final LocalDateTime to) {
		return this.eventRepository.findByStartAfterAndEndBeforeToJson(from, to);
	}

	public void updateEvents(final List<Event> events) {
		if (events != null && !events.isEmpty()) {
			try {
				final List<Event> onlineEvents = new LinkedList<Event>();
				final List<Event> restEvents = new LinkedList<Event>();
				events.forEach(e -> {
					if (e.isOnlineSale()) {
						onlineEvents.add(e);
					} else {
						restEvents.add(e);
					}
				});
				this.eventRepository.insertOrUpdate(onlineEvents);
				this.eventRepository.update(restEvents);
			} catch (final Exception e) {
				log.warn(String.join(" ", "Exception updating events: ", e.getMessage()));
			}
		}
	}

}
