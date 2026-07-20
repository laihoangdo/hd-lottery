package com.hdplatform.modules.identity.adapter.in.rest;

import com.hdplatform.modules.identity.application.auth.AuthenticationScope;
import com.hdplatform.modules.identity.application.auth.TokenPair;
import com.hdplatform.modules.identity.application.port.AccessTokenIssuer;
import com.hdplatform.modules.identity.application.service.AuthenticationService;
import com.hdplatform.shared.domain.exception.DomainException;
import com.hdplatform.shared.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authentication;

    @Value("${hd-platform.security.workspace-exchange-key:}")
    private String workspaceExchangeKey;

    @PostMapping("/login")
    public ApiResponse<TokenPair> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authentication.login(request.email(), request.password(), request.scope(), request.tenantId()));
    }

    @PostMapping("/refresh")
    public ApiResponse<TokenPair> refresh(@Valid @RequestBody RefreshRequest request) {
        return ApiResponse.success(authentication.refresh(request.refreshToken()));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@Valid @RequestBody RefreshRequest request) {
        authentication.logout(request.refreshToken());
        return ApiResponse.success(null);
    }

    @PostMapping("/workspace-exchange")
    public ApiResponse<AccessTokenResponse> workspaceExchange(
            @RequestHeader("X-Workspace-Exchange-Key") String providedKey,
            @Valid @RequestBody WorkspaceExchangeRequest request) {
        if (workspaceExchangeKey.isBlank() || !MessageDigest.isEqual(
                workspaceExchangeKey.getBytes(StandardCharsets.UTF_8),
                providedKey.getBytes(StandardCharsets.UTF_8))) {
            throw new DomainException("Workspace identity exchange is not authorized");
        }
        AccessTokenIssuer.IssuedAccessToken token = authentication.exchangePlatformIdentity(request.email());
        return ApiResponse.success(new AccessTokenResponse(token.value(), token.expiresAt(), "Bearer"));
    }
}

record LoginRequest(@NotBlank @Email String email, @NotBlank String password,
                    @NotNull AuthenticationScope scope, UUID tenantId) {}
record RefreshRequest(@NotBlank String refreshToken) {}
record WorkspaceExchangeRequest(@NotBlank @Email String email) {}
record AccessTokenResponse(String accessToken, Instant accessTokenExpiresAt, String tokenType) {}
