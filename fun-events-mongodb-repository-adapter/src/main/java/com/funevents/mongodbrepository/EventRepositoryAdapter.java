package com.funevents.mongodbrepository;

import static com.funevents.mongodbrepository.model.Constants.EVENT_COLLECTION;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.funevents.EventRepository;
import com.funevents.model.Event;
import com.funevents.mongodbrepository.model.EventDB;
import com.funevents.mongodbrepository.model.EventOut;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventRepositoryAdapter implements EventRepository {

	private final MongoDBEventMapper mapper = MongoDBEventMapper.INSTANCE;
	private final MongoTemplate mongoTemplate;

	@Override
	public List<Event> findByStartAfterAndEndBefore(final LocalDateTime from, final LocalDateTime to) {
		final Aggregation aggregation = newAggregation(
				match(Criteria.where("starts_at").gte(from).andOperator(Criteria.where("ends_at").lte(to))));

		final AggregationResults<EventDB> results = this.mongoTemplate.aggregate(aggregation, EVENT_COLLECTION, EventDB.class);

		return this.mapper.map(results.getMappedResults());
	}

	@Override
	public String findByStartAfterAndEndBeforeToJson(final LocalDateTime from, final LocalDateTime to) {
		final Aggregation aggregation = newAggregation(
				match(Criteria.where("starts_at").gte(from).andOperator(Criteria.where("ends_at").lte(to))),
				project().andExpression("{$toUUID: '$_id'}").as("id").andInclude("title")
						.andExpression("{$dateToString: {format: '%Y-%m-%d', date: '$starts_at'}}").as("start_date")
						.andExpression("{$dateToString: {format: '%H:%M:%S', date: '$starts_at'}}").as("start_time")
						.andExpression("{$dateToString: {format: '%Y-%m-%d', date: '$ends_at'}}").as("end_date")
						.andExpression("{$dateToString: {format: '%H:%M:%S', date: '$ends_at'}}").as("end_time")
						.andInclude("max_price", "min_price"));
		final AggregationResults<EventOut> results = this.mongoTemplate.aggregate(aggregation, EVENT_COLLECTION, EventOut.class);

		final String json = results.getRawResults().toJson();
		final int idxfrom = json.indexOf("[") - 1;
		final int idxto = json.lastIndexOf("]") + 1;
		return json.substring(idxfrom, idxto);
	}

	@Override
	public void update(final List<Event> toUpdate) {
		toUpdate.stream().forEach(this::update);
	}

	public void update(final Event toUpdate) {
		final EventDB event = this.mapper.map(toUpdate);
		final Query query = new Query(Criteria.where("prov_id").is(event.getProvId()));

		final Update update = new Update().min("min_price", event.getMinPrice()).max("max_price", event.getMaxPrice());

		this.mongoTemplate.updateFirst(query, update, EventDB.class);
	}

	@Override
	public void insertOrUpdate(final List<Event> toInsert) {
		toInsert.stream().forEach(this::insertOrUpdate);
	}

	public void insertOrUpdate(final Event toInsert) {
		final Query query = new Query(Criteria.where("prov_id").is(toInsert.getProvId()));
		if (!this.mongoTemplate.exists(query, EventDB.class)) {
			final EventDB event = this.mapper.map(toInsert);
			event.setId(UUID.randomUUID());
			this.mongoTemplate.insert(event);
		} else {
			update(toInsert);
		}
	}

}
