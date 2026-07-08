package com.hdplatform.shared.util;

import java.util.UUID;

public final class UuidGenerator {

    private UuidGenerator() {
    }

    public static UUID generate() {
        return UUID.randomUUID();
    }

}