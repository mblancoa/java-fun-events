package com.funevents.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

import com.funevents.xxxprovider.EventProviderAdapter;
import com.funevents.xxxprovider.XxxProviderEventMapper;

@Configuration
public class XxxProviderConfiguration {

	@Value("${provider.name:xxx}")
	private String providerName;

	@Value("${provider.url:https://provider.code-challenge.funup.com/api/events}")
	private String providerUrl;

	@Value("${provider.timeout:2000}") // milliseconds
	private int timeout;

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

		marshaller.setPackagesToScan("com.funevents.xxxprovider.model");
		return marshaller;
	}

	@Bean
	public RestTemplate restTemplate(@Autowired final Jaxb2Marshaller jaxb2Marshaller) {
		final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(this.timeout);
		requestFactory.setReadTimeout(this.timeout);
		final RestTemplate restTemplate = new RestTemplate(requestFactory);

		restTemplate.getMessageConverters()
				.add(new org.springframework.http.converter.xml.MarshallingHttpMessageConverter(jaxb2Marshaller));

		return restTemplate;
	}

	@Bean
	public XxxProviderEventMapper xxxProviderEventMapper() {
		return new XxxProviderEventMapper(this.providerName);
	}

	@Bean
	EventProviderAdapter eventProviderAdapter(@Autowired final RestTemplate restTemplate,
			@Autowired final XxxProviderEventMapper xxxProviderEventMapper) {
		return new EventProviderAdapter(this.providerUrl, restTemplate, xxxProviderEventMapper);
	}

}
