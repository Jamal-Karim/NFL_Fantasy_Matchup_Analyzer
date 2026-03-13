package com.jamalkarim.analyzer.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.Instant;

@Getter
@JsonPropertyOrder({"status", "timestamp", "data"})
public class ApiResponse<T> {
    private final String status;
    private final String timestamp;
    private final T data;

    public ApiResponse(String status, T data) {
        this.status = status;
        this.timestamp = Instant.now().toString();
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", data);
    }
}