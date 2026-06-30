# Tenant Module

## Overview

The Tenant module is the core of HD Platform's multi-tenant architecture.

A Tenant represents a lottery agency (dealer) operating one or more websites
under the HD Platform ecosystem.

Examples:

- xosophuongnghi.com.vn
- xosomientrung.vn
- vesomiennam.vn

Each tenant owns:

- Website configuration
- Theme
- Logo
- Hotline
- Lottery settings
- Dealer information
- CMS contents
- SEO configuration

---

## Responsibilities

- Create Tenant
- Update Tenant
- Activate / Deactivate Tenant
- Find Tenant by Domain
- Find Tenant by Site Key
- Validate Tenant

---

## Package Structure

tenant

├── application
├── domain
├── adapter
└── bootstrap

---

## Design Principles

- DDD
- Hexagonal Architecture
- Rich Domain Model
- Domain Events
- Aggregate Root
- Value Objects

---

## Aggregate Root

Tenant

---

## Value Objects

TenantId

SiteKey

DomainName

Timezone

Locale

Logo

Hotline

TenantStatus

---

## Notes

The Domain layer MUST NOT depend on:

- Spring
- JPA
- Hibernate
- REST
- Database

Only Java.