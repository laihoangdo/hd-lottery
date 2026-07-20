package com.hdplatform.modules.identity.application.auth;

import java.time.Instant;

public record TokenPair(
        String accessToken,
        Instant accessTokenExpiresAt,
        String refreshToken,
        Instant refreshTokenExpiresAt,
        String tokenType
) {
    public TokenPair(String accessToken, Instant accessTokenExpiresAt,
                     String refreshToken, Instant refreshTokenExpiresAt) {
        this(accessToken, accessTokenExpiresAt, refreshToken, refreshTokenExpiresAt, "Bearer");
    }
}
