# HD Lottery Platform

> Enterprise Multi-Tenant SaaS Platform for Lottery Dealers

---

## Document Information

| Item         | Value                |
| ------------ | -------------------- |
| Project      | HD Lottery Platform  |
| Version      | v0.1 Draft           |
| Status       | Draft                |
| Author       | HD Architecture Team |
| Last Updated | 2026-06-29           |

---

# 1. Introduction

HD Lottery Platform là nền tảng SaaS (Software as a Service) được xây dựng nhằm cung cấp giải pháp tạo và quản lý website đại lý vé số chuyên nghiệp.

Khác với việc xây dựng một website đơn lẻ, hệ thống được thiết kế theo kiến trúc Multi-Tenant, cho phép nhiều đại lý cùng sử dụng một nền tảng chung nhưng vẫn có website, dữ liệu và cấu hình riêng.

Mục tiêu của dự án không chỉ là hiển thị kết quả xổ số mà còn trở thành nền tảng quản lý toàn diện cho đại lý vé số.

---

# 2. Vision

Trở thành nền tảng SaaS hàng đầu dành cho các đại lý vé số tại Việt Nam, giúp việc xây dựng và vận hành website trở nên nhanh chóng, đơn giản và chuyên nghiệp.

---

# 3. Mission

* Chuẩn hóa quy trình vận hành đại lý vé số.
* Cung cấp nền tảng công nghệ hiện đại.
* Giảm chi phí xây dựng website.
* Cho phép triển khai website mới trong vài phút.
* Dễ dàng mở rộng và bảo trì.

---

# 4. Business Goal

Các mục tiêu kinh doanh chính:

* Cung cấp dịch vụ SaaS theo tháng/năm.
* Hỗ trợ hàng trăm đại lý trên cùng nền tảng.
* Xây dựng hệ sinh thái quản lý đại lý vé số.
* Tăng doanh thu thông qua các gói dịch vụ mở rộng.

---

# 5. Product Goal

Phiên bản đầu tiên (MVP) cần đáp ứng:

* Website đại lý vé số.
* Kết quả xổ số realtime.
* Dò vé số.
* Đổi thưởng.
* CMS.
* Quản trị đại lý.
* Quản lý nhiều Tenant.

Các phiên bản tiếp theo sẽ bổ sung:

* Mobile App.
* AI Statistics.
* Notification.
* Wallet.
* Commission.
* CRM.

---

# 6. Target Customers

## Primary Customers

* Đại lý vé số cấp 1.
* Đại lý vé số cấp 2.
* Chuỗi đại lý vé số.

## Secondary Customers

* Khách hàng dò vé.
* Người đổi thưởng.
* Người xem kết quả xổ số.

---

# 7. Product Scope

Bao gồm:

* Multi-Tenant Platform.
* Lottery Engine.
* Statistics Engine.
* CMS.
* Dealer Management.
* Customer Portal.
* Admin Portal.
* Reporting.
* Notification.

Không bao gồm trong Version 1:

* Thanh toán trực tuyến.
* Bán vé số online.
* AI dự đoán kết quả.
* ERP Integration.

---

# 8. Core Principles

Toàn bộ hệ thống phải tuân thủ các nguyên tắc:

* Single Codebase.
* Multi-Tenant.
* API First.
* Modular Monolith.
* Domain Driven Design.
* Security First.
* Cloud Ready.
* Mobile Ready.

---

# 9. High Level Modules

Platform được chia thành các module:

* Authentication
* Tenant Management
* Lottery Engine
* Statistics Engine
* CMS
* Dealer Management
* Customer Portal
* Admin Portal
* Notification
* Reporting
* Media Library

Mỗi module có thể phát triển độc lập nhưng vẫn hoạt động trên cùng một nền tảng.

---

# 10. Technology Stack

Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* PostgreSQL
* Redis
* Flyway
* WebSocket

Frontend

* Next.js
* TypeScript
* TailwindCSS
* ShadCN UI

Infrastructure

* Docker
* Nginx
* GitHub Actions
* Ubuntu Server

---

# 11. Development Methodology

Dự án được phát triển theo các giai đoạn:

1. Product Discovery
2. Business Analysis
3. Architecture Design
4. Database Design
5. API Design
6. Backend Development
7. Frontend Development
8. Testing
9. Deployment
10. Production

Không triển khai code trước khi tài liệu thiết kế được phê duyệt.

---

# 12. Documentation Structure

Tài liệu của dự án được tổ chức theo từng giai đoạn:

* Product Discovery
* Business Requirements
* Architecture
* ADR
* Domain Model
* Database
* API
* Backend
* Frontend
* Deployment
* Monitoring
* Production

Mỗi tài liệu đều có Version, Status và Checklist review.

---

# 13. Success Criteria

Một phiên bản được xem là thành công khi:

* Hỗ trợ nhiều Tenant trên cùng một hệ thống.
* Có thể tạo website đại lý mới mà không cần deploy lại.
* Hỗ trợ realtime kết quả xổ số.
* Có kiến trúc mở rộng được lên Microservices trong tương lai.
* Đạt hiệu năng và tính ổn định phù hợp cho môi trường Production.

---

# 14. Project Roadmap

Phase 0 – Product Discovery

Phase 1 – Architecture

Phase 2 – Database

Phase 3 – Backend

Phase 4 – Frontend

Phase 5 – Testing

Phase 6 – Deployment

Phase 7 – Production

---

# 15. Next Document

Sau khi README được phê duyệt sẽ tiếp tục:

`00_Project_Vision.md`

Tài liệu này sẽ mô tả chi tiết tầm nhìn kinh doanh, chiến lược sản phẩm và định hướng phát triển trong 3–5 năm tới.
