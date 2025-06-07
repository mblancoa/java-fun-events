package com.funevents.start;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Iterator;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.funevents.model.Event;
import com.funevents.mongodbrepository.MongoDBEventMapper;
import com.funevents.mongodbrepository.model.EventDB;

public class MongoDBTestUtilsTest {

	private MongoTemplate mongoTemplate = mock(MongoTemplate.class);
	private MongoDBTestUtils mongo = new MongoDBTestUtils(this.mongoTemplate);

	private EasyRandom random = new EasyRandom();
	private MongoDBEventMapper mapper = MongoDBEventMapper.INSTANCE;

	@BeforeEach
	void resetMocks() {
		reset(this.mongoTemplate);
	}

	@Test
	void insertOneTest() {
		final ArgumentCaptor<EventDB> captor = ArgumentCaptor.forClass(EventDB.class);
		final Event event = this.random.nextObject(Event.class);

		this.mongo.insertOne(event);

		verify(this.mongoTemplate, times(1)).insert(captor.capture());
		final EventDB capture = captor.getValue();
		assertEquals(event.getId(), capture.getId());
		assertEquals(event.getProvId(), capture.getProvId());
		assertEquals(event.getTitle(), capture.getTitle());
		assertEquals(event.getStartsAt(), capture.getStartsAt());
		assertEquals(event.getEndsAt(), capture.getEndsAt());
		assertEquals(event.getMinPrice(), capture.getMinPrice());
		assertEquals(event.getMaxPrice(), capture.getMaxPrice());
	}

	@Test
	void insertMultipleOnesTest() {
		@SuppressWarnings("unchecked")
		final ArgumentCaptor<List<EventDB>> captor = ArgumentCaptor.forClass(List.class);
		final List<Event> events = this.random.objects(Event.class, 2).toList();
		final List<EventDB> eventDBs = this.mapper.mapToDb(events);

		this.mongo.insertMultipleOnes(events);

		verify(this.mongoTemplate, times(1)).insert(captor.capture(), eq(EventDB.class));
		final List<EventDB> list = captor.getValue();
		assertEquals(2, list.size());

		final Iterator<EventDB> expectedIt = eventDBs.iterator();
		for (final Iterator<EventDB> iterator = list.iterator(); iterator.hasNext();) {
			final EventDB expected = expectedIt.next();
			final EventDB eventDB = iterator.next();
			assertEquals(expected.getId(), eventDB.getId());
			assertEquals(expected.getProvId(), eventDB.getProvId());
			assertEquals(expected.getTitle(), eventDB.getTitle());
			assertEquals(expected.getStartsAt(), eventDB.getStartsAt());
			assertEquals(expected.getEndsAt(), eventDB.getEndsAt());
			assertEquals(expected.getMinPrice(), eventDB.getMinPrice());
			assertEquals(expected.getMaxPrice(), eventDB.getMaxPrice());
		}
	}

	@Test
	void cleanCollectionTest() {
		this.mongo.cleanCollection(EventDB.class);

		verify(this.mongoTemplate, times(1)).remove(any(), eq(EventDB.class));
	}

	@Test
	void countTest() {
		this.mongo.count(EventDB.class);

		verify(this.mongoTemplate, times(1)).count(any(), eq(EventDB.class));
	}
}
