package com.nitin.otel.oteltracing;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * Lifecycle management bean for handling application shutdown events.
 * Implements DisposableBean to perform cleanup operations when the application stops.
 */
@Component
public class LifecycleBean implements DisposableBean {
    
    /**
     * Called when the application context is being destroyed.
     * Performs cleanup operations during application shutdown.
     * @throws Exception if cleanup fails
     */
    @Override
    public void destroy() throws Exception {
      System.out.println("lifecycle bean manual imp");
    }
}
