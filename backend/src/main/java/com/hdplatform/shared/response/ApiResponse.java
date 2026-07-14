package com.hdplatform.shared.response;

import java.time.OffsetDateTime;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        OffsetDateTime timestamp
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                true,
                "Success",
                data,
                OffsetDateTime.now()
        );
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                data,
                OffsetDateTime.now()
        );
    }

    public static ApiResponse<String> error(String message) {
        return new ApiResponse<>(
            false,
            message,
            null,
            OffsetDateTime.now()
        );
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(
                false,
                message,
                data,
                OffsetDateTime.now()
        );
    }

}