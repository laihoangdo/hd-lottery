🏗️ PLATFORM SOURCE CODE BASE - ENTERPRISE GRADE
# PROJECT STRUCTURE TỔNG QUAN
```

platform/
├── backend/                    # Spring Boot 3.2 + Java 21
│   ├── platform-core/          # Shared library
│   ├── platform-api/           # Main API service
│   └── platform-worker/        # Background jobs
│
├── frontend/
│   ├── admin-panel/            # Next.js 14 - Super Admin
│   ├── web-app/                # Next.js 14 - Public websites
│   └── shared-ui/              # Shared components
│
├── infrastructure/
│   ├── docker/
│   ├── nginx/
│   └── scripts/
│
└── docs/
```
# BACKEND - SPRING BOOT 3.2 + JAVA 21
## 1. ROOT POM.XML
```xml

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.websitehub</groupId>
    <artifactId>platform-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    <name>WebsiteHub Platform - Parent</name>

    <modules>
        <module>platform-core</module>
        <module>platform-api</module>
        <module>platform-worker</module>
    </modules>

    <properties>
        <java.version>21</java.version>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring-boot.version>3.2.1</spring-boot.version>
        <mapstruct.version>1.5.5.Final</mapstruct.version>
        <lombok.version>1.18.30</lombok.version>
        <jjwt.version>0.12.3</jjwt.version>
        <flyway.version>10.4.1</flyway.version>
        <jsoup.version>1.17.2</jsoup.version>
        <okhttp.version>4.12.0</okhttp.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- Internal modules -->
            <dependency>
                <groupId>com.websitehub</groupId>
                <artifactId>platform-core</artifactId>
                <version>${project.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <version>${spring-boot.version}</version>
                    <configuration>
                        <excludes>
                            <exclude>
                                <groupId>org.projectlombok</groupId>
                                <artifactId>lombok</artifactId>
                            </exclude>
                        </excludes>
                    </configuration>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.11.0</version>
                    <configuration>
                        <source>21</source>
                        <target>21</target>
                        <annotationProcessorPaths>
                            <path>
                                <groupId>org.projectlombok</groupId>
                                <artifactId>lombok</artifactId>
                                <version>${lombok.version}</version>
                            </path>
                            <path>
                                <groupId>org.mapstruct</groupId>
                                <artifactId>mapstruct-processor</artifactId>
                                <version>${mapstruct.version}</version>
                            </path>
                        </annotationProcessorPaths>
                        <compilerArgs>
                            <arg>--enable-preview</arg>
                        </compilerArgs>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
</project>
```
## 2. PLATFORM-CORE MODULE
### 2.1 Core pom.xml
```xml

<?xml version="1.0" encoding="UTF-8"?>
<project>
    <parent>
        <groupId>com.websitehub</groupId>
        <artifactId>platform-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <artifactId>platform-core</artifactId>
    <name>Platform Core - Shared Library</name>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jjwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jjwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>
    </dependencies>
</project>
```
### 2.2 Tenant Context
```java

// platform-core/src/main/java/com/websitehub/core/tenant/TenantContext.java
package com.websitehub.core.tenant;

import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
public class TenantContext {
    private final UUID   tenantId;
    private final String tenantSlug;
    private final String tenantName;
    private final String verticalCode;
    private final String templateCode;
    private final String status;         // active, suspended, building
}

// TenantContextHolder.java
package com.websitehub.core.tenant;

public final class TenantContextHolder {

    private static final ThreadLocal<TenantContext> CONTEXT =
        new InheritableThreadLocal<>();
    // InheritableThreadLocal: truyền context cho child threads
    // Phù hợp với Java 21 Virtual Threads

    public static void set(TenantContext ctx) {
        CONTEXT.set(ctx);
    }

    public static TenantContext get() {
        TenantContext ctx = CONTEXT.get();
        if (ctx == null) {
            throw new TenantContextMissingException(
                "Tenant context not set for this thread"
            );
        }
        return ctx;
    }

    public static TenantContext getOptional() {
        return CONTEXT.get();
    }

    public static UUID getTenantId() {
        return get().getTenantId();
    }

    public static String getTenantSlug() {
        return get().getTenantSlug();
    }

    public static boolean hasContext() {
        return CONTEXT.get() != null;
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
```
### 2.3 Security - UserPrincipal
```java

// platform-core/src/main/java/com/websitehub/core/security/UserPrincipal.java
package com.websitehub.core.security;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Builder
public class UserPrincipal implements UserDetails {

    private final UUID        userId;
    private final String      userType;   // platform_user | tenant_user
    private final String      email;
    private final String      fullName;
    private final String      roleCode;
    private final int         roleLevel;
    private final UUID        tenantId;   // null nếu platform_user
    private final String      tenantSlug;
    private final Set<String> permissions;

    // ── Helper Methods ─────────────────────────────────────────

    public boolean isPlatformUser() {
        return "platform_user".equals(userType);
    }

    public boolean isTenantUser() {
        return "tenant_user".equals(userType);
    }

    public boolean isSuperAdmin() {
        return "super_admin".equals(roleCode);
    }

    public boolean isAdmin() {
        return "admin".equals(roleCode) || isSuperAdmin();
    }

    public boolean isWebsiteOwner() {
        return "website_owner".equals(roleCode);
    }

    public boolean hasPermission(String permissionCode) {
        if (isSuperAdmin()) return true;
        return permissions != null && permissions.contains(permissionCode);
    }

    public boolean hasAnyPermission(String... codes) {
        if (isSuperAdmin()) return true;
        if (permissions == null) return false;
        return Arrays.stream(codes).anyMatch(permissions::contains);
    }

    public boolean hasAllPermissions(String... codes) {
        if (isSuperAdmin()) return true;
        if (permissions == null) return false;
        return Arrays.stream(codes).allMatch(permissions::contains);
    }

    public boolean belongsToTenant(UUID targetTenantId) {
        if (isPlatformUser()) return true; // Platform users manage all
        return tenantId != null && tenantId.equals(targetTenantId);
    }

    // ── UserDetails Interface ────────────────────────────────────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleCode.toUpperCase()));
        if (permissions != null) {
            permissions.stream()
                .map(p -> new SimpleGrantedAuthority("PERMISSION_" + p))
                .forEach(authorities::add);
        }
        return authorities;
    }

    @Override public String getPassword()  { return null; }
    @Override public String getUsername()  { return email; }
    @Override public boolean isAccountNonExpired()   { return true; }
    @Override public boolean isAccountNonLocked()    { return true; }
    @Override public boolean isCredentialsNonExpired(){ return true; }
    @Override public boolean isEnabled()             { return true; }
}

// SecurityContextUtils.java
package com.websitehub.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityContextUtils {

    public static UserPrincipal getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("Not authenticated");
        }
        return (UserPrincipal) auth.getPrincipal();
    }

    public static UserPrincipal getCurrentUserOptional() {
        try {
            return getCurrentUser();
        } catch (Exception e) {
            return null;
        }
    }

    private SecurityContextUtils() {}
}
```
### 2.4 JWT Service
```java

// platform-core/src/main/java/com/websitehub/core/security/JwtService.java
package com.websitehub.core.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class JwtService {

    @Value("${platform.security.jwt.secret}")
    private String jwtSecret;

    @Value("${platform.security.jwt.access-token-expiry-minutes:60}")
    private long accessTokenExpiryMinutes;

    @Value("${platform.security.jwt.refresh-token-expiry-days:7}")
    private long refreshTokenExpiryDays;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
            jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateAccessToken(UserPrincipal user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId",     user.getUserId().toString());
        claims.put("userType",   user.getUserType());
        claims.put("roleCode",   user.getRoleCode());
        claims.put("roleLevel",  user.getRoleLevel());
        claims.put("tenantId",   user.getTenantId() != null
            ? user.getTenantId().toString() : null);
        claims.put("tenantSlug", user.getTenantSlug());
        claims.put("tokenType",  "access");

        return Jwts.builder()
            .claims(claims)
            .subject(user.getEmail())
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(
                Instant.now().plus(accessTokenExpiryMinutes, ChronoUnit.MINUTES)
            ))
            .signWith(getSigningKey())
            .compact();
    }

    public String generateRefreshToken(UUID userId, String userType) {
        return Jwts.builder()
            .claims(Map.of(
                "userId",    userId.toString(),
                "userType",  userType,
                "tokenType", "refresh",
                "jti",       UUID.randomUUID().toString()
            ))
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(
                Instant.now().plus(refreshTokenExpiryDays, ChronoUnit.DAYS)
            ))
            .signWith(getSigningKey())
            .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public boolean isValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("JWT validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean isAccessToken(Claims claims) {
        return "access".equals(claims.get("tokenType", String.class));
    }

    public boolean isRefreshToken(Claims claims) {
        return "refresh".equals(claims.get("tokenType", String.class));
    }
}
```
### 2.5 Annotations
```java

// platform-core/src/main/java/com/websitehub/core/annotation/RequirePermission.java
package com.websitehub.core.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    String[] value();
    boolean requireAll() default false;
    String message() default "";
}

// RequireRole.java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    String[] value();
    int minLevel() default 0;
}

// TenantScoped.java - Đánh dấu method cần tenant context
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantScoped {
}

// AuditLog.java - Đánh dấu method cần audit
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    String action();
    String resourceType() default "";
}
```
### 2.6 AOP Aspects
```java

// platform-core/src/main/java/com/websitehub/core/aop/PermissionAspect.java
package com.websitehub.core.aop;

import com.websitehub.core.annotation.RequirePermission;
import com.websitehub.core.annotation.RequireRole;
import com.websitehub.core.exception.AccessDeniedException;
import com.websitehub.core.security.SecurityContextUtils;
import com.websitehub.core.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class PermissionAspect {

    @Around("@annotation(requirePermission)")
    public Object checkPermission(
            ProceedingJoinPoint pjp,
            RequirePermission requirePermission) throws Throwable {

        UserPrincipal user = SecurityContextUtils.getCurrentUser();

        boolean hasAccess = requirePermission.requireAll()
            ? user.hasAllPermissions(requirePermission.value())
            : user.hasAnyPermission(requirePermission.value());

        if (!hasAccess) {
            String msg = requirePermission.message().isBlank()
                ? "Bạn không có quyền thực hiện hành động này"
                : requirePermission.message();

            log.warn("Access denied: user={}, role={}, required={}",
                user.getEmail(), user.getRoleCode(),
                Arrays.toString(requirePermission.value()));

            throw new AccessDeniedException(msg);
        }

        return pjp.proceed();
    }

    @Around("@annotation(requireRole)")
    public Object checkRole(
            ProceedingJoinPoint pjp,
            RequireRole requireRole) throws Throwable {

        UserPrincipal user = SecurityContextUtils.getCurrentUser();

        boolean hasRole = Arrays.asList(requireRole.value())
                              .contains(user.getRoleCode())
                       || user.getRoleLevel() >= requireRole.minLevel();

        if (!hasRole) {
            throw new AccessDeniedException(
                "Yêu cầu vai trò: " + Arrays.toString(requireRole.value())
            );
        }

        return pjp.proceed();
    }
}

// AuditAspect.java
package com.websitehub.core.aop;

import com.websitehub.core.annotation.Auditable;
import com.websitehub.core.audit.AuditLogService;
import com.websitehub.core.security.SecurityContextUtils;
import com.websitehub.core.security.UserPrincipal;
import com.websitehub.core.tenant.TenantContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {

    private final AuditLogService auditLogService;

    @Around("@annotation(auditable)")
    public Object audit(
            ProceedingJoinPoint pjp,
            Auditable auditable) throws Throwable {

        UserPrincipal user = SecurityContextUtils.getCurrentUserOptional();
        Object result = null;
        Exception error = null;

        try {
            result = pjp.proceed();
            return result;
        } catch (Exception e) {
            error = e;
            throw e;
        } finally {
            try {
                auditLogService.log(
                    user,
                    TenantContextHolder.hasContext()
                        ? TenantContextHolder.getTenantId()
                        : null,
                    auditable.action(),
                    auditable.resourceType(),
                    error == null ? "SUCCESS" : "FAILED",
                    error != null ? error.getMessage() : null
                );
            } catch (Exception logEx) {
                log.error("Failed to write audit log: {}", logEx.getMessage());
            }
        }
    }
}
```
### 2.7 API Response
```java

// platform-core/src/main/java/com/websitehub/core/dto/ApiResponse.java
package com.websitehub.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final String  message;
    private final T       data;
    private final String  errorCode;
    private final Map<String, String> errors;

    @Builder.Default
    private final long timestamp = Instant.now().toEpochMilli();

    // ── Factory Methods ──────────────────────────────────────────

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .data(data)
            .build();
    }

    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder()
            .success(true)
            .data(data)
            .message(message)
            .build();
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
            .success(true)
            .data(data)
            .message(message)
            .build();
    }

    public static <T> ApiResponse<T> noContent(String message) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .build();
    }

    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return ApiResponse.<T>builder()
            .success(false)
            .errorCode(errorCode)
            .message(message)
            .build();
    }

    public static <T> ApiResponse<T> validationError(
            Map<String, String> errors) {
        return ApiResponse.<T>builder()
            .success(false)
            .errorCode("VALIDATION_ERROR")
            .message("Dữ liệu không hợp lệ")
            .errors(errors)
            .build();
    }
}

// PageResponse.java
package com.websitehub.core.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {
    private final List<T> items;
    private final int     page;
    private final int     size;
    private final long    totalItems;
    private final int     totalPages;
    private final boolean hasNext;
    private final boolean hasPrev;

    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
            .items(page.getContent())
            .page(page.getNumber() + 1)
            .size(page.getSize())
            .totalItems(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .hasNext(page.hasNext())
            .hasPrev(page.hasPrevious())
            .build();
    }
}
```
### 2.8 Exception Handling
```java

// platform-core/src/main/java/com/websitehub/core/exception/
// Exceptions
public class PlatformException extends RuntimeException {
    private final String errorCode;
    public PlatformException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    public String getErrorCode() { return errorCode; }
}

public class NotFoundException       extends PlatformException {
    public NotFoundException(String msg) {
        super("NOT_FOUND", msg);
    }
}
public class AccessDeniedException   extends PlatformException {
    public AccessDeniedException(String msg) {
        super("ACCESS_DENIED", msg);
    }
}
public class BusinessException       extends PlatformException {
    public BusinessException(String msg) {
        super("BUSINESS_ERROR", msg);
    }
}
public class TenantContextMissingException extends PlatformException {
    public TenantContextMissingException(String msg) {
        super("TENANT_CONTEXT_MISSING", msg);
    }
}
public class UnauthorizedException   extends PlatformException {
    public UnauthorizedException(String msg) {
        super("UNAUTHORIZED", msg);
    }
}
public class ConflictException       extends PlatformException {
    public ConflictException(String msg) {
        super("CONFLICT", msg);
    }
}

// GlobalExceptionHandler.java
package com.websitehub.core.exception;

import com.websitehub.core.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNotFound(NotFoundException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleAccessDenied(AccessDeniedException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<?> handleUnauthorized(UnauthorizedException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResponse<?> handleBusiness(BusinessException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<?> handleConflict(ConflictException ex) {
        return ApiResponse.error(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResponse<?> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                e -> e.getDefaultMessage() != null
                    ? e.getDefaultMessage() : "Không hợp lệ",
                (existing, replacement) -> existing
            ));
        return ApiResponse.validationError(errors);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleGeneral(
            Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception [{} {}]: {}",
            req.getMethod(), req.getRequestURI(), ex.getMessage(), ex);
        return ApiResponse.error("INTERNAL_ERROR",
            "Có lỗi xảy ra. Vui lòng thử lại sau.");
    }
}
```
## 3. PLATFORM-API MODULE
### 3.1 Application Properties
```yaml


# platform-api/src/main/resources/application.yml
spring:
  application:
    name: platform-api

  # Database
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/websitehub}
    username: ${DATABASE_USERNAME:platform_user}
    password: ${DATABASE_PASSWORD:changeme}
    hikari:
      maximum-pool-size: ${DB_POOL_MAX:20}
      minimum-idle: ${DB_POOL_MIN:5}
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      connection-test-query: SELECT 1

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties
  hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.PostgreSQLDialect
        jdbc:
          batch_size: 50
        order_inserts: true
        order_updates: true

  # Redis
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
      password: ${REDIS_PASSWORD:}
      timeout: 2000ms
      lettuce:
        pool:
          max-active: 16
          max-idle: 8
          min-idle: 2
          max-wait: 1000ms

  # Flyway
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    validate-on-migrate: true

  # Virtual Threads (Java 21)
  threads:
    virtual:
      enabled: true

  # Cache
  cache:
    type: redis

  # Mail
  mail:
    host: ${MAIL_HOST:smtp.resend.com}
    port: ${MAIL_PORT:587}
    username: ${MAIL_USERNAME:resend}
    password: ${MAIL_PASSWORD:}
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true

# Server
server:
  port: ${SERVER_PORT:8080}
  compression:
    enabled: true
    mime-types: application/json,application/xml,text/html,text/xml,text/plain
  servlet:
    context-path: /
  tomcat:
    threads:
      max: 200
      min-spare: 10

# Platform Config
platform:
  domain: ${PLATFORM_DOMAIN:websitehub.vn}
  admin-url: ${PLATFORM_ADMIN_URL:https://admin.websitehub.vn}
  api-url: ${PLATFORM_API_URL:https://api.websitehub.vn}
  support-phone: ${PLATFORM_SUPPORT_PHONE:0909000000}

  security:
    jwt:
      secret: ${JWT_SECRET:your-256-bit-secret-key-change-in-production}
      access-token-expiry-minutes: ${JWT_ACCESS_EXPIRY:60}
      refresh-token-expiry-days: ${JWT_REFRESH_EXPIRY:7}
    cors:
      allowed-origins:
        - ${FRONTEND_URL:http://localhost:3000}
        - ${ADMIN_URL:http://localhost:3001}

  storage:
    provider: ${STORAGE_PROVIDER:r2}
    r2:
      access-key: ${R2_ACCESS_KEY:}
      secret-key: ${R2_SECRET_KEY:}
      bucket: ${R2_BUCKET:websitehub-media}
      endpoint: ${R2_ENDPOINT:}
      public-url: ${R2_PUBLIC_URL:https://media.websitehub.vn}

  telegram:
    bot-token: ${TELEGRAM_BOT_TOKEN:}
    chat-id: ${TELEGRAM_CHAT_ID:}

  lottery:
    fetch:
      enabled: ${LOTTERY_FETCH_ENABLED:true}
      retry-max: 3
      retry-delay-seconds: 30
      cache-ttl-seconds: 120

# Logging
logging:
  level:
    root: INFO
    com.websitehub: DEBUG
    org.springframework.security: WARN
    org.hibernate.SQL: WARN
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level [%X{tenantSlug}] %logger{36} - %msg%n"

# Actuator
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when-authorized
  metrics:
    export:
      prometheus:
        enabled: true

# Swagger/OpenAPI
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    operations-sorter: alpha
    tags-sorter: alpha
  show-actuator: false
```  
### 3.2 Security Configuration
```java

// platform-api/src/main/java/com/websitehub/api/config/SecurityConfig.java
package com.websitehub.api.config;

import com.websitehub.api.security.JwtAuthenticationFilter;
import com.websitehub.api.security.TenantResolutionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final TenantResolutionFilter tenantResolutionFilter;

    @Value("${platform.security.cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(HttpMethod.GET,
                    "/v1/lottery/**",
                    "/v1/cms/**",
                    "/v1/seo/**",
                    "/v1/platform/resolve-domain",
                    "/v1/platform/tenant/*/config"
                ).permitAll()
                // Auth endpoints
                .requestMatchers(
                    "/admin/v1/auth/login",
                    "/admin/v1/auth/refresh",
                    "/platform/v1/auth/login",
                    "/platform/v1/auth/refresh"
                ).permitAll()
                // Webhooks
                .requestMatchers("/v1/webhooks/**").permitAll()
                // Actuator health
                .requestMatchers("/actuator/health").permitAll()
                // Swagger (chỉ dev)
                .requestMatchers(
                    "/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html"
                ).permitAll()
                // Everything else requires auth
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                tenantResolutionFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                    res.setStatus(401);
                    res.setContentType("application/json");
                    res.getWriter().write(
                        """
                        {"success":false,"errorCode":"UNAUTHORIZED",
                         "message":"Vui lòng đăng nhập"}
                        """
                    );
                })
                .accessDeniedHandler((req, res, e) -> {
                    res.setStatus(403);
                    res.setContentType("application/json");
                    res.getWriter().write(
                        """
                        {"success":false,"errorCode":"FORBIDDEN",
                         "message":"Không có quyền truy cập"}
                        """
                    );
                })
            )
            .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
```
### 3.3 Filters
```java

// platform-api/src/main/java/com/websitehub/api/security/TenantResolutionFilter.java
package com.websitehub.api.security;

import com.websitehub.api.modules.tenant.TenantCacheService;
import com.websitehub.core.tenant.TenantContext;
import com.websitehub.core.tenant.TenantContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class TenantResolutionFilter extends OncePerRequestFilter {

    private final TenantCacheService tenantCacheService;

    @Value("${platform.domain}")
    private String platformDomain;

    private static final Set<String> SKIP_PATHS = Set.of(
        "/actuator",
        "/api-docs",
        "/swagger-ui",
        "/v1/platform"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip tenant resolution cho platform-level paths
        if (shouldSkip(path)) {
            chain.doFilter(request, response);
            return;
        }

        String host = resolveHost(request);
        TenantContext ctx = resolveTenant(host);

        if (ctx != null) {
            TenantContextHolder.set(ctx);
            MDC.put("tenantSlug", ctx.getTenantSlug());
            MDC.put("tenantId",   ctx.getTenantId().toString());
        }

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
            MDC.remove("tenantSlug");
            MDC.remove("tenantId");
        }
    }

    private TenantContext resolveTenant(String host) {
        // Case 1: subdomain.platform.vn
        if (host.endsWith("." + platformDomain)) {
            String slug = host
                .replace("." + platformDomain, "")
                .replace("www.", "")
                .toLowerCase();
            return tenantCacheService.getBySlug(slug);
        }

        // Case 2: Custom domain
        if (!host.equals(platformDomain)
            && !host.equals("www." + platformDomain)) {
            return tenantCacheService.getByDomain(host);
        }

        return null;
    }

    private String resolveHost(HttpServletRequest request) {
        // Support X-Forwarded-Host (behind Nginx/Cloudflare)
        String forwarded = request.getHeader("X-Forwarded-Host");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim().toLowerCase();
        }
        return request.getServerName().toLowerCase();
    }

    private boolean shouldSkip(String path) {
        return SKIP_PATHS.stream().anyMatch(path::startsWith);
    }
}

// JwtAuthenticationFilter.java
package com.websitehub.api.security;

import com.websitehub.api.modules.auth.AuthService;
import com.websitehub.core.security.JwtService;
import com.websitehub.core.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService    jwtService;
    private final AuthService   authService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String token = extractToken(request);

        if (token != null && jwtService.isValid(token)) {
            try {
                Claims claims = jwtService.parseToken(token);

                if (jwtService.isAccessToken(claims)) {
                    UserPrincipal user = authService.buildPrincipal(claims);

                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities()
                        );

                    SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                    MDC.put("userId",    user.getUserId().toString());
                    MDC.put("userEmail", user.getEmail());
                }
            } catch (Exception e) {
                log.debug("JWT processing failed: {}", e.getMessage());
            }
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("userId");
            MDC.remove("userEmail");
        }
    }

    private String extractToken(HttpServletRequest request) {
        // Bearer token từ header
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        // Cookie fallback (cho Admin Panel)
        if (request.getCookies() != null) {
            for (var cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
```
### 3.4 Cache Configuration
```java

// platform-api/src/main/java/com/websitehub/api/config/CacheConfig.java
package com.websitehub.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String TENANT_CONFIG      = "tenant-config";
    public static final String TENANT_RESOLUTION  = "tenant-resolution";
    public static final String LOTTERY_TODAY      = "lottery-today";
    public static final String LOTTERY_HISTORY    = "lottery-history";
    public static final String LOTTERY_STATS      = "lottery-stats";
    public static final String LOTTERY_PREDICTIONS = "lottery-predictions";
    public static final String ARTICLES           = "articles";
    public static final String SITEMAP            = "sitemap";
    public static final String PERMISSIONS        = "user-permissions";

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        Map<String, RedisCacheConfiguration> configs = new HashMap<>();

        // Tenant configs - 5 phút
        configs.put(TENANT_CONFIG,
            defaultConfig().entryTtl(Duration.ofMinutes(5)));

        // Domain resolution - 5 phút
        configs.put(TENANT_RESOLUTION,
            defaultConfig().entryTtl(Duration.ofMinutes(5)));

        // KQXS hôm nay - 2 phút
        configs.put(LOTTERY_TODAY,
            defaultConfig().entryTtl(Duration.ofMinutes(2)));

        // KQXS lịch sử - 24 giờ
        configs.put(LOTTERY_HISTORY,
            defaultConfig().entryTtl(Duration.ofHours(24)));

        // Thống kê - 30 phút
        configs.put(LOTTERY_STATS,
            defaultConfig().entryTtl(Duration.ofMinutes(30)));

        // Nhận định - 5 phút
        configs.put(LOTTERY_PREDICTIONS,
            defaultConfig().entryTtl(Duration.ofMinutes(5)));

        // Bài viết - 10 phút
        configs.put(ARTICLES,
            defaultConfig().entryTtl(Duration.ofMinutes(10)));

        // Sitemap - 1 giờ
        configs.put(SITEMAP,
            defaultConfig().entryTtl(Duration.ofHours(1)));

        // User permissions - 15 phút
        configs.put(PERMISSIONS,
            defaultConfig().entryTtl(Duration.ofMinutes(15)));

        return RedisCacheManager.builder(factory)
            .cacheDefaults(defaultConfig().entryTtl(Duration.ofMinutes(10)))
            .withInitialCacheConfigurations(configs)
            .transactionAware()
            .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jsonSerializer());
        template.afterPropertiesSet();
        return template;
    }

    private RedisCacheConfiguration defaultConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair
                .fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair
                .fromSerializer(jsonSerializer()))
            .disableCachingNullValues()
            .prefixCacheNameWith("wh:");
    }

    private RedisSerializer<Object> jsonSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.activateDefaultTyping(
            mapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL
        );
        return new GenericJackson2JsonRedisSerializer(mapper);
    }
}
```
### 3.5 Database Entities
```java

// platform-api/src/main/java/com/websitehub/api/entity/Tenant.java
package com.websitehub.api.entity;

import com.websitehub.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant extends BaseEntity {

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(name = "vertical_id", nullable = false)
    private UUID verticalId;

    @Column(name = "template_id", nullable = false)
    private UUID templateId;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "color_overrides", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> colorOverrides;

    @Column(name = "feature_overrides", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> featureOverrides;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TenantStatus status;

    @Column(name = "template_history", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Map<String, Object>> templateHistory;

    @Column(name = "setup_completed_at")
    private Instant setupCompletedAt;

    @Column(name = "setup_notes", columnDefinition = "TEXT")
    private String setupNotes;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vertical_id", insertable = false, updatable = false)
    private Vertical vertical;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", insertable = false, updatable = false)
    private Template template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private Client client;

    @OneToOne(mappedBy = "tenant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TenantSettings settings;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
    private List<Domain> domains;

    public enum TenantStatus {
        BUILDING, ACTIVE, SUSPENDED, CANCELLED
    }
}

// BaseEntity.java
package com.websitehub.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;
}
```
### 3.6 Auth Module
```java

// platform-api/src/main/java/com/websitehub/api/modules/auth/AuthController.java
package com.websitehub.api.modules.auth;

import com.websitehub.core.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ── Platform Admin Login ──────────────────────────────────────
    @PostMapping("/platform/v1/auth/login")
    public ApiResponse<AuthResponse> platformLogin(
            @Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(
            authService.platformLogin(request),
            "Đăng nhập thành công"
        );
    }

    // ── Tenant Admin Login ────────────────────────────────────────
    @PostMapping("/admin/v1/auth/login")
    public ApiResponse<AuthResponse> tenantLogin(
            @Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(
            authService.tenantLogin(request),
            "Đăng nhập thành công"
        );
    }

    // ── Refresh Token ─────────────────────────────────────────────
    @PostMapping("/platform/v1/auth/refresh")
    public ApiResponse<RefreshResponse> platformRefresh(
            @Valid @RequestBody RefreshRequest request) {
        return ApiResponse.ok(authService.refresh(request, "platform_user"));
    }

    @PostMapping("/admin/v1/auth/refresh")
    public ApiResponse<RefreshResponse> tenantRefresh(
            @Valid @RequestBody RefreshRequest request) {
        return ApiResponse.ok(authService.refresh(request, "tenant_user"));
    }

    // ── Get Current User ──────────────────────────────────────────
    @GetMapping("/platform/v1/auth/me")
    public ApiResponse<MeResponse> platformMe() {
        return ApiResponse.ok(authService.getCurrentPlatformUser());
    }

    @GetMapping("/admin/v1/auth/me")
    public ApiResponse<MeResponse> tenantMe() {
        return ApiResponse.ok(authService.getCurrentTenantUser());
    }

    // ── Logout ────────────────────────────────────────────────────
    @PostMapping("/platform/v1/auth/logout")
    public ApiResponse<Void> platformLogout(
            @RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return ApiResponse.noContent("Đăng xuất thành công");
    }

    @PostMapping("/admin/v1/auth/logout")
    public ApiResponse<Void> tenantLogout(
            @RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader);
        return ApiResponse.noContent("Đăng xuất thành công");
    }

    // ── Change Password ───────────────────────────────────────────
    @PostMapping("/admin/v1/auth/change-password")
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ApiResponse.noContent("Đổi mật khẩu thành công");
    }
}

// AuthService.java
package com.websitehub.api.modules.auth;

import com.websitehub.api.entity.*;
import com.websitehub.api.modules.tenant.TenantCacheService;
import com.websitehub.core.exception.*;
import com.websitehub.core.security.*;
import com.websitehub.core.tenant.TenantContextHolder;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PlatformUserRepository platformUserRepo;
    private final TenantUserRepository   tenantUserRepo;
    private final PermissionRepository   permissionRepo;
    private final JwtService             jwtService;
    private final PasswordEncoder        passwordEncoder;
    private final TenantCacheService     tenantCacheService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String BLACKLIST_PREFIX = "token:blacklist:";
    private static final String REFRESH_PREFIX   = "token:refresh:";

    // ── Platform Login ────────────────────────────────────────────
    public AuthResponse platformLogin(LoginRequest request) {
        PlatformUser user = platformUserRepo
            .findByEmailAndIsActiveTrue(request.getEmail())
            .orElseThrow(() -> new UnauthorizedException(
                "Email hoặc mật khẩu không đúng"
            ));

        if (!passwordEncoder.matches(request.getPassword(),
                user.getPasswordHash())) {
            throw new UnauthorizedException("Email hoặc mật khẩu không đúng");
        }

        Set<String> permissions = loadPlatformPermissions(user);
        UserPrincipal principal = buildPlatformPrincipal(user, permissions);

        String accessToken  = jwtService.generateAccessToken(principal);
        String refreshToken = jwtService.generateRefreshToken(
            user.getId(), "platform_user"
        );

        // Store refresh token in Redis (7 days)
        redisTemplate.opsForValue().set(
            REFRESH_PREFIX + refreshToken,
            user.getId().toString(),
            Duration.ofDays(7)
        );

        // Update last login
        platformUserRepo.updateLastLogin(user.getId(), request.getIpAddress());

        log.info("Platform user logged in: {}", user.getEmail());

        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(toUserDTO(principal))
            .expiresIn(3600)
            .build();
    }

    // ── Tenant Login ──────────────────────────────────────────────
    public AuthResponse tenantLogin(LoginRequest request) {
        // Lấy tenant từ context (đã resolve từ domain)
        if (!TenantContextHolder.hasContext()) {
            throw new BusinessException("Không xác định được website");
        }

        UUID tenantId = TenantContextHolder.getTenantId();

        TenantUser user = tenantUserRepo
            .findByEmailAndTenantIdAndIsActiveTrue(
                request.getEmail(), tenantId
            )
            .orElseThrow(() -> new UnauthorizedException(
               "Email hoặc mật khẩu không đúng"
            ));

        if (!passwordEncoder.matches(request.getPassword(),
                user.getPasswordHash())) {
            throw new UnauthorizedException("Email hoặc mật khẩu không đúng");
        }

        Set<String> permissions = loadTenantPermissions(user);
        UserPrincipal principal = buildTenantPrincipal(user, permissions);

        String accessToken  = jwtService.generateAccessToken(principal);
        String refreshToken = jwtService.generateRefreshToken(
            user.getId(), "tenant_user"
        );

        redisTemplate.opsForValue().set(
            REFRESH_PREFIX + refreshToken,
            user.getId().toString(),
            Duration.ofDays(7)
        );

        tenantUserRepo.updateLastLogin(user.getId(), request.getIpAddress());

        log.info("Tenant user logged in: {} on tenant={}",
            user.getEmail(), tenantId);

        return AuthResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .user(toUserDTO(principal))
            .expiresIn(3600)
            .build();
    }

    // ── Refresh Token ─────────────────────────────────────────────
    public RefreshResponse refresh(RefreshRequest request, String expectedUserType) {
        if (!jwtService.isValid(request.getRefreshToken())) {
            throw new UnauthorizedException("Refresh token không hợp lệ");
        }

        Claims claims = jwtService.parseToken(request.getRefreshToken());
        if (!jwtService.isRefreshToken(claims)) {
            throw new UnauthorizedException("Token type không hợp lệ");
        }

        // Check in Redis
        String storedUserId = (String) redisTemplate.opsForValue()
            .get(REFRESH_PREFIX + request.getRefreshToken());
        if (storedUserId == null) {
            throw new UnauthorizedException("Refresh token đã hết hạn");
        }

        String userType = claims.get("userType", String.class);
        UUID userId     = UUID.fromString(storedUserId);

        UserPrincipal principal = switch (userType) {
            case "platform_user" -> {
                PlatformUser user = platformUserRepo.findById(userId).orElseThrow();
                yield buildPlatformPrincipal(user, loadPlatformPermissions(user));
            }
            case "tenant_user" -> {
                TenantUser user = tenantUserRepo.findById(userId).orElseThrow();
                yield buildTenantPrincipal(user, loadTenantPermissions(user));
            }
            default -> throw new UnauthorizedException("Unknown user type");
        };

        String newAccessToken = jwtService.generateAccessToken(principal);

        return RefreshResponse.builder()
            .accessToken(newAccessToken)
            .expiresIn(3600)
            .build();
    }

    // ── Logout ────────────────────────────────────────────────────
    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // Blacklist token in Redis
            if (jwtService.isValid(token)) {
                Claims claims = jwtService.parseToken(token);
                long ttl = claims.getExpiration().getTime()
                         - System.currentTimeMillis();
                if (ttl > 0) {
                    redisTemplate.opsForValue().set(
                        BLACKLIST_PREFIX + token,
                        "1",
                        Duration.ofMillis(ttl)
                    );
                }
            }
        }
    }

    // ── Build Principal from Claims (for JwtFilter) ───────────────
    public UserPrincipal buildPrincipal(Claims claims) {
        String userType = claims.get("userType", String.class);
        UUID   userId   = UUID.fromString(claims.get("userId", String.class));

        // Load permissions from cache
        String cacheKey = "permissions:" + userType + ":" + userId;
        @SuppressWarnings("unchecked")
        Set<String> permissions = (Set<String>) redisTemplate.opsForValue().get(cacheKey);

        if (permissions == null) {
            permissions = userType.equals("platform_user")
                ? loadPlatformPermissionsById(userId)
                : loadTenantPermissionsById(userId);

            redisTemplate.opsForValue().set(
                cacheKey, permissions, Duration.ofMinutes(15)
            );
        }

        UUID tenantId = claims.get("tenantId") != null
            ? UUID.fromString(claims.get("tenantId", String.class))
            : null;

        return UserPrincipal.builder()
            .userId(userId)
            .userType(userType)
            .email(claims.getSubject())
            .roleCode(claims.get("roleCode", String.class))
            .roleLevel(claims.get("roleLevel", Integer.class))
            .tenantId(tenantId)
            .tenantSlug(claims.get("tenantSlug", String.class))
            .permissions(permissions)
            .build();
    }

    // ── Change Password ───────────────────────────────────────────
    public void changePassword(ChangePasswordRequest request) {
        UserPrincipal user = SecurityContextUtils.getCurrentUser();

        if (user.isTenantUser()) {
            TenantUser tenantUser = tenantUserRepo
                .findById(user.getUserId()).orElseThrow();
            if (!passwordEncoder.matches(request.getCurrentPassword(),
                    tenantUser.getPasswordHash())) {
                throw new BusinessException("Mật khẩu hiện tại không đúng");
            }
            tenantUser.setPasswordHash(
                passwordEncoder.encode(request.getNewPassword())
            );
            tenantUserRepo.save(tenantUser);
        } else {
            PlatformUser platformUser = platformUserRepo
                .findById(user.getUserId()).orElseThrow();
            if (!passwordEncoder.matches(request.getCurrentPassword(),
                    platformUser.getPasswordHash())) {
                throw new BusinessException("Mật khẩu hiện tại không đúng");
            }
            platformUser.setPasswordHash(
                passwordEncoder.encode(request.getNewPassword())
            );
            platformUserRepo.save(platformUser);
        }
    }

    // ── Private Helpers ───────────────────────────────────────────
    private Set<String> loadPlatformPermissions(PlatformUser user) {
        if ("super_admin".equals(user.getRole().getCode())) {
            return permissionRepo.findAllCodes(); // All permissions
        }
        return permissionRepo.findByRoleId(user.getRole().getId());
    }

    private Set<String> loadTenantPermissions(TenantUser user) {
        return permissionRepo.findByRoleId(user.getRole().getId());
    }

    private Set<String> loadPlatformPermissionsById(UUID userId) {
        return platformUserRepo.findById(userId)
            .map(this::loadPlatformPermissions)
            .orElse(Set.of());
    }

    private Set<String> loadTenantPermissionsById(UUID userId) {
        return tenantUserRepo.findById(userId)
            .map(this::loadTenantPermissions)
            .orElse(Set.of());
    }

    private UserPrincipal buildPlatformPrincipal(
            PlatformUser user, Set<String> permissions) {
        return UserPrincipal.builder()
            .userId(user.getId())
            .userType("platform_user")
            .email(user.getEmail())
            .fullName(user.getFullName())
            .roleCode(user.getRole().getCode())
            .roleLevel(user.getRole().getLevel())
            .tenantId(null)
            .tenantSlug(null)
            .permissions(permissions)
            .build();
    }

    private UserPrincipal buildTenantPrincipal(
            TenantUser user, Set<String> permissions) {
        return UserPrincipal.builder()
            .userId(user.getId())
            .userType("tenant_user")
            .email(user.getEmail())
            .fullName(user.getFullName())
            .roleCode(user.getRole().getCode())
            .roleLevel(user.getRole().getLevel())
            .tenantId(user.getTenantId())
            .tenantSlug(
                tenantCacheService.getSlugById(user.getTenantId())
            )
            .permissions(permissions)
            .build();
    }

    private UserDTO toUserDTO(UserPrincipal principal) {
        return UserDTO.builder()
            .userId(principal.getUserId())
            .email(principal.getEmail())
            .fullName(principal.getFullName())
            .roleCode(principal.getRoleCode())
            .userType(principal.getUserType())
            .tenantId(principal.getTenantId())
            .tenantSlug(principal.getTenantSlug())
            .permissions(principal.getPermissions())
            .build();
    }
}
```
### 3.7 Tenant Module
```java

// TenantCacheService.java
package com.websitehub.api.modules.tenant;

import com.websitehub.core.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantCacheService {

    private final TenantRepository tenantRepository;
    private final DomainRepository domainRepository;

    @Cacheable(value = "tenant-resolution", key = "'slug:' + #slug",
               unless = "#result == null")
    public TenantContext getBySlug(String slug) {
        return tenantRepository
            .findBySlugWithDetails(slug)
            .map(this::toContext)
            .orElse(null);
    }

    @Cacheable(value = "tenant-resolution", key = "'domain:' + #domain",
               unless = "#result == null")
    public TenantContext getByDomain(String domain) {
        return domainRepository
            .findByDomainAndVerifiedTrue(domain)
            .flatMap(d -> tenantRepository.findByIdWithDetails(d.getTenantId()))
            .map(this::toContext)
            .orElse(null);
    }

    @Cacheable(value = "tenant-config", key = "#slug",
               unless = "#result == null")
    public TenantConfigDTO getConfig(String slug) {
        return tenantRepository
            .findBySlugWithFullDetails(slug)
            .map(this::toConfigDTO)
            .orElse(null);
    }

    public String getSlugById(UUID tenantId) {
        return tenantRepository.findSlugById(tenantId).orElse(null);
    }

    private TenantContext toContext(Tenant tenant) {
        return TenantContext.builder()
            .tenantId(tenant.getId())
            .tenantSlug(tenant.getSlug())
            .tenantName(tenant.getName())
            .verticalCode(tenant.getVertical().getCode())
            .templateCode(tenant.getTemplate().getCode())
            .status(tenant.getStatus().name().toLowerCase())
            .build();
    }

    private TenantConfigDTO toConfigDTO(Tenant tenant) {
        // Build full config DTO cho frontend
        Template template = tenant.getTemplate();
        TenantSettings settings = tenant.getSettings();

        // Merge colors: template default + tenant overrides
        Map<String, String> effectiveColors = new HashMap<>(
            template.getDefaultColors()
        );
        if (tenant.getColorOverrides() != null) {
            effectiveColors.putAll(tenant.getColorOverrides());
        }

        // Merge features
        Map<String, Object> layoutConfig = template.getLayoutConfig();
        Map<String, Object> templateFeatures = (Map<String, Object>)
            layoutConfig.getOrDefault("features_visible", Map.of());
        Map<String, Object> effectiveFeatures = new HashMap<>(templateFeatures);
        if (tenant.getFeatureOverrides() != null) {
            effectiveFeatures.putAll(tenant.getFeatureOverrides());
        }

        return TenantConfigDTO.builder()
            .id(tenant.getId())
            .slug(tenant.getSlug())
            .name(tenant.getName())
            .status(tenant.getStatus().name().toLowerCase())
            .verticalCode(tenant.getVertical().getCode())
            .template(TemplateDTO.builder()
                .id(template.getId())
                .code(template.getCode())
                .name(template.getName())
                .layoutConfig(layoutConfig)
                .defaultColors(template.getDefaultColors())
                .build())
            .colorOverrides(tenant.getColorOverrides())
            .effectiveColors(effectiveColors)
            .effectiveFeatures(effectiveFeatures)
            .settings(settings != null ? toSettingsDTO(settings) : null)
            .primaryDomain(getPrimaryDomain(tenant))
            .hostingStatus(getHostingStatus(tenant.getId()))
            .build();
    }
}

// TenantController.java (Platform Admin)
package com.websitehub.api.modules.tenant;

import com.websitehub.core.annotation.RequirePermission;
import com.websitehub.core.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.websitehub.core.constants.PermissionConstants.*;

@RestController
@RequestMapping("/platform/v1/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService       tenantService;
    private final TenantSetupService  setupService;
    private final TemplateSwitchService templateSwitchService;

    @GetMapping
    @RequirePermission(PLATFORM_TENANTS_VIEW)
    public ApiResponse<PageResponse<TenantListDTO>> list(
            @RequestParam(defaultValue = "1")  int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false)    String status,
            @RequestParam(required = false)    String vertical,
            @RequestParam(required = false)    String search) {
        return ApiResponse.ok(
            tenantService.list(status, vertical, search, page, size)
        );
    }

    @PostMapping("/setup")
    @RequirePermission(PLATFORM_TENANTS_CREATE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TenantSetupResult> setup(
            @Valid @RequestBody QuickSetupRequest request) {
        return ApiResponse.created(
            setupService.quickSetup(request),
            "Website đã tạo thành công"
        );
    }

    @GetMapping("/{id}")
    @RequirePermission(PLATFORM_TENANTS_VIEW)
    public ApiResponse<TenantDetailDTO> getDetail(@PathVariable UUID id) {
        return ApiResponse.ok(tenantService.getDetail(id));
    }

    @PutMapping("/{id}/template")
    @RequirePermission(PLATFORM_TENANTS_EDIT)
    public ApiResponse<Void> switchTemplate(
            @PathVariable UUID id,
            @Valid @RequestBody SwitchTemplateRequest req) {
        templateSwitchService.switchTemplate(id, req.getTemplateId());
        return ApiResponse.noContent("Đã đổi template thành công");
    }

    @GetMapping("/{id}/template/preview")
    @RequirePermission(PLATFORM_TENANTS_VIEW)
    public ApiResponse<TemplatePreviewDTO> previewTemplate(
            @PathVariable UUID id,
            @RequestParam UUID templateId) {
        return ApiResponse.ok(
            templateSwitchService.preview(id, templateId)
        );
    }

    @PutMapping("/{id}/colors")
    @RequirePermission(PLATFORM_TENANTS_EDIT)
    public ApiResponse<Void> updateColors(
            @PathVariable UUID id,
            @RequestBody Map<String, String> colors) {
        tenantService.updateColors(id, colors);
        return ApiResponse.noContent("Cập nhật màu sắc thành công");
    }

    @PutMapping("/{id}/features")
    @RequirePermission(PLATFORM_TENANTS_EDIT)
    public ApiResponse<Void> updateFeatures(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> features) {
        tenantService.updateFeatures(id, features);
        return ApiResponse.noContent("Cập nhật tính năng thành công");
    }

    @PostMapping("/{id}/suspend")
    @RequirePermission(PLATFORM_TENANTS_SUSPEND)
    public ApiResponse<Void> suspend(
            @PathVariable UUID id,
            @RequestBody SuspendRequest req) {
        tenantService.suspend(id, req.getReason());
        return ApiResponse.noContent("Đã tạm ngưng website");
    }

    @PostMapping("/{id}/activate")
    @RequirePermission(PLATFORM_TENANTS_SUSPEND)
    public ApiResponse<Void> activate(@PathVariable UUID id) {
        tenantService.activate(id);
        return ApiResponse.noContent("Đã kích hoạt website");
    }

    @PostMapping("/{id}/impersonate")
    @RequirePermission(PLATFORM_TENANTS_IMPERSONATE)
    public ApiResponse<ImpersonateResponse> impersonate(@PathVariable UUID id) {
        return ApiResponse.ok(tenantService.impersonate(id));
    }

    @GetMapping("/{id}/template/history")
    @RequirePermission(PLATFORM_TENANTS_VIEW)
    public ApiResponse<List<TemplateHistoryDTO>> templateHistory(
            @PathVariable UUID id) {
        return ApiResponse.ok(templateSwitchService.getHistory(id));
    }
}
```
# FRONTEND - NEXT.JS 14
## 4. WEB-APP (Public Websites)
### 4.1 Package.json
```json

{
  "name": "websitehub-web",
  "version": "1.0.0",
  "private": true,
  "scripts": {
    "dev": "next dev",
    "build": "next build",
    "start": "next start",
    "lint": "next lint",
    "type-check": "tsc --noEmit"
  },
  "dependencies": {
    "next": "14.1.0",
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "@stomp/stompjs": "^7.0.0",
    "sockjs-client": "^1.6.1",
    "swr": "^2.2.4",
    "date-fns": "^3.2.0",
    "clsx": "^2.1.0",
    "tailwind-merge": "^2.2.0",
    "lucide-react": "^0.312.0",
    "zustand": "^4.5.0"
  },
  "devDependencies": {
    "typescript": "^5.3.3",
    "@types/node": "^20.11.5",
    "@types/react": "^18.2.48",
    "@types/react-dom": "^18.2.18",
    "@types/sockjs-client": "^1.5.4",
    "tailwindcss": "^3.4.1",
    "autoprefixer": "^10.4.17",
    "postcss": "^8.4.33",
    "eslint": "^8.56.0",
    "eslint-config-next": "14.1.0"
  }
}
```
### 4.2 Next.js Config
```typescript

// web-app/next.config.ts
import type { NextConfig } from 'next'

const nextConfig: NextConfig = {
  // Experimental features
  experimental: {
    typedRoutes: true,
  },

  // Image optimization
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: 'media.websitehub.vn',
      },
      {
        protocol: 'https',
        hostname: '**.r2.cloudflarestorage.com',
      },
    ],
  },

  // Rewrites cho SEO-friendly URLs
  async rewrites() {
    return [
      {
        source: '/sitemap.xml',
        destination: '/api/sitemap',
      },
      {
        source: '/robots.txt',
        destination: '/api/robots',
      },
    ]
  },

  // Headers bảo mật
  async headers() {
    return [
      {
        source: '/(.*)',
        headers: [
          { key: 'X-Content-Type-Options',    value: 'nosniff' },
          { key: 'X-Frame-Options',           value: 'SAMEORIGIN' },
          { key: 'X-XSS-Protection',          value: '1; mode=block' },
          { key: 'Referrer-Policy',           value: 'strict-origin-when-cross-origin' },
        ],
      },
    ]
  },

  // Webpack config
  webpack(config) {
    config.module.rules.push({
      test: /\.svg$/,
      use: ['@svgr/webpack'],
    })
    return config
  },
}

export default nextConfig
```
### 4.3 Middleware
```typescript

// web-app/middleware.ts
import { NextRequest, NextResponse } from 'next/server'

const PLATFORM_DOMAIN = process.env.NEXT_PUBLIC_PLATFORM_DOMAIN
    || 'websitehub.vn'
const API_BASE        = process.env.API_BASE_URL
    || 'http://localhost:8080'

// Cache domain → slug (Edge cache)
const domainCache = new Map<string, {
  slug:      string
  cachedAt:  number
}>()

const CACHE_TTL = 5 * 60 * 1000 // 5 phút

export async function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl
  const hostname = request.headers.get('host') || ''

  // ── Skip static assets & API routes ───────────────────────────
  if (
    pathname.startsWith('/_next') ||
    pathname.startsWith('/api/')  ||
    pathname === '/favicon.ico'   ||
    pathname.includes('.')
  ) {
    return NextResponse.next()
  }

  // ── Resolve tenant slug ────────────────────────────────────────
  let tenantSlug: string | null = null

  const cleanHost = hostname.replace('www.', '').toLowerCase()

  if (cleanHost.endsWith(`.${PLATFORM_DOMAIN}`)) {
    // Subdomain: phuongnghi.websitehub.vn → phuongnghi
    tenantSlug = cleanHost.replace(`.${PLATFORM_DOMAIN}`, '')
  } else if (
    cleanHost !== PLATFORM_DOMAIN &&
    cleanHost !== `www.${PLATFORM_DOMAIN}`
  ) {
    // Custom domain: Lookup từ API
    tenantSlug = await resolveTenantFromDomain(cleanHost)
  }

  // ── Platform homepage (no tenant) ────────────────────────────
  if (!tenantSlug) {
    return NextResponse.next()
  }

  // ── Inject tenant info vào headers ───────────────────────────
  const requestHeaders = new Headers(request.headers)
  requestHeaders.set('x-tenant-slug', tenantSlug)
  requestHeaders.set('x-original-host', hostname)

  // ── Admin panel routing ────────────────────────────────────────
  if (pathname.startsWith('/admin')) {
    // Admin panel có cùng domain với public website
    // Nhưng render từ admin app
    return NextResponse.next({
      request: { headers: requestHeaders }
    })
  }

  return NextResponse.next({
    request: { headers: requestHeaders }
  })
}

async function resolveTenantFromDomain(
    domain: string): Promise<string | null> {
  // Check cache
  const cached = domainCache.get(domain)
  if (cached && Date.now() - cached.cachedAt < CACHE_TTL) {
    return cached.slug
  }

  try {
    const res = await fetch(
      `${API_BASE}/v1/platform/resolve-domain?domain=${domain}`,
      {
        headers: { 'Content-Type': 'application/json' },
        next:    { revalidate: 300 }
      }
    )

    if (!res.ok) return null

    const data = await res.json()
    if (data?.data?.slug) {
      domainCache.set(domain, {
        slug:     data.data.slug,
        cachedAt: Date.now()
      })
      return data.data.slug
    }
  } catch (e) {
    console.error('[Middleware] Domain resolution failed:', e)
  }

  return null
}

export const config = {
  matcher: [
    '/((?!_next/static|_next/image|favicon.ico).*)',
  ],
}
```
### 4.4 App Layout
```typescript

// web-app/app/(public)/layout.tsx
import { headers }    from 'next/headers'
import { notFound }   from 'next/navigation'
import { Metadata }   from 'next'
import { Suspense }   from 'react'
import { getTenantConfig }     from '@/lib/tenant-api'
import { loadTemplateLayout }  from '@/lib/template-registry'
import { SuspendedPage }       from '@/components/SuspendedPage'
import { LoadingSpinner }      from '@/components/ui/LoadingSpinner'

// Dynamic metadata per tenant
export async function generateMetadata(): Promise<Metadata> {
  const headersList = headers()
  const tenantSlug  = headersList.get('x-tenant-slug')
  if (!tenantSlug) return {}

  const tenant = await getTenantConfig(tenantSlug)
  if (!tenant) return {}

  const s = tenant.settings

  return {
    title: {
      default:  s?.seoDefaultTitle  || tenant.name,
      template: s?.seoTitleTemplate || `%s - ${tenant.name}`,
    },
    description: s?.seoDefaultDesc || `Website ${tenant.name}`,
    openGraph: {
      siteName: tenant.name,
      images:   s?.seoOgImage ? [s.seoOgImage] : [],
    },
    icons: {
      icon:    s?.faviconUrl || '/favicon.ico',
      apple:   s?.faviconUrl,
    },
    robots: {
      index:  true,
      follow: true,
    },
    verification: {
      google: s?.googleSiteVerify,
    },
  }
}

export default async function PublicLayout({
  children,
}: {
  children: React.ReactNode
}) {
  const headersList = headers()
  const tenantSlug  = headersList.get('x-tenant-slug')

  // No tenant = platform homepage
  if (!tenantSlug) {
    return (
      <html lang="vi">
        <body>{children}</body>
      </html>
    )
  }

  const tenant = await getTenantConfig(tenantSlug)
  if (!tenant) notFound()

  // Suspended website
  if (tenant.status === 'suspended') {
    return <SuspendedPage tenant={tenant} />
  }

  // Load template layout dynamically
  const TemplateLayout = await loadTemplateLayout(tenant.template.code)

  // Effective colors (template default + overrides)
  const colors = tenant.effectiveColors

  return (
    <html lang="vi" suppressHydrationWarning>
      <head>
        {/* Dynamic CSS Variables */}
        <style>{`
          :root {
            --color-primary:    ${colors.primary    || '#E53E3E'};
            --color-secondary:  ${colors.secondary  || '#2D3748'};
            --color-accent:     ${colors.accent     || '#ECC94B'};
            --color-background: ${colors.background || '#FFFFFF'};
            --color-text:       ${colors.text       || '#1A202C'};
          }
        `}</style>

        {/* Custom CSS */}
        {tenant.settings?.customCss && (
          <style
            dangerouslySetInnerHTML={{
              __html: tenant.settings.customCss
            }}
          />
        )}

        {/* Header Scripts (GA, GTM...) */}
        {tenant.settings?.headerScripts && (
          <div
            dangerouslySetInnerHTML={{
              __html: tenant.settings.headerScripts
            }}
          />
        )}
      </head>

      <body>
        <Suspense fallback={<LoadingSpinner />}>
          <TemplateLayout tenant={tenant}>
            {children}
          </TemplateLayout>
        </Suspense>

        {/* Footer Scripts */}
        {tenant.settings?.footerScripts && (
          <div
            dangerouslySetInnerHTML={{
              __html: tenant.settings.footerScripts
            }}
          />
        )}
      </body>
    </html>
  )
}
```
### 4.5 Template Registry
```typescript

// web-app/lib/template-registry.ts
import React from 'react'
import type { TenantConfig, TemplateConfig } from '@/types'

// ── Template Layout Registry ─────────────────────────────────────
type LayoutComponent = React.ComponentType<{
  tenant:   TenantConfig
  children: React.ReactNode
}>

const LAYOUT_REGISTRY: Record<
  string,
  () => Promise<{ default: LayoutComponent }>
> = {
  'lottery-classic': () => import(
      '@/templates/lottery/classic/Layout'
  ),
  'lottery-modern':  () => import(
      '@/templates/lottery/modern/Layout'
  ),
  'lottery-minimal': () => import(
      '@/templates/lottery/minimal/Layout'
  ),
  // Future verticals
  // 'bds-professional': () => import('@/templates/realestate/professional/Layout'),
  // 'spa-elegant':      () => import('@/templates/spa/elegant/Layout'),
}

const FALLBACK_LAYOUT = 'lottery-classic'

export async function loadTemplateLayout(
    templateCode: string): Promise<LayoutComponent> {
  const loader = LAYOUT_REGISTRY[templateCode]
               || LAYOUT_REGISTRY[FALLBACK_LAYOUT]
  const mod    = await loader()
  return mod.default
}

// ── Page Registry ────────────────────────────────────────────────
export type PageName =
  | 'HomePage'
  | 'KQXSPage'
  | 'ThongKePage'
  | 'SoiCauPage'
  | 'BlogPage'
  | 'BlogDetailPage'
  | 'StaticPage'

type PageComponent = React.ComponentType<any>

const PAGE_REGISTRY: Record<
  string,

tiep di
Avatar for Assistant
typescript

  Partial<Record<PageName, () => Promise<any>>>
> = {
  'lottery-classic': {
    HomePage:       () => import('@/templates/lottery/classic/pages/HomePage'),
    KQXSPage:       () => import('@/templates/lottery/classic/pages/KQXSPage'),
    ThongKePage:    () => import('@/templates/lottery/classic/pages/ThongKePage'),
    SoiCauPage:     () => import('@/templates/lottery/classic/pages/SoiCauPage'),
    BlogPage:       () => import('@/templates/lottery/classic/pages/BlogPage'),
    BlogDetailPage: () => import('@/templates/lottery/classic/pages/BlogDetailPage'),
    StaticPage:     () => import('@/templates/lottery/classic/pages/StaticPage'),
  },
  'lottery-modern': {
    HomePage:       () => import('@/templates/lottery/modern/pages/HomePage'),
    KQXSPage:       () => import('@/templates/lottery/modern/pages/KQXSPage'),
    ThongKePage:    () => import('@/templates/lottery/classic/pages/ThongKePage'), // fallback
    SoiCauPage:     () => import('@/templates/lottery/classic/pages/SoiCauPage'), // fallback
    BlogPage:       () => import('@/templates/lottery/classic/pages/BlogPage'),   // fallback
    BlogDetailPage: () => import('@/templates/lottery/classic/pages/BlogDetailPage'),
    StaticPage:     () => import('@/templates/lottery/classic/pages/StaticPage'),
  },
  'lottery-minimal': {
    HomePage:       () => import('@/templates/lottery/minimal/pages/HomePage'),
    KQXSPage:       () => import('@/templates/lottery/minimal/pages/KQXSPage'),
    ThongKePage:    () => import('@/templates/lottery/classic/pages/ThongKePage'),
    SoiCauPage:     () => import('@/templates/lottery/classic/pages/SoiCauPage'),
    BlogPage:       () => import('@/templates/lottery/classic/pages/BlogPage'),
    BlogDetailPage: () => import('@/templates/lottery/classic/pages/BlogDetailPage'),
    StaticPage:     () => import('@/templates/lottery/classic/pages/StaticPage'),
  },
}

export async function loadPage(
    templateCode: string,
    pageName: PageName
): Promise<PageComponent> {
  const registry = PAGE_REGISTRY[templateCode]
                || PAGE_REGISTRY[FALLBACK_LAYOUT]

  const loader = registry[pageName]
              || PAGE_REGISTRY[FALLBACK_LAYOUT][pageName]

  if (!loader) {
    throw new Error(`Page ${pageName} not found`)
  }

  const mod = await loader()
  // Support both named export and default export
  return mod[pageName] || mod.default
}
```
### 4.6 API Client
```typescript

// web-app/lib/api-client.ts
const API_BASE = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080'

interface FetchOptions extends RequestInit {
  params?:     Record<string, string | number | boolean | undefined | null>
  tenantSlug?: string
}

class HttpClient {
  private baseUrl: string

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl
  }

  private buildUrl(path: string, params?: FetchOptions['params']): string {
    const url = new URL(this.baseUrl + path)
    if (params) {
      Object.entries(params).forEach(([k, v]) => {
        if (v !== undefined && v !== null) {
          url.searchParams.set(k, String(v))
        }
      })
    }
    return url.toString()
  }

  private getHeaders(options?: FetchOptions): HeadersInit {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
    }
    if (options?.tenantSlug) {
      headers['x-tenant-slug'] = options.tenantSlug
    }
    return { ...headers, ...options?.headers }
  }

  async get<T>(path: string, options?: FetchOptions): Promise<T> {
    const res = await fetch(
      this.buildUrl(path, options?.params),
      {
        method:  'GET',
        headers: this.getHeaders(options),
        next:    options?.next as any,
        cache:   options?.cache,
      }
    )
    return this.handle<T>(res)
  }

  async post<T>(
      path: string,
      body?: unknown,
      options?: FetchOptions): Promise<T> {
    const res = await fetch(this.buildUrl(path), {
      method:  'POST',
      headers: this.getHeaders(options),
      body:    body ? JSON.stringify(body) : undefined,
    })
    return this.handle<T>(res)
  }

  async put<T>(
      path: string,
      body?: unknown,
      options?: FetchOptions): Promise<T> {
    const res = await fetch(this.buildUrl(path), {
      method:  'PUT',
      headers: this.getHeaders(options),
      body:    body ? JSON.stringify(body) : undefined,
    })
    return this.handle<T>(res)
  }

  async delete<T>(path: string, options?: FetchOptions): Promise<T> {
    const res = await fetch(this.buildUrl(path), {
      method:  'DELETE',
      headers: this.getHeaders(options),
    })
    return this.handle<T>(res)
  }

  private async handle<T>(res: Response): Promise<T> {
    const data = await res.json()
    if (!res.ok || data.success === false) {
      throw new ApiError(
        data.message    || 'Unknown error',
        data.errorCode  || 'UNKNOWN',
        res.status,
        data.errors
      )
    }
    return data.data as T
  }
}

export class ApiError extends Error {
  constructor(
    message: string,
    public readonly code:        string,
    public readonly status:      number,
    public readonly fieldErrors?: Record<string, string>
  ) {
    super(message)
    this.name = 'ApiError'
  }
}

// ── Public API (Server-side - no auth) ───────────────────────────
export const publicApi = new HttpClient(API_BASE)

// ── Lottery API ───────────────────────────────────────────────────
export const lotteryApi = {
  getTodayResults: (tenantSlug: string, region?: string) =>
    publicApi.get<LotteryResultsResponse>(
      '/v1/lottery/results/today',
      {
        params:     { region },
        tenantSlug,
        next: { revalidate: 120, tags: [`lottery-today-${tenantSlug}`] }
      }
    ),

  getResultsByDate: (tenantSlug: string, date: string, region?: string) =>
    publicApi.get<LotteryResultsResponse>(
      `/v1/lottery/results/${date}`,
      {
        params:     { region },
        tenantSlug,
        next: { revalidate: 3600, tags: [`lottery-${date}`] }
      }
    ),

  getProvinces: (tenantSlug: string, region?: string) =>
    publicApi.get<ProvinceListResponse>(
      '/v1/lottery/provinces',
      {
        params: { region },
        tenantSlug,
        next: { revalidate: 86400 }
      }
    ),

  getFrequencyStats: (
      tenantSlug: string,
      provinceCode: string,
      period: string) =>
    publicApi.get<FrequencyStatsResponse>(
      '/v1/lottery/stats/frequency',
      {
        params:     { provinceCode, period },
        tenantSlug,
        next: { revalidate: 1800, tags: [`stats-${provinceCode}`] }
      }
    ),

  getGanStats: (tenantSlug: string, provinceCode: string) =>
    publicApi.get<GanStatsResponse>(
      '/v1/lottery/stats/gan',
      {
        params:     { provinceCode, limit: 20 },
        tenantSlug,
        next: { revalidate: 1800 }
      }
    ),

  getPredictions: (
      tenantSlug: string,
      provinceCode?: string,
      date?: string) =>
    publicApi.get<PredictionsResponse>(
      '/v1/lottery/predictions',
      {
        params:     { provinceCode, date, size: 10 },
        tenantSlug,
        next: { revalidate: 300 }
      }
    ),

  checkTicket: (tenantSlug: string, data: CheckTicketRequest) =>
    publicApi.post<CheckTicketResponse>(
      '/v1/lottery/check-ticket',
      data,
      { tenantSlug }
    ),
}

// ── CMS API ───────────────────────────────────────────────────────
export const cmsApi = {
  getArticles: (
      tenantSlug: string,
      params?: ArticleListParams) =>
    publicApi.get<ArticleListResponse>(
      '/v1/cms/articles',
      {
        params:     params as any,
        tenantSlug,
        next: { revalidate: 600 }
      }
    ),

  getArticleBySlug: (tenantSlug: string, slug: string) =>
    publicApi.get<ArticleResponse>(
      `/v1/cms/articles/${slug}`,
      {
        tenantSlug,
        next: {
          revalidate: 600,
          tags: [`article-${tenantSlug}-${slug}`]
        }
      }
    ),

  getPageBySlug: (tenantSlug: string, slug: string) =>
    publicApi.get<PageResponse_>(
      `/v1/cms/pages/${slug}`,
      {
        tenantSlug,
        next: { revalidate: 3600 }
      }
    ),
}

// ── Tenant Config API ─────────────────────────────────────────────
export const tenantApi = {
  getConfig: (slug: string) =>
    publicApi.get<TenantConfig>(
      `/v1/platform/tenant/${slug}/config`,
      { next: { revalidate: 60, tags: [`tenant-${slug}`] } }
    ),
}
```
### 4.7 Types
```typescript

// web-app/types/index.ts
export interface TenantConfig {
  id:     string
  slug:   string
  name:   string
  status: 'building' | 'active' | 'suspended' | 'cancelled'

  verticalCode: string
  template: {
    id:           string
    code:         string
    name:         string
    layoutConfig: TemplateLayoutConfig
    defaultColors: ColorConfig
  }

  colorOverrides:   Partial<ColorConfig> | null
  effectiveColors:  ColorConfig
  effectiveFeatures: FeatureConfig

  settings: TenantSettings | null
  primaryDomain: string | null
  hostingStatus: 'active' | 'suspended' | 'cancelled'
}

export interface ColorConfig {
  primary:    string
  secondary:  string
  accent:     string
  background: string
  text:       string
}

export interface FeatureConfig {
  show_live_result:    boolean
  show_statistics:     boolean
  show_predictions:    boolean
  show_ticket_checker: boolean
  show_schedule:       boolean
  show_print_button:   boolean
  show_share_buttons:  boolean
}

export interface TemplateLayoutConfig {
  layout_type:       'sidebar-right' | 'sidebar-left' | 'no-sidebar' | 'full-width'
  header_style:      'sticky' | 'static' | 'transparent'
  footer_style:      'simple' | 'detailed' | 'minimal'
  widget_order:      string[]
  sidebar_widgets:   string[]
  homepage_sections: string[]
  features_visible:  FeatureConfig
}

export interface TenantSettings {
  logoUrl:            string | null
  faviconUrl:         string | null
  tagline:            string | null
  description:        string | null
  phone:              string | null
  email:              string | null
  address:            string | null
  socialLinks:        Record<string, string>
  seoTitleTemplate:   string
  seoDefaultTitle:    string | null
  seoDefaultDesc:     string | null
  seoOgImage:         string | null
  googleAnalyticsId:  string | null
  googleSiteVerify:   string | null
  headerScripts:      string | null
  footerScripts:      string | null
  customCss:          string | null
  timezone:           string
  locale:             string
}

export interface LotteryResult {
  id:             string
  provinceId:     string
  provinceCode:   string
  provinceName:   string
  provinceSlug:   string
  regionCode:     'north' | 'central' | 'south'
  regionName:     string
  drawDate:       string
  specialPrize:   string | null
  firstPrizes:    string[]
  secondPrizes:   string[]
  thirdPrizes:    string[]
  fourthPrizes:   string[]
  fifthPrizes:    string[]
  sixthPrizes:    string[]
  seventhPrizes:  string[]
  eighthPrizes:   string[]
  lotoNumbers:    string[]
  isVerified:     boolean
  updatedAt:      string
}

export interface FrequencyStats {
  number:        string
  frequency:     number
  frequencyPct:  number
  lastAppeared:  string | null
  daysSinceLast: number
  avgCycle:      number
  maxGap:        number
  minGap:        number
  trend:         'hot' | 'warm' | 'neutral' | 'cold' | 'frozen'
}

export interface LotteryPrediction {
  id:             string
  provinceName:   string
  drawDate:       string
  predictionType: string
  numbers:        string[]
  confidence:     1 | 2 | 3 | 4 | 5
  authorName:     string
  authorAvatar:   string | null
  methodName:     string
  analysis:       string
  isVip:          boolean
  resultStatus:   'pending' | 'win' | 'lose' | 'partial'
  viewCount:      number
  createdAt:      string
}

export interface Article {
  id:          string
  title:       string
  slug:        string
  excerpt:     string | null
  thumbnail:   string | null
  authorName:  string
  categoryName: string | null
  tags:        string[]
  publishedAt: string
  viewCount:   number
}
```
### 4.8 Shared UI Components
```typescript

// web-app/components/shared/FeatureGate.tsx
'use client'

import { useTenantConfig } from '@/hooks/useTenantConfig'

interface Props {
  feature:  keyof import('@/types').FeatureConfig
  fallback?: React.ReactNode
  children:  React.ReactNode
}

export function FeatureGate({ feature, fallback = null, children }: Props) {
  const { effectiveFeatures } = useTenantConfig()
  return effectiveFeatures?.[feature] !== false
    ? <>{children}</>
    : <>{fallback}</>
}

// web-app/components/shared/AdSlot.tsx
'use client'

import { useEffect, useRef } from 'react'

interface Props {
  slotId:   string
  position: string
  className?: string
}

export function AdSlot({ slotId, position, className }: Props) {
  const ref = useRef<HTMLDivElement>(null)

  useEffect(() => {
    // Lazy load AdSense
    if (ref.current && (window as any).adsbygoogle) {
      try {
        ;((window as any).adsbygoogle = (window as any).adsbygoogle || [])
          .push({})
      } catch (e) {}
    }
  }, [])

  return (
    <div
      ref={ref}
      className={`ad-slot ad-${position} ${className || ''}`}
      data-slot-id={slotId}
    />
  )
}

// web-app/components/shared/Breadcrumb.tsx
import Link from 'next/link'

interface BreadcrumbItem {
  label: string
  href?: string
}

export function Breadcrumb({ items }: { items: BreadcrumbItem[] }) {
  return (
    <nav aria-label="breadcrumb" className="text-sm text-gray-500 mb-4">
      <ol className="flex items-center gap-2 flex-wrap">
        {items.map((item, idx) => (
          <li key={idx} className="flex items-center gap-2">
            {idx > 0 && <span className="text-gray-300">›</span>}
            {item.href && idx < items.length - 1 ? (
              <Link
                href={item.href}
                className="hover:text-primary transition-colors"
              >
                {item.label}
              </Link>
            ) : (
              <span className={idx === items.length - 1
                ? 'text-gray-700 font-medium' : ''}>
                {item.label}
              </span>
            )}
          </li>
        ))}
      </ol>
    </nav>
  )
}
5. ADMIN PANEL (Super Admin)
5.1 Admin Panel Structure
typescript

// admin-panel/app/layout.tsx
import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import { AuthProvider }   from '@/providers/AuthProvider'
import { QueryProvider }  from '@/providers/QueryProvider'
import { ToastProvider }  from '@/providers/ToastProvider'
import './globals.css'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: {
    default:  'WebsiteHub Admin',
    template: '%s | WebsiteHub Admin',
  },
  robots: { index: false, follow: false },
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="vi" suppressHydrationWarning>
      <body className={inter.className}>
        <QueryProvider>
          <AuthProvider>
            <ToastProvider>
              {children}
            </ToastProvider>
          </AuthProvider>
        </QueryProvider>
      </body>
    </html>
  )
}

// admin-panel/app/(dashboard)/layout.tsx
'use client'

import { useAuthGuard }  from '@/hooks/useAuthGuard'
import { Sidebar }       from '@/components/layout/Sidebar'
import { Header }        from '@/components/layout/Header'
import { LoadingScreen } from '@/components/ui/LoadingScreen'

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode
}) {
  const { isLoading, isAuthenticated } = useAuthGuard()

  if (isLoading)        return <LoadingScreen />
  if (!isAuthenticated) return null // Redirect handled by hook

  return (
    <div className="flex h-screen bg-gray-50 overflow-hidden">
      {/* Sidebar */}
      <Sidebar />

      {/* Main content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header />
        <main className="flex-1 overflow-y-auto p-6">
          {children}
        </main>
      </div>
    </div>
  )
}
5.2 Admin Sidebar
typescript

// admin-panel/components/layout/Sidebar.tsx
'use client'

import Link           from 'next/link'
import { usePathname } from 'next/navigation'
import { useAuth }    from '@/hooks/useAuth'
import {
  LayoutDashboard,
  Globe,
  Users,
  CreditCard,
  Settings,
  Palette,
  Building2,
  BarChart3,
  Bell,
  LogOut,
  ChevronDown,
} from 'lucide-react'
import { useState } from 'react'
import { cn } from '@/lib/utils'

interface NavItem {
  label:       string
  href?:       string
  icon:        React.ComponentType<any>
  permission?: string
  children?:   NavItem[]
  badge?:      number
}

const NAV_ITEMS: NavItem[] = [
  {
    label: 'Dashboard',
    href:  '/dashboard',
    icon:  LayoutDashboard,
  },
  {
    label: 'Websites',
    icon:  Globe,
    permission: 'platform.tenants.view',
    children: [
      { label: 'Tất cả websites', href: '/tenants',        icon: Globe },
      { label: 'Tạo website mới', href: '/tenants/new',    icon: Globe },
      { label: 'Setup requests',  href: '/setup-requests', icon: Globe },
    ],
  },
  {
    label:      'Khách hàng',
    icon:       Building2,
    permission: 'platform.clients.view',
    children: [
      { label: 'Danh sách KH',   href: '/clients',     icon: Building2 },
      { label: 'Thêm KH mới',    href: '/clients/new', icon: Building2 },
    ],
  },
  {
    label:      'Thanh toán',
    icon:       CreditCard,
    permission: 'platform.payments.view',
    children: [
      { label: 'Lịch sử TT',     href: '/payments',        icon: CreditCard },
      { label: 'Ghi nhận TT',    href: '/payments/record', icon: CreditCard },
      { label: 'Hosting status', href: '/hosting',         icon: CreditCard },
    ],
  },
  {
    label:      'Templates',
    icon:       Palette,
    permission: 'platform.templates.view',
    href:       '/templates',
  },
  {
    label:      'Admins',
    icon:       Users,
    permission: 'platform.admins.view',
    href:       '/admins',
  },
  {
    label:      'Báo cáo',
    icon:       BarChart3,
    href:       '/reports',
  },
  {
    label:      'Hệ thống',
    icon:       Settings,
    permission: 'platform.system.config',
    children: [
      { label: 'Cấu hình',    href: '/system/config', icon: Settings },
      { label: 'Jobs',        href: '/system/jobs',   icon: Settings },
      { label: 'Audit logs',  href: '/system/logs',   icon: Settings },
    ],
  },
]

export function Sidebar() {
  const pathname = usePathname()
  const { user, logout } = useAuth()
  const [collapsed, setCollapsed]     = useState(false)
  const [openGroups, setOpenGroups]   = useState<string[]>([])

  const toggleGroup = (label: string) => {
    setOpenGroups(prev =>
      prev.includes(label)
        ? prev.filter(g => g !== label)
        : [...prev, label]
    )
  }

  const hasPermission = (permission?: string) => {
    if (!permission) return true
    return user?.permissions?.includes(permission)
        || user?.roleCode === 'super_admin'
  }

  return (
    <aside className={cn(
      'bg-gray-900 text-white flex flex-col transition-all duration-300',
      collapsed ? 'w-16' : 'w-64'
    )}>
      {/* Logo */}
      <div className="h-16 flex items-center justify-between px-4
                      border-b border-gray-700">
        {!collapsed && (
          <span className="font-bold text-lg text-white">
            WebsiteHub
          </span>
        )}
        <button
          onClick={() => setCollapsed(!collapsed)}
          className="text-gray-400 hover:text-white p-1 rounded"
        >
          {collapsed ? '→' : '←'}
        </button>
      </div>

      {/* Navigation */}
      <nav className="flex-1 overflow-y-auto py-4">
        {NAV_ITEMS.map(item => {
          if (!hasPermission(item.permission)) return null

          // Group with children
          if (item.children) {
            const isOpen = openGroups.includes(item.label)
            const isActive = item.children.some(
              c => c.href && pathname.startsWith(c.href)
            )

            return (
              <div key={item.label}>
                <button
                  onClick={() => toggleGroup(item.label)}
                  className={cn(
                    'w-full flex items-center gap-3 px-4 py-2.5',
                    'text-sm font-medium transition-colors',
                    isActive
                      ? 'text-white bg-gray-700'
                      : 'text-gray-400 hover:text-white hover:bg-gray-800'
                  )}
                >
                  <item.icon size={18} className="flex-shrink-0" />
                  {!collapsed && (
                    <>
                      <span className="flex-1 text-left">{item.label}</span>
                      <ChevronDown
                        size={14}
                        className={cn(
                          'transition-transform',
                          isOpen && 'rotate-180'
                        )}
                      />
                    </>
                  )}
                </button>

                {!collapsed && isOpen && (
                  <div className="bg-gray-800 pl-4">
                    {item.children.map(child => (
                      <Link
                        key={child.href}
                        href={child.href!}
                        className={cn(
                          'flex items-center gap-3 px-4 py-2 text-sm',
                          'transition-colors',
                          pathname === child.href
                            ? 'text-white font-medium'
                            : 'text-gray-400 hover:text-white'
                        )}
                      >
                        <span className="w-1 h-1 rounded-full bg-current" />
                        {child.label}
                      </Link>
                    ))}
                  </div>
                )}
              </div>
            )
          }

          // Single item
          return (
            <Link
              key={item.href}
              href={item.href!}
              className={cn(
                'flex items-center gap-3 px-4 py-2.5',
                'text-sm font-medium transition-colors',
                pathname === item.href
                  ? 'text-white bg-gray-700'
                  : 'text-gray-400 hover:text-white hover:bg-gray-800'
              )}
            >
              <item.icon size={18} className="flex-shrink-0" />
              {!collapsed && <span>{item.label}</span>}
              {!collapsed && item.badge && (
                <span className="ml-auto bg-red-500 text-white
                                 text-xs px-1.5 py-0.5 rounded-full">
                  {item.badge}
                </span>
              )}
            </Link>
          )
        })}
      </nav>

      {/* User info + Logout */}
      <div className="border-t border-gray-700 p-4">
        {!collapsed && (
          <div className="mb-3">
            <p className="text-sm font-medium text-white truncate">
              {user?.fullName}
            </p>
            <p className="text-xs text-gray-400 truncate">
              {user?.email}
            </p>
            <span className="text-xs bg-blue-600 text-white
                             px-1.5 py-0.5 rounded mt-1 inline-block">
              {user?.roleCode}
            </span>
          </div>
        )}
        <button
          onClick={logout}
          className="flex items-center gap-2 text-gray-400
                     hover:text-white text-sm w-full"
        >
          <LogOut size={16} />
          {!collapsed && 'Đăng xuất'}
        </button>
      </div>
    </aside>
  )
}
5.3 Dashboard Page
typescript

// admin-panel/app/(dashboard)/dashboard/page.tsx
import { Metadata }        from 'next'
import { DashboardStats }  from '@/components/dashboard/DashboardStats'
import { ExpiringWebsites }from '@/components/dashboard/ExpiringWebsites'
import { RecentActivity }  from '@/components/dashboard/RecentActivity'
import { QuickActions }    from '@/components/dashboard/QuickActions'
import { platformAdminApi }from '@/lib/admin-api'

export const metadata: Metadata = {
  title: 'Dashboard',
}

export default async function DashboardPage() {
  const [stats, expiring] = await Promise.all([
    platformAdminApi.getDashboardStats(),
    platformAdminApi.getExpiringWebsites(7),
  ])

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Dashboard</h1>
        <p className="text-gray-500 mt-1">
          Tổng quan hệ thống WebsiteHub
        </p>
      </div>

      {/* Stats */}
      <DashboardStats stats={stats} />

      {/* Alert: Expiring websites */}
      {expiring.length > 0 && (
        <ExpiringWebsites websites={expiring} />
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Quick Actions */}
        <div className="lg:col-span-1">
          <QuickActions />
        </div>

        {/* Recent Activity */}
        <div className="lg:col-span-2">
          <RecentActivity />
        </div>
      </div>
    </div>
  )
}

// admin-panel/components/dashboard/DashboardStats.tsx
import {
  Globe, Users, DollarSign,
  TrendingUp, AlertCircle, CheckCircle
} from 'lucide-react'

interface Stats {
  totalTenants:   number
  activeTenants:  number
  suspendedTenants: number
  totalClients:   number
  monthlyRevenue: number
  expiringCount:  number
}

export function DashboardStats({ stats }: { stats: Stats }) {
  const cards = [
    {
      label:  'Tổng websites',
      value:  stats.totalTenants,
      icon:   Globe,
      color:  'blue',
      sub:    `${stats.activeTenants} đang hoạt động`,
    },
    {
      label:  'Khách hàng',
      value:  stats.totalClients,
      icon:   Users,
      color:  'purple',
      sub:    'Tổng khách đang có',
    },

tiep di
Avatar for Assistant
typescript

    {
      label:  'Doanh thu tháng',
      value:  formatVND(stats.monthlyRevenue),
      icon:   DollarSign,
      color:  'green',
      sub:    'Tháng hiện tại',
    },
    {
      label:  'Sắp hết hạn',
      value:  stats.expiringCount,
      icon:   AlertCircle,
      color:  stats.expiringCount > 0 ? 'orange' : 'gray',
      sub:    'Trong 7 ngày tới',
    },
  ]

  return (
    <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
      {cards.map((card, idx) => (
        <div
          key={idx}
          className="bg-white rounded-xl border border-gray-200
                     p-5 shadow-sm hover:shadow-md transition-shadow"
        >
          <div className="flex items-center justify-between mb-3">
            <span className="text-sm font-medium text-gray-500">
              {card.label}
            </span>
            <div className={`p-2 rounded-lg bg-${card.color}-50`}>
              <card.icon
                size={18}
                className={`text-${card.color}-600`}
              />
            </div>
          </div>
          <p className="text-2xl font-bold text-gray-900">
            {card.value}
          </p>
          <p className="text-xs text-gray-400 mt-1">{card.sub}</p>
        </div>
      ))}
    </div>
  )
}

function formatVND(amount: number): string {
  if (amount >= 1_000_000) {
    return `${(amount / 1_000_000).toFixed(1)}tr đ`
  }
  return new Intl.NumberFormat('vi-VN').format(amount) + 'đ'
}
5.4 Tenant Management Pages
typescript

// admin-panel/app/(dashboard)/tenants/page.tsx
'use client'

import { useState }       from 'react'
import { useQuery }       from '@tanstack/react-query'
import { platformAdminApi } from '@/lib/admin-api'
import { TenantTable }    from '@/components/tenants/TenantTable'
import { TenantFilters }  from '@/components/tenants/TenantFilters'
import { Button }         from '@/components/ui/Button'
import { Plus }           from 'lucide-react'
import Link               from 'next/link'

export default function TenantsPage() {
  const [filters, setFilters] = useState({
    status:   '',
    vertical: '',
    search:   '',
    page:     1,
    size:     20,
  })

  const { data, isLoading, refetch } = useQuery({
    queryKey: ['tenants', filters],
    queryFn:  () => platformAdminApi.getTenants(filters),
  })

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">Websites</h1>
          <p className="text-gray-500">
            Quản lý tất cả websites trên platform
          </p>
        </div>
        <Link href="/tenants/new">
          <Button icon={<Plus size={16} />}>
            Tạo website mới
          </Button>
        </Link>
      </div>

      {/* Filters */}
      <TenantFilters
        filters={filters}
        onChange={setFilters}
      />

      {/* Table */}
      <TenantTable
        data={data}
        isLoading={isLoading}
        onRefresh={refetch}
      />
    </div>
  )
}

// admin-panel/components/tenants/TenantTable.tsx
'use client'

import { useState }          from 'react'
import Link                  from 'next/link'
import { useRouter }         from 'next/navigation'
import { platformAdminApi }  from '@/lib/admin-api'
import { Badge }             from '@/components/ui/Badge'
import { Button }            from '@/components/ui/Button'
import { ConfirmDialog }     from '@/components/ui/ConfirmDialog'
import {
  ExternalLink, Settings, Pause,
  Play, LogIn, Palette, MoreVertical
} from 'lucide-react'
import type { TenantListDTO, PageResponse } from '@/types'

interface Props {
  data?:      PageResponse<TenantListDTO>
  isLoading:  boolean
  onRefresh:  () => void
}

const STATUS_BADGE: Record<string, { label: string; color: string }> = {
  active:    { label: 'Hoạt động',  color: 'green'  },
  suspended: { label: 'Tạm ngưng', color: 'orange' },
  building:  { label: 'Đang setup', color: 'blue'   },
  cancelled: { label: 'Đã hủy',    color: 'red'    },
}

export function TenantTable({ data, isLoading, onRefresh }: Props) {
  const router = useRouter()
  const [suspendTarget, setSuspendTarget] = useState<TenantListDTO | null>(null)
  const [loading, setLoading]             = useState<string | null>(null)

  const handleSuspend = async (tenant: TenantListDTO, reason: string) => {
    setLoading(tenant.id)
    try {
      await platformAdminApi.suspendTenant(tenant.id, reason)
      setSuspendTarget(null)
      onRefresh()
    } finally {
      setLoading(null)
    }
  }

  const handleActivate = async (tenantId: string) => {
    setLoading(tenantId)
    try {
      await platformAdminApi.activateTenant(tenantId)
      onRefresh()
    } finally {
      setLoading(null)
    }
  }

  const handleImpersonate = async (tenantId: string) => {
    const { adminUrl } = await platformAdminApi.impersonate(tenantId)
    window.open(adminUrl, '_blank')
  }

  if (isLoading) {
    return <TableSkeleton />
  }

  return (
    <>
      <div className="bg-white rounded-xl border border-gray-200
                      shadow-sm overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-50 border-b border-gray-200">
            <tr>
              {['Website', 'Khách hàng', 'Ngành', 'Template',
                'Hosting', 'Trạng thái', 'Thao tác'].map(h => (
                <th
                  key={h}
                  className="px-4 py-3 text-left text-xs font-semibold
                             text-gray-500 uppercase tracking-wider"
                >
                  {h}
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {data?.items.map(tenant => {
              const badge = STATUS_BADGE[tenant.status]
              return (
                <tr
                  key={tenant.id}
                  className="hover:bg-gray-50 transition-colors"
                >
                  {/* Website */}
                  <td className="px-4 py-3">
                    <div className="flex items-center gap-3">
                      {tenant.logoUrl ? (
                        <img
                          src={tenant.logoUrl}
                          alt={tenant.name}
                          className="w-8 h-8 rounded object-cover"
                        />
                      ) : (
                        <div className="w-8 h-8 rounded bg-primary-100
                                        flex items-center justify-center">
                          <span className="text-primary-600 font-bold text-xs">
                            {tenant.name[0]}
                          </span>
                        </div>
                      )}
                      <div>
                        <p className="font-medium text-sm text-gray-900">
                          {tenant.name}
                        </p>
                        <a
                          href={`https://${tenant.primaryDomain}`}
                          target="_blank"
                          className="text-xs text-blue-500 hover:underline
                                     flex items-center gap-1"
                        >
                          {tenant.primaryDomain}
                          <ExternalLink size={10} />
                        </a>
                      </div>
                    </div>
                  </td>

                  {/* Client */}
                  <td className="px-4 py-3">
                    <p className="text-sm font-medium">
                      {tenant.clientName}
                    </p>
                    <p className="text-xs text-gray-400">
                      {tenant.clientPhone}
                    </p>
                  </td>

                  {/* Vertical */}
                  <td className="px-4 py-3">
                    <Badge color="blue" size="sm">
                      {tenant.verticalName}
                    </Badge>
                  </td>

                  {/* Template */}
                  <td className="px-4 py-3">
                    <span className="text-sm text-gray-600">
                      {tenant.templateName}
                    </span>
                  </td>

                  {/* Hosting */}
                  <td className="px-4 py-3">
                    <HostingStatusCell hosting={tenant.hostingStatus} />
                  </td>

                  {/* Status */}
                  <td className="px-4 py-3">
                    <Badge color={badge.color as any} size="sm">
                      {badge.label}
                    </Badge>
                  </td>

                  {/* Actions */}
                  <td className="px-4 py-3">
                    <div className="flex items-center gap-1">
                      <Link href={`/tenants/${tenant.id}`}>
                        <Button
                          variant="ghost"
                          size="sm"
                          icon={<Settings size={14} />}
                          title="Quản lý"
                        />
                      </Link>

                      <Button
                        variant="ghost"
                        size="sm"
                        icon={<Palette size={14} />}
                        title="Đổi template"
                        onClick={() =>
                          router.push(`/tenants/${tenant.id}?tab=template`)
                        }
                      />

                      <Button
                        variant="ghost"
                        size="sm"
                        icon={<LogIn size={14} />}
                        title="Đăng nhập vào website"
                        onClick={() => handleImpersonate(tenant.id)}
                      />

                      {tenant.status === 'active' ? (
                        <Button
                          variant="ghost"
                          size="sm"
                          icon={<Pause size={14} />}
                          title="Tạm ngưng"
                          onClick={() => setSuspendTarget(tenant)}
                          className="text-orange-500"
                        />
                      ) : tenant.status === 'suspended' ? (
                        <Button
                          variant="ghost"
                          size="sm"
                          icon={<Play size={14} />}
                          title="Kích hoạt"
                          onClick={() => handleActivate(tenant.id)}
                          loading={loading === tenant.id}
                          className="text-green-500"
                        />
                      ) : null}
                    </div>
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>

        {/* Pagination */}
        {data && <TablePagination data={data} />}
      </div>

      {/* Suspend confirm dialog */}
      {suspendTarget && (
        <SuspendDialog
          tenant={suspendTarget}
          onConfirm={(reason) => handleSuspend(suspendTarget, reason)}
          onCancel={() => setSuspendTarget(null)}
          loading={loading === suspendTarget.id}
        />
      )}
    </>
  )
}

