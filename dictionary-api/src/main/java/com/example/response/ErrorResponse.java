package com.example.response;

public record ErrorResponse(int status, String message, long timestamp) {
}
