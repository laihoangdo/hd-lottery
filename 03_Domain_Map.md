# HD Platform
## Domain Map

---

# Document Information

| Item | Value |
|------|------|
| Project | HD Platform |
| Product | HD Lottery |
| Document | Domain Map |
| Version | v1.0 |
| Status | Draft |
| Owner | Chief Architect |

---

# 1. Purpose

Tài liệu này xác định toàn bộ Domain của HD Platform.

Domain là đơn vị thiết kế quan trọng nhất trong hệ thống.

Mỗi Domain sẽ có:

- Business Rules
- Database
- API
- Package
- Permission
- Scheduler
- Events

Mục tiêu:

- Xác định ranh giới nghiệp vụ (Bounded Context).
- Giảm coupling giữa các module.
- Tăng khả năng mở rộng.
- Chuẩn bị cho Microservices trong tương lai.

---

# 2. Platform Landscape

HD Platform

├── Platform Domains
│
├── Identity
├── Tenant
├── Configuration
├── Media
├── CMS
├── Notification
├── Analytics
├── Reporting
├── Integration
│
└── Business Domains
    │
    ├── Lottery
    ├── Statistics
    ├── Reward
    ├── Dealer
    ├── Customer
    └── Vietlott (Future)

---

# 3. Platform Domains

## Identity Domain

Chịu trách nhiệm:

- User
- Login
- JWT
- Permission
- Role
- Session

Không chứa:

- Lottery
- CMS
- Dealer

---

## Tenant Domain

Chịu trách nhiệm:

- Tenant
- Domain
- Theme
- Subscription
- Settings

---

## Configuration Domain

Quản lý:

- Dictionary
- Province
- Region
- Timezone
- Locale

---

## CMS Domain

Quản lý:

- Banner
- News
- Category
- Page
- Menu
- Contact
- SEO

---

## Media Domain

Quản lý:

- Images
- Video
- Upload
- Storage

---

## Notification Domain

Quản lý:

- Email
- SMS
- Push
- Zalo
- In-App Notification

---

## Reporting Domain

Quản lý:

- Dashboard
- Export Excel
- Export PDF
- Revenue Report
- Reward Report

---

## Analytics Domain

Quản lý:

- Traffic
- SEO
- Visitor
- Search
- Performance

---

## Integration Domain

Quản lý:

- Lottery API
- SMS Gateway
- Email Provider
- Payment Gateway
- Cloud Storage

---

# 4. Business Domains

## Lottery Domain

Aggregate

- LotteryResult
- DrawSession
- Prize
- PrizeType
- Province
- Region

Business Rules

- Công bố kết quả
- Quản lý lịch quay
- Quản lý giải thưởng

---

## Statistics Domain

Aggregate

- Frequency
- MissingNumber
- Cycle
- HeadTail
- PairAnalysis

Business Rules

- Thống kê
- Tính toán
- Phân tích dữ liệu

---

## Reward Domain

Aggregate

- RewardRequest
- TicketImage
- RewardApproval
- RewardPayment

Business Rules

- Tiếp nhận yêu cầu
- Kiểm tra vé
- Phê duyệt
- Theo dõi thanh toán

---

## Dealer Domain

Aggregate

- Dealer
- Branch
- Staff
- Wallet
- Commission

Business Rules

- Quản lý đại lý
- Quản lý chi nhánh
- Hoa hồng

---

## Customer Domain

Aggregate

- Customer
- FavoriteNumber
- CheckedTicket
- RewardHistory

Business Rules

- Dò vé
- Lưu lịch sử
- Nhận thông báo

---

# 5. Domain Relationship

Identity
        │
        ▼
Tenant
        │
        ▼
CMS
        │
        ▼
Lottery
        │
 ┌──────┼────────┐
 ▼      ▼        ▼
Reward Statistics Customer
        │
        ▼
Dealer

Notification và Reporting là Shared Domains được sử dụng bởi tất cả Domain.

---

# 6. Bounded Context

| Domain | Context |
|----------|----------|
| Identity | Authentication |
| Tenant | Tenant Management |
| CMS | Content Management |
| Lottery | Lottery Engine |
| Statistics | Statistics Engine |
| Reward | Reward Engine |
| Dealer | Dealer Management |
| Customer | Customer Portal |
| Notification | Messaging |
| Analytics | BI |
| Reporting | Reporting |

---

# 7. Domain Ownership

Mỗi Domain có:

- Database Tables
- API
- Events
- Services
- Scheduler
- Cache
- Permission

Không Domain nào được thao tác trực tiếp Database của Domain khác.

Mọi giao tiếp phải thông qua:

- API
- Domain Service
- Domain Event

---

# 8. Shared Kernel

Các Domain dùng chung:

- User
- Tenant
- Province
- Region
- Dictionary
- Media

Các Domain này chỉ được sửa bởi Platform Team.

---

# 9. Future Expansion

Có thể bổ sung:

- Marketplace Domain
- Booking Domain
- CRM Domain
- ERP Domain
- Real Estate Domain

Không cần thay đổi Platform Core.

---

# 10. Architecture Impact

Document này quyết định:

- Package Structure
- Database Schema
- API Prefix
- Microservice Boundary
- Maven Modules
- Source Code Layout

---

# 11. Implementation Impact

Sau này Spring Boot sẽ có package:

com.hdplatform.identity

com.hdplatform.tenant

com.hdplatform.cms

com.hdplatform.media

com.hdplatform.notification

com.hdplatform.lottery

com.hdplatform.statistics

com.hdplatform.reward

com.hdplatform.dealer

com.hdplatform.customer

com.hdplatform.analytics

com.hdplatform.reporting

---

# 12. Review Checklist

☐ Domain có độc lập?

☐ Aggregate có hợp lý?

☐ Có vòng phụ thuộc?

☐ Có Domain quá lớn?

☐ Có thể tách Microservice?

☐ Có tái sử dụng được?

---

# 13. Next Document

04_Bounded_Context.md