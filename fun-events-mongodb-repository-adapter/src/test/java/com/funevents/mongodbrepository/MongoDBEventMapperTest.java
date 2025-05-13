package com.funevents.mongodbrepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

import com.funevents.model.Event;
import com.funevents.mongodbrepository.MongoDBEventMapper;
import com.funevents.mongodbrepository.model.EventDB;

public class MongoDBEventMapperTest {

	private final MongoDBEventMapper mapper = MongoDBEventMapper.INSTANCE;
	private final EasyRandom random = new EasyRandom();

	@Test
	void map_EventDB() {
		final EventDB eventDb = this.random.nextObject(EventDB.class);

		final Event event = this.mapper.map(eventDb);

		assertEquals(eventDb.getId(), event.getId());
		assertEquals(eventDb.getProvId(), event.getProvId());
		assertEquals(eventDb.getTitle(), event.getTitle());
		assertEquals(eventDb.getStartsAt(), event.getStartsAt());
		assertEquals(eventDb.getEndsAt(), event.getEndsAt());
		assertEquals(eventDb.getMinPrice(), event.getMinPrice());
		assertEquals(eventDb.getMaxPrice(), event.getMaxPrice());
	}

	@Test
	void map_event() {
		final Event event = this.random.nextObject(Event.class);

		final EventDB eventDb = this.mapper.map(event);

		assertEquals(event.getId(), eventDb.getId());
		assertEquals(event.getProvId(), eventDb.getProvId());
		assertEquals(event.getTitle(), eventDb.getTitle());
		assertEquals(event.getStartsAt(), eventDb.getStartsAt());
		assertEquals(event.getEndsAt(), eventDb.getEndsAt());
		assertEquals(event.getMinPrice(), eventDb.getMinPrice());
		assertEquals(event.getMaxPrice(), eventDb.getMaxPrice());
	}

	@Test
	void map() {
		final List<EventDB> list = this.random.objects(EventDB.class, 2).toList();

		final List<Event> mapped = this.mapper.map(list);

		assertNotNull(mapped);
		assertEquals(list.size(), mapped.size());

		for (int i = 0; i < list.size(); i++) {
			final EventDB eventDb = list.get(i);
			final Event event = mapped.get(i);
			assertEquals(eventDb.getId(), event.getId());
			assertEquals(eventDb.getProvId(), event.getProvId());
			assertEquals(eventDb.getTitle(), event.getTitle());
			assertEquals(eventDb.getStartsAt(), event.getStartsAt());
			assertEquals(eventDb.getEndsAt(), event.getEndsAt());
			assertEquals(eventDb.getMinPrice(), event.getMinPrice());
			assertEquals(eventDb.getMaxPrice(), event.getMaxPrice());
		}
	}

	@Test
	void mapToDB() {
		final List<Event> list = this.random.objects(Event.class, 2).toList();

		final List<EventDB> mapped = this.mapper.mapToDb(list);

		assertNotNull(mapped);
		assertEquals(list.size(), mapped.size());

		for (int i = 0; i < list.size(); i++) {
			final Event event = list.get(i);
			final EventDB eventDb = mapped.get(i);
			assertEquals(event.getId(), eventDb.getId());
			assertEquals(event.getProvId(), eventDb.getProvId());
			assertEquals(event.getTitle(), eventDb.getTitle());
			assertEquals(event.getStartsAt(), eventDb.getStartsAt());
			assertEquals(event.getEndsAt(), eventDb.getEndsAt());
			assertEquals(event.getMinPrice(), eventDb.getMinPrice());
			assertEquals(event.getMaxPrice(), eventDb.getMaxPrice());
		}
	}

}
