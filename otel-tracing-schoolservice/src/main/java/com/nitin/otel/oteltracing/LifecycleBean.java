package com.nitin.otel.oteltracing;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class LifecycleBean implements DisposableBean {
    @Override
    public void destroy() throws Exception {
      System.out.println("lifecycle bean manual imp");
    }
}
