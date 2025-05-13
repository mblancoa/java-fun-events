package com.funevents.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.funevents.EventRepository;
import com.funevents.EventService;

@Configuration
public class DomainConfiguration {

	@Bean
	public EventService eventService(@Autowired final EventRepository eventRepository) {
		return new EventService(eventRepository);
	}

}
