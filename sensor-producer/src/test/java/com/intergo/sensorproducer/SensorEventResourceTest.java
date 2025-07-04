package com.intergo.sensorproducer;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class SensorEventResourceTest {

    @Test
    public void testValidSensorEvent() {
        given()
          .contentType(ContentType.JSON)
          .body("""
                {
                  "sensorId": "sensor-123",
                  "timestamp": "2025-07-04T12:00:00Z",
                  "temperature": 25.0
                }
                """)
        .when()
          .post("/sensors/events")
        .then()
          .statusCode(Response.Status.ACCEPTED.getStatusCode()); // 202
    }

    @Test
    public void testInvalidSensorEvent_MissingId() {
        given()
          .contentType(ContentType.JSON)
          .body("""
                {
                  "timestamp": "2025-07-04T12:00:00Z",
                  "temperature": 25.0
                }
                """)
        .when()
          .post("/sensors/events")
        .then()
          .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
          .body("errors", hasItem("Sensor ID cannot be blank"));
    }

    @Test
    public void testInvalidSensorEvent_TemperatureTooHigh() {
        given()
          .contentType(ContentType.JSON)
          .body("""
                {
                  "sensorId": "sensor-001",
                  "timestamp": "2025-07-04T12:00:00Z",
                  "temperature": 200.0
                }
                """)
        .when()
          .post("/sensors/events")
        .then()
          .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
          .body("errors", hasItem("Temperature too high"));
    }
}