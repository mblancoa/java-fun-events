package com.funevents.configuration;

import static com.funevents.configuration.DomainConfiguration.CONSUMER_PROFILE;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(CONSUMER_PROFILE)
@Configuration
@ComponentScan(basePackages = { "com.funevents.api" })
public class ApiConfiguration {

}
