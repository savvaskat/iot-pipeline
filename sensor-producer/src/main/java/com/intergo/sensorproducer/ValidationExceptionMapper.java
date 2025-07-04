package com.intergo.sensorproducer;


// Required imports for validation and JAX-RS exception mapping
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.stream.Collectors;

@Provider // Registers this class as a JAX-RS provider for exception handling
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    // This method is automatically called when a ConstraintViolationException is thrown
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        // Extract all validation error messages from the exception
        List<String> errors = exception.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage) // Get the actual validation error message
            .collect(Collectors.toList()); // Convert to a List<String>

        // Create a structured error response object
        ErrorResponse errorResponse = new ErrorResponse(
            400, // HTTP status code
            "Validation failed", // General message
            errors // List of detailed error messages
        );

        // Return an HTTP 400 Bad Request response with the error payload
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse) // JSON response body
                .build();
    }

    // Inner class representing the JSON error response structure
    public static class ErrorResponse {
        public int status; // HTTP status code
        public String message; // General error message
        public List<String> errors; // List of specific field validation messages

        public ErrorResponse(int status, String message, List<String> errors) {
            this.status = status;
            this.message = message;
            this.errors = errors;
        }
    }
}