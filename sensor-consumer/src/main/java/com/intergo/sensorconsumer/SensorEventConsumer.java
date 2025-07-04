package com.intergo.sensorconsumer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

@ApplicationScoped
public class SensorEventConsumer {

    private static final Logger log = Logger.getLogger(SensorEventConsumer.class);

    @Inject
    SensorEventRepository repository;

    /**
     * Consumes sensor events from Kafka topic `sensor-events`.
     * This method is automatically triggered via the @Incoming annotation.
     * 
     * If processing fails, we log the error and rethrow a RuntimeException,
     * which signals Kafka to retry the message based on its retry policy.
     */
    @Incoming("sensor-events")
    @Counted(name = "sensor_event_count", description = "How many sensor events have been received")
    @Timed(name = "sensor_event_processing_time", description = "Time taken to process sensor events")
    public void consume(SensorEvent event) {
        try {
            log.infof("Received event from Kafka: %s - %.2f°C at %s",
                      event.sensorId, event.temperature, event.timestamp);

            // Attempt to store the event in MongoDB
            repository.save(event);

        } catch (Exception e) {
            log.errorf(e, "❌ Failed to process sensor event %s", event.sensorId);

            // Trigger Kafka retry by rethrowing a RuntimeException
            throw new RuntimeException("Kafka message processing failed; will be retried", e);
        }
    }
}