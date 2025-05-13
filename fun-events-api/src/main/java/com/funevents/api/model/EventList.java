package com.funevents.api.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EventList {

	private List<EventSummary> events;
}
