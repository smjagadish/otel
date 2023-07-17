package com.nitin.otel.oteltracing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class MockHealthEndpointScheduler {

    @Autowired
    RestTemplate restTemplate;

    @Scheduled(fixedRate = 10000)
    public void fixedRateSch() {

        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:9001/health",String.class);
        System.out.println(response.getBody());
    }
}