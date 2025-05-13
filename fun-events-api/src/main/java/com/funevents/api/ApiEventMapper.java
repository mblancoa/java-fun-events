package com.funevents.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.funevents.api.model.EventList;
import com.funevents.api.model.EventResponse;
import com.funevents.api.model.EventSummary;
import com.funevents.model.Event;

@Mapper
public interface ApiEventMapper {
	public static final ApiEventMapper INSTANCE = Mappers.getMapper(ApiEventMapper.class);

	@Mapping(target = "startDate", source = "startsAt", qualifiedByName = "toLocalDate")
	@Mapping(target = "startTime", source = "startsAt", qualifiedByName = "toLocalTime")
	@Mapping(target = "endDate", source = "endsAt", qualifiedByName = "toLocalDate")
	@Mapping(target = "endTime", source = "endsAt", qualifiedByName = "toLocalTime")
	EventSummary map(Event event);

	List<EventSummary> map(List<Event> list);

	default EventList mapToEventList(final List<Event> list) {
		if (list == null) {
			return null;
		}
		final EventList eventList = new EventList();
		eventList.setEvents(map(list));
		return eventList;
	}

	default EventResponse mapToEventResponse(final List<Event> list) {
		if (list == null) {
			return null;
		}
		final EventResponse response = new EventResponse();
		response.setData(new EventList());
		response.getData().setEvents(map(list));
		return response;
	}

	@Named("toLocalDate")
	default LocalDate mapFullDateToLocalDate(final LocalDateTime fullDate) {
		if (fullDate == null) {
			return null;
		}
		return fullDate.toLocalDate();
	}

	@Named("toLocalTime")
	default LocalTime mapFullDateToLocalTime(final LocalDateTime fullDate) {
		if (fullDate == null) {
			return null;
		}
		return fullDate.toLocalTime();
	}
}
