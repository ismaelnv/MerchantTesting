package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApiResponse<T> {
    private ResponseStatus responseStatus;
    private String message;
    private T data;

    public static <T> ApiResponse<T> createResponse(ResponseStatus responseStatus, String message, T data) {
        return new ApiResponse<>(responseStatus, message, data);
    }
}
