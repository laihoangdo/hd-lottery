package com.hdplatform.shared.exception;

public abstract sealed class BaseException
        extends RuntimeException
        permits ValidationException,
                NotFoundException,
                ConflictException {

    private final String code;

    protected BaseException(
            String code,
            String message
    ) {

        super(message);

        this.code = code;

    }

    public String getCode() {
        return code;
    }

}