function HostingStatusCell({
  hosting,
}: {
  hosting: { status: string; paidUntil: string; daysRemaining: number }
}) {
  const days = hosting.daysRemaining
  const color = days <= 0    ? 'red'
              : days <= 7    ? 'orange'
              : days <= 30   ? 'yellow'
              : 'green'

  return (
    <div>
      <Badge color={color as any} size="sm">
        {days <= 0
          ? 'Hết hạn'
          : `Còn ${days} ngày`}
      </Badge>
      <p className="text-xs text-gray-400 mt-0.5">
        {new Date(hosting.paidUntil).toLocaleDateString('vi-VN')}
      </p>
    </div>
  )
}
```
## 6. INFRASTRUCTURE
### 6.1 Docker Compose
```yaml


# docker-compose.yml
version: '3.9'

services:

  # ── PostgreSQL ──────────────────────────────────────────────────
  postgres:
    image: postgres:15-alpine
    container_name: wh_postgres
    restart: unless-stopped
    environment:
      POSTGRES_DB:       websitehub
      POSTGRES_USER:     wh_user
      POSTGRES_PASSWORD: ${DB_PASSWORD:?err}
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./infrastructure/scripts/init.sql:/docker-entrypoint-initdb.d/init.sql:ro
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U wh_user -d websitehub"]
      interval: 10s
      timeout:  5s
      retries:  5
    networks:
      - platform_net

  # ── Redis ───────────────────────────────────────────────────────
  redis:
    image: redis:7.2-alpine
    container_name: wh_redis
    restart: unless-stopped
    command: >
      redis-server
      --requirepass ${REDIS_PASSWORD:?err}
      --maxmemory 512mb
      --maxmemory-policy allkeys-lru
      --save 60 1000
    volumes:
      - redis_data:/data
    ports:
      - "6379:6379"
    healthcheck:
      test: ["CMD", "redis-cli", "-a", "${REDIS_PASSWORD}", "ping"]
      interval: 10s
      timeout:  5s
      retries:  5
    networks:
      - platform_net

  # ── Spring Boot API ─────────────────────────────────────────────
  api:
    build:
      context:    ./backend/platform-api
      dockerfile: Dockerfile
    container_name: wh_api
    restart: unless-stopped
    depends_on:
      postgres:
        condition: service_healthy
      redis:
        condition: service_healthy
    environment:
      # Database
      DATABASE_URL:      jdbc:postgresql://postgres:5432/websitehub
      DATABASE_USERNAME: wh_user
      DATABASE_PASSWORD: ${DB_PASSWORD}
      # Redis
      REDIS_HOST:        redis
      REDIS_PORT:        6379
      REDIS_PASSWORD:    ${REDIS_PASSWORD}
      # Platform
      PLATFORM_DOMAIN:   ${PLATFORM_DOMAIN:websitehub.vn}
      JWT_SECRET:        ${JWT_SECRET:?err}
      # Storage
      R2_ACCESS_KEY:     ${R2_ACCESS_KEY}
      R2_SECRET_KEY:     ${R2_SECRET_KEY}
      R2_BUCKET:         ${R2_BUCKET}
      R2_ENDPOINT:       ${R2_ENDPOINT}
      R2_PUBLIC_URL:     ${R2_PUBLIC_URL}
      # Email
      MAIL_PASSWORD:     ${RESEND_API_KEY}
      # Telegram
      TELEGRAM_BOT_TOKEN: ${TELEGRAM_BOT_TOKEN}
      TELEGRAM_CHAT_ID:   ${TELEGRAM_CHAT_ID}
      # Lottery
      LOTTERY_FETCH_ENABLED: true
      # Java 21 optimizations
      JAVA_OPTS: >-
        -server
        -XX:+UseZGC
        -XX:+ZGenerational
        -Xms512m
        -Xmx1g
        -Djava.security.egd=file:/dev/./urandom
      SPRING_THREADS_VIRTUAL_ENABLED: 'true'
    ports:
      - "8080:8080"
    deploy:
      resources:
        limits:
          memory: 1.5G
    networks:
      - platform_net

  # ── Next.js Web App ─────────────────────────────────────────────
  web:
    build:
      context:    ./frontend/web-app
      dockerfile: Dockerfile
    container_name: wh_web
    restart: unless-stopped
    depends_on:
      - api
    environment:
      NEXT_PUBLIC_API_URL:         https://api.${PLATFORM_DOMAIN}
      NEXT_PUBLIC_WS_URL:          wss://api.${PLATFORM_DOMAIN}
      NEXT_PUBLIC_PLATFORM_DOMAIN: ${PLATFORM_DOMAIN}
      API_BASE_URL:                http://api:8080
    ports:
      - "3000:3000"
    networks:
      - platform_net

  # ── Admin Panel ─────────────────────────────────────────────────
  admin:
    build:
      context:    ./frontend/admin-panel
      dockerfile: Dockerfile
    container_name: wh_admin
    restart: unless-stopped
    depends_on:
      - api
    environment:
      NEXT_PUBLIC_API_URL: https://api.${PLATFORM_DOMAIN}
      API_BASE_URL:        http://api:8080
    ports:
      - "3001:3000"
    networks:
      - platform_net

  # ── Nginx ───────────────────────────────────────────────────────
  nginx:
    image: nginx:1.25-alpine
    container_name: wh_nginx
    restart: unless-stopped
    depends_on:
      - api
      - web
      - admin
    volumes:
      - ./infrastructure/nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./infrastructure/nginx/conf.d:/etc/nginx/conf.d:ro
      - nginx_cache:/var/cache/nginx
      - certbot_certs:/etc/letsencrypt:ro
    ports:
      - "80:80"
      - "443:443"
    networks:
      - platform_net

