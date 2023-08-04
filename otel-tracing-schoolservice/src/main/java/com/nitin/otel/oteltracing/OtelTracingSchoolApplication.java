package com.nitin.otel.oteltracing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
// Leveraging Spring Web MVC Async
@SpringBootApplication
@EnableAsync
public class OtelTracingSchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(OtelTracingSchoolApplication.class, args);
	}
	 @Bean
	  public RestTemplate restTemplate() {
	    return new RestTemplate();
	  }
}
