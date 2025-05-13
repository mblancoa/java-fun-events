package com.funevents.startapp;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.funevents.model.Event;
import com.funevents.mongodbrepository.MongoDBEventMapper;
import com.funevents.mongodbrepository.model.EventDB;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(onConstructor = @__({ @org.springframework.beans.factory.annotation.Autowired }))
public class MongoDBTestUtils {

	private final MongoTemplate mongoTemplate;
	private MongoDBEventMapper mapper = MongoDBEventMapper.INSTANCE;

	public void insertOne(final Event event) {
		final EventDB db = this.mapper.map(event);
		this.mongoTemplate.insert(db);
	}

	public void insertMultipleOnes(final List<Event> list) {
		final List<EventDB> db = this.mapper.mapToDb(list);
		this.mongoTemplate.insert(db, EventDB.class);
	}

	public void cleanCollection(final Class<?> type) {
		this.mongoTemplate.remove(new Query(), type);
	}
}
