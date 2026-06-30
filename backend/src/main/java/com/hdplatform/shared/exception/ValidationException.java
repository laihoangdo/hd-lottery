package com.hdplatform.shared.exception;

public final class ValidationException
        extends BaseException {

    public ValidationException(
            String code,
            String message
    ) {

        super(code, message);

    }

}
