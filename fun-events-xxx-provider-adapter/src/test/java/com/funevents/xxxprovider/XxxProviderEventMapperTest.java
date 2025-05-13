package com.funevents.xxxprovider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.api.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.funevents.model.Event;
import com.funevents.xxxprovider.XxxProviderEventMapper;
import com.funevents.xxxprovider.model.ProviderResponse;

public class XxxProviderEventMapperTest {

	private static final String PROVIDER_NAME = "xxx";
	private final XxxProviderEventMapper mapper = new XxxProviderEventMapper(PROVIDER_NAME);

	private final Jaxb2Marshaller jaxb2Marshaller = jaxb2Marshaller();

	@Test
	void map() {
		final ProviderResponse response = parserXml("response.xml");
		final List<Event> expected = getEventsFromResponse();

		final List<Event> result = this.mapper.map(response);

		assertEquals(expected.size(), result.size());
		assertIterableEquals(expected, result);
	}

	@Test
	void map_resultIsEmptyWhenResponseIsNull() {
		final List<Event> result = this.mapper.map(null);

		assertTrue(result.isEmpty());
	}

	private ProviderResponse parserXml(final String fileName) {
		try {
			final InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
			if (is == null) {
				throw new RuntimeException("File not found in classpath: " + fileName);
			}
			return (ProviderResponse) this.jaxb2Marshaller.unmarshal(new StreamSource(is));
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Jaxb2Marshaller jaxb2Marshaller() {
		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

		marshaller.setPackagesToScan("com.funevents.xxxprovider.model");
		return marshaller;
	}

	private static List<Event> getEventsFromResponse() {
		final List<Event> list = new LinkedList();
		final Event event1 = Event.builder().provId("xxx-291-291").title("Camela en concierto").onlineSale(true)
				.startsAt(LocalDateTime.parse("2021-06-30T21:00:00")).endsAt(LocalDateTime.parse("2021-06-30T22:00:00"))
				.minPrice(15.00).maxPrice(30.00).build();
		final Event event2 = Event.builder().provId("xxx-322-1642").title("Pantomima Full").onlineSale(true)
				.startsAt(LocalDateTime.parse("2021-02-10T20:00:00")).endsAt(LocalDateTime.parse("2021-02-10T21:30:00"))
				.minPrice(55.00).maxPrice(55.00).build();
		final Event event3 = Event.builder().provId("xxx-322-1643").title("Pantomima Full").onlineSale(true)
				.startsAt(LocalDateTime.parse("2021-02-11T20:00:00")).endsAt(LocalDateTime.parse("2021-02-11T21:30:00"))
				.minPrice(55.00).maxPrice(55.00).build();
		final Event event4 = Event.builder().provId("xxx-1591-1642").title("Los Morancos").onlineSale(true)
				.startsAt(LocalDateTime.parse("2021-07-31T20:00:00")).endsAt(LocalDateTime.parse("2021-07-31T21:00:00"))
				.minPrice(65.00).maxPrice(75.00).build();
		list.add(event1);
		list.add(event2);
		list.add(event3);
		list.add(event4);

		return list;
	}

}
