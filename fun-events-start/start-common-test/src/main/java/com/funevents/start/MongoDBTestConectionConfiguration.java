package com.funevents.start;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import java.io.IOException;

import org.bson.UuidRepresentation;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.DirtiesContext;

import com.funevents.configuration.MongoDBRepositoryConfiguration;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.process.runtime.Network;
import jakarta.annotation.PreDestroy;

@Configuration
@Import(MongoDBRepositoryConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MongoDBTestConectionConfiguration {

	@Value("${mongodb.port:27018}")
	private int mongodbPort;

	private MongodProcess mongodProcess;
	private MongodExecutable mongodExecutable;

	@Bean
	@DependsOn("mongodProcess")
	public MongoClient mongoClient() {
		fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
				fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		final String mongodbUri = "mongodb://localhost:" + this.mongodbPort;
		final MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(new ConnectionString(mongodbUri))
				.uuidRepresentation(UuidRepresentation.JAVA_LEGACY).build();

		return MongoClients.create(settings);
	}

	@Bean
	public MongodExecutable mongodExecutable() throws Exception {
		final MongodConfig mongodConfig = ImmutableMongodConfig.builder()
				.version(de.flapdoodle.embed.mongo.distribution.Version.Main.V6_0)
				.net(new de.flapdoodle.embed.mongo.config.Net(this.mongodbPort, Network.localhostIsIPv6())).build();

		final MongodStarter starter = MongodStarter.getDefaultInstance();
		this.mongodExecutable = starter.prepare(mongodConfig);
		return this.mongodExecutable;
	}

	@Bean
	public MongodProcess mongodProcess(@Autowired final MongodExecutable mongodExecutable) throws IOException {
		this.mongodProcess = mongodExecutable.start();
		return this.mongodProcess;
	}

	@Bean
	public MongoDBTestUtils mongoDBTestUtils(@Autowired final MongoTemplate mongoTemplate) {
		return new MongoDBTestUtils(mongoTemplate);
	}

	@PreDestroy
	public void stop() {
		if (this.mongodProcess != null && this.mongodProcess.isProcessRunning()) {
			this.mongodProcess.stop();
		}
		if (this.mongodExecutable != null) {
			this.mongodExecutable.stop();
		}
	}

}