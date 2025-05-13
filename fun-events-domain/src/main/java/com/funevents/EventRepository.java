package com.funevents;

import java.time.LocalDateTime;
import java.util.List;

import com.funevents.model.Event;

public interface EventRepository {

	List<Event> findByStartAfterAndEndBefore(LocalDateTime from, LocalDateTime to);

	String findByStartAfterAndEndBeforeToJson(LocalDateTime from, LocalDateTime to);

	void update(List<Event> toUpdate);

	void insertOrUpdate(List<Event> toInsert);
}
