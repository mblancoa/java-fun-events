package com.funevents.mongodbrepository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.funevents.mongodbrepository.model.EventDB;

public interface EventSpringRepository extends MongoRepository<EventDB, UUID> {

}
