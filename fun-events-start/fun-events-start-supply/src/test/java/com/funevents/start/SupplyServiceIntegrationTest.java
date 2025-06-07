package com.funevents.start;

import static com.funevents.configuration.DomainConfiguration.SUPPLY_PROFILE;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;

import com.funevents.mongodbrepository.model.EventDB;
import com.github.tomakehurst.wiremock.WireMockServer;

@ActiveProfiles({ "test", SUPPLY_PROFILE })
@SpringBootTest(classes = { MongoDBTestConetionConfiguration.class, EventsStartSupplyApplication.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SupplyServiceIntegrationTest {

	private static final String GET_EVENTS_PATH = "/events";
	private static final int PROVIDER_URL_PORT = 9090;

	private static WireMockServer wireMockServer = new WireMockServer(PROVIDER_URL_PORT);
	@Autowired
	private MongoDBTestUtils mongodb;
	@Value("${supply.feed-interval}")
	private long feedInterval;

	private final RestTemplate restTemplate = new RestTemplate();

	@DynamicPropertySource
	static void dynamicProperties(final DynamicPropertyRegistry registry) {
		registry.add("supply.feed-interval", () -> 3000L);
		registry.add("provider.url", () -> "http://localhost:9090/events");
	}

	@BeforeAll
	static void setup() {
		wireMockServer.start();
		wireMockServer.stubFor(get(urlEqualTo(GET_EVENTS_PATH))
				.willReturn(aResponse().withHeader("Content-Type", "application/xml").withBodyFile("response.xml")));
	}

	@AfterAll
	static void shudown() {
		wireMockServer.stop();
	}

	@Test
	void fetchEventsFromProviderTest() throws InterruptedException {
		this.mongodb.cleanCollection(EventDB.class);
		Thread.sleep(this.feedInterval + 2000L);
		final long count = this.mongodb.count(EventDB.class);

		assertEquals(4L, count);
	}
}
