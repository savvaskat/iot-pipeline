package com.intergo.sensorconsumer;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.jboss.logging.Logger;

/**
 * Repository class responsible for persisting sensor events to MongoDB.
 * Includes retry logic for transient MongoDB failures.
 */
@ApplicationScoped
public class SensorEventRepository {

    private static final Logger log = Logger.getLogger(SensorEventRepository.class);

    @Inject
    MongoClient mongoClient;

    // Number of MongoDB retry attempts before giving up
    private static final int MAX_RETRIES = 3;

    /**
     * Saves a SensorEvent to the `events` collection of the `sensordb` database.
     * Retries the insert operation up to MAX_RETRIES on failure.
     */
    public void save(SensorEvent event) {
        MongoCollection<Document> collection = mongoClient
            .getDatabase("sensordb")
            .getCollection("events");

        Document doc = new Document()
            .append("sensorId", event.sensorId)
            .append("timestamp", event.timestamp)
            .append("temperature", event.temperature);

        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                collection.insertOne(doc);
                return; // Success
            } catch (MongoException e) {
                attempt++;
                log.warnf("⚠️ MongoDB insert failed (attempt %d/%d): %s", attempt, MAX_RETRIES, e.getMessage());

                if (attempt >= MAX_RETRIES) {
                    log.error("❌ Giving up after multiple MongoDB insert failures.");
                    throw new RuntimeException("MongoDB insert failed after retries", e);
                }

                try {
                    Thread.sleep(500L); // short delay before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted", ie);
                }
            }
        }
    }
}