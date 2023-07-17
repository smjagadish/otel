package com.nitin.otel.oteltracing;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
 
@RestController
public class SchoolServiceController {
  @Autowired
  RestTemplate restTemplate;
 
  @RequestMapping(value = "/getSchoolDetails/{schoolname}", method = RequestMethod.GET)
  public String getStudents(@PathVariable String schoolname) 
  {
    // creating my own span
 Span span = null;
    Tracer tracer = GlobalOpenTelemetry.getTracer("my tracer");
    Span span2 = tracer.spanBuilder("sp11")
            .setSpanKind(SpanKind.INTERNAL)
            .setAttribute("attr","fff")
         //   .setParent(Context.current())
            .startSpan();

    try {
     // reference to the parent span
      span = Span.current();
     // custom span made as current
     span2.makeCurrent();
     // attr injection
      dummy();
      // span create using annotation . will use span2 as parent
      manual_span();

      restTemplate.exchange("http://localhost:9001/health", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
      });
    }
    catch (Exception e)
    {
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
    span.setAttribute("rtgf","popopopgo");
    System.out.println("Getting School details for " + schoolname);
 
    String response = restTemplate.exchange("http://localhost:9000/getStudentDetailsForSchool/{schoolname}",
                HttpMethod.GET, null, new ParameterizedTypeReference<String>() {}, schoolname).getBody();
 
    System.out.println("Response Received as " + response);

    return "School Name -  " + schoolname + " \n Student Details " + response;
  }
  @RequestMapping(value = "/health", method = RequestMethod.GET)
  public String getHealth()   {
    return "School Service is Healthy";
  }

  public void dummy()
  {
    System.out.print("called for fun");
    Span span = Span.current();

    span.setAttribute("dummymeth","kklkljlk");
  }

  //Jul11 2023
  // annotation based manual spanning
  @WithSpan
  public void manual_span()
  {
      for (int i=0; i<100000;i++)
      {

      }
  }

}
