package com.intergo.sensorproducer;

// Bean Validation annotations (Jakarta Validation)
// Used to enforce constraints on incoming JSON data for the REST endpoint
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

import java.time.Instant; // Represents a timestamp in ISO-8601 format (e.g., 2025-07-04T12:00:00Z)

public class SensorEvent {

    // The sensorId must not be null or empty (after trimming whitespace)
    // If it's missing or blank, the validation will fail with the provided message
    @NotBlank(message = "Sensor ID cannot be blank")
    public String sensorId;

    // The timestamp must not be null. This ensures we always receive a time for the event.
    @NotNull(message = "Timestamp is required")
    public Instant timestamp;

    // The temperature must be greater than or equal to -50.0
    // This is useful to reject obviously incorrect values (e.g., sensor malfunction)
    @DecimalMin(value = "-50.0", message = "Temperature too low")

    // The temperature must be less than or equal to 150.0
    // Upper bound also protects against unrealistic values
    @DecimalMax(value = "150.0", message = "Temperature too high")
    public double temperature;

    // No-argument constructor is required by frameworks like Jackson to instantiate the class
    // when converting incoming JSON into Java objects
    public SensorEvent() {}

    // This constructor can be used manually in unit tests or internal logic
    public SensorEvent(String sensorId, Instant timestamp, double temperature) {
        this.sensorId = sensorId;
        this.timestamp = timestamp;
        this.temperature = temperature;
    }
}