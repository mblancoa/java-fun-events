package com.funevents.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.funevents.EventProvider;
import com.funevents.EventRepository;
import com.funevents.EventService;
import com.funevents.SupplyService;

@Configuration
@EnableScheduling
public class DomainConfiguration {
	public static final String CONSUMER_PROFILE = "consumer";
	public static final String SUPPLY_PROFILE = "supply";

	@Bean
	public EventService eventService(@Autowired final EventRepository eventRepository) {
		return new EventService(eventRepository);
	}

	@Bean
	@Profile(SUPPLY_PROFILE)
	public SupplyService supplyService(@Autowired final EventProvider eventProvider, @Autowired final EventService eventService) {
		return new SupplyService(eventService, eventProvider);
	}

}
