package com.funevents.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.funevents.configuration.ApiConfiguration;
import com.funevents.configuration.DomainConfiguration;
import com.funevents.configuration.MongoDBRepositoryConfiguration;
import com.funevents.configuration.XxxProviderConfiguration;

@SpringBootApplication
@Import(value = { MongoDBRepositoryConfiguration.class, ApiConfiguration.class, XxxProviderConfiguration.class,
		DomainConfiguration.class })
public class EventsStartAppApplication {
	public static void main(final String[] args) {
		SpringApplication.run(EventsStartAppApplication.class, args);
	}
}
