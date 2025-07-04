package com.intergo.sensorproducer;

// This annotation marks this class as a CDI bean with application-wide scope,
// meaning a single instance will be used for the entire application.
import jakarta.enterprise.context.ApplicationScoped;

// Used to inject and send data into a Kafka channel
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

// Standard CDI injection mechanism
import jakarta.inject.Inject;

// Quarkus' logging API built on top of JBoss logging
import org.jboss.logging.Logger;

@ApplicationScoped
public class KafkaProducerService {

    // Logger instance for this class, used for logging messages to the console or file.
    private static final Logger logger = Logger.getLogger(KafkaProducerService.class);

    // Inject a Kafka Emitter which publishes messages to the Kafka topic "sensor-events".
    // The topic name must match the channel defined in your `application.properties`.
    @Inject
    @Channel("sensor-events")
    Emitter<SensorEvent> emitter;

    // This method is called whenever a new sensor event is received and needs to be sent to Kafka.
    public void send(SensorEvent event) {
        // Send the event asynchronously. `emitter.send()` returns a CompletionStage,
        // so we can attach a callback using `.whenComplete()` to handle success/failure.
        emitter.send(event).whenComplete((res, ex) -> {
            if (ex != null) {
                // Log the error if the message fails to send
                logger.errorf("Failed to publish event: %s", ex.getMessage());
            } else {
                // Log success if the message is published
                logger.infof("Successfully published sensor event: %s", event.sensorId);
            }
        });
    }
}
