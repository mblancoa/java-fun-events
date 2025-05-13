package com.funevents.xxxprovider;

import java.util.List;

import org.springframework.web.client.RestTemplate;

import com.funevents.EventProvider;
import com.funevents.model.Event;
import com.funevents.xxxprovider.model.ProviderResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class EventProviderAdapter implements EventProvider {

	private final String url;
	private final RestTemplate restTemplate;
	private final XxxProviderEventMapper mapper;

	@Override
	public List<Event> getEvents() {
		try {
			final ProviderResponse response = this.restTemplate.getForObject(this.url, ProviderResponse.class);
			return this.mapper.map(response);
		} catch (final Exception e) {
			log.debug(e.getMessage());
			throw e;
		}
	}

}
