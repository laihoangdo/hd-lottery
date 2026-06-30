package com.hdplatform.shared.exception;

public final class NotFoundException
        extends BaseException {

    public NotFoundException(
            String code,
            String message
    ) {

        super(code, message);

    }

}