/**
 * Tenant Module.
 *
 * <p>
 * This module contains all business capabilities related to multi-tenant
 * management.
 * </p>
 *
 * <p>
 * The Tenant module follows:
 * </p>
 *
 * <ul>
 *     <li>Domain-Driven Design (DDD)</li>
 *     <li>Hexagonal Architecture</li>
 *     <li>Rich Domain Model</li>
 * </ul>
 *
 * <p>
 * Dependency Rule:
 * </p>
 *
 * <pre>
 * Adapter
 *     ↓
 * Application
 *     ↓
 * Domain
 * </pre>
 *
 * <p>
 * Domain never depends on Spring Framework,
 * JPA or Infrastructure.
 * </p>
 */
package com.hdplatform.modules.tenant;