volumes:
  postgres_data:
  redis_data:
  nginx_cache:
  certbot_certs:

networks:
  platform_net:
    driver: bridge
6.2 Nginx Config
nginx

# infrastructure/nginx/conf.d/platform.conf

# Upstreams
upstream api_backend {
    least_conn;
    server api:8080;
    keepalive 32;
}

upstream web_frontend {
    least_conn;
    server web:3000;
    keepalive 16;
}

upstream admin_frontend {
    least_conn;
    server admin:3000;
    keepalive 8;
}

# Rate limiting
limit_req_zone $binary_remote_addr zone=api:10m   rate=60r/m;
limit_req_zone $binary_remote_addr zone=web:10m   rate=200r/m;
limit_req_zone $binary_remote_addr zone=login:10m rate=5r/m;

# ── Admin Panel ─────────────────────────────────────────────────
server {
    listen 443 ssl http2;
    server_name admin.websitehub.vn;

    ssl_certificate     /etc/letsencrypt/live/websitehub.vn/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/websitehub.vn/privkey.pem;

    location / {
        proxy_pass         http://admin_frontend;
        proxy_http_version 1.1;
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-For   $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto $scheme;
    }
}

# ── API ─────────────────────────────────────────────────────────
server {
    listen 443 ssl http2;
    server_name api.websitehub.vn;

    ssl_certificate     /etc/letsencrypt/live/websitehub.vn/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/websitehub.vn/privkey.pem;

    # Login rate limit
    location ~ ^/(admin|platform)/v1/auth/login {
        limit_req zone=login burst=3 nodelay;
        proxy_pass http://api_backend;
        proxy_set_header Host              $host;
        proxy_set_header X-Real-IP         $remote_addr;
        proxy_set_header X-Forwarded-Host  $host;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # WebSocket
    location /ws {
        proxy_pass         http://api_backend;
        proxy_http_version 1.1;
        proxy_set_header   Upgrade    $http_upgrade;
        proxy_set_header   Connection "upgrade";
        proxy_set_header   Host       $host;
        proxy_read_timeout 86400;
    }

    # API
    location / {
        limit_req zone=api burst=20 nodelay;
        proxy_pass         http://api_backend;
        proxy_http_version 1.1;
        proxy_set_header   Connection        "";
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-Host  $host;
        proxy_set_header   X-Forwarded-Proto $scheme;

        # Cache GET requests
        proxy_cache_methods GET HEAD;
        proxy_cache_valid   200 2m;
        proxy_cache_bypass  $http_authorization;
        proxy_no_cache      $http_authorization;
    }
}

# ── Tenant Websites (Wildcard + Custom domains) ─────────────────
server {
    listen 443 ssl http2;
    server_name *.websitehub.vn websitehub.vn;

    ssl_certificate     /etc/letsencrypt/live/websitehub.vn/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/websitehub.vn/privkey.pem;

    # Next.js static files - aggressive cache
    location /_next/static/ {
        proxy_pass http://web_frontend;
        proxy_cache_valid 200 7d;
        add_header Cache-Control "public, max-age=604800, immutable";
    }

    # Main app
    location / {
        limit_req zone=web burst=50 nodelay;
        proxy_pass         http://web_frontend;
        proxy_http_version 1.1;
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-Host  $host;
        proxy_set_header   X-Forwarded-Proto $scheme;
    }
}

# HTTP → HTTPS redirect
server {
    listen 80;
    server_name _;
    return 301 https://$host$request_uri;
}
```
### 6.3 Flyway Migration
```sql

-- infrastructure/scripts/db/migration/V1__init_extensions.sql
CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";
CREATE EXTENSION IF NOT EXISTS "unaccent";

-- V2__create_core_tables.sql
-- (Toàn bộ CREATE TABLE statements từ phần III Database Design)

-- V3__seed_verticals.sql
INSERT INTO verticals (id, code, name, display_order) VALUES
(gen_random_uuid(),'lottery',    'Xổ Số',        1),
(gen_random_uuid(),'realestate', 'Bất Động Sản', 2),
(gen_random_uuid(),'spa',        'Spa & Beauty',  3),
(gen_random_uuid(),'restaurant', 'Nhà Hàng',     4);

-- V4__seed_templates.sql
INSERT INTO templates (id, vertical_id, code, name, layout_config, default_colors, display_order)
VALUES
(gen_random_uuid(),
 (SELECT id FROM verticals WHERE code='lottery'),
 'lottery-classic', 'Classic - Đỏ Vàng',
 '{"layout_type":"sidebar-right","header_style":"sticky",
    "widget_order":["kqxs","lo_gan","soi_cau","blog"],
    "sidebar_widgets":["lo_gan","soi_cau","lich"],
    "homepage_sections":["kqxs","stats","predictions","blog"],
    "features_visible":{"show_live_result":true,"show_statistics":true,
    "show_predictions":true,"show_ticket_checker":true,
    "show_schedule":true,"show_print_button":true,"show_share_buttons":true}}',
 '{"primary":"#E53E3E","secondary":"#2D3748","accent":"#ECC94B",
    "background":"#FFFFFF","text":"#1A202C"}',
 1),
