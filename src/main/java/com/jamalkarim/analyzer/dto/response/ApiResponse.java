package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.Instant;

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

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", "Operation successful", data);
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        return new ApiResponse<>("ERROR", errorMessage, null);
    }
}