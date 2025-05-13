package com.funevents.configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.UuidRepresentation;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.funevents.EventRepository;
import com.funevents.mongodbrepository.EventRepositoryAdapter;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoDBRepositoryConfiguration {

	@Value("${mongodb.uri:mongodb://localhost:27017}")
	private String mongodbUri;
	@Value("${mongodb.database.name:FunDataBase}")
	private String databaseName;

	@Profile("!test")
	@Bean
	public MongoClient mongoClient() {
		fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		final MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(new ConnectionString(this.mongodbUri)).uuidRepresentation(UuidRepresentation.JAVA_LEGACY)
				.build();

		return MongoClients.create(settings);
	}

	@Bean
	public MongoTemplate mongoTemplate(@Autowired final MongoClient mongoClient) {

		return new MongoTemplate(mongoClient, this.databaseName);
	}

	@Bean
	public EventRepository eventRepository(@Autowired final MongoTemplate mongoTemplate) {
		return new EventRepositoryAdapter(mongoTemplate);
	}
}
