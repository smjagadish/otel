package com.nitin.otel.oteltracing;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapSetter;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;

@RestController

public class SchoolServiceController {
    @Autowired
    RestTemplate restTemplate;

    /**
     * Retrieves school details asynchronously with OpenTelemetry tracing.
     * Controller task runs async using default thread pool on a different thread.
     * Demonstrates baggage propagation, manual span creation, and distributed tracing.
     * @param schoolname the name of the school
     * @return CompletableFuture containing school details response
     * @throws Exception if processing fails
     */
    @RequestMapping(value = "/getSchoolDetails/{schoolname}", method = RequestMethod.GET)
    @Async
    public CompletableFuture<ResponseEntity<String>> getStudents(@PathVariable String schoolname) throws Exception {
        // creating my own span
        Span span = null;
        Tracer tracer = GlobalOpenTelemetry.getTracer("my tracer");

        // Jul 21, 2023
        // experimented with baggage , but this is not picked up by the SDK ?
        // Update on Jul 25, 2023
        // the baggage propogation in the context is working , but SDK doesnt cascade it to collector
        Baggage bg = Baggage.builder().put("liv", "pool").put("usa", "cxa").build();
        bg.storeInContext(Context.current()).makeCurrent();
        Span span2 = tracer.spanBuilder("sp11")
                .setSpanKind(SpanKind.INTERNAL)
                .setAttribute("attr", "fff")
                .setParent(Context.current())
                .startSpan();

        try {
            // reference to the parent span
            span = Span.current();
            // custom span made as current
            span2.makeCurrent();
            //Jul 25, 2023
            // HTTP header to be used for entity setting and usage in baggage
            final HttpHeaders headers = new HttpHeaders();
            //Setter called by the baggage API
            TextMapSetter<RestTemplate> setter =
                    new TextMapSetter<RestTemplate>() {
                        @Override
                        public void set(RestTemplate carrier, String key, String value) {

                            headers.set(key, value);


                        }
                    };

            // attr injection
            dummy();
            // span create using annotation . will use span2 as parent
            manual_span();
            // Baggage injection - note that the restTemplate here isn't used directly
            W3CBaggagePropagator.getInstance().inject(Context.current(), restTemplate, setter);
            // Http header inclusion
            HttpEntity<String> entity = new HttpEntity<>(headers);
            restTemplate.exchange("http://localhost:9001/health", HttpMethod.GET, entity, new ParameterizedTypeReference<String>() {

            });

            // Jul 21 , 2023
            // checking the relevance when using threads
            // the parent span must be same for these invocations and i expect to see 5 + 5 spans overall , i.e. for the http client lib and the servlet handler
            ExecutorService exec = Executors.newFixedThreadPool(5);
            for (int i = 0; i < 5; i++) {
                Future<String> cf = exec.submit(new Callable<String>() {

                    @Override
                    public String call() throws Exception {
                        restTemplate.exchange("http://localhost:9001/health", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                        });
                        Thread.sleep(10000);
                        manual_span();
                        return "Thread completed";
                    }
                });
            }
        } catch (Exception e) {
            // we can record exceptions into spans
            span2.recordException(e);
        }
        // new span ends
        finally {
            span2.setStatus(StatusCode.OK);
            span2.end();
        }

        // falling back to the span created by auto instrumented agent using the reference

        span.makeCurrent();

        // attribute injection
        span.setAttribute("rtgf", "popopopgo");
        System.out.println("Getting School details for " + schoolname);

        String response = restTemplate.exchange("http://localhost:9000/getStudentDetailsForSchool/{schoolname}",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                }, schoolname).getBody();

        System.out.println("Response Received as " + response);

        // allowing threads to run to completion , so i purposefully delay main thread
        // Aug 11 , 2023 - Updated to use a response entity based return stmt
        // keep improving this to embark spring learning in ||
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body(response));
        //return CompletableFuture.completedFuture("School Name -  " + schoolname + " \n Student Details " + response);


    }

    /**
     * Health check endpoint for service monitoring.
     * @return health status message
     */
    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public String getHealth() {
        return "School Service is Healthy";
    }

    /**
     * Dummy method for testing span attribute injection.
     * Adds custom attributes to the current OpenTelemetry span.
     */
    public void dummy() {
        System.out.print("called for fun");
        Span span = Span.current();

        span.setAttribute("dummymeth", "kklkljlk");
    }

    /**
     * Demonstrates annotation-based manual spanning with OpenTelemetry.
     * Runs asynchronously from a thread in the default thread pool.
     * Creates a custom span using @WithSpan annotation.
     */
    @WithSpan
    @Async
    public void manual_span() {
        for (int i = 0; i < 100000; i++) {

        }
    }

    /**
     * Empty endpoint demonstrating HTTP status control through ResponseStatus annotation.
     * Returns HTTP 201 CREATED status without response body.
     */
    @GetMapping("empty")
    @ResponseStatus(HttpStatus.CREATED)
    public void emptyResponseWithoutResponseStatus() {
    }

}
