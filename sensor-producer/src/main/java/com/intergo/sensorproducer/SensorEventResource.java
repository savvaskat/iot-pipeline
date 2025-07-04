package com.intergo.sensorproducer;

// CDI and Jakarta REST annotations for dependency injection and REST endpoint definitions
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

// OpenAPI annotations to generate API documentation automatically
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

// MicroProfile Metrics annotation to enable metrics collection
import org.eclipse.microprofile.metrics.annotation.Counted;

// The class defines a REST resource located at the /sensors/events path
@Path("/sensors/events")

// Tag used for Swagger UI documentation grouping
@Tag(name = "Sensor Events", description = "Send sensor data for processing")
public class SensorEventResource {

    // Inject the KafkaProducerService to publish events to Kafka
    @Inject
    KafkaProducerService producerService;

    // Inject the logger class to audit/log each incoming sensor event
    @Inject
    SensorEventLogger eventLogger;

    /**
     * Handle HTTP POST requests to /sensors/events with JSON payload.
     * The request body is expected to be a valid SensorEvent object.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)   // Accepts JSON data
    @Produces(MediaType.APPLICATION_JSON)   // Responds with JSON (though body is empty here)

    // OpenAPI summary/description used in Swagger UI and generated documentation
    @Operation(
        summary = "Send a sensor reading",
        description = "Publishes a temperature reading to Kafka"
    )

    // Define the possible API responses for documentation and client awareness
    @APIResponse(
        responseCode = "202",
        description = "Sensor event accepted for processing"
    )
    @APIResponse(
        responseCode = "400",
        description = "Invalid sensor event payload",
        content = @Content(schema = @Schema(implementation = String.class))
    )

    // MicroProfile metric: count how many times this endpoint is hit
    @Counted(name = "sensor_event_requests", description = "Number of sensor event POST requests")

    public Response receiveSensorEvent(@Valid SensorEvent event) {
        // Log the incoming event for auditing
        eventLogger.logEvent(event);

        // Publish the event to Kafka
        producerService.send(event);

        // Return HTTP 202 Accepted status to indicate async processing
        return Response.accepted().build();
    }
}