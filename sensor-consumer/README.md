# Sensor Consumer Microservice

This microservice consumes IoT sensor data from a Kafka topic and stores it in MongoDB.

---

## Features

- Subscribes to Kafka topic `'sensor-events'`
- Deserializes JSON messages into `SensorEvent` objects
- Persists each event to MongoDB (`sensordb.events`)
- Includes error handling with Kafka retry via `RuntimeException`
- Exposes health and metrics endpoints
- Unit tests using JUnit & Mockito
- Dockerized and runs with Kafka & MongoDB

---

## Tech Stack

| Component         | Technology                        |
|------------------|------------------------------------|
| Language          | Java 17                           |
| Framework         | Quarkus 3.24.x                    |
| Messaging         | Apache Kafka (via Docker)         |
| Persistence       | MongoDB (via Docker)              |
| Kafka Messaging   | SmallRye Reactive Messaging       |
| Logging           | JBoss Logger                      |
| Testing           | JUnit 5, Mockito                  |
| Containerization  | Docker & Docker Compose           |

---

## Getting Started

### 1. Clone the Project

```bash
git clone https://github.com/savvaskat/iot-pipeline.git
cd iot-pipeline/sensor-consumer
```

### 2. Build the Project
```bash
./mvnw clean package
```
Or with local Maven:

```bash
mvn clean package
```

---

## Docker Details 

- This service is wired into the root-level `docker-compose.yml`
- The image is built from this directory using:

```yaml
sensor-consumer:
  build:
    context: ./sensor-consumer
  image: savvas/sensor-consumer:1.0.0-SNAPSHOT
  environment:
    - KAFKA_BOOTSTRAP_SERVERS=kafka:9092
    - MONGODB_CONNECTION_STRING=mongodb://mongodb:27017
  depends_on:
    - kafka
    - mongodb
```
---

## Kafka Integration

- This service listens to the topic: 'sensor-events'

---

## Configuration Reference

**src/main/resources/application.properties**

```properties
# Kafka Consumer
mp.messaging.incoming.sensor-events.connector=smallrye-kafka
mp.messaging.incoming.sensor-events.topic=sensor-events
mp.messaging.incoming.sensor-events.value.deserializer=com.intergo.sensorconsumer.SensorEventDeserializer
mp.messaging.incoming.sensor-events.bootstrap.servers=${KAFKA_BOOTSTRAP_SERVERS}

# MongoDB
quarkus.mongodb.connection-string=${MONGODB_CONNECTION_STRING}
quarkus.mongodb.database=sensordb

# Swagger + Health + OpenAPI
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.path=/q/swagger-ui
quarkus.smallrye-openapi.path=/openapi
quarkus.smallrye-health.root-path=/q/health
quarkus.smallrye-metrics.path=/q/metrics

# JVM workaround for Java 21+
quarkus.jib.jvm-arguments=--add-opens=java.base/java.lang=ALL-UNNAMED

# HTTP root
quarkus.http.root-path=/
```
---

## MongoDB Persistence

Sensor events are saved in:

- Database: sensordb
- Collection: events

A sample document:

```json
{
  "sensorId": "sensor-001",
  "timestamp": "2025-07-04T12:00:00Z",
  "temperature": 25.4
}
```
---

## Running Unit Tests

```bash
./mvnw test
```

Includes:

- Verifies MongoDB persistence
- Verifies Kafka message handling and exception logic

---

## Error Handling

If MongoDB fails, a RuntimeException is thrown to allow Kafka to retry delivery automatically:

```java
throw new RuntimeException("Kafka message processing failed; will be retried", e);
```
---
