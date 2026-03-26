package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Generic response wrapper for all API endpoints, providing consistent status and metadata")
public class RestResponse<T> {
    @Schema(description = "Response status indicator", example = "SUCCESS")
    private final String status;

    @Schema(description = "ISO-8601 UTC timestamp of the response generation", example = "2024-03-26T15:30:00Z")
    private final String timestamp;

    @Schema(description = "Descriptive message about the operation result", example = "Operation successful")
    private final String message;

    @Schema(description = "The actual data payload of the response")
    private final T data;

    /**
     * Constructs an ApiResponse with the specified status, message, and data.
     *
     * @param status  The status of the response (e.g., "SUCCESS", "ERROR")
     * @param message A descriptive message about the response
     * @param data    The data payload
     */
    public RestResponse(String status, String message, T data) {
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
    public static <T> RestResponse<T> success(T data) {
        return new RestResponse<>("SUCCESS", "Operation successful", data);
    }

    /**
     * Creates an error API response.
     *
     * @param errorMessage The error message to return
     * @param <T>          The expected payload type
     * @return An error ApiResponse
     */
    public static <T> RestResponse<T> error(String errorMessage) {
        return new RestResponse<>("ERROR", errorMessage, null);
    }
}