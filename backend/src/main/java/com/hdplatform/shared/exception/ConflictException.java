package com.hdplatform.shared.exception;

public final class ConflictException
        extends BaseException {

    public ConflictException(
            String code,
            String message
    ) {

        super(code, message);

    }

}