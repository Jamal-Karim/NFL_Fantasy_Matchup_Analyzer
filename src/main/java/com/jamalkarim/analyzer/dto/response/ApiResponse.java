package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.Instant;

/**
 * Generic wrapper for all API responses.
 * Provides a consistent structure including status, timestamp, message, and payload data.
 *
 * @param <T> The type of the data payload
 */
@Getter
@JsonPropertyOrder({"status", "message", "timestamp", "data"})
public class ApiResponse<T> {
    private final String status;
    private final String timestamp;
    private final String message;
    private final T data;

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.timestamp = Instant.now().toString();
        this.data = data;
    }

    /**
     * Creates a successful API response with a data payload.
     *
     * @param data The payload to return
     * @param <T>  The payload type
     * @return A success ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", "Operation successful", data);
    }

    /**
     * Creates an error API response.
     *
     * @param errorMessage The error message to return
     * @param <T>          The expected payload type
     * @return An error ApiResponse
     */
    public static <T> ApiResponse<T> error(String errorMessage) {
        return new ApiResponse<>("ERROR", errorMessage, null);
    }
}