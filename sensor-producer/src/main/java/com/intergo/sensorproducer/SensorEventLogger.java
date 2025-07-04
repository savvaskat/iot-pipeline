package com.intergo.sensorproducer;

// Marks this class as an application-scoped CDI bean, meaning
// it will be instantiated once and reused throughout the app.
import jakarta.enterprise.context.ApplicationScoped;

// Logger from JBoss used for application logging.
import org.jboss.logging.Logger;

@ApplicationScoped
public class SensorEventLogger {

    // Define a custom logger named "AUDIT"
    // This logger can be configured separately in log settings if needed
    private static final Logger audit = Logger.getLogger("AUDIT");

    /**
     * Logs the sensor event in a structured format.
     * This is intended for audit purposes, to track every sensor reading processed.
     * 
     * @param event - the incoming SensorEvent object containing:
     *              - sensorId: ID of the sensor
     *              - temperature: the measured temperature
     *              - timestamp: when the event occurred
     */
    public void logEvent(SensorEvent event) {
        // Log an info-level message with formatted output
        // Example: AUDIT LOG: Sensor ID sensor-001, Temp 25.40 at 2025-07-04T12:00:00Z
        audit.infof("AUDIT LOG: Sensor ID %s, Temp %.2f at %s", event.sensorId, event.temperature, event.timestamp);
    }
}
