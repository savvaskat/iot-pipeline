package com.intergo.sensorconsumer;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

/**
 * Custom deserializer for SensorEvent objects, used by Kafka to convert JSON into Java.
 */
public class SensorEventDeserializer extends ObjectMapperDeserializer<SensorEvent> {
    public SensorEventDeserializer() {
        super(SensorEvent.class);
    }
}