(gen_random_uuid(),
 (SELECT id FROM verticals WHERE code='lottery'),
 'lottery-modern', 'Modern - Navy Blue',
 '{"layout_type":"no-sidebar","header_style":"sticky",
    "widget_order":["hero","kqxs","stats","predictions","blog"],
    "sidebar_widgets":[],
    "homepage_sections":["hero","kqxs","stats","predictions","blog"],
    "features_visible":{"show_live_result":true,"show_statistics":true,
    "show_predictions":true,"show_ticket_checker":true,
    "show_schedule":true,"show_print_button":false,"show_share_buttons":true}}',
 '{"primary":"#3B82F6","secondary":"#0F172A","accent":"#F59E0B",
    "background":"#0F172A","text":"#F1F5F9"}',
 2);

-- V5__seed_roles_permissions.sql
-- (Toàn bộ INSERT INTO roles, permissions, role_permissions từ phần III)

-- V6__seed_system_configs.sql
INSERT INTO system_configs (key, value, value_type, description) VALUES
('platform.name',               'WebsiteHub',    'string',  'Platform name'),
('platform.domain',             'websitehub.vn', 'string',  'Platform domain'),
('hosting.grace_period_days',   '5',             'number',  'Grace period before suspend'),
('hosting.reminder_days_before','7',             'number',  'Days before expiry to remind'),
('lottery.fetch_retry_max',     '3',             'number',  'Max fetch retries'),
('lottery.cache_ttl_seconds',   '120',           'number',  'KQXS cache TTL');

