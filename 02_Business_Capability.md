# HD Platform
## Business Capability

---

Document Information

| Item | Value |
|------|------|
| Version | 1.0 |
| Status | Draft |
| Owner | Product Owner |
| Reviewer | Solution Architect |

---

# 1. Purpose

Business Capability mô tả những năng lực (capabilities) mà HD Platform phải cung cấp để đáp ứng mục tiêu kinh doanh.

Capability không phụ thuộc công nghệ, framework hay ngôn ngữ lập trình.

Đây là cơ sở để:

- Thiết kế Domain
- Thiết kế Database
- Thiết kế Package
- Thiết kế API
- Chia module

---

# 2. Capability Map

HD Platform

├── Platform Core

├── Identity

├── Tenant

├── CMS

├── Lottery

├── Statistics

├── Reward

├── Dealer

├── Customer

├── Notification

├── Analytics

├── Report

└── Integration

---

# 3. Platform Core

Mục tiêu

Cung cấp các dịch vụ dùng chung.

Capabilities

- Configuration
- Audit Log
- Scheduler
- File Storage
- Cache
- Dictionary
- Lookup Data

Future

- Workflow Engine
- Rule Engine

---

# 4. Identity

Capabilities

- Login
- Logout
- JWT
- Refresh Token
- RBAC
- Permission
- User Management
- Session
- MFA (Future)

---

# 5. Tenant

Capabilities

- Create Tenant
- Suspend Tenant
- Activate Tenant
- Tenant Theme
- Domain
- Domain Alias
- Tenant Settings

---

# 6. CMS

Capabilities

- Banner
- Menu
- News
- Page
- Category
- Media
- SEO
- Contact
- FAQ

Future

- Landing Page Builder

---

# 7. Lottery

Capabilities

- Region
- Province
- Draw Schedule
- Draw Session
- Prize Type
- Lottery Result
- Live Result
- Result History

---

# 8. Statistics

Capabilities

- Frequency
- Missing Number
- Cycle
- Head Tail
- Pair Analysis
- Monthly Statistics
- Yearly Statistics

Future

- AI Analysis

---

# 9. Reward

Capabilities

- Reward Request
- Upload Ticket
- Upload Image
- Approval
- Reject
- Payment Tracking

---

# 10. Dealer

Capabilities

- Branch
- Staff
- Wallet
- Commission
- Performance
- Customer Management

---

# 11. Customer

Capabilities

- Ticket Checker
- Reward History
- Favorite Numbers
- Notifications

---

# 12. Notification

Capabilities

- Email
- SMS
- Push Notification
- Zalo
- In-App Notification

---

# 13. Analytics

Capabilities

- Dashboard
- Traffic
- User Statistics
- Revenue
- SEO Report

---

# 14. Report

Capabilities

- Lottery Report
- Reward Report
- Customer Report
- Revenue Report
- Dealer Report

---

# 15. Integration

Capabilities

- Lottery API
- Payment Gateway (Future)
- SMS Gateway
- Email Provider
- Cloud Storage

---

# 16. Capability Dependency

Platform Core

↓

Identity

↓

Tenant

↓

CMS

↓

Lottery

↓

Reward

↓

Dealer

↓

Customer

↓

Analytics

---

# 17. Architecture Impact

Business Capability sẽ quyết định:

- Package Structure
- Database Module
- API Group
- Permission
- Microservice Boundary

---

# 18. Implementation Impact

Sau này Spring Boot sẽ có package:

platform

identity

tenant

cms

lottery

statistics

reward

dealer

customer

notification

analytics

report

integration

---

# 19. Review Checklist

□ Có Capability nào bị trùng?

□ Có Capability nào quá lớn?

□ Capability có độc lập không?

□ Có thể tách Microservice không?

□ Có thể tái sử dụng không?

---

# 20. Next Document

03_Domain_Map.md