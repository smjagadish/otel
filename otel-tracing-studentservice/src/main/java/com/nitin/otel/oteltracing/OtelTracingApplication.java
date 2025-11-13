package com.nitin.otel.oteltracing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Main Spring Boot application for OpenTelemetry tracing student service.
 * This service demonstrates distributed tracing capabilities with OpenTelemetry.
 */
@SpringBootApplication
public class OtelTracingApplication {

	/**
	 * Application entry point.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(OtelTracingApplication.class, args);
	}
	
	/**
	 * Creates RestTemplate bean for HTTP client operations.
	 * @return configured RestTemplate instance
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