-- V7__seed_lottery_provinces.sql
-- (Toàn bộ INSERT INTO lottery_regions, lottery_provinces)
```
## 7. .ENV TEMPLATE
```bash

# .env.example - Copy thành .env và điền giá trị thực

# ════════════════════════════════════════
# DATABASE
# ════════════════════════════════════════
DB_PASSWORD=your_secure_db_password

# ════════════════════════════════════════
# REDIS
# ════════════════════════════════════════
REDIS_PASSWORD=your_secure_redis_password

# ════════════════════════════════════════
# PLATFORM
# ════════════════════════════════════════
PLATFORM_DOMAIN=websitehub.vn
JWT_SECRET=your-256-bit-secret-minimum-32-chars-long!!

# ════════════════════════════════════════
# STORAGE (Cloudflare R2)
# ════════════════════════════════════════
R2_ACCESS_KEY=your_r2_access_key
R2_SECRET_KEY=your_r2_secret_key
R2_BUCKET=websitehub-media
R2_ENDPOINT=https://ACCOUNT_ID.r2.cloudflarestorage.com
R2_PUBLIC_URL=https://media.websitehub.vn

# ════════════════════════════════════════
# EMAIL (Resend)
# ════════════════════════════════════════
RESEND_API_KEY=re_your_resend_api_key

