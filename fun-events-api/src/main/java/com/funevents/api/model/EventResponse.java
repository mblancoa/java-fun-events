package com.funevents.api.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EventResponse {
	private Error error;
	private EventList data;
}
