package com.hdplatform.shared.response;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponse(

    boolean success,

    String code,

    String message,

    List<String> errors,

    OffsetDateTime timestamp

) {
}