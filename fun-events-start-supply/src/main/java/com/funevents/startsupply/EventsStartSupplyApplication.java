package com.funevents.startsupply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.funevents.configuration.DomainConfiguration;
import com.funevents.configuration.MongoDBRepositoryConfiguration;
import com.funevents.configuration.SupplyConfiguration;
import com.funevents.configuration.XxxProviderConfiguration;

@SpringBootApplication
@Import(value = { MongoDBRepositoryConfiguration.class, XxxProviderConfiguration.class, SupplyConfiguration.class,
		DomainConfiguration.class })
public class EventsStartSupplyApplication {
	public static void main(final String[] args) {
		SpringApplication.run(EventsStartSupplyApplication.class, args);
	}
}
