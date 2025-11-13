package com.nitin.otel.oteltracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MockHealthEndpointScheduler {

    private static final Logger log = LoggerFactory.getLogger(MockHealthEndpointScheduler.class);

    @Autowired
    RestTemplate restTemplate;

    @Scheduled(fixedRate = 10000)
    public void fixedRateSch() {
        log.debug("Starting health check at {}", System.currentTimeMillis());
        
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:9000/health", String.class);
            log.debug("Health check response - Status: {}, Body: {}", response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            log.error("Health check failed: {}", e.getMessage(), e);
        }
    }
}