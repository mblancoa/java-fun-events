package com.funevents.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.funevents.EventService;
import com.funevents.supply.EventProvider;
import com.funevents.supply.SupplyService;

@Configuration
@EnableScheduling
public class SupplyConfiguration {

	@Bean
	public SupplyService supplyService(@Autowired final EventProvider eventProvider, @Autowired final EventService eventService) {
		return new SupplyService(eventService, eventProvider);
	}
}
