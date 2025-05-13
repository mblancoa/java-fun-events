package com.funevents.xxxprovider;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

import com.funevents.model.Event;
import com.funevents.supply.EventProvider;
import com.funevents.xxxprovider.EventProviderAdapter;
import com.funevents.xxxprovider.XxxProviderEventMapper;
import com.funevents.xxxprovider.model.ProviderResponse;

public class EventProviderAdapterTest {

	private static final String PROVIDER_URL = "http://localhost:8081/events";

	private final RestTemplate restTemplate = mock(RestTemplate.class);
	private final XxxProviderEventMapper mapper = mock(XxxProviderEventMapper.class);

	private final EventProvider provider = new EventProviderAdapter(PROVIDER_URL, this.restTemplate, this.mapper);

	private final EasyRandom random = new EasyRandom();

	@BeforeEach
	void resetAll() {
		reset(this.restTemplate, this.mapper);
	}

	@Test
	void getEvents_is_successful_when_every_thing_is_ok() {
		final ProviderResponse response = this.random.nextObject(ProviderResponse.class);
		final List<Event> list = this.random.objects(Event.class, 7).toList();
		when(this.restTemplate.getForObject(PROVIDER_URL, ProviderResponse.class)).thenReturn(response);
		when(this.mapper.map(response)).thenReturn(list);

		final List<Event> result = this.provider.getEvents();

		assertNotNull(result);
		assertIterableEquals(list, result);
	}

	@Test
	void getEvents_fails_when_restTemplate_fails() {
		final ProviderResponse response = this.random.nextObject(ProviderResponse.class);
		when(this.restTemplate.getForObject(PROVIDER_URL, ProviderResponse.class)).thenThrow(RuntimeException.class);

		try {
			this.provider.getEvents();
			fail("An exception was expected");
		} catch (final RuntimeException e) {
			verify(this.restTemplate).getForObject(PROVIDER_URL, ProviderResponse.class);
		}

	}

	@Test
	void getEvents_fails_when_mapper_fails() {
		final ProviderResponse response = this.random.nextObject(ProviderResponse.class);
		when(this.restTemplate.getForObject(PROVIDER_URL, ProviderResponse.class)).thenReturn(response);
		when(this.mapper.map(response)).thenThrow(RuntimeException.class);

		try {
			this.provider.getEvents();
			fail("An exception was expected");
		} catch (final RuntimeException e) {
			verify(this.restTemplate).getForObject(PROVIDER_URL, ProviderResponse.class);
		}
	}

	private RestTemplate restTemplate(final int timeout) {
		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("com.funevents.xxxprovider.model");

		final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(timeout);
		requestFactory.setReadTimeout(timeout);
		final RestTemplate restTemplate = new RestTemplate(requestFactory);

		restTemplate.getMessageConverters()
				.add(new org.springframework.http.converter.xml.MarshallingHttpMessageConverter(marshaller));

		return restTemplate;
	}

}
