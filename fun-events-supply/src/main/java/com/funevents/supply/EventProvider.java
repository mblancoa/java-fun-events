package com.funevents.supply;

import java.util.List;

import com.funevents.model.Event;

public interface EventProvider {
	List<Event> getEvents();
}
