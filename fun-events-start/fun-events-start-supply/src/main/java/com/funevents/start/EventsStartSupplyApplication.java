package com.funevents.start;

import static com.funevents.configuration.DomainConfiguration.SUPPLY_PROFILE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;

import com.funevents.SupplyService;
import com.funevents.configuration.DomainConfiguration;
import com.funevents.configuration.MongoDBRepositoryConfiguration;
import com.funevents.configuration.XxxProviderConfiguration;

@SpringBootApplication
@Profile(SUPPLY_PROFILE)
@Import(value = { MongoDBRepositoryConfiguration.class, XxxProviderConfiguration.class, DomainConfiguration.class })
public class EventsStartSupplyApplication implements CommandLineRunner {

	@Autowired
	private SupplyService supplyService;

	public static void main(final String[] args) {
		SpringApplication.run(EventsStartSupplyApplication.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {
	}

	@Scheduled(fixedRateString = "${supply.feed-interval:60000}")
	public void fetchEventsFromProvider() {
		System.out.println("Estoy aqui");
		this.supplyService.fetchEventsFromProvider();
	}

}
