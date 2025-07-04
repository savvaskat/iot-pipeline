# IoT Sensor Event Pipeline

A distributed system for ingesting and processing IoT sensor data using **Quarkus**, **Kafka**, and **MongoDB**.

This project consists of two microservices:

- **Sensor Producer**: Exposes a REST API to receive sensor data and publishes it to Kafka.
- **Sensor Consumer**: Consumes Kafka messages and stores them in MongoDB.

---

## Architecture Overview

```text
  [ Client ]
     |
     | HTTP POST /sensors/event
     v
[SENSOR PRODUCER]
     |
     | Kafka Topic: sensor-events
     v
[SENSOR CONSUMER]
     |
     | MongoDB (sensordb.events)
     v
[ Data Store ]
```

---

## Project Structure

```text
iot-pipeline/
├── docker-compose.yml
├── sensor-producer/
│   ├── src/
│   └── README.md
├── sensor-consumer/
│   ├── src/
│   └── README.md
└── ...
```

---

## Tech Stack

| Component       | Technology                       |
|----------------|-----------------------------------|
| Framework       | Quarkus 3.24.x                   |
| Messaging       | Apache Kafka                     |
| Database        | MongoDB                          |
| Containerization| Docker & Docker Compose          |
| REST API        | Jakarta REST + Swagger UI        |
| Logging         | JBoss Logger                     |
| Validation      | Jakarta Bean Validation          |
| Testing         | JUnit 5, Mockito, REST Assured   |

---

## Prerequisites

- Docker & Docker Compose
- Java 17+
- Optional: `mvn` or `./mvnw` for local testing

---

## Getting Started

### 1. Clone the Project

```bash
git clone https://github.com/your-username/iot-pipeline.git  # TODO: update URL
cd iot-pipeline
```

---

### 2. Start the System

```bash
docker-compose up --build
```

This launches:

- Kafka & Zookeeper
- MongoDB
- Sensor Producer on `http://localhost:8080`
- Sensor Consumer (background processing)

---

### 3. Send a Test Event

Use Swagger UI or cURL:

```bash
curl -X POST http://localhost:8080/sensors/event \
  -H "Content-Type: application/json" \
  -d '{
        "sensorId": "sensor-001",
        "timestamp": "2025-07-04T10:00:00Z",
        "temperature": 23.7
      }'
```

Swagger UI:
```text
http://localhost:8080/q/swagger-ui
```

---

### 4. View MongoDB Data

```bash
docker exec -it iot-pipeline-mongodb-1 mongosh
use sensordb
db.events.find().pretty()
```

---

## Microservices Overview

### 🔹 Sensor Producer

- REST API for ingesting sensor events
- Publishes to Kafka
- Swagger at `/q/swagger-ui`
- Health: `/q/health`

See [`sensor-producer/README.md`](sensor-producer/README.md)

---

### 🔹 Sensor Consumer

- Listens to Kafka topic
- Saves data to MongoDB
- Implements Kafka retry logic
- Health: `/q/health` on port 8081

See [`sensor-consumer/README.md`](sensor-consumer/README.md)

---

## Running Tests

Each service has its own test suite.

Example for `sensor-consumer`:

```bash
cd sensor-consumer
./mvnw test
```

---

## Stopping Everything

```bash
docker-compose down
```

---

## Notes

- Kafka topic used: `sensor-events`
- MongoDB database: `sensordb`
- MongoDB collection: `events`

---