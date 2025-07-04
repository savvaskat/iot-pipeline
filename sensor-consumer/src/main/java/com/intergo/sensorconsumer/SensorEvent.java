package com.intergo.sensorconsumer;

import java.time.Instant;

/**
 * POJO representing a sensor event.
 * This class is used for JSON deserialization and Kafka messaging.
 */
public class SensorEvent {

    public String sensorId;
    public Instant timestamp;
    public double temperature;

    // Required by ObjectMapper and Kafka deserialization
    public SensorEvent() {}

    public SensorEvent(String sensorId, Instant timestamp, double temperature) {
        this.sensorId = sensorId;
        this.timestamp = timestamp;
        this.temperature = temperature;
    }
}