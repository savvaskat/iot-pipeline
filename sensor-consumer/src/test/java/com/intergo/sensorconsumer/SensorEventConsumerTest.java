package com.intergo.sensorconsumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SensorEventConsumerTest {

    private SensorEventConsumer consumer;
    private SensorEventRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = mock(SensorEventRepository.class);
        consumer = new SensorEventConsumer();
        consumer.repository = mockRepository;
    }

    @Test
    void testConsumeHandlesException() {
        SensorEvent mockEvent = new SensorEvent("sensor-1", java.time.Instant.now(), 25.4);

        // Simulate Mongo failure
        doThrow(new RuntimeException("Mongo down")).when(mockRepository).save(mockEvent);

        // Expect RuntimeException to be thrown (for Kafka retry logic)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            consumer.consume(mockEvent);
        });

        assertTrue(exception.getMessage().contains("Kafka message processing failed"));
    }
}