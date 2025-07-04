# Sensor Producer Microservice

This microservice receives IoT sensor data via a REST API and publishes it to an Apache Kafka topic named 'sensors/events'.

---

## Features

- Exposes a REST endpoint: 'POST /sensors/event'
- Validates incoming JSON payloads (sensor ID, temperature, UTC timestamp)
- Produces events to Kafka using SmallRye Reactive Messaging
- Logs audit information via a custom logger
- Exposes Swagger UI ('/q/swagger-ui')
- Health checks at '/q/health'
- Unit & validation tests
- Dockerized and ready to run with Kafka & MongoDB

---

## Tech Stack

| Component         | Technology                        |
|------------------|------------------------------------|
| Language          | Java 17                           |
| Framework         | Quarkus 3.24.x                    |
| Messaging         | Apache Kafka (via Docker)         |
| Validation        | Jakarta Bean Validation           |
| API Docs          | Swagger / OpenAPI                 |
| Containerization  | Docker & Docker Compose           |
| Logging           | JBoss Logger                      |
| Testing           | JUnit 5, REST Assured             |

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/sensor-producer.git #TODO
cd iot-pipeline/sensor-producer
```

---

### 2. Build the Project

If you're using Maven wrapper:

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
sensor-producer:
  build:
    context: ./sensor-producer
  image: savvas/sensor-producer:1.0.0-SNAPSHOT
  ports:
    - "8080:8080"
  environment:
    - KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```
---

## API Endpoints

### POST '/sensors/event'

Send a sensor reading to Kafka.

**Request Body:**

```json
{
  "sensorId": "sensor-123",
  "timestamp": "2025-07-04T10:00:00Z",
  "temperature": 26.5
}
```

- `sensorId`: Required, non-empty string
- `timestamp`: Required, must be UTC ISO format
- `temperature`: Required, between -50.0 and 150.0

---

## Testing the API

### Swagger UI

Access:
```text
http://localhost:8080/q/swagger-ui
```

Try out the 'POST /sensors/event' endpoint.

---

### Health Check

Access:
```text
http://localhost:8080/q/health
```

You should see:
```json
{
  "status": "UP",
  "checks": [...]
}
```

---

## Validation Rules

| Field       | Rule                                  |
|-------------|----------------------------------------|
| sensorId    | Not blank                              |
| timestamp   | Must be present and in UTC ISO format  |
| temperature | Between -50.0 and 150.0                |

Invalid input returns `400 Bad Request` with error messages.

---

## Docker Details

This microservice is defined in the `docker-compose.yml` with:

```yaml
sensor-producer:
  image: savvas/sensor-producer:1.0.0-SNAPSHOT
  ports:
    - "8080:8080"
  environment:
    - KAFKA_BOOTSTRAP_SERVERS=kafka:9092
    - MONGODB_CONNECTION_STRING=mongodb://mongodb:27017
```

Other services:

- Kafka: `localhost:9092`
- MongoDB: `localhost:27018`

---

## Running Unit Tests

```bash
./mvnw test
```

Includes:

- Happy path test
- Validation error tests
- JSON payload testing using REST Assured

---

## Configuration Reference

**src/main/resources/application.properties**

```properties
# Kafka configuration
mp.messaging.outgoing.sensor-events.connector=smallrye-kafka
mp.messaging.outgoing.sensor-events.topic=sensor-events
mp.messaging.outgoing.sensor-events.value.serializer=io.quarkus.kafka.client.serialization.ObjectMapperSerializer
mp.messaging.outgoing.sensor-events.bootstrap.servers=kafka:9092

# Use localhost in test environment or let Dev Services spin up Kafka
%test.mp.messaging.outgoing.sensor-events.bootstrap.servers=localhost:9092

# Optional: Enable Dev Services if you're not running Kafka yourself
# %test.quarkus.devservices.enabled=true

# Health and metrics (enabled by default, but can be explicit)
quarkus.smallrye-health.root-path=/q/health
quarkus.smallrye-metrics.path=/q/metrics

# JVM arguments (needed for Java 21+ and Quarkus threading)
quarkus.jib.jvm-arguments=--add-opens=java.base/java.lang=ALL-UNNAMED

# Swagger UI configuration
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.path=/q/swagger-ui
quarkus.http.root-path=/

quarkus.smallrye-openapi.path=/openapi
```