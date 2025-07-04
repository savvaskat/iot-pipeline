package com.intergo.sensorconsumer;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class SensorEventRepositoryTest {

    private MongoClient mockClient;
    private MongoDatabase mockDatabase;
    private MongoCollection<Document> mockCollection;
    private SensorEventRepository repository;

    @BeforeEach
    void setup() {
        mockClient = mock(MongoClient.class);
        mockDatabase = mock(MongoDatabase.class);

        // Suppress only this unchecked cast from raw to generic type
        @SuppressWarnings("unchecked")
        MongoCollection<Document> tempCollection = (MongoCollection<Document>) mock(MongoCollection.class);
        mockCollection = tempCollection;

        when(mockClient.getDatabase("sensordb")).thenReturn(mockDatabase);
        when(mockDatabase.getCollection("events")).thenReturn(mockCollection);

        repository = new SensorEventRepository();
        repository.mongoClient = mockClient; // inject mock client manually
    }

    @Test
    void testSaveInsertsDocumentIntoMongoDB() {
        // Arrange
        SensorEvent event = new SensorEvent("sensor-001", 
            java.time.Instant.parse("2025-07-04T12:00:00Z"), 
            25.4);

        // Act
        repository.save(event);

        // Assert
        verify(mockCollection, times(1)).insertOne(argThat(doc ->
            "sensor-001".equals(doc.getString("sensorId")) &&
            doc.get("timestamp") != null &&
            Double.valueOf(25.4).equals(doc.getDouble("temperature"))
        ));
    }
}