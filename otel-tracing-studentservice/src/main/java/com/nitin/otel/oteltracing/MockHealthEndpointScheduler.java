package com.nitin.otel.oteltracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Scheduled health check component for monitoring the school service availability.
 * This component performs periodic health checks to ensure the school service is running
 * and generates distributed tracing data for OpenTelemetry monitoring.
 */
@Component
public class MockHealthEndpointScheduler {

    private static final Logger log = LoggerFactory.getLogger(MockHealthEndpointScheduler.class);
    private static final int HEALTH_CHECK_INTERVAL_MS = 10000;

    @Autowired
    RestTemplate restTemplate;

    /**
     * Performs a health check every 10 seconds (HEALTH_CHECK_INTERVAL_MS).
     * This method calls the school service health endpoint to verify service availability
     * and generates trace spans for distributed tracing analysis.
     * 
     * @throws RuntimeException if the health check fails
     */
    @Scheduled(fixedRate = HEALTH_CHECK_INTERVAL_MS)
    public void fixedRateSch() {
        log.debug("Starting health check at {}", System.currentTimeMillis());
        
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:9000/health", String.class);
            log.debug("Health check response - Status: {}, Body: {}", response.getStatusCode(), response.getBody());
        } catch (Exception e) {
            log.error("Health check failed: {}", e.getMessage(), e);
            throw new RuntimeException("Health check failed", e);
        }
    }
}