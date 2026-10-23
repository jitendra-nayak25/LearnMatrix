package com.oblms.attainment_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

	 @Bean
	    public RestClient assessmentRestClient() {
	        return RestClient.builder()
	                .baseUrl("http://localhost:8083")
	                .build();
	    }
}
