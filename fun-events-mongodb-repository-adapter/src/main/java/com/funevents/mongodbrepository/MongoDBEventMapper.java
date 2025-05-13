package com.funevents.mongodbrepository;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.funevents.model.Event;
import com.funevents.mongodbrepository.model.EventDB;

@Mapper
public interface MongoDBEventMapper {
	public static final MongoDBEventMapper INSTANCE = Mappers.getMapper(MongoDBEventMapper.class);

	Event map(EventDB eventDb);

	List<Event> map(List<EventDB> list);

	EventDB map(Event event);

	List<EventDB> mapToDb(List<Event> list);
}