# ════════════════════════════════════════
# TELEGRAM (Alert notifications)
# ════════════════════════════════════════
TELEGRAM_BOT_TOKEN=your_bot_token
TELEGRAM_CHAT_ID=your_chat_id

# ════════════════════════════════════════
# FRONTEND
# ════════════════════════════════════════
NEXT_PUBLIC_API_URL=https://api.websitehub.vn
NEXT_PUBLIC_WS_URL=wss://api.websitehub.vn
NEXT_PUBLIC_PLATFORM_DOMAIN=websitehub.vn
```
## 8. QUICK START
```bash

#!/bin/bash
# infrastructure/scripts/quickstart.sh

echo "🚀 Starting WebsiteHub Platform..."

# 1. Copy env
cp .env.example .env
echo "⚙️  Edit .env with your values, then press ENTER"
read

# 2. Start services
docker-compose up -d postgres redis
echo "⏳ Waiting for DB to be ready..."
sleep 10

# 3. Start API (Flyway runs migrations automatically)
docker-compose up -d api
echo "⏳ Waiting for API to start..."
sleep 15

# 4. Seed super admin
curl -s -X POST http://localhost:8080/v1/platform/setup \
  -H "Content-Type: application/json" \
  -d '{
    "email":    "admin@websitehub.vn",
    "password": "Admin@123456",
    "fullName": "Super Admin",
    "setupKey": "INITIAL_SETUP_2024"
  }'

# 5. Start frontends
docker-compose up -d web admin

# 6. Start Nginx
docker-compose up -d nginx

echo ""
echo "✅ Platform is ready!"
echo ""
echo "🌐 Platform Admin: https://admin.websitehub.vn"
echo "🔑 Login: admin@websitehub.vn / Admin@123456"
echo "📚 API Docs: https://api.websitehub.vn/swagger-ui.html"
echo ""
echo "⚠️  Remember to change the admin password!"