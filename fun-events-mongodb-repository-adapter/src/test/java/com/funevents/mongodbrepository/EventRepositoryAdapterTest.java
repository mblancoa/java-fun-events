package com.funevents.mongodbrepository;

import static com.funevents.mongodbrepository.model.Constants.EVENT_COLLECTION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.funevents.EventRepository;
import com.funevents.model.Event;
import com.funevents.mongodbrepository.EventRepositoryAdapter;
import com.funevents.mongodbrepository.model.EventDB;
import com.mongodb.client.result.UpdateResult;

public class EventRepositoryAdapterTest {

	private final MongoTemplate mongoTemplate = mock(MongoTemplate.class);
	private final AggregationResults<EventDB> aggregationResults = mock(AggregationResults.class);
	private final UpdateResult updateResult = mock(UpdateResult.class);

	private final EventRepository repository = new EventRepositoryAdapter(this.mongoTemplate);

	private final EasyRandom random = new EasyRandom();
	private final LocalDateTime today = LocalDateTime.now();
	private final LocalDateTime tomorrow = this.today.plusDays(1L);

	@BeforeEach
	void resetAll() {
		reset(this.mongoTemplate, this.aggregationResults, this.updateResult);
	}

	@Test
	void findByStartAfterAndEndBefore() {
		final List<EventDB> dbList = this.random.objects(EventDB.class, 7).toList();
		when(this.aggregationResults.getMappedResults()).thenReturn(dbList);
		when(this.mongoTemplate.aggregate(any(Aggregation.class), eq(EVENT_COLLECTION), eq(EventDB.class)))
				.thenReturn(this.aggregationResults);

		final List<Event> result = this.repository.findByStartAfterAndEndBefore(this.today, this.tomorrow);

		assertNotNull(result);
		assertEquals(7, result.size());
	}

	@Test
	void update() {
		final List<Event> list = this.random.objects(Event.class, 3).toList();

		when(this.mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(EventDB.class)))
				.thenReturn(this.updateResult);
		this.repository.update(list);

		verify(this.mongoTemplate, times(3)).updateFirst(any(Query.class), any(Update.class), eq(EventDB.class));
	}

	@Test
	public void insertOrUpdate_inserting() {
		final List<Event> list = this.random.objects(Event.class, 3).toList();
		when(this.mongoTemplate.exists(any(Query.class), eq(EventDB.class))).thenReturn(false);

		this.repository.insertOrUpdate(list);

		verify(this.mongoTemplate, times(3)).insert(any(EventDB.class));
	}

	@Test
	public void insertOrUpdate_updating() {
		final List<Event> list = this.random.objects(Event.class, 3).toList();
		when(this.mongoTemplate.exists(any(Query.class), eq(EventDB.class))).thenReturn(true);
		when(this.mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(EventDB.class)))
				.thenReturn(this.updateResult);

		this.repository.insertOrUpdate(list);

		verify(this.mongoTemplate, times(3)).updateFirst(any(Query.class), any(Update.class), eq(EventDB.class));
	}
}
