package com.intergo.sensorproducer;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsInAnyOrder;

@QuarkusTest
public class SensorEventResourceValidationTest {

    private static final String ENDPOINT = "/sensors/events";

    @Test
    void testMissingSensorId() {
        String payload = """
        {
            "timestamp": "2025-07-04T12:00:00Z",
            "temperature": 25.0
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post(ENDPOINT)
        .then()
            .statusCode(400)
            .body("errors", containsInAnyOrder("Sensor ID cannot be blank"));
    }

    @Test
    void testInvalidTemperatureTooLow() {
        String payload = """
        {
            "sensorId": "sensor-001",
            "timestamp": "2025-07-04T12:00:00Z",
            "temperature": -99.0
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post(ENDPOINT)
        .then()
            .statusCode(400)
            .body("errors", containsInAnyOrder("Temperature too low"));
    }

    @Test
    void testInvalidTemperatureTooHigh() {
        String payload = """
        {
            "sensorId": "sensor-001",
            "timestamp": "2025-07-04T12:00:00Z",
            "temperature": 200.0
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post(ENDPOINT)
        .then()
            .statusCode(400)
            .body("errors", containsInAnyOrder("Temperature too high"));
    }

    @Test
    void testMissingTimestamp() {
        String payload = """
        {
            "sensorId": "sensor-001",
            "temperature": 25.0
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post(ENDPOINT)
        .then()
            .statusCode(400)
            .body("errors", containsInAnyOrder("Timestamp is required"));
    }

    @Test
    void testMalformedTimestamp() {
        String payload = """
        {
            "sensorId": "sensor-001",
            "timestamp": "not-a-date",
            "temperature": 25.0
        }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(payload)
        .when()
            .post(ENDPOINT)
        .then()
            .statusCode(400); // JSON deserialization fails before validation
    }
}