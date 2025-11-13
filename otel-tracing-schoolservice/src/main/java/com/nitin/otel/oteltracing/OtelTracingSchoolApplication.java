package com.nitin.otel.oteltracing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
/**
 * Main Spring Boot application for OpenTelemetry tracing school service.
 * Leverages Spring Web MVC Async capabilities for asynchronous processing
 * and demonstrates distributed tracing with OpenTelemetry.
 */
@SpringBootApplication
@EnableAsync
public class OtelTracingSchoolApplication {

	/**
	 * Application entry point.
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(OtelTracingSchoolApplication.class, args);
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
