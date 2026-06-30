# HD Platform
## Project Vision

---
Document Information

| Item | Value |
|------|------|
| Document | Project Vision |
| Project | HD Platform |
| Product | HD Lottery |
| Version | 1.0 |
| Status | Approved |
| Owner | Product Owner |
| Architect | Chief Software Architect |

---

# 1. Vision Statement

Xây dựng **HD Platform** trở thành nền tảng SaaS đa tenant hàng đầu tại Việt Nam, giúp doanh nghiệp vừa và nhỏ triển khai các hệ thống số nhanh chóng, dễ mở rộng và có chi phí hợp lý.

Sản phẩm đầu tiên trên nền tảng là **HD Lottery**, phục vụ các đại lý vé số với website chuyên nghiệp, hệ thống quản trị hiện đại và các công cụ phân tích dữ liệu.

---

# 2. Mission

HD Platform hướng tới việc cung cấp một nền tảng công nghệ thống nhất để doanh nghiệp không phải xây dựng lại hệ thống từ đầu cho mỗi lĩnh vực kinh doanh.

Các module dùng chung như:

- Authentication
- Multi Tenant
- CMS
- Media
- Notification
- Reporting
- Analytics

được phát triển một lần và tái sử dụng cho nhiều sản phẩm.

---

# 3. Long-term Vision

Trong 5 năm tới, HD Platform sẽ trở thành nền tảng có thể triển khai nhiều sản phẩm SaaS khác nhau trên cùng một Core Platform.

Ví dụ:

HD Lottery

HD Marketplace

HD Booking

HD Real Estate

HD CRM

HD ERP

---

# 4. Core Philosophy

Chỉ xây dựng một lần.

Tái sử dụng nhiều lần.

Mọi Business Module đều sử dụng chung Platform Core.

---

# 5. Core Values

## Reusability

Module phải có khả năng tái sử dụng.

---

## Scalability

Có thể mở rộng từ vài Tenant lên hàng nghìn Tenant.

---

## Security

Bảo mật là yêu cầu bắt buộc ngay từ khi thiết kế.

---

## Performance

Thiết kế hướng tới hiệu năng cao, sử dụng cache, queue và tối ưu cơ sở dữ liệu.

---

## Simplicity

Kiến trúc rõ ràng, dễ bảo trì và dễ đào tạo đội ngũ phát triển.

---

# 6. Business Opportunity

Thị trường hiện nay có nhiều đại lý vé số nhưng phần lớn:

- Chưa có website.
- Website cũ, khó quản trị.
- Không có CMS.
- Không hỗ trợ Multi Tenant.
- Không có hệ thống quản lý khách hàng.
- Không có báo cáo tập trung.

HD Lottery giải quyết các vấn đề này bằng một nền tảng thống nhất.

---

# 7. Product Positioning

HD Lottery không chỉ là website hiển thị kết quả xổ số.

Đây là nền tảng quản lý và vận hành dành cho đại lý vé số.

---

# 8. Product Principles

Mọi tính năng mới phải trả lời được ba câu hỏi:

1. Có tạo giá trị cho người dùng không?
2. Có tái sử dụng được không?
3. Có ảnh hưởng đến khả năng mở rộng của Platform không?

Nếu câu trả lời là "không", cần xem xét lại trước khi triển khai.

---

# 9. Strategic Goals

## Giai đoạn 1

Hoàn thành HD Lottery MVP.

## Giai đoạn 2

Mở rộng số lượng Tenant.

## Giai đoạn 3

Triển khai Mobile App.

## Giai đoạn 4

Bổ sung AI Analytics.

## Giai đoạn 5

Phát triển thêm các Business Module khác trên HD Platform.

---

# 10. Definition of Success

Dự án được xem là thành công khi:

- Một đại lý mới có thể tạo website trong dưới 5 phút.
- Một codebase phục vụ nhiều Tenant.
- Có thể bổ sung Business Module mới mà không ảnh hưởng Platform Core.
- Kiến trúc đủ linh hoạt để phát triển trong nhiều năm.

---

# 11. Architecture Direction

HD Platform sẽ được xây dựng theo kiến trúc Modular Monolith trước.

Khi quy mô tăng trưởng, từng module có thể tách thành Microservices mà không phải viết lại toàn bộ hệ thống.

---

# 12. ADR-001

**Decision**

HD Platform là Core Platform.

HD Lottery là Business Product đầu tiên.

**Reason**

- Tái sử dụng code.
- Giảm chi phí phát triển.
- Dễ mở rộng.
- Hỗ trợ nhiều ngành nghề.

**Status**

Approved.

---

# 13. Next Document

01_Business_Model.md