markdown

# 🏗️ ENTERPRISE SAAS PLATFORM - COMPLETE TECHNICAL BLUEPRINT
## Multi-Tenant | Multi-Vertical | Multi-Domain
### Version 1.0 — Full Analysis Document

---

# PHẦN I: PHÂN TÍCH THỊ TRƯỜNG & SẢN PHẨM

---

## 1. VISION & POSITIONING

### 1.1 Tầm nhìn sản phẩm

Xây dựng một **White-Label SaaS Platform** tương tự mô hình của:
- **Shopify** → Cung cấp website thương mại điện tử cho mọi merchant
- **Wix/Squarespace** → Website builder cho mọi ngành
- **Ghost.org** → Blogging platform multi-tenant
- **Vercel/Netlify** → Custom domain per project

Nhưng **khác biệt** ở chỗ: **Vertical-specific features** — 
Thay vì generic website builder, platform cung cấp 
website **chuyên sâu theo ngành** (xổ số, BĐS, spa, nhà hàng...)
với đầy đủ tính năng nghiệp vụ của từng ngành.

### 1.2 Định vị cạnh tranh

```

                Generic (Wix/WordPress)
                       │
          LOW ─────────┼───────── HIGH  
       VERTICAL        │         VERTICAL
       SPECIFIC        │         SPECIFIC
                       │
──────────────────[OUR PLATFORM]──────────────
                       │
                Custom Dev Agency
```                
```


| Tiêu chí | WordPress | Wix | Custom Dev | **Our Platform** |
|----------|-----------|-----|------------|-----------------|
| Thời gian go-live | 1-4 tuần | 1-3 ngày | 2-6 tháng | **< 1 giờ** |
| Chi phí setup | 5-50tr | Free-3tr/năm | 50-200tr | **0đ setup** |
| Tính năng ngành | Plugin (phức tạp) | Không có | Có | **Built-in** |
| Tự động hóa data | Không | Không | Tùy | **Có sẵn** |
| SEO chuyên ngành | Manual | Cơ bản | Tùy | **Auto** |
| Multi-site mgmt | Không | Không | Không | **Có** |
```
---

## 2. PHÂN TÍCH CHI TIẾT WEBSITE XỔ SỐ

### 2.1 Phân tích toàn diện tính năng (dựa trên top 20 website xổ số VN)

#### NHÓM A: Kết quả xổ số (Core Feature)

```
A1. KQXS Hôm nay
├── Hiển thị theo 3 miền: Bắc / Trung / Nam
├── Mỗi miền có tab riêng, active theo giờ quay
├── Bảng kết quả:
│   ├── Miền Bắc (1 đài/ngày):
│   │   ├── Giải ĐB:  XXXXX (5 số, highlight đỏ to)
│   │   ├── Giải Nhất: XXXXX
│   │   ├── Giải Nhì:  XXXXX XXXXX
│   │   ├── Giải Ba:   XXXXX XXXXX XXXXX XXXXX XXXXX XXXXX
│   │   ├── Giải Tư:   XXXX XXXX XXXX XXXX
│   │   ├── Giải Năm:  XXXX XXXX XXXX XXXX XXXX XXXX
│   │   ├── Giải Sáu:  XXX XXX XXX
│   │   ├── Giải Bảy:  XX XX XX XX
│   │   └── 2 số cuối tổng hợp tất cả (Loto)
│   ├── Miền Nam/Trung (nhiều đài/ngày):
│   │   ├── Giải Tám:    XX
│   │   ├── Giải Bảy:    XXX
│   │   ├── Giải Sáu:    XXXX XXXX XXX
│   │   ├── Giải Năm:    XXXXX
│   │   ├── Giải Tư:     XXXXX XXXXX XXXXX XXXXX XXXXX XXXXX XXXXX
│   │   ├── Giải Ba:     XXXXX XXXXX
│   │   ├── Giải Nhì:    XXXXX
│   │   ├── Giải Nhất:   XXXXX
│   │   └── Giải ĐB:     XXXXXX (6 số)
│   └── Trạng thái: "Đang quay..." / "Đã có kết quả" / "Chưa đến giờ"
│
├── Real-time update (WebSocket / SSE khi đang trong giờ quay)
├── Highlight số đặc biệt
├── Tô màu theo giải
└── Nút in / chia sẻ / copy
A2. KQXS Theo ngày (Archive)
├── URL pattern: /ket-qua-xo-so/{yyyy-mm-dd}
├── Date picker với disable ngày tương lai
├── Breadcrumb: Trang chủ > KQXS > {Ngày}
├── SEO title tự động: "KQXS ngày {dd/mm/yyyy} - Kết quả XS hôm nay"
└── Pagination sang ngày trước/sau
A3. KQXS Theo đài
├── URL: /ket-qua/{ten-dai-slug}
│   Ví dụ: /ket-qua/mien-bac, /ket-qua/tp-hcm, /ket-qua/da-nang
├── Lịch sử 30 ngày gần nhất
├── Bảng tổng hợp compact (xem nhiều ngày cùng lúc)
└── Link chi tiết từng ngày
A4. Live Result (Trực tiếp)
├── Countdown đến giờ quay
├── Hiển thị từng giải khi có kết quả (real-time)
├── Animation khi số xuất hiện
└── Sound notification (optional)
```


#### NHÓM B: Thống kê & Phân tích
```
B1. Thống kê tần suất Lô Tô
├── Bảng 10x10 hiển thị số 00-99
├── Color heatmap: xanh (ra nhiều) → đỏ (ra ít/gan)
├── Bộ lọc:
│   ├── Đài: Tất cả / Miền Bắc / Từng đài cụ thể
│   ├── Kỳ: 10 / 20 / 30 / 60 / 90 / 180 kỳ
│   └── Loại: Tần suất / Ngày gan / Lần cuối xuất hiện
├── Xuất Excel/PDF
└── So sánh 2 kỳ (advanced)
B2. Lô Gan (Số lâu chưa ra)
├── Danh sách số theo thứ tự gan giảm dần
├── Thông tin: Số, Số ngày gan, Ngày cuối xuất hiện, TB chu kỳ
├── Phân loại:
│   ├── Siêu gan (> 30 ngày)
│   ├── Gan (15-30 ngày)

│   └── Bình thường (< 15 ngày)
└── Biểu đồ lịch sử xuất hiện của từng số
B3. Cặp số / Lô kép
├── Thống kê lô kép (00, 11, 22...99) ra theo mùa
├── Cặp số hay ra cùng nhau
└── Phân tích chu kỳ
B4. Đầu - Đuôi thống kê

├── Tần suất theo đầu số (0x đến 9x)
├── Tần suất theo đuôi số (x0 đến x9)
├── Xu hướng: đầu/đuôi đang "hot"
└── Biểu đồ cột interactive
B5. Giải Đặc biệt thống kê
├── Lịch sử giải ĐB n kỳ gần nhất
├── Tần suất đầu số của giải ĐB
├── Khoảng cách trung bình giữa 2 lần ra cùng 1 số
├── Record gan dài nhất
└── Biểu đồ xu hướng
B6. Thống kê theo tháng / năm
├── Số ra nhiều nhất tháng X
├── So sánh các tháng
└── Export báo cáo
B7. Bảng tổng hợp KQXS (Compact View)
├── Hiển thị nhiều đài nhiều ngày trong 1 màn hình
├── Filter theo đài, theo tháng
└── Print-friendly layout
```


#### NHÓM C: Soi Cầu & Dự Đoán
```
C1. Soi cầu hệ thống (Algorithm-based)
├── Bạch thủ lô (1 số)
│   ├── Phương pháp 1: Dựa theo giải ĐB hôm trước
│   ├── Phương pháp 2: Số gan sắp về theo chu kỳ
│   └── Phương pháp 3: Pattern recognition
├── Lô xiên 2 (2 số)
├── Lô xiên 3 (3 số)
├── 3 càng (3 số cuối giải ĐB)
└── Độ tin cậy dựa trên backtesting
C2. Cầu lô đẹp hôm nay
├── Admin/Editor tạo bài nhận định thủ công
├── Cấu trúc bài:
│   ├── Tên tác giả (avatar, nickname)
│   ├── Con số gợi ý
│   ├── Phân tích lý do (rich text)
│   ├── Ngày áp dụng
│   └── Mức độ tự tin (1-5 sao)
├── Sau khi có KQXS: Auto check và hiển thị kết quả (✅/❌)
├── Tỷ lệ thắng của từng tác giả (statistics)
└── Comment/Rating từ user
C3. Cầu theo chu kỳ
├── Phát hiện pattern: số X hay ra vào ngày thứ Y hàng tuần
├── Pattern "song thủ": số A ra thì số B ra theo
└── Chu kỳ ngày chẵn/lẻ
C4. Cầu VIP (Premium Content)
├── Locked content → yêu cầu đăng ký/mua
├── Tenant có thể bán subscription
└── Watermark trên preview
```


#### NHÓM D: Công cụ Tiện ích
```
D1. Tra cứu vé số
├── Form:
│   ├── Chọn đài
│   ├── Chọn ngày quay
│   └── Nhập số vé (6-8 chữ số)
├── Kết quả: Trúng giải X - Giá trị Y đồng
├── Hỗ trợ kiểm tra nhiều vé cùng lúc
└── Chia sẻ kết quả
D2. In kết quả xổ số
├── Trang in chuyên dụng (print stylesheet)
├── Chọn đài, chọn ngày
├── Format A4 chuẩn
└── PDF export
D3. Lịch xổ số
├── Calendar view (tháng/tuần)
├── Từng ngày hiển thị: đài nào quay, giờ quay
├── Click vào ngày → KQXS ngày đó
├── Upcoming: countdown đến lần quay tiếp
└── Thêm vào Google Calendar / iCal
D4. Bảng lô tô kép / Đề về
├── Nếu lô XX về → đề thường về con gì
├── Bảng tra cứu nhanh
└── Dựa trên thống kê lịch sử
D5. Calculator (Tính tiền thưởng)
├── Nhập: Loại cược, số tiền đặt
├── Output: Tiền thưởng nếu trúng
└── Các loại cược: Đề, Lô, 3 càng, Xiên...
D6. Tìm kiếm số
├── Nhập số muốn tìm
├── Lịch sử xuất hiện theo thời gian
├── Trong đài nào, ngày nào
└── Tần suất xuất hiện
```


#### NHÓM E: Nội dung & SEO
```
E1. Blog / Tin tức
├── Bài viết xổ số hôm nay (daily post - auto generate template)
├── Tin tức giải thưởng lớn
├── Kinh nghiệm chơi xổ số
├── Hướng dẫn soi cầu
└── Liên kết KQXS → Bài viết liên quan
E2. Trang landing theo đài (SEO Pages)
├── /xo-so-mien-bac → Landing page XSMB
├── /xo-so-tp-hcm → Landing page XSHCM
├── Mỗi trang: Giới thiệu đài, lịch sử, KQXS mới nhất, thống kê
└── Auto-update content
E3. Trang landing theo từ khóa
├── /ket-qua-xo-so-hom-nay
├── /soi-cau-bach-thu-lo-mien-bac-hom-nay
├── /thong-ke-tan-suat-lo-to-mien-bac
└── SEO schema: FAQPage, HowTo, Article
E4. Internal linking system
├── Auto link từ bài viết → KQXS liên quan
├── Related posts dựa trên tags
└── Breadcrumb tự động
```


#### NHÓM F: Quảng cáo & Monetization
```
F1. Ad Management
├── Google AdSense integration (inject code)
├── Custom banner ads
├── Native ad placements
└── Affiliate links tracking
F2. Widget system
├── KQXS widget nhúng vào site khác
├── Lô gan widget
└── Countdown widget
F3. Membership (Optional per tenant)
├── Free / VIP membership
├── VIP: Xem cầu VIP, không quảng cáo
└── Payment integration
```


### 2.2 Phân tích UX Flow chi tiết
```
User Journey - Người dùng thông thường:
[Google Search "KQXS hôm nay"]
↓
[Landing Page - SEO optimized]
↓
[Xem KQXS hôm nay - Tab Miền Bắc/Trung/Nam]
↓
[Cuộn xuống xem Thống kê nhanh / Lô gan]
↓
[Click vào "Soi cầu hôm nay"]
↓
[Đọc nhận định, xem số gợi ý]
↓
[Bookmark / Subscribe notification]
↓
[Return tomorrow]
Conversion Funnel:
Visitor → Regular Reader → Subscriber → VIP Member
```


---

# PHẦN II: KIẾN TRÚC PLATFORM

---

## 3. MULTI-TENANT ARCHITECTURE - PHÂN TÍCH SÂU

### 3.1 Ba mô hình Multi-tenancy phổ biến
```
MODEL 1: Separate Database per Tenant
┌─────────┐   ┌─────────┐   ┌─────────┐
│ Tenant A│   │ Tenant B│   │ Tenant C│
│   DB_A  │   │   DB_B  │   │   DB_C  │
└─────────┘   └─────────┘   └─────────┘
✅ Isolation tuyệt đối, performance độc lập
✅ Dễ migrate, backup từng tenant
❌ Chi phí hạ tầng cao
❌ Khó quản lý khi có 1000+ tenants
→ Phù hợp: Enterprise SaaS (Salesforce, ServiceNow)
MODEL 2: Separate Schema per Tenant (PostgreSQL)
┌─────────────────────────────────┐
│         Single Database         │
│  ┌────────┐  ┌────────┐         │
│  │Schema A│  │Schema B│  ...    │
│  └────────┘  └────────┘         │
└─────────────────────────────────┘
✅ Isolation tốt hơn shared tables
✅ Dễ backup/restore từng tenant
❌ Connection pooling phức tạp
❌ PostgreSQL max ~10,000 schemas thực tế
→ Phù hợp: Mid-size SaaS (100-1000 tenants)
MODEL 3: Shared Database, Shared Schema (tenant_id)
┌─────────────────────────────────┐
│         Single Database         │
│  ┌──────────────────────────┐   │
│  │ articles (tenant_id FK)  │   │
│  │ settings (tenant_id FK)  │   │
│  └──────────────────────────┘   │
└─────────────────────────────────┘
✅ Chi phí thấp nhất
✅ Dễ scale, quản lý centralized
❌ Risk data leak nếu query sai
❌ "Noisy neighbor" problem
→ Phù hợp: Startup SaaS, 0-10,000 tenants
═══════════════════════════════════════
OUR CHOICE: HYBRID MODEL (Model 3 + 2)
═══════════════════════════════════════
Phase 1 (0-500 tenants): Model 3
→ Tất cả shared table với tenant_id
→ Row-level security (PostgreSQL RLS)
Phase 2 (500+ tenants hoặc Enterprise plan):
→ Migrate tenant sang Schema riêng
→ Schema name = tenant_slug
→ Connection pooling với PgBouncer
Phase 3 (1000+ tenants, lớn):
→ Database sharding theo region
→ Tenant có thể chọn region (VN/SG/US)
```


### 3.2 Row Level Security (RLS) - PostgreSQL

```sql
-- Bật RLS cho tất cả tenant tables
ALTER TABLE articles ENABLE ROW LEVEL SECURITY;
ALTER TABLE lottery_results ENABLE ROW LEVEL SECURITY;
ALTER TABLE media ENABLE ROW LEVEL SECURITY;

-- Policy: User chỉ thấy data của tenant mình
CREATE POLICY tenant_isolation ON articles
  USING (tenant_id = current_setting('app.current_tenant_id')::UUID);

-- Application set tenant context trước mỗi query
SET app.current_tenant_id = '{tenant_uuid}';
```
```java

// Java - Set tenant context với Spring AOP
@Aspect
@Component
public class TenantContextAspect {
    
    @Around("@annotation(TenantScoped)")
    public Object setTenantContext(ProceedingJoinPoint pjp) throws Throwable {
        String tenantId = TenantContextHolder.getCurrentTenantId();
        
        // Set PostgreSQL session variable
        jdbcTemplate.execute(
            "SET app.current_tenant_id = '" + tenantId + "'"
        );
        
        try {
            return pjp.proceed();
        } finally {
            TenantContextHolder.clear();
        }
    }
}

```
### 3.3 Domain Resolution Architecture


Luồng xử lý domain:

```
Request: xosophuongnghi.com.vn
    ↓
[Cloudflare DNS]
    ↓ CNAME → saas-platform.vn
[Nginx/Caddy Reverse Proxy]
    ↓ Forward Host header
[Spring Boot Application]
    ↓
[DomainResolutionFilter]
    │
    ├── Check: Is it a subdomain of .saas-platform.vn?
    │   └── Extract slug: "phuongnghi"
    │
    └── Check: Is it a custom domain?
        └── Lookup DB: SELECT tenant_id FROM domains 
                       WHERE domain = 'xosophuongnghi.com.vn'
    ↓
[TenantContextHolder.set(tenantId)]
    ↓
[Request proceeds with tenant context]
```
```java

// Spring Boot Filter
@Component
@Order(1)
public class TenantResolutionFilter implements Filter {
    
    private static final String PLATFORM_DOMAIN = "saas-platform.vn";
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
        HttpServletRequest request = (HttpServletRequest) req;
        String host = request.getServerName().toLowerCase();
        
        String tenantId = resolveTenant(host);
        
        if (tenantId == null) {
            ((HttpServletResponse) res).sendError(404, "Tenant not found");
            return;
        }
        
        TenantContextHolder.setCurrentTenantId(tenantId);
        
        try {
            chain.doFilter(req, res);
        } finally {
            TenantContextHolder.clear();
        }
    }
    
    private String resolveTenant(String host) {
        // Case 1: Subdomain (phuongnghi.saas-platform.vn)
        if (host.endsWith("." + PLATFORM_DOMAIN)) {
            String slug = host.replace("." + PLATFORM_DOMAIN, "");
            return tenantCache.getBySlug(slug);
        }
        
        // Case 2: Custom domain
        return domainRepository.findTenantIdByDomain(host);
    }
}

```
### 3.4 SSL Certificate Management
```

Wildcard SSL cho subdomain:
  *.saas-platform.vn → Cloudflare (automatic)
  
Custom Domain SSL (2 approaches):

Approach A - Cloudflare Proxy:
  Tenant CNAME → saas-platform.vn (Cloudflare proxied)
  → Cloudflare tự handle SSL cho custom domain
  → Không cần làm gì thêm ✅
  
Approach B - Let's Encrypt ACME (Self-hosted):
  1. Tenant thêm CNAME record
  2. Platform verify domain ownership
  3. Request cert qua ACME protocol
  4. Cert stored in DB, renewed tự động
  5. Nginx reload với cert mới
  
Recommendation: Dùng Cloudflare SSL for SaaS 
(paid feature ~$10/domain/month hoặc per zone pricing)
```
### 4. MULTI-VERTICAL PLATFORM DESIGN

### 4.1 Plugin/Vertical Architecture
```

Core Platform (Luôn có)
├── Tenant Management
├── User Authentication  
├── Domain Management
├── Media Management
├── Blog/CMS
├── SEO Engine
├── Analytics
├── Billing
└── Admin Dashboard

Vertical Plugins (Cài thêm theo ngành)
├── vertical-lottery/        ← Xổ số
├── vertical-realestate/     ← Bất động sản  
├── vertical-ecommerce/      ← Thương mại điện tử
├── vertical-restaurant/     ← Nhà hàng
├── vertical-spa/            ← Spa & Beauty
├── vertical-hotel/          ← Khách sạn
└── vertical-education/      ← Giáo dục
```
### 4.2 Vertical Plugin Contract (Interface)
```java

// Core interface mỗi vertical phải implement
public interface VerticalPlugin {
    
    // Metadata
    String getVerticalId();           // "lottery", "realestate"
    String getDisplayName();          // "Xổ Số", "Bất Động Sản"
    String getVersion();
    List<String> getRequiredPlans();  // Plans nào được dùng vertical này
    
    // Database
    List<Migration> getMigrations();  // DB migrations của vertical
    
    // API Routes
    List<RouteDefinition> getApiRoutes();
    
    // Frontend Pages
    List<PageDefinition> getPages();
    
    // Scheduled Jobs
    List<ScheduledJob> getScheduledJobs();
    
    // Settings Schema
    JsonSchema getSettingsSchema();   // Cấu hình riêng của vertical
    
    // Hooks into core events
    Map<String, EventHandler> getEventHandlers();
    
    // Widgets for dashboard
    List<DashboardWidget> getDashboardWidgets();
}

// Ví dụ Lottery Vertical
@Component
public class LotteryVertical implements VerticalPlugin {
    
    @Override
    public String getVerticalId() { return "lottery"; }
    
    @Override
    public List<ScheduledJob> getScheduledJobs() {
        return List.of(
            new ScheduledJob("fetch-kqxs-north", "0 10,15 18 * * MON-SUN"),
            new ScheduledJob("fetch-kqxs-south", "0 35 18 * * MON-SUN"),
            new ScheduledJob("calc-frequency-stats", "0 0 20 * * *"),
            new ScheduledJob("generate-seo-pages", "0 30 20 * * *")
        );
    }
    
    @Override
    public Map<String, EventHandler> getEventHandlers() {
        return Map.of(
            "tenant.created", this::onTenantCreated,
            "tenant.plan.upgraded", this::onPlanUpgraded
        );
    }
}
```
### 4.3 Theme/Frontend System
```

Theme Architecture:

base-theme/                    ← Core components, layout
├── components/
│   ├── layout/
│   │   ├── Header.tsx
│   │   ├── Footer.tsx
│   │   └── Sidebar.tsx
│   ├── blog/
│   └── seo/

theme-lottery-default/         ← Lottery theme
├── extends: base-theme
├── pages/
│   ├── home.tsx              ← Override home
│   ├── kqxs/
│   └── thong-ke/
└── components/
    ├── LotteryTable.tsx
    └── FrequencyChart.tsx

theme-lottery-modern/          ← Lottery theme variant 2
theme-realestate-default/      ← BDS theme
theme-ecommerce-default/       ← Ecommerce theme

Tenant chọn theme → Platform render đúng theme
```

# PHẦN III: DATABASE DESIGN CHI TIẾT

## 5. DATABASE SCHEMA - ENTERPRISE LEVEL

### 5.1 Core Schema
```sql


-- ============================================================
-- SCHEMA: core (Platform-wide tables)
-- ============================================================

-- 5.1.1 PLANS & FEATURES
-- ============================================================

CREATE TABLE plans (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(100) NOT NULL,          -- 'Starter', 'Pro', 'Business', 'Enterprise'
    slug            VARCHAR(50) UNIQUE NOT NULL,
    description     TEXT,
    
    -- Pricing
    price_monthly   DECIMAL(12,2) NOT NULL DEFAULT 0,
    price_yearly    DECIMAL(12,2),                  -- Thường giảm 20%
    currency        CHAR(3) DEFAULT 'VND',
    trial_days      INT DEFAULT 14,
    
    -- Limits
    max_articles    INT DEFAULT 50,                 -- -1 = unlimited
    max_media_mb    INT DEFAULT 500,
    max_admin_users INT DEFAULT 1,
    max_pages       INT DEFAULT 10,
    max_ad_slots    INT DEFAULT 2,
    
    -- Feature flags (JSONB để dễ thêm feature mới)
    features        JSONB DEFAULT '{}',
    -- Ví dụ:
    -- {
    --   "custom_domain": true,
    --   "remove_branding": false,
    --   "api_access": false,
    --   "priority_support": false,
    --   "advanced_seo": true,
    --   "analytics": "basic",        -- "basic" | "advanced"
    --   "lottery_vip_predictions": false,
    --   "lottery_api_results": false, -- true = dùng API, false = scraping
    --   "white_label": false,
    --   "custom_css": false,
    --   "verticals": ["lottery"],    -- Verticals được phép dùng
    --   "themes": ["default"]        -- Themes được phép dùng
    -- }
    
    -- Vertical configs
    allowed_verticals TEXT[] DEFAULT '{"lottery"}',
    
    is_active       BOOLEAN DEFAULT TRUE,
    is_public       BOOLEAN DEFAULT TRUE,           -- Hiển thị trên pricing page
    display_order   INT DEFAULT 0,
    
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

-- 5.1.2 TENANTS (Core entity)
-- ============================================================

CREATE TABLE tenants (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    
    -- Identity
    slug            VARCHAR(100) UNIQUE NOT NULL,   -- subdomain: phuongnghi.saas.vn
    name            VARCHAR(255) NOT NULL,           -- Tên hiển thị: "Xổ Số Phương Nghi"
    
    -- Plan & Status
    plan_id         UUID REFERENCES plans(id),
    status          VARCHAR(20) DEFAULT 'trialing'
                    CHECK (tatus IN ('trialing','active','past_due','suspended','cancelled')),

============================================================
-- STATUS CHECK:
-- status IN ('trialing','active','past_due','suspended','cancelled')
-- ============================================================

    -- Vertical được kích hoạt
    active_vertical VARCHAR(50) DEFAULT 'lottery',  -- Vertical đang dùng
    
    -- Theme
    active_theme    VARCHAR(100) DEFAULT 'lottery-default',
    
    -- Timestamps
    trial_ends_at   TIMESTAMPTZ,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ                     -- Soft delete
);

-- Index
CREATE INDEX idx_tenants_slug ON tenants(slug);
CREATE INDEX idx_tenants_status ON tenants(status);
CREATE INDEX idx_tenants_plan_id ON tenants(plan_id);

-- 5.1.3 TENANT SETTINGS (tách riêng để không bloat tenants table)
-- ============================================================

CREATE TABLE tenant_settings (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID UNIQUE REFERENCES tenants(id) ON DELETE CASCADE,
    
    -- Branding
    logo_url        VARCHAR(1000),
    favicon_url     VARCHAR(1000),
    tagline         VARCHAR(500),
    description     TEXT,
    
    -- Theme customization
    primary_color   VARCHAR(7) DEFAULT '#E53E3E',   -- Hex color
    secondary_color VARCHAR(7) DEFAULT '#2D3748',
    accent_color    VARCHAR(7) DEFAULT '#ECC94B',
    font_heading    VARCHAR(100) DEFAULT 'Be Vietnam Pro',
    font_body       VARCHAR(100) DEFAULT 'Inter',
    
    -- Contact info
    phone           VARCHAR(50),
    email           VARCHAR(255),
    address         TEXT,
    
    -- Social links
    social_links    JSONB DEFAULT '{}',
    -- {
    --   "facebook": "https://...",
    --   "youtube": "https://...",
    --   "zalo": "https://...",
    --   "tiktok": "https://..."
    -- }
    
    -- SEO defaults
    seo_title_template  VARCHAR(255) DEFAULT '{page_title} - {site_name}',
    seo_default_title   VARCHAR(255),
    seo_default_desc    VARCHAR(500),
    seo_og_image        VARCHAR(1000),
    
    -- Analytics & Tracking
    google_analytics_id     VARCHAR(50),            -- G-XXXXXXXXXX
    google_tag_manager_id   VARCHAR(50),            -- GTM-XXXXXXX
    facebook_pixel_id       VARCHAR(50),
    google_site_verify      VARCHAR(255),
    
    -- Scripts injection
    header_scripts  TEXT,                           -- Inject vào <head>
    footer_scripts  TEXT,                           -- Inject trước </body>
    custom_css      TEXT,                           -- Custom CSS (Pro+)
    
    -- Features toggle (per tenant override)
    features_override JSONB DEFAULT '{}',
    
    -- Localization
    timezone        VARCHAR(50) DEFAULT 'Asia/Ho_Chi_Minh',
    locale          VARCHAR(10) DEFAULT 'vi-VN',
    date_format     VARCHAR(30) DEFAULT 'DD/MM/YYYY',
    
    -- Notification settings
    notify_email    VARCHAR(255),                   -- Nhận thông báo hệ thống
    notify_events   TEXT[] DEFAULT '{"payment","system"}',
    
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

-- 5.1.4 DOMAINS (Custom domain mapping)
-- ============================================================

CREATE TABLE domains (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID REFERENCES tenants(id) ON DELETE CASCADE,
    
    domain          VARCHAR(255) UNIQUE NOT NULL,   -- 'xosophuongnghi.com.vn'
    type            VARCHAR(20) DEFAULT 'custom'
                    CHECK (type IN ('subdomain', 'custom')),
    is_primary      BOOLEAN DEFAULT FALSE,          -- Domain chính
    
    -- SSL Certificate management
    ssl_status      VARCHAR(20) DEFAULT 'pending'
                    CHECK (ssl_status IN ('pending','provisioning','active','failed','expired')),
    ssl_provider    VARCHAR(20) DEFAULT 'cloudflare', -- 'cloudflare' | 'letsencrypt'
    ssl_expires_at  TIMESTAMPTZ,
    
    -- DNS Verification
    verified        BOOLEAN DEFAULT FALSE,
    verify_token    VARCHAR(255),                   -- TXT record để verify ownership
    verified_at     TIMESTAMPTZ,
    
    -- Redirect config
    redirect_to     VARCHAR(255),                   -- Redirect domain này sang domain khác
    redirect_type   SMALLINT DEFAULT 301,
    
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_domains_tenant_id ON domains(tenant_id);
CREATE INDEX idx_domains_domain ON domains(domain);

-- 5.1.5 TENANT USERS (Admin users của mỗi tenant)
-- ============================================================

CREATE TABLE tenant_users (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID REFERENCES tenants(id) ON DELETE CASCADE,
    
    -- Credentials
    email           VARCHAR(255) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    
    -- Profile
    full_name       VARCHAR(255),
    avatar_url      VARCHAR(1000),
    phone           VARCHAR(50),
    
    -- Role & Permissions
    role            VARCHAR(20) DEFAULT 'editor'
                    CHECK (role IN ('owner','admin','editor','viewer')),
    permissions     JSONB DEFAULT '{}',
    -- {
    --   "articles": ["read","write","delete"],
    --   "settings": ["read"],
    --   "billing": [],
    --   "predictions": ["read","write"]
    -- }
    
    -- Auth
    email_verified  BOOLEAN DEFAULT FALSE,
    email_verify_token VARCHAR(255),
    reset_token     VARCHAR(255),
    reset_expires_at TIMESTAMPTZ,
    last_login_at   TIMESTAMPTZ,
    last_login_ip   INET,
    
    -- MFA
    mfa_enabled     BOOLEAN DEFAULT FALSE,
    mfa_secret      VARCHAR(255),                   -- TOTP secret (encrypted)
    
    -- Status
    is_active       BOOLEAN DEFAULT TRUE,
    
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    
    UNIQUE(tenant_id, email)
);

CREATE INDEX idx_tenant_users_tenant_id ON tenant_users(tenant_id);
CREATE INDEX idx_tenant_users_email ON tenant_users(email);

-- 5.1.6 SESSIONS
-- ============================================================

CREATE TABLE sessions (
    id              VARCHAR(255) PRIMARY KEY,       -- Session token (UUID v4)
    tenant_user_id  UUID REFERENCES tenant_users(id) ON DELETE CASCADE,
    tenant_id       UUID REFERENCES tenants(id) ON DELETE CASCADE,
    
    -- Device info
    user_agent      TEXT,
    ip_address      INET,
    device_type     VARCHAR(20),                    -- 'mobile','tablet','desktop'
    
    -- Token rotation
    refresh_token   VARCHAR(255) UNIQUE,
    
    expires_at      TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    last_active_at  TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_sessions_tenant_user_id ON sessions(tenant_user_id);
CREATE INDEX idx_sessions_expires_at ON sessions(expires_at);

-- 5.1.7 SUPER ADMIN USERS (Platform owner)
-- ============================================================

CREATE TABLE platform_admins (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(255) UNIQUE NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    full_name       VARCHAR(255),
    role            VARCHAR(20) DEFAULT 'admin'
                    CHECK (role IN ('super_admin','admin','support','billing')),
    is_active       BOOLEAN DEFAULT TRUE,
    last_login_at   TIMESTAMPTZ,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

-- 5.1.8 AUDIT LOGS (Ghi lại mọi action quan trọng)
-- ============================================================

CREATE TABLE audit_logs (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID REFERENCES tenants(id) ON DELETE SET NULL,
    user_id         UUID,                           -- tenant_user_id hoặc platform_admin_id
    user_type       VARCHAR(20),                    -- 'tenant_user' | 'platform_admin' | 'system'
    
    action          VARCHAR(100) NOT NULL,
    -- Ví dụ: 'article.created', 'domain.added', 'plan.upgraded'
    
    resource_type   VARCHAR(50),                    -- 'article', 'domain', 'tenant'
    resource_id     UUID,
    
    changes         JSONB,                          -- {before: {...}, after: {...}}
    metadata        JSONB DEFAULT '{}',             -- IP, user_agent, etc
    
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_audit_logs_tenant_id ON audit_logs(tenant_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
-- Partition by month cho performance
-- CREATE TABLE audit_logs_2024_01 PARTITION OF audit_logs
--   FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');


-- ============================================================
-- 5.2 CMS SCHEMA (Dùng chung cho mọi vertical)
-- ============================================================

-- 5.2.1 CATEGORIES
-- ============================================================

CREATE TABLE categories (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID REFERENCES tenants(id) ON DELETE CASCADE,
    
    name            VARCHAR(255) NOT NULL,
    slug            VARCHAR(255) NOT NULL,
    description     TEXT,
    thumbnail       VARCHAR(1000),
    
    parent_id       UUID REFERENCES categories(id), -- Nested categories
    display_order   INT DEFAULT 0,
    
    -- SEO
    seo_title       VARCHAR(255),
    seo_description VARCHAR(500),
    
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    
    UNIQUE(tenant_id, slug)
);

-- 5.2.2 ARTICLES (Blog / Tin tức / Nhận định)
-- ============================================================

CREATE TABLE articles (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID REFERENCES tenants(id) ON DELETE CASCADE,
    
    -- Content
    title           VARCHAR(500) NOT NULL,
    slug            VARCHAR(500) NOT NULL,
    excerpt         TEXT,                           -- Tóm tắt (auto hoặc manual)
    content         TEXT NOT NULL,                  -- HTML content từ rich text editor
    content_json    JSONB,                          -- Tiptap JSON format (để re-edit)
    
    -- Media
    thumbnail_url   VARCHAR(1000),
    thumbnail_alt   VARCHAR(255),
    
    -- Classification
    category_id     UUID REFERENCES categories(id),
    tags            TEXT[] DEFAULT '{}',
    article_type    VARCHAR(50) DEFAULT 'blog'
                    CHECK (article_type IN (
                        'blog',                     -- Bài viết thông thường
                        'news',                     -- Tin tức
                        'prediction',               -- Nhận định / Soi cầu
                        'guide',                    -- Hướng dẫn
                        'page'                      -- Static page (Giới thiệu, Liên hệ)
                    )),
    
    -- Author
    author_id       UUID REFERENCES tenant_users(id),
    author_name     VARCHAR(255),                   -- Override nếu muốn dùng pen name
    author_avatar   VARCHAR(1000),
    
    -- Publishing
    status          VARCHAR(20) DEFAULT 'draft'
                    CHECK (status IN ('draft','published','scheduled','archived')),
    published_at    TIMESTAMPTZ,
    scheduled_at    TIMESTAMPTZ,
    
    -- SEO
    seo_title       VARCHAR(255),
    seo_description VARCHAR(500),
    seo_og_image    VARCHAR(1000),
    canonical_url   VARCHAR(1000),
    
    -- Stats
    view_count      BIGINT DEFAULT 0,
    share_count     INT DEFAULT 0,
    
    -- Settings
    allow_comments  BOOLEAN DEFAULT FALSE,
    is_featured     BOOLEAN DEFAULT FALSE,
    is_pinned       BOOLEAN DEFAULT FALSE,
    
    -- Vertical-specific metadata
    -- Ví dụ cho prediction article:
    -- {"lottery": {"province_id": "...", "numbers": ["23","45"], "draw_date": "2024-01-15"}}
    vertical_meta   JSONB DEFAULT '{}',
    
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ,                    -- Soft delete
    
    UNIQUE(tenant_id, slug)
);

CREATE INDEX idx_articles_tenant_id ON articles(tenant_id);
CREATE INDEX idx_articles_status ON articles(status);
CREATE INDEX idx_articles_published_at ON articles(published_at DESC);
CREATE INDEX idx_articles_category_id ON articles(category_id);
CREATE INDEX idx_articles_article_type ON articles(article_type);
CREATE INDEX idx_articles_tags ON articles USING GIN(tags);

-- Full text search
CREATE INDEX idx_articles_fts ON articles
    USING GIN(to_tsvector('simple', title || ' ' || COALESCE(excerpt, '')));

-- 5.2.3 PAGES (Static pages: Giới thiệu, Liên hệ, etc.)
-- ============================================================

CREATE TABLE pages (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID REFERENCES tenants(id) ON DELETE CASCADE,
    
    title           VARCHAR(500) NOT NULL,
    slug            VARCHAR(500) NOT NULL,
    content         TEXT,
    content_json    JSONB,
    
    -- Template
    template        VARCHAR(50) DEFAULT 'default',
    -- 'default' | 'contact' | 'about' | 'landing' | 'custom'
    
    -- SEO
    seo_title       VARCHAR(255),
    seo_description VARCHAR(500),
    
    -- Status
    status          VARCHAR(20) DEFAULT 'draft'
                    CHECK (status IN ('draft','published')),
    is_homepage     BOOLEAN DEFAULT FALSE,          -- Chỉ 1 page là homepage
    show_in_nav     BOOLEAN DEFAULT TRUE,
    nav_order       INT DEFAULT 0,
    
    published_at    TIMESTAMPTZ,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    
    UNIQUE(tenant_id, slug)
);

-- 5.2.4 MENUS (Navigation builder)
-- ============================================================

CREATE TABLE menus (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID REFERENCES tenants(id) ON DELETE CASCADE,
    name            VARCHAR(100) NOT NULL,          -- 'header', 'footer', 'sidebar'
    location        VARCHAR(50),
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE menu_items (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    menu_id         UUID REFERENCES menus(id) ON DELETE CASCADE,
    parent_id       UUID REFERENCES menu_items(id), -- Nested menu
    
    label           VARCHAR(255) NOT NULL,
    url             VARCHAR(1000),
    target          VARCHAR(10) DEFAULT '_self',    -- '_self' | '_blank'
    icon            VARCHAR(100),                   -- Icon class hoặc emoji
    display_order   INT DEFAULT 0,
    
    -- Dynamic link (optional)
    link_type       VARCHAR(20) DEFAULT 'custom'
                    CHECK (link_type IN ('custom','page','category','article','route'))
);

-- 5.2.5 MEDIA LIBRARY
-- ============================================================

CREATE TABLE media (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID REFERENCES tenants(id) ON DELETE CASCADE,
    
    -- File info
    original_name   VARCHAR(500) NOT NULL,
    filename        VARCHAR(500) NOT NULL,          -- Stored filename (UUID-based)
    file_path       VARCHAR(1000) NOT NULL,         -- Path trên storage
    url             VARCHAR(1000) NOT NULL,          -- Public URL
    
    -- Type & Size
    mime_type       VARCHAR(100),
    file_size       BIGINT,                         -- Bytes
    media_type      VARCHAR(20)
                    CHECK (media_type IN ('image','video','audio','document','other')),
    
    -- Image metadata
    width           INT,
    height          INT,
    
    -- Thumbnails (auto-generated)
    thumbnails      JSONB DEFAULT '{}',
    -- {
    --   "small": "https://...150x150.jpg",
    --   "medium": "https://...400x400.jpg",
    --   "large": "https://...800x800.jpg"
    -- }
    
    -- Organization
    folder          VARCHAR(500) DEFAULT '/',
    alt_text        VARCHAR(500),
    caption         TEXT,
    tags            TEXT[] DEFAULT '{}',
    
    uploaded_by     UUID REFERENCES tenant_users(id),
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_media_tenant_id ON media(tenant_id);
CREATE INDEX idx_media_type ON media(media_type);


-- ============================================================
-- 5.3 BILLING & SUBSCRIPTION SCHEMA
-- ============================================================

-- 5.3.1 SUBSCRIPTIONS
-- ============================================================

CREATE TABLE subscriptions (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id               UUID UNIQUE REFERENCES tenants(id) ON DELETE CASCADE,
    plan_id                 UUID REFERENCES plans(id),
    
    status                  VARCHAR(20) NOT NULL DEFAULT 'trialing'
                            CHECK (status IN (
                                'trialing',
                                'active',
                                'past_due',     -- Thanh toán thất bại, đang retry
                                'cancelled',    -- Đã hủy, hết hạn vào end_date
                                'expired',      -- Đã hết hạn
                                'paused'        -- Tạm dừng
                            )),
    
    billing_cycle           VARCHAR(10) DEFAULT 'monthly'
                            CHECK (billing_cycle IN ('monthly','yearly')),
    
    -- Period
    current_period_start    TIMESTAMPTZ,
    current_period_end      TIMESTAMPTZ,
    trial_start             TIMESTAMPTZ,
    trial_end               TIMESTAMPTZ,
    cancelled_at            TIMESTAMPTZ,
    cancel_at_period_end    BOOLEAN DEFAULT FALSE, -- Hủy vào cuối kỳ
    
    -- Pricing
    amount                  DECIMAL(12,2) NOT NULL,
    currency                CHAR(3) DEFAULT 'VND',
    discount_percent        DECIMAL(5,2) DEFAULT 0,
    coupon_code             VARCHAR(50),
    
    -- External payment gateway IDs
    stripe_subscription_id  VARCHAR(255),
    payos_subscription_id   VARCHAR(255),
    
    -- Metadata
    notes                   TEXT,
    created_at              TIMESTAMPTZ DEFAULT NOW(),
    updated_at              TIMESTAMPTZ DEFAULT NOW()
);

-- 5.3.2 INVOICES
-- ============================================================

CREATE TABLE invoices (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID REFERENCES tenants(id),
    subscription_id     UUID REFERENCES subscriptions(id),
    
    -- Invoice number
    invoice_number      VARCHAR(50) UNIQUE NOT NULL,
    -- Format: INV-2024-000001
    
    status              VARCHAR(20) DEFAULT 'pending'
                        CHECK (status IN ('draft','pending','paid','void','uncollectible')),
    
    -- Amounts
    subtotal            DECIMAL(12,2),
    discount_amount     DECIMAL(12,2) DEFAULT 0,
    tax_amount          DECIMAL(12,2) DEFAULT 0,
    total               DECIMAL(12,2),
    amount_paid         DECIMAL(12,2) DEFAULT 0,
    amount_due          DECIMAL(12,2),
    currency            CHAR(3) DEFAULT 'VND',
    
    -- Period
    period_start        TIMESTAMPTZ,
    period_end          TIMESTAMPTZ,
    due_date            TIMESTAMPTZ,
    paid_at             TIMESTAMPTZ,
    
    -- Line items
    line_items          JSONB DEFAULT '[]',
    -- [{
    --   "description": "Pro Plan - Tháng 01/2024",
    --   "quantity": 1,
    --   "unit_price": 299000,
    --   "amount": 299000
    -- }]
    
    -- Payment
    payment_method      VARCHAR(30),
    payment_reference   VARCHAR(255),
    
    -- PDF
    pdf_url             VARCHAR(1000),
    
    created_at          TIMESTAMPTZ DEFAULT NOW()
);

-- 5.3.3 PAYMENTS
-- ============================================================

CREATE TABLE payments (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    invoice_id          UUID REFERENCES invoices(id),
    tenant_id           UUID REFERENCES tenants(id),
    
    amount              DECIMAL(12,2) NOT NULL,
    currency            CHAR(3) DEFAULT 'VND',
    
    status              VARCHAR(20) DEFAULT 'pending'
                        CHECK (status IN ('pending','processing','completed','failed','refunded','cancelled')),
    
    -- Payment method details
    payment_method      VARCHAR(30) NOT NULL,
    -- 'momo' | 'vnpay' | 'payos' | 'bank_transfer' | 'stripe' | 'free'
    
    gateway_transaction_id  VARCHAR(255),           -- ID từ payment gateway
    gateway_response    JSONB,                      -- Raw response từ gateway
    
    -- Bank transfer specific
    bank_account        VARCHAR(50),
    bank_transfer_ref   VARCHAR(100),
    
    -- Timing
    initiated_at        TIMESTAMPTZ DEFAULT NOW(),
    completed_at        TIMESTAMPTZ,
    failed_at           TIMESTAMPTZ,
    failure_reason      TEXT,
    
    -- Refund
    refunded_amount     DECIMAL(12,2) DEFAULT 0,
    refunded_at         TIMESTAMPTZ,
    
    metadata            JSONB DEFAULT '{}',
    created_at          TIMESTAMPTZ DEFAULT NOW()
);

-- 5.3.4 COUPONS
-- ============================================================

CREATE TABLE coupons (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code            VARCHAR(50) UNIQUE NOT NULL,    -- 'WELCOME50'
    
    discount_type   VARCHAR(10) CHECK (discount_type IN ('percent','fixed')),
    discount_value  DECIMAL(10,2),
    
    -- Restrictions
    applicable_plans UUID[],                        -- NULL = all plans
    min_months      INT DEFAULT 1,                  -- Minimum subscription months
    
    -- Usage limits
    max_uses        INT,                            -- NULL = unlimited
    used_count      INT DEFAULT 0,
    max_uses_per_tenant INT DEFAULT 1,
    
    -- Validity
    valid_from      TIMESTAMPTZ DEFAULT NOW(),
    valid_until     TIMESTAMPTZ,
    
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);


-- ============================================================
-- 5.4 NOTIFICATIONS & EMAILS
-- ============================================================

CREATE TABLE notification_templates (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code        VARCHAR(100) UNIQUE NOT NULL,
    -- 'tenant.welcome', 'subscription.expiring', 'payment.success'
    
    channel     VARCHAR(20) CHECK (channel IN ('email','sms','in_app','push')),
    subject     VARCHAR(500),                       -- Email subject template
    body_html   TEXT,                               -- HTML template (Handlebars/Thymeleaf)
    body_text   TEXT,                               -- Plain text fallback
    
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE notification_logs (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID REFERENCES tenants(id),
    template_code   VARCHAR(100),
    
    channel         VARCHAR(20),
    recipient       VARCHAR(255),                   -- Email or phone
    subject         VARCHAR(500),
    
    status          VARCHAR(20) DEFAULT 'pending'
                    CHECK (status IN ('pending','sent','delivered','failed','bounced')),
    
    sent_at         TIMESTAMPTZ,
    failure_reason  TEXT,
    provider_message_id VARCHAR(255),
    
    metadata        JSONB DEFAULT '{}',
    created_at      TIMESTAMPTZ DEFAULT NOW()
);


-- ============================================================
-- 5.5 AD MANAGEMENT
-- ============================================================

CREATE TABLE ad_zones (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID REFERENCES tenants(id) ON DELETE CASCADE,
    
    name        VARCHAR(100) NOT NULL,              -- 'Header Banner'
    code        VARCHAR(50) NOT NULL,               -- 'header_728x90'
    position    VARCHAR(50),                        -- 'header','sidebar','content','footer'
    description TEXT,
    
    -- Dimensions
    width       INT,
    height      INT,
    
    -- Config
    is_active   BOOLEAN DEFAULT TRUE,
    
    UNIQUE(tenant_id, code)
);

CREATE TABLE ads (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID REFERENCES tenants(id) ON DELETE CASCADE,
    zone_id         UUID REFERENCES ad_zones(id),
    
    name            VARCHAR(255),
    type            VARCHAR(20) CHECK (type IN ('adsense','banner','text','html','video')),
    
    -- AdSense
    adsense_client  VARCHAR(100),                   -- ca-pub-XXXXXXXXXX
    adsense_slot    VARCHAR(50),
    
    -- Custom banner
    image_url       VARCHAR(1000),
    click_url       VARCHAR(1000),
    open_in_tab     BOOLEAN DEFAULT TRUE,
    
    -- Custom HTML/Script
    custom_html     TEXT,
    
    -- Schedule
    start_date      DATE,
    end_date        DATE,
    
    -- Stats
    impression_count BIGINT DEFAULT 0,
    click_count     BIGINT DEFAULT 0,
    
    is_active       BOOLEAN DEFAULT TRUE,
    display_order   INT DEFAULT 0,
    
    created_at      TIMESTAMPTZ DEFAULT NOW()
);


-- ============================================================
-- 5.6 ANALYTICS (Lightweight, không cần GA)
-- ============================================================

CREATE TABLE page_views (
    id          BIGSERIAL PRIMARY KEY,
    tenant_id   UUID NOT NULL,
    
    -- Page info
    path        VARCHAR(2000),
    title       VARCHAR(500),
    
    -- Session
    session_id  VARCHAR(100),
    visitor_id  VARCHAR(100),                       -- Anonymous ID (localStorage)
    
    -- Traffic source
    referrer    VARCHAR(2000),
    utm_source  VARCHAR(100),
    utm_medium  VARCHAR(100),
    utm_campaign VARCHAR(100),
    
    -- Device
    device_type VARCHAR(20),
    browser     VARCHAR(50),
    os          VARCHAR(50),
    country     CHAR(2),
    
    created_at  TIMESTAMPTZ DEFAULT NOW()
)
PARTITION BY RANGE (created_at);   -- Partition by month

-- Monthly partitions
CREATE TABLE page_views_2024_01 PARTITION OF page_views
    FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');
-- (Tạo auto bằng pg_partman)

-- Aggregated stats (tính hàng ngày, không query raw)
CREATE TABLE analytics_daily (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID REFERENCES tenants(id) ON DELETE CASCADE,
    date        DATE NOT NULL,
    
    page_views      BIGINT DEFAULT 0,
    unique_visitors BIGINT DEFAULT 0,
    sessions        BIGINT DEFAULT 0,
    
    -- Top pages (JSONB array)
    top_pages   JSONB DEFAULT '[]',
    -- [{"path": "/ket-qua/mien-bac", "views": 1234}, ...]
    
    -- Traffic sources
    top_sources JSONB DEFAULT '[]',
    
    -- Device breakdown
    mobile_pct  DECIMAL(5,2),
    desktop_pct DECIMAL(5,2),
    tablet_pct  DECIMAL(5,2),
    
    UNIQUE(tenant_id, date)
);


-- ============================================================
-- 5.7 LOTTERY VERTICAL SCHEMA
-- ============================================================

-- 5.7.1 LOTTERY REGIONS & PROVINCES
-- ============================================================

CREATE TABLE lottery_regions (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code        VARCHAR(10) UNIQUE NOT NULL,        -- 'north', 'central', 'south'
    name        VARCHAR(100) NOT NULL,              -- 'Miền Bắc', 'Miền Trung', 'Miền Nam'
    draw_time   TIME NOT NULL,                      -- 18:10, 17:15, 16:15
    timezone    VARCHAR(50) DEFAULT 'Asia/Ho_Chi_Minh',
    display_order INT DEFAULT 0
);

INSERT INTO lottery_regions VALUES
    (gen_random_uuid(), 'north',   'Miền Bắc',  '18:10', 'Asia/Ho_Chi_Minh', 1),
    (gen_random_uuid(), 'central', 'Miền Trung', '17:15', 'Asia/Ho_Chi_Minh', 2),
    (gen_random_uuid(), 'south',   'Miền Nam',   '16:15', 'Asia/Ho_Chi_Minh', 3);

CREATE TABLE lottery_provinces (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    region_id       UUID REFERENCES lottery_regions(id),
    
    code            VARCHAR(20) UNIQUE NOT NULL,
    -- 'mb','vt','tphcm','ct','dn','hue','qnb','bd','bth',...
    
    name            VARCHAR(255) NOT NULL,
    -- 'Thành phố Hồ Chí Minh', 'Cần Thơ'...
    
    short_name      VARCHAR(50),                    -- 'TP.HCM', 'Cần Thơ'
    slug            VARCHAR(100) UNIQUE,            -- 'tp-hcm', 'can-tho'
    
    -- Schedule: ngày quay trong tuần
    -- 0=CN, 1=T2, 2=T3, 3=T4, 4=T5, 5=T6, 6=T7
    draw_days       INT[] NOT NULL,
    
    -- Result structure type
    result_type     VARCHAR(20) DEFAULT 'south'
                    CHECK (result_type IN ('north','central','south')),
    -- north: 8 giải (ĐB + 1+2+3+4+5+6+7)
    -- south/central: 8 giải (ĐB + 1+2+3+4+5+6+7+8)
    
    is_active       BOOLEAN DEFAULT TRUE,
    display_order   INT DEFAULT 0
);

-- Seed data mẫu
INSERT INTO lottery_provinces (id, region_id, code, name, short_name, slug, draw_days, result_type, display_order) VALUES
-- Miền Bắc
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='north'), 'mb',   'Miền Bắc',              'MB',       'mien-bac',      '{1,2,3,4,5,6,0}', 'north', 1),
-- Miền Nam
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'tphcm','Thành phố Hồ Chí Minh', 'TP.HCM',   'tp-hcm',        '{1,3,6}',         'south', 1),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'dongthap','Đồng Tháp',          'Đồng Tháp','dong-thap',     '{1}',             'south', 2),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'camau','Cà Mau',                'Cà Mau',   'ca-mau',        '{1}',             'south', 3),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'benluc','Bến Tre',              'Bến Tre',  'ben-tre',       '{2}',             'south', 4),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'vungtau','Vũng Tàu',            'Vũng Tàu', 'vung-tau',      '{2}',             'south', 5),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'baclieu','Bạc Liêu',            'Bạc Liêu', 'bac-lieu',      '{2}',             'south', 6),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'dongnai','Đồng Nai',            'Đồng Nai', 'dong-nai',      '{3}',             'south', 7),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'cantho','Cần Thơ',              'Cần Thơ',  'can-tho',       '{3}',             'south', 8),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'soctrang','Sóc Trăng',          'Sóc Trăng','soc-trang',     '{3}',             'south', 9),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'tayninh','Tây Ninh',            'Tây Ninh', 'tay-ninh',      '{4}',             'south', 10),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'angiang','An Giang',            'An Giang', 'an-giang',      '{4}',             'south', 11),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'binhthuan','Bình Thuận',        'B.Thuận',  'binh-thuan',    '{4}',             'south', 12),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'vinhlong','Vĩnh Long',          'V.Long',   'vinh-long',     '{5}',             'south', 13),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'binhduong','Bình Dương',        'B.Dương',  'binh-duong',    '{5}',             'south', 14),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'travinh','Trà Vinh',            'Trà Vinh', 'tra-vinh',      '{5}',             'south', 15),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'longan','Long An',              'Long An',  'long-an',       '{6}',             'south', 16),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'binhphuoc','Bình Phước',        'B.Phước',  'binh-phuoc',    '{6}',             'south', 17),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'haugiang','Hậu Giang',          'H.Giang',  'hau-giang',     '{6}',             'south', 18),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'kiengiang','Kiên Giang',        'K.Giang',  'kien-giang',    '{0}',             'south', 19),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'tiengiang','Tiền Giang',        'T.Giang',  'tien-giang',    '{0}',             'south', 20),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='south'), 'dalat','Đà Lạt',               'Đà Lạt',   'da-lat',        '{0}',             'south', 21),
-- Miền Trung
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'danang','Đà Nẵng',             'Đà Nẵng',  'da-nang',       '{2,6}',           'central',1),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'hue',   'Thừa T.Huế',          'TT.Huế',   'thua-thien-hue','{1,4}',           'central',2),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'phuyen','Phú Yên',             'Phú Yên',  'phu-yen',       '{1,4}',           'central',3),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'quangbinh','Quảng Bình',       'Q.Bình',   'quang-binh',    '{2,5}',           'central',4),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'quangngai','Quảng Ngãi',       'Q.Ngãi',   'quang-ngai',    '{2}',             'central',5),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'kontum','Kon Tum',             'Kon Tum',  'kon-tum',       '{0}',             'central',6),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'khanhhoa','Khánh Hòa',         'K.Hòa',    'khanh-hoa',     '{3,0}',           'central',7),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'quangtri','Quảng Trị',         'Q.Trị',    'quang-tri',     '{3}',             'central',8),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'binhdinh','Bình Định',         'B.Định',   'binh-dinh',     '{1,4}',           'central',9),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'gialai','Gia Lai',             'Gia Lai',  'gia-lai',       '{2,5}',           'central',10),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'ninhthuan','Ninh Thuận',       'N.Thuận',  'ninh-thuan',    '{5}',             'central',11),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'quangnam','Quảng Nam',         'Q.Nam',    'quang-nam',     '{3,6}',           'central',12),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'daklak','Đắk Lắk',            'Đắk Lắk',  'dak-lak',       '{3}',             'central',13),
(gen_random_uuid(), (SELECT id FROM lottery_regions WHERE code='central'),'daknong','Đắk Nông',           'Đắk Nông', 'dak-nong',      '{0}',             'central',14);

-- ============================================================
-- 5.7.2 LOTTERY RESULTS (Kết quả xổ số)
-- ============================================================

CREATE TABLE lottery_results (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    province_id     UUID NOT NULL REFERENCES lottery_provinces(id),
    draw_date       DATE NOT NULL,
    
    -- Miền Bắc prizes (giải ĐB 5 số, các giải 5 số)
    -- Miền Nam/Trung (giải ĐB 6 số, giải 8: 2 số)

    -- Giải đặc biệt
    special_prize   VARCHAR(10),                    -- '12345' (MB:5 số) | '123456' (MN:6 số)
    
    -- Giải nhất
    first_prizes    VARCHAR(10)[] DEFAULT '{}',     -- MB: 1 số | MN: 1 số
    
    -- Giải nhì
    second_prizes   VARCHAR(10)[] DEFAULT '{}',     -- MB: 2 số | MN: 1 số
    
    -- Giải ba
    third_prizes    VARCHAR(10)[] DEFAULT '{}',     -- MB: 6 số | MN: 2 số
    
    -- Giải tư
    fourth_prizes   VARCHAR(10)[] DEFAULT '{}',     -- MB: 4 số | MN: 7 số
    
    -- Giải năm
    fifth_prizes    VARCHAR(10)[] DEFAULT '{}',     -- MB: 6 số | MN: 1 số
    
    -- Giải sáu
    sixth_prizes    VARCHAR(10)[] DEFAULT '{}',     -- MB: 3 số | MN: 3 số
    
    -- Giải bảy
    seventh_prizes  VARCHAR(10)[] DEFAULT '{}',     -- MB: 4 số | MN: 4 số
    
    -- Giải tám (chỉ Miền Nam/Trung)
    eighth_prizes   VARCHAR(10)[] DEFAULT '{}',     -- MN: 1 số (2 chữ số)

    -- Raw full data (backup, dễ re-parse)
    raw_data        JSONB,

    -- Loto extracted (2 số cuối của tất cả giải - để query nhanh)
    -- Tính toán 1 lần khi insert, tránh tính lại
    loto_numbers    CHAR(2)[] DEFAULT '{}',
    -- Ví dụ: ['12','34','56','23','45','67',...]

    -- Metadata
    source          VARCHAR(100),                   -- 'api_provider' | 'scraper_minhngoc'
    source_url      VARCHAR(500),
    is_verified     BOOLEAN DEFAULT FALSE,
    is_official     BOOLEAN DEFAULT FALSE,

    fetched_at      TIMESTAMPTZ DEFAULT NOW(),
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT uq_lottery_result UNIQUE(province_id, draw_date)
);

-- Indexes quan trọng
CREATE INDEX idx_lottery_results_province_date 
    ON lottery_results(province_id, draw_date DESC);

CREATE INDEX idx_lottery_results_draw_date 
    ON lottery_results(draw_date DESC);

-- GIN index cho loto_numbers array query
CREATE INDEX idx_lottery_results_loto 
    ON lottery_results USING GIN(loto_numbers);

-- Partition by year (kết quả nhiều năm = data lớn)
-- ALTER TABLE lottery_results PARTITION BY RANGE (draw_date);
-- CREATE TABLE lottery_results_2024 PARTITION OF lottery_results
--     FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');

-- ============================================================
-- 5.7.3 LOTTERY LOTO DETAIL (Chi tiết từng số loto)
-- ============================================================

CREATE TABLE lottery_loto_detail (
    id              BIGSERIAL PRIMARY KEY,
    province_id     UUID NOT NULL REFERENCES lottery_provinces(id),
    draw_date       DATE NOT NULL,
    result_id       UUID REFERENCES lottery_results(id) ON DELETE CASCADE,
    
    number          CHAR(2) NOT NULL,               -- '00' đến '99'
    
    -- Xuất hiện ở giải nào (để display highlight)
    prize_positions JSONB DEFAULT '[]',
    -- [{"prize": "special", "full_number": "12345", "position": 0}]
    
    occurrence      SMALLINT DEFAULT 1,             -- Số lần xuất hiện trong ngày (lô kép)

    CONSTRAINT uq_loto_detail UNIQUE(province_id, draw_date, number)
);

CREATE INDEX idx_loto_detail_province_date 
    ON lottery_loto_detail(province_id, draw_date DESC);
CREATE INDEX idx_loto_detail_number 
    ON lottery_loto_detail(province_id, number, draw_date DESC);

-- ============================================================
-- 5.7.4 LOTTERY FREQUENCY STATS (Materialized / Cache)
-- ============================================================

CREATE TABLE lottery_frequency_stats (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    province_id     UUID NOT NULL REFERENCES lottery_provinces(id),
    number          CHAR(2) NOT NULL,               -- '00' - '99'
    period_type     VARCHAR(10) NOT NULL
                    CHECK (period_type IN ('10','20','30','60','90','180','365','all')),

    -- Thống kê
    frequency       INT DEFAULT 0,                  -- Số lần xuất hiện trong period
    frequency_pct   DECIMAL(5,2),                   -- % so với tổng kỳ
    last_appeared   DATE,                           -- Ngày cuối cùng xuất hiện
    days_since_last INT,                            -- Số ngày kể từ lần cuối
    avg_cycle       DECIMAL(8,2),                   -- Chu kỳ trung bình (ngày)
    max_gap         INT,                            -- Khoảng cách lớn nhất giữa 2 lần ra
    min_gap         INT,                            -- Khoảng cách nhỏ nhất
    
    -- Trend (so với period trước)
    trend           VARCHAR(10) DEFAULT 'neutral'
                    CHECK (trend IN ('hot','warm','neutral','cold','frozen')),
    -- hot: ra nhiều hơn TB, frozen: gan > 2x avg_cycle

    updated_at      TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT uq_freq_stats UNIQUE(province_id, number, period_type)
);

CREATE INDEX idx_freq_stats_province 
    ON lottery_frequency_stats(province_id, period_type);
CREATE INDEX idx_freq_stats_number 
    ON lottery_frequency_stats(province_id, number);

-- ============================================================
-- 5.7.5 LOTTERY HEAD-TAIL STATS (Đầu - Đuôi thống kê)
-- ============================================================

CREATE TABLE lottery_head_tail_stats (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    province_id     UUID NOT NULL REFERENCES lottery_provinces(id),
    stat_type       VARCHAR(10) CHECK (stat_type IN ('head','tail')),
    -- head: đầu số (0-9), tail: đuôi số (0-9)
    digit           SMALLINT CHECK (digit BETWEEN 0 AND 9),
    period_type     VARCHAR(10),

    frequency       INT DEFAULT 0,
    frequency_pct   DECIMAL(5,2),
    last_appeared   DATE,
    days_since_last INT,
    trend           VARCHAR(10),

    updated_at      TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT uq_ht_stats UNIQUE(province_id, stat_type, digit, period_type)
);

-- ============================================================
-- 5.7.6 LOTTERY SPECIAL PRIZE HISTORY (Giải ĐB archive)
-- ============================================================

CREATE TABLE lottery_special_prize_stats (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    province_id     UUID NOT NULL REFERENCES lottery_provinces(id),

    -- Thống kê giải ĐB theo đầu số
    head_digit      SMALLINT,                       -- Đầu giải ĐB (0-9)
    head_frequency  INT,
    head_last_date  DATE,

    -- Thống kê theo 2 số cuối (đề ĐB)
    last_two        CHAR(2),
    last_two_freq   INT,
    last_two_date   DATE,

    period_type     VARCHAR(10),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT uq_sp_stats UNIQUE(province_id, last_two, period_type)
);

-- ============================================================
-- 5.7.7 LOTTERY PREDICTIONS (Soi cầu / Dự đoán)
-- ============================================================

CREATE TABLE lottery_predictions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    province_id     UUID NOT NULL REFERENCES lottery_provinces(id),

    -- Nội dung dự đoán
    draw_date       DATE NOT NULL,                  -- Ngày áp dụng
    
    prediction_type VARCHAR(30) NOT NULL
                    CHECK (prediction_type IN (
                        'bach_thu',                 -- Bạch thủ (1 số)
                        'xien_2',                   -- Xiên 2
                        'xien_3',                   -- Xiên 3
                        'xien_4',                   -- Xiên 4
                        '3_cang',                   -- 3 càng
                        'dau_duoi',                 -- Đầu đuôi
                        'lo_cap',                   -- Lô cặp
                        'custom'                    -- Tùy chỉnh
                    )),

    numbers         VARCHAR(10)[] NOT NULL,         -- ['23','45']
    confidence      SMALLINT DEFAULT 3              -- 1-5 sao
                    CHECK (confidence BETWEEN 1 AND 5),

    -- Author
    author_id       UUID REFERENCES tenant_users(id),
    author_name     VARCHAR(255),
    author_avatar   VARCHAR(1000),

    -- Method description
    method_name     VARCHAR(255),                   -- 'Cầu theo chu kỳ 7 ngày'
    analysis        TEXT,                           -- Phân tích chi tiết (rich text)
    analysis_json   JSONB,

    -- Visibility
    is_vip          BOOLEAN DEFAULT FALSE,          -- VIP content (ẩn một phần)
    is_featured     BOOLEAN DEFAULT FALSE,

    -- Result verification (auto sau khi có KQXS)
    result_status   VARCHAR(20) DEFAULT 'pending'
                    CHECK (result_status IN ('pending','win','lose','partial')),
    -- partial: xiên đúng 1/2 số
    result_numbers  VARCHAR(10)[],                  -- Số nào đúng
    result_checked_at TIMESTAMPTZ,

    -- Stats
    view_count      INT DEFAULT 0,

    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_predictions_tenant_date 
    ON lottery_predictions(tenant_id, draw_date DESC);
CREATE INDEX idx_predictions_province_date 
    ON lottery_predictions(province_id, draw_date DESC);

-- ============================================================
-- 5.7.8 LOTTERY AUTHOR STATS (Tỷ lệ thắng của tác giả)
-- ============================================================

CREATE TABLE lottery_author_stats (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID REFERENCES tenants(id) ON DELETE CASCADE,
    author_id       UUID REFERENCES tenant_users(id),
    author_name     VARCHAR(255),
    province_id     UUID REFERENCES lottery_provinces(id),
    prediction_type VARCHAR(30),

    total_predictions   INT DEFAULT 0,
    win_count           INT DEFAULT 0,
    lose_count          INT DEFAULT 0,
    win_rate            DECIMAL(5,2),               -- Tỷ lệ thắng %

    -- Streak
    current_streak      INT DEFAULT 0,              -- Số ngày thắng/thua liên tiếp
    streak_type         VARCHAR(10),                -- 'win' | 'lose'
    best_streak         INT DEFAULT 0,

    updated_at          TIMESTAMPTZ DEFAULT NOW(),

    CONSTRAINT uq_author_stats UNIQUE(tenant_id, author_id, province_id, prediction_type)
);

-- ============================================================
-- 5.7.9 LOTTERY SETTINGS (Per-tenant lottery config)
-- ============================================================

CREATE TABLE lottery_tenant_settings (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID UNIQUE REFERENCES tenants(id) ON DELETE CASCADE,

    -- Hiển thị đài nào
    enabled_provinces   UUID[] DEFAULT '{}',        -- Empty = show all
    default_region      VARCHAR(10) DEFAULT 'south',-- Tab mặc định khi vào trang

    -- Data source config
    data_source         VARCHAR(20) DEFAULT 'scraper'
                        CHECK (data_source IN ('scraper','api','manual')),
    api_provider        VARCHAR(50),                -- Tên API provider (nếu dùng API)
    api_key_encrypted   TEXT,                       -- Encrypted API key

    -- Features toggle
    show_predictions    BOOLEAN DEFAULT TRUE,
    show_statistics     BOOLEAN DEFAULT TRUE,
    show_vip_section    BOOLEAN DEFAULT FALSE,
    show_live_result    BOOLEAN DEFAULT TRUE,
    show_print_button   BOOLEAN DEFAULT TRUE,
    show_share_buttons  BOOLEAN DEFAULT TRUE,

    -- SEO auto-generate
    auto_seo_pages      BOOLEAN DEFAULT TRUE,
    seo_title_template  VARCHAR(500)
        DEFAULT 'KQXS {province_name} {draw_date} - Kết quả xổ số hôm nay',
    seo_desc_template   VARCHAR(1000)
        DEFAULT 'Kết quả xổ số {province_name} ngày {draw_date}. Xem KQXS nhanh nhất, chính xác nhất.',

    -- Notification: push khi có KQXS
    notify_results      BOOLEAN DEFAULT FALSE,
    notify_channel      VARCHAR(20),                -- 'telegram' | 'zalo_oa' | 'email'
    notify_config       JSONB DEFAULT '{}',

    updated_at          TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================
-- 5.7.10 LOTTERY DATA FETCH LOGS (Monitor scraper/API)
-- ============================================================

CREATE TABLE lottery_fetch_logs (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    province_id     UUID REFERENCES lottery_provinces(id),
    draw_date       DATE NOT NULL,

    status          VARCHAR(20) DEFAULT 'pending'
                    CHECK (status IN ('pending','success','failed','partial')),
    source          VARCHAR(100),
    attempt_count   SMALLINT DEFAULT 1,
    max_attempts    SMALLINT DEFAULT 3,

    -- Response info
    response_time_ms INT,
    error_message   TEXT,
    raw_response    TEXT,                           -- Lưu raw response để debug

    next_retry_at   TIMESTAMPTZ,
    fetched_at      TIMESTAMPTZ,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_fetch_logs_status ON lottery_fetch_logs(status, next_retry_at);
CREATE INDEX idx_fetch_logs_date ON lottery_fetch_logs(draw_date DESC);


-- ============================================================
-- 5.8 REAL ESTATE VERTICAL SCHEMA (Preview - expand later)
-- ============================================================

CREATE TABLE realestate_listings (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,

    title           VARCHAR(500) NOT NULL,
    slug            VARCHAR(500) NOT NULL,
    description     TEXT,

    listing_type    VARCHAR(20)
                    CHECK (listing_type IN ('sale','rent','project')),
    property_type   VARCHAR(30)
                    CHECK (property_type IN (
                        'apartment','house','villa','land',
                        'office','shophouse','warehouse','other'
                    )),

    -- Location
    province        VARCHAR(100),
    district        VARCHAR(100),
    ward            VARCHAR(100),
    address         TEXT,
    latitude        DECIMAL(10,8),
    longitude       DECIMAL(11,8),

    -- Details
    price           DECIMAL(20,2),
    price_unit      VARCHAR(10) DEFAULT 'VND',      -- 'VND' | 'USD' | 'per_m2'
    area_m2         DECIMAL(10,2),
    bedrooms        SMALLINT,
    bathrooms       SMALLINT,
    floors          SMALLINT,
    direction       VARCHAR(20),                    -- 'north','south','east','west'
    legal_status    VARCHAR(50),                    -- 'so_do','so_hong','hop_dong'
    
    -- Media
    images          JSONB DEFAULT '[]',
    video_url       VARCHAR(1000),
    virtual_tour_url VARCHAR(1000),

    -- Contact
    contact_name    VARCHAR(255),
    contact_phone   VARCHAR(50),
    contact_email   VARCHAR(255),

    -- SEO
    seo_title       VARCHAR(255),
    seo_description VARCHAR(500),

    -- Status
    status          VARCHAR(20) DEFAULT 'active'
                    CHECK (status IN ('draft','active','sold','rented','expired')),
    is_featured     BOOLEAN DEFAULT FALSE,
    is_urgent       BOOLEAN DEFAULT FALSE,
    expires_at      DATE,

    view_count      INT DEFAULT 0,

    posted_by       UUID REFERENCES tenant_users(id),
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ,

    UNIQUE(tenant_id, slug)
);

CREATE INDEX idx_realestate_tenant ON realestate_listings(tenant_id);
CREATE INDEX idx_realestate_type ON realestate_listings(listing_type, property_type);
CREATE INDEX idx_realestate_location ON realestate_listings(province, district);
CREATE INDEX idx_realestate_price ON realestate_listings(price);
CREATE INDEX idx_realestate_status ON realestate_listings(status);

-- Full text search cho BDS
CREATE INDEX idx_realestate_fts ON realestate_listings
    USING GIN(to_tsvector('simple', 
        title || ' ' || COALESCE(description,'') || ' ' || 
        COALESCE(address,'') || ' ' || COALESCE(district,'')
    ));


-- ============================================================
-- 5.9 E-COMMERCE VERTICAL SCHEMA (Preview)
-- ============================================================

CREATE TABLE ecom_products (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,

    name            VARCHAR(500) NOT NULL,
    slug            VARCHAR(500) NOT NULL,
    description     TEXT,
    description_json JSONB,
    short_description VARCHAR(1000),

    -- Pricing
    price           DECIMAL(15,2) NOT NULL,
    compare_price   DECIMAL(15,2),                  -- Giá gốc (để show giảm giá)
    cost_price      DECIMAL(15,2),                  -- Giá vốn (internal)
    currency        CHAR(3) DEFAULT 'VND',

    -- Inventory
    sku             VARCHAR(100),
    barcode         VARCHAR(100),
    track_inventory BOOLEAN DEFAULT TRUE,
    stock_quantity  INT DEFAULT 0,
    low_stock_alert INT DEFAULT 5,
    allow_backorder BOOLEAN DEFAULT FALSE,

    -- Physical
    weight_gram     INT,
    dimensions      JSONB,                          -- {length, width, height, unit}

    -- Media
    images          JSONB DEFAULT '[]',
    -- [{"url":"...","alt":"...","position":0,"is_primary":true}]

    -- Classification
    category_id     UUID REFERENCES categories(id),
    tags            TEXT[] DEFAULT '{}',
    brand           VARCHAR(100),

    -- Variants (nếu có)
    has_variants    BOOLEAN DEFAULT FALSE,
    options         JSONB DEFAULT '[]',
    -- [{"name":"Màu sắc","values":["Đỏ","Xanh"]},{"name":"Size","values":["S","M","L"]}]

    -- SEO
    seo_title       VARCHAR(255),
    seo_description VARCHAR(500),

    -- Status
    status          VARCHAR(20) DEFAULT 'draft'
                    CHECK (status IN ('draft','active','archived')),
    is_featured     BOOLEAN DEFAULT FALSE,

    view_count      INT DEFAULT 0,
    sold_count      INT DEFAULT 0,

    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ,

    UNIQUE(tenant_id, slug)
);

CREATE TABLE ecom_product_variants (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id      UUID REFERENCES ecom_products(id) ON DELETE CASCADE,
    tenant_id       UUID REFERENCES tenants(id),

    -- Variant identity
    title           VARCHAR(255),                   -- 'Đỏ / Size M'
    option_values   JSONB DEFAULT '{}',
    -- {"Màu sắc": "Đỏ", "Size": "M"}

    sku             VARCHAR(100),
    barcode         VARCHAR(100),
    price           DECIMAL(15,2),
    compare_price   DECIMAL(15,2),

    stock_quantity  INT DEFAULT 0,
    weight_gram     INT,
    image_url       VARCHAR(1000),

    is_active       BOOLEAN DEFAULT TRUE,
    display_order   INT DEFAULT 0
);

CREATE TABLE ecom_orders (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id),

    order_number    VARCHAR(50) UNIQUE NOT NULL,    -- 'ORD-20240115-0001'

    -- Customer
    customer_name   VARCHAR(255),
    customer_email  VARCHAR(255),
    customer_phone  VARCHAR(50),

    -- Shipping address
    shipping_address JSONB,
    -- {province, district, ward, address, postal_code}

    -- Items
    items           JSONB NOT NULL DEFAULT '[]',
    -- [{product_id, variant_id, name, sku, price, quantity, subtotal, image}]

    -- Amounts
    subtotal        DECIMAL(15,2) DEFAULT 0,
    shipping_fee    DECIMAL(15,2) DEFAULT 0,
    discount_amount DECIMAL(15,2) DEFAULT 0,
    tax_amount      DECIMAL(15,2) DEFAULT 0,
    total           DECIMAL(15,2) NOT NULL,
    currency        CHAR(3) DEFAULT 'VND',

    -- Coupon
    coupon_code     VARCHAR(50),
    coupon_discount DECIMAL(15,2) DEFAULT 0,

    -- Status
    status          VARCHAR(30) DEFAULT 'pending'
                    CHECK (status IN (
                        'pending',          -- Chờ xác nhận
                        'confirmed',        -- Đã xác nhận
                        'processing',       -- Đang xử lý
                        'shipping',         -- Đang giao hàng
                        'delivered',        -- Đã giao
                        'completed',        -- Hoàn thành
                        'cancelled',        -- Đã hủy
                        'refunded'          -- Đã hoàn tiền
                    )),

    -- Payment
    payment_status  VARCHAR(20) DEFAULT 'unpaid'
                    CHECK (payment_status IN ('unpaid','paid','partially_paid','refunded')),
    payment_method  VARCHAR(30),
    payment_ref     VARCHAR(255),
    paid_at         TIMESTAMPTZ,

    -- Shipping
    shipping_method VARCHAR(50),
    shipping_carrier VARCHAR(50),                   -- 'ghn','ghtk','vnpost'
    tracking_number VARCHAR(100),
    shipped_at      TIMESTAMPTZ,
    delivered_at    TIMESTAMPTZ,
    estimated_delivery DATE,

    -- Notes
    customer_note   TEXT,
    internal_note   TEXT,

    -- Source
    source          VARCHAR(20) DEFAULT 'website'
                    CHECK (source IN ('website','admin','api','pos')),

    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_ecom_orders_tenant ON ecom_orders(tenant_id);
CREATE INDEX idx_ecom_orders_status ON ecom_orders(tenant_id, status);
CREATE INDEX idx_ecom_orders_created ON ecom_orders(tenant_id, created_at DESC);
CREATE INDEX idx_ecom_orders_customer ON ecom_orders(tenant_id, customer_phone);


-- ============================================================
-- 5.10 RESTAURANT VERTICAL SCHEMA (Preview)
-- ============================================================

CREATE TABLE restaurant_menus (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    name            VARCHAR(255) NOT NULL,           -- 'Thực đơn buổi trưa'
    description     TEXT,
    is_active       BOOLEAN DEFAULT TRUE,
    display_order   INT DEFAULT 0,
    available_from  TIME,                            -- Giờ phục vụ bắt đầu
    available_to    TIME,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE restaurant_menu_items (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id),
    menu_id         UUID REFERENCES restaurant_menus(id),
    category_id     UUID REFERENCES categories(id),

    name            VARCHAR(255) NOT NULL,
    slug            VARCHAR(255),
    description     TEXT,
    image_url       VARCHAR(1000),

    price           DECIMAL(12,2) NOT NULL,
    original_price  DECIMAL(12,2),
    currency        CHAR(3) DEFAULT 'VND',

    -- Dietary info
    is_vegetarian   BOOLEAN DEFAULT FALSE,
    is_vegan        BOOLEAN DEFAULT FALSE,
    is_spicy        BOOLEAN DEFAULT FALSE,
    allergens       TEXT[] DEFAULT '{}',
    calories        INT,

    -- Status
    status          VARCHAR(20) DEFAULT 'available'
                    CHECK (status IN ('available','unavailable','seasonal')),
    is_featured     BOOLEAN DEFAULT FALSE,
    is_new          BOOLEAN DEFAULT FALSE,
    display_order   INT DEFAULT 0,

    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE restaurant_reservations (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id),

    -- Customer
    customer_name   VARCHAR(255) NOT NULL,
    customer_phone  VARCHAR(50) NOT NULL,
    customer_email  VARCHAR(255),

    -- Booking details
    reservation_date DATE NOT NULL,
    reservation_time TIME NOT NULL,
    party_size      SMALLINT NOT NULL,
    duration_minutes INT DEFAULT 90,
    table_number    VARCHAR(20),
    special_requests TEXT,

    -- Status
    status          VARCHAR(20) DEFAULT 'pending'
                    CHECK (status IN ('pending','confirmed','seated','completed','cancelled','no_show')),

    confirmed_at    TIMESTAMPTZ,
    reminder_sent   BOOLEAN DEFAULT FALSE,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_reservations_tenant_date
    ON restaurant_reservations(tenant_id, reservation_date);

-- ============================================================
-- 5.11 FEATURE FLAGS & SYSTEM CONFIG
-- ============================================================

CREATE TABLE feature_flags (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    flag_key        VARCHAR(100) UNIQUE NOT NULL,
    -- 'lottery.live_results', 'lottery.vip_predictions', 'global.maintenance'

    description     TEXT,
    is_enabled      BOOLEAN DEFAULT FALSE,          -- Global default

    -- Rollout strategy
    rollout_type    VARCHAR(20) DEFAULT 'all'
                    CHECK (rollout_type IN ('all','none','percentage','tenant_list','plan_list')),
    rollout_percent SMALLINT DEFAULT 0,             -- 0-100 cho percentage rollout
    enabled_tenants UUID[] DEFAULT '{}',            -- Whitelist specific tenants
    enabled_plans   UUID[] DEFAULT '{}',            -- Enable cho specific plans

    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE system_configs (
    key             VARCHAR(255) PRIMARY KEY,
    value           TEXT NOT NULL,
    value_type      VARCHAR(20) DEFAULT 'string'
                    CHECK (value_type IN ('string','number','boolean','json')),
    description     TEXT,
    is_sensitive    BOOLEAN DEFAULT FALSE,          -- Encrypted nếu sensitive
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

-- Seed system configs
INSERT INTO system_configs (key, value, value_type, description) VALUES
('platform.name',               'LotteryHub',      'string',  'Platform display name'),
('platform.domain',             'lotteryhub.vn',   'string',  'Platform root domain'),
('platform.support_email',      'support@...',     'string',  'Support email'),
('lottery.fetch_retry_max',     '3',               'number',  'Max retry khi fetch KQXS thất bại'),
('lottery.fetch_retry_delay',   '300',             'number',  'Giây chờ giữa các retry'),
('lottery.cache_ttl_seconds',   '300',             'number',  'TTL cache KQXS (giây)'),
('lottery.live_window_minutes', '30',              'number',  'Phút trước giờ quay bắt đầu live mode'),
('billing.trial_days',          '14',              'number',  'Số ngày trial mặc định'),
('billing.grace_period_days',   '3',               'number',  'Ngày gia hạn khi thanh toán trễ'),
('email.provider',              'resend',          'string',  'Email provider'),
('storage.provider',            'r2',              'string',  'File storage provider'),
('storage.max_file_mb',         '10',              'number',  'Max file size upload (MB)');

-- ============================================================
-- 5.12 JOBS & QUEUES TRACKING
-- ============================================================

CREATE TABLE job_executions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_name        VARCHAR(100) NOT NULL,
    -- 'lottery.fetch_results', 'stats.recalculate', 'billing.check_renewals'

    status          VARCHAR(20) DEFAULT 'running'
                    CHECK (status IN ('running','completed','failed','skipped')),

    -- Context
    tenant_id       UUID REFERENCES tenants(id),    -- NULL nếu là global job
    payload         JSONB DEFAULT '{}',             -- Input params
    result          JSONB DEFAULT '{}',             -- Output / summary

    -- Timing
    started_at      TIMESTAMPTZ DEFAULT NOW(),
    completed_at    TIMESTAMPTZ,
    duration_ms     INT,

    -- Error
    error_message   TEXT,
    stack_trace     TEXT,
    retry_count     SMALLINT DEFAULT 0,

    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_job_executions_name ON job_executions(job_name, created_at DESC);
CREATE INDEX idx_job_executions_status ON job_executions(status);

-- ============================================================
-- 5.13 VIEWS & MATERIALIZED VIEWS
-- ============================================================

-- View: Tenant đầy đủ thông tin (join thường dùng)
CREATE VIEW v_tenant_full AS
SELECT
    t.id,
    t.slug,
    t.name,
    t.status,
    t.active_vertical,
    t.active_theme,
    t.trial_ends_at,
    t.created_at,
    ts.logo_url,
    ts.primary_color,
    ts.secondary_color,
    ts.seo_title_template,
    ts.seo_default_title,
    ts.seo_default_desc,
    ts.google_analytics_id,
    ts.timezone,
    ts.locale,
    p.name          AS plan_name,
    p.slug          AS plan_slug,
    p.features      AS plan_features,
    p.allowed_verticals,
    d.domain        AS primary_domain,
    s.status        AS subscription_status,
    s.current_period_end AS subscription_expires_at
FROM tenants t
LEFT JOIN tenant_settings ts ON ts.tenant_id = t.id
LEFT JOIN plans p ON p.id = t.plan_id
LEFT JOIN domains d ON d.tenant_id = t.id AND d.is_primary = TRUE
LEFT JOIN subscriptions s ON s.tenant_id = t.id
WHERE t.deleted_at IS NULL;

-- Materialized View: KQXS hôm nay (refresh mỗi 5 phút)
CREATE MATERIALIZED VIEW mv_today_results AS
SELECT
    lr.id,
    lr.province_id,
    lp.code         AS province_code,
    lp.name         AS province_name,
    lp.slug         AS province_slug,
    lr.region_id,
    lrg.code        AS region_code,
    lrg.name        AS region_name,
    lr.draw_date,
    lr.special_prize,
    lr.first_prizes,
    lr.second_prizes,
    lr.third_prizes,
    lr.fourth_prizes,
    lr.fifth_prizes,
    lr.sixth_prizes,
    lr.seventh_prizes,
    lr.eighth_prizes,
    lr.loto_numbers,
    lr.is_verified,
    lr.updated_at
FROM lottery_results lr
JOIN lottery_provinces lp ON lp.id = lr.province_id
JOIN lottery_regions lrg ON lrg.id = lp.region_id
WHERE lr.draw_date = CURRENT_DATE
WITH DATA;

CREATE UNIQUE INDEX idx_mv_today_results ON mv_today_results(province_id);

-- Refresh command (gọi từ scheduler)
-- REFRESH MATERIALIZED VIEW CONCURRENTLY mv_today_results;

-- Materialized View: Lô gan top 20 (refresh mỗi đêm)
CREATE MATERIALIZED VIEW mv_top_gan AS
SELECT
    province_id,
    number,
    days_since_last,
    last_appeared,
    avg_cycle,
    trend,
    RANK() OVER (PARTITION BY province_id ORDER BY days_since_last DESC) AS rank
FROM lottery_frequency_stats
WHERE period_type = '90'
WITH DATA;

CREATE INDEX idx_mv_top_gan ON mv_top_gan(province_id, rank);

```

# PHẦN IV: BACKEND ARCHITECTURE (Java 21)
## 6. JAVA 21 SPRING BOOT ARCHITECTURE
### 6.1 Project Structure
```

saas-platform/
├── platform-parent/                    ← Maven parent POM
│   └── pom.xml
│
├── platform-core/                      ← Core shared library
│   └── src/main/java/com/platform/core/
│       ├── tenant/
│       │   ├── TenantContext.java
│       │   ├── TenantContextHolder.java
│       │   └── TenantResolutionFilter.java
│       ├── security/
│       ├── exception/
│       ├── audit/
│       └── utils/
│
├── platform-api/                       ← Main API application
│   └── src/main/java/com/platform/api/
│       ├── PlatformApiApplication.java
│       ├── config/
│       ├── modules/
│       │   ├── tenant/
│       │   ├── auth/
│       │   ├── cms/
│       │   ├── billing/
│       │   ├── media/
│       │   └── analytics/
│       └── verticals/
│           ├── lottery/
│           ├── realestate/
│           └── ecommerce/
│
├── platform-worker/                    ← Background jobs (Quartz/Spring Batch)
│   └── src/main/java/com/platform/worker/
│       ├── lottery/
│       │   ├── LotteryResultFetchJob.java
│       │   ├── StatsRecalculateJob.java
│       │   └── SeoPageGenerateJob.java
│       └── billing/
│           └── SubscriptionRenewalJob.java
│
└── platform-admin/                     ← Super admin API (separate service)
    └── src/main/java/com/platform/admin/
```
### 6.2 Core Dependencies (pom.xml)
```xml

<properties>
    <java.version>21</java.version>
    <spring-boot.version>3.2.0</spring-boot.version>
</properties>

<dependencies>
    <!-- Spring Boot Starters -->
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
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>

    <!-- Database -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
    <dependency>
        <groupId>org.flywaydb</groupId>
        <artifactId>flyway-core</artifactId>          <!-- DB Migration -->
    </dependency>

    <!-- Security -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.12.3</version>
    </dependency>

    <!-- Cache -->
    <dependency>
        <groupId>com.github.ben-manes.caffeine</groupId>
        <artifactId>caffeine</artifactId>            <!-- Local L1 cache -->
    </dependency>

    <!-- Job Scheduling -->
    <dependency>
        <groupId>org.quartz-scheduler</groupId>
        <artifactId>quartz</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-quartz</artifactId>
    </dependency>

    <!-- HTTP Client (scraping/API calls) -->
    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>4.12.0</version>
    </dependency>
    <dependency>
        <groupId>org.jsoup</groupId>
        <artifactId>jsoup</artifactId>              <!-- HTML parsing/scraping -->
        <version>1.17.1</version>
    </dependency>

    <!-- Utils -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>           <!-- DTO mapping -->
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <!-- Java 21 Virtual Threads support (built-in) -->
    <!-- Không cần dependency thêm -->
</dependencies>
```
### 6.3 Tenant Context (Thread-safe với Java 21)
```java

// TenantContext.java
public record TenantContext(
    String tenantId,
    String tenantSlug,
    String planSlug,
    String vertical,
    Map<String, Object> features
) {}

// TenantContextHolder.java
public class TenantContextHolder {

    // ThreadLocal hoạt động tốt với Virtual Threads (Java 21)
    // Dùng ScopedValue cho cleaner code (Java 21 preview)
    private static final ThreadLocal<TenantContext> CONTEXT = new ThreadLocal<>();

    public static void set(TenantContext ctx) {
        CONTEXT.set(ctx);
    }

    public static TenantContext get() {
        TenantContext ctx = CONTEXT.get();
        if (ctx == null) throw new TenantContextNotFoundException();
        return ctx;
    }

    public static String getTenantId() {
        return get().tenantId();
    }

    public static void clear() {
        CONTEXT.remove();
    }

    // Java 21 ScopedValue approach (preview feature)
    public static final ScopedValue<TenantContext> SCOPED = ScopedValue.newInstance();
}

// TenantResolutionFilter.java
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
@Slf4j
public class TenantResolutionFilter extends OncePerRequestFilter {

    private final TenantResolutionService resolutionService;
    private final TenantCacheService tenantCache;

    private static final String PLATFORM_DOMAIN = "saas-platform.vn";
    // Public paths không cần tenant context
    private static final Set<String> PUBLIC_PATHS = Set.of(
        "/actuator/health", "/api/v1/platform/webhook"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            chain.doFilter(request, response);
            return;
        }

        String host = extractHost(request);
        TenantContext ctx = resolutionService.resolve(host);

        if (ctx == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tenant not found");
            return;
        }

        if ("suspended".equals(ctx.tenantSlug())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Account suspended");
            return;
        }

        TenantContextHolder.set(ctx);
        // Cũng set vào MDC cho logging
        MDC.put("tenantId", ctx.tenantId());
        MDC.put("tenantSlug", ctx.tenantSlug());

        try {
            chain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
            MDC.clear();
        }
    }

    private String extractHost(HttpServletRequest request) {
        // Lấy từ X-Forwarded-Host nếu có (behind reverse proxy)
        String forwarded = request.getHeader("X-Forwarded-Host");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim().toLowerCase();
        }
        return request.getServerName().toLowerCase();
    }
}

// TenantResolutionService.java
@Service
@RequiredArgsConstructor
@Slf4j
public class TenantResolutionService {

    private final TenantRepository tenantRepository;
    private final DomainRepository domainRepository;

    // L1 Cache: Caffeine (in-memory, 5 phút)
    @Cacheable(value = "tenant-resolution", key = "#host")
    public TenantContext resolve(String host) {
        Tenant tenant = null;

        // Strategy 1: Subdomain
        if (host.endsWith("." + PLATFORM_DOMAIN)) {
            String slug = host.replace("." + PLATFORM_DOMAIN, "")
                              .replace("www.", "");
            tenant = tenantRepository.findBySlugAndDeletedAtIsNull(slug);
        }

        // Strategy 2: Custom domain lookup
        if (tenant == null) {
            tenant = domainRepository
                .findByDomainAndVerifiedTrue(host)
                .map(Domain::getTenant)
                .orElse(null);
        }

        if (tenant == null) return null;

        return new TenantContext(
            tenant.getId().toString(),
            tenant.getSlug(),
            tenant.getPlan().getSlug(),
            tenant.getActiveVertical(),
            tenant.getPlan().getFeatures()
        );
    }
}
```
### 6.4 Multi-tenant Repository Pattern
```java

// Base Repository với tenant isolation tự động
@NoRepositoryBean
public interface TenantBaseRepository<T, ID> extends JpaRepository<T, ID> {

    // Tất cả method tự động filter theo tenantId
    List<T> findAllByTenantId(UUID tenantId);
    Optional<T> findByIdAndTenantId(ID id, UUID tenantId);
    void deleteByIdAndTenantId(ID id, UUID tenantId);
    long countByTenantId(UUID tenantId);
}

// AOP Interceptor: Tự động inject tenantId vào mọi query
@Aspect
@Component
@RequiredArgsConstructor
public class TenantQueryInterceptor {

    @Around("execution(* com.platform..repository..*(..))")
    public Object injectTenantContext(ProceedingJoinPoint pjp) throws Throwable {
        // Với JPA: dùng Hibernate Filter
        // Với JDBC: dùng Spring Data JPA Specification
        return pjp.proceed();
    }
}

// Hibernate Filter approach
@FilterDef(
    name = "tenantFilter",
    parameters = @ParamDef(name = "tenantId", type = String.class)
)
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
@Entity
@Table(name = "articles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(nullable = false, length = 500)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "content_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> contentJson;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    // Vertical-specific metadata
    @Column(name = "vertical_meta", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> verticalMeta;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;
}

// Enable filter trong Session
@Component
@RequiredArgsConstructor
public class TenantFilterSessionCustomizer implements HibernatePropertiesCustomizer {

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        // Enable tenant filter tự động
    }
}

// Repository với filter
@Repository
public interface ArticleRepository extends TenantBaseRepository<Article, UUID> {

    // Spring Data JPA tự build query
    Optional<Article> findBySlugAndTenantIdAndDeletedAtIsNull(String slug, UUID tenantId);

    Page<Article> findByTenantIdAndStatusAndDeletedAtIsNull(
        UUID tenantId,
        ArticleStatus status,
        Pageable pageable
    );

    @Query("""
        SELECT a FROM Article a
        WHERE a.tenantId = :tenantId
        AND a.status = 'PUBLISHED'
        AND a.deletedAt IS NULL
        AND (
            to_tsvector('simple', a.title || ' ' || COALESCE(a.excerpt, ''))
            @@ plainto_tsquery('simple', :query)
        )
        ORDER BY a.publishedAt DESC
        """)
    Page<Article> fullTextSearch(
        @Param("tenantId") UUID tenantId,
        @Param("query") String query,
        Pageable pageable
    );
}
```
### 6.5 Lottery Result Fetcher (Java 21 Virtual Threads)
```java

// LotteryResultFetchJob.java
@Component
@RequiredArgsConstructor
@Slf4j
public class LotteryResultFetchJob {

    private final LotteryProvinceRepository provinceRepo;
    private final LotteryResultRepository resultRepo;
    private final LotteryScraperService scraperService;
    private final LotteryApiService apiService;
    private final LotteryStatsService statsService;
    private final FetchLogRepository fetchLogRepo;
    private final ApplicationEventPublisher eventPublisher;

    // Java 21 Virtual Thread Executor
    private final ExecutorService virtualThreadExecutor =
        Executors.newVirtualThreadPerTaskExecutor();

    @Scheduled(cron = "0 10 18 * * MON-SUN", zone = "Asia/Ho_Chi_Minh")
    public void fetchNorthResults() {
        fetchByRegion("north");
    }

    @Scheduled(cron = "0 35 17 * * MON-SUN", zone = "Asia/Ho_Chi_Minh")
    public void fetchCentralResults() {
        fetchByRegion("central");
    }

    @Scheduled(cron = "0 30 16 * * MON-SUN", zone = "Asia/Ho_Chi_Minh")
    public void fetchSouthResults() {
        fetchByRegion("south");
    }

    private void fetchByRegion(String regionCode) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        int dayOfWeek = today.getDayOfWeek().getValue() % 7; // 0=CN, 1=T2...

        List<LotteryProvince> provinces = provinceRepo
            .findByRegionCodeAndDrawDaysContaining(regionCode, dayOfWeek);

        log.info("Fetching {} results for {} provinces on {}",
            regionCode, provinces.size(), today);

        // Dùng Virtual Threads để fetch song song
        List<CompletableFuture<Void>> futures = provinces.stream()
            .map(province -> CompletableFuture.runAsync(
                () -> fetchProvinceResult(province, today),
                virtualThreadExecutor
            ))
            .toList();

        // Chờ tất cả hoàn thành
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> {
                log.info("All {} fetches completed for region {}", provinces.size(), regionCode);
                // Trigger recalculate stats sau khi có đủ kết quả
                statsService.recalculateForDate(today, regionCode);
                // Publish event để notify tenants
                eventPublisher.publishEvent(new ResultsFetchedEvent(regionCode, today));
            })
            .exceptionally(ex -> {
                log.error("Error in bulk fetch for region {}: {}", regionCode, ex.getMessage());
                return null;
            });
    }

    private void fetchProvinceResult(LotteryProvince province, LocalDate date) {
        FetchLog fetchLog = FetchLog.builder()
            .provinceId(province.getId())
            .drawDate(date)
            .status(FetchStatus.PENDING)
            .build();
        fetchLogRepo.save(fetchLog);

        int maxRetries = 3;
        int attempt = 0;
        Exception lastException = null;

        while (attempt < maxRetries) {
            attempt++;
            long startTime = System.currentTimeMillis();

            try {
                LotteryResultData data = fetchFromSource(province, date);

                if (data != null) {
                    saveResult(province, date, data);

                    fetchLog.setStatus(FetchStatus.SUCCESS);
                    fetchLog.setAttemptCount(attempt);
                    fetchLog.setResponseTimeMs((int)(System.currentTimeMillis() - startTime));
                    fetchLog.setFetchedAt(Instant.now());
                    fetchLogRepo.save(fetchLog);

                    log.info("✅ Fetched {} - {}", province.getCode(), date);
                    return;
                }

            } catch (Exception e) {
                lastException = e;
                log.warn("⚠️ Fetch attempt {}/{} failed for {} - {}: {}",
                    attempt, maxRetries, province.getCode(), date, e.getMessage());

                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(Duration.ofSeconds(30L * attempt)); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        // Tất cả retry thất bại
        fetchLog.setStatus(FetchStatus.FAILED);
        fetchLog.setAttemptCount(attempt);
        fetchLog.setErrorMessage(lastException != null ? lastException.getMessage() : "Unknown error");
        fetchLog.setNextRetryAt(Instant.now().plus(Duration.ofMinutes(15)));
        fetchLogRepo.save(fetchLog);

        log.error("❌ Failed to fetch {} - {} after {} attempts",
            province.getCode(), date, maxRetries);
    }

    private LotteryResultData fetchFromSource(LotteryProvince province, LocalDate date) {
        // Strategy: API first, fallback to scraper
        try {
            return apiService.fetchResult(province.getCode(), date);
        } catch (Exception e) {
            log.warn("API failed for {}, falling back to scraper: {}", province.getCode(), e.getMessage());
            return scraperService.scrape(province.getCode(), date);
        }
    }

    @Transactional
    private void saveResult(LotteryProvince province, LocalDate date, LotteryResultData data) {
        // Upsert result
        LotteryResult result = resultRepo
            .findByProvinceIdAndDrawDate(province.getId(), date)
            .orElse(LotteryResult.builder()
                .id(UUID.randomUUID())
                .provinceId(province.getId())
                .drawDate(date)
                .build());

        result.setSpecialPrize(data.getSpecialPrize());
        result.setFirstPrizes(data.getFirstPrizes());
        result.setSecondPrizes(data.getSecondPrizes());
        result.setThirdPrizes(data.getThirdPrizes());
        result.setFourthPrizes(data.getFourthPrizes());
        result.setFifthPrizes(data.getFifthPrizes());
        result.setSixthPrizes(data.getSixthPrizes());
        result.setSeventhPrizes(data.getSeventhPrizes());
        result.setEighthPrizes(data.getEighthPrizes());
        result.setLotoNumbers(extractLotoNumbers(data));
        result.setRawData(data.getRaw());
        result.setSource(data.getSource());
        result.setVerified(false);

        resultRepo.save(result);

        // Save loto detail (cho query thống kê)
        saveLotoDetail(province.getId(), date, result.getId(), result.getLotoNumbers());
    }

    private String[] extractLotoNumbers(LotteryResultData data) {
        // Lấy 2 số cuối của tất cả giải
        Set<String> lotoSet = new LinkedHashSet<>();

        Stream.of(
            data.getSpecialPrize() != null ?
                List.of(data.getSpecialPrize()) : List.<String>of(),
            safeList(data.getFirstPrizes()),
            safeList(data.getSecondPrizes()),
            safeList(data.getThirdPrizes()),
            safeList(data.getFourthPrizes()),
            safeList(data.getFifthPrizes()),
            safeList(data.getSixthPrizes()),
            safeList(data.getSeventhPrizes()),
            safeList(data.getEighthPrizes())
        )
        .flatMap(Collection::stream)
        .filter(s -> s != null && s.length() >= 2)
        .map(s -> s.substring(s.length() - 2))
        .forEach(lotoSet::add);

        return lotoSet.toArray(new String[0]);
    }

    private List<String> safeList(String[] arr) {
        return arr != null ? Arrays.asList(arr) : List.of();
    }

    @Transactional
    private void saveLotoDetail(UUID provinceId, LocalDate date, UUID resultId, String[] lotoNumbers) {
        // Delete old records cho ngày này nếu có
        lotoDetailRepo.deleteByProvinceIdAndDrawDate(provinceId, date);

        // Count occurrence của từng số
        Map<String, Long> countMap = Arrays.stream(lotoNumbers)
            .collect(Collectors.groupingBy(s -> s, Collectors.counting()));

        List<LotteryLotoDetail> details = countMap.entrySet().stream()
            .map(entry -> LotteryLotoDetail.builder()
                .provinceId(provinceId)
                .drawDate(date)
                .resultId(resultId)
                .number(entry.getKey())
                .occurrence(entry.getValue().intValue())
                .build())
            .toList();

        lotoDetailRepo.saveAll(details);
    }
}

// ============================================================
// LotteryScraperService.java (Jsoup scraping)
// ============================================================
@Service
@RequiredArgsConstructor
@Slf4j
public class LotteryScraperService {

    private final OkHttpClient httpClient;

    private static final Map<String, String> SCRAPER_URLS = Map.of(
        "mb",     "https://www.minhngoc.net.vn/ket-qua-xo-so/mien-bac/{date}.html",
        "tphcm",  "https://www.minhngoc.net.vn/ket-qua-xo-so/mien-nam/{date}.html",
        "danang", "https://www.minhngoc.net.vn/ket-qua-xo-so/mien-trung/{date}.html"
    );

    public LotteryResultData scrape(String provinceCode, LocalDate date) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String url = buildUrl(provinceCode, dateStr);

        try {
            Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .timeout(10_000)
                .get();

            return parseDocument(doc, provinceCode, date);

        } catch (IOException e) {
            throw new ScraperException("Failed to scrape " + provinceCode + " on " + date, e);
        }
    }

    private LotteryResultData parseDocument(Document doc, String provinceCode, LocalDate date) {
        LotteryResultData data = new LotteryResultData();
        data.setSource("scraper_minhngoc");

        // Parse based on province type
        if ("mb".equals(provinceCode)) {
            parseNorthResult(doc, data);
        } else {
            parseSouthCentralResult(doc, data, provinceCode);
        }

        return data;
    }

    private void parseNorthResult(Document doc, LotteryResultData data) {
        // Giải ĐB
        Element special = doc.selectFirst("td.giaiDB span");
        if (special != null) {
            data.setSpecialPrize(special.text().trim());
        }

        // Giải nhất
        Elements first = doc.select("td.giai1 span");
        data.setFirstPrizes(extractTexts(first));

        // Giải nhì
        Elements second = doc.select("td.giai2 span");
        data.setSecondPrizes(extractTexts(second));

        // Giải ba (6 số)
        Elements third = doc.select("td.giai3 span");
        data.setThirdPrizes(extractTexts(third));

        // Giải tư
        Elements fourth = doc.select("td.giai4 span");
        data.setFourthPrizes(extractTexts(fourth));

        // Giải năm
        Elements fifth = doc.select("td.giai5 span");
        data.setFifthPrizes(extractTexts(fifth));

        // Giải sáu
        Elements sixth = doc.select("td.giai6 span");
        data.setSixthPrizes(extractTexts(sixth));

        // Giải bảy
        Elements seventh = doc.select("td.giai7 span");
        data.setSeventhPrizes(extractTexts(seventh));
    }

    private String[] extractTexts(Elements elements) {
        return elements.stream()
            .map(e -> e.text().trim())
            .filter(s -> !s.isEmpty())
            .toArray(String[]::new);
    }

    private String buildUrl(String provinceCode, String dateStr) {
        String template = SCRAPER_URLS.getOrDefault(provinceCode,
            SCRAPER_URLS.get("tphcm")); // Default to south template
        return template.replace("{date}", dateStr);
    }
}

// ============================================================
// LotteryStatsService.java
// ============================================================
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LotteryStatsService {

    private final LotteryLotoDetailRepository lotoDetailRepo;
    private final LotteryFrequencyStatsRepository frequencyRepo;
    private final LotteryHeadTailStatsRepository headTailRepo;
    private final JdbcTemplate jdbcTemplate;

    private static final String[] PERIOD_TYPES = {"10","20","30","60","90","180","365","all"};

    public void recalculateForDate(LocalDate date, String regionCode) {
        log.info("Recalculating stats for region {} on {}", regionCode, date);

        List<UUID> provinceIds = getProvinceIdsByRegion(regionCode);

        // Dùng Virtual Threads để tính song song
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            provinceIds.forEach(provinceId ->
                executor.submit(() -> recalculateForProvince(provinceId, date))
            );
        }

        // Refresh materialized views sau khi tính xong
        refreshMaterializedViews();
    }

    public void recalculateForProvince(UUID provinceId, LocalDate refDate) {
        for (String period : PERIOD_TYPES) {
            recalculateFrequency(provinceId, refDate, period);
            recalculateHeadTail(provinceId, refDate, period);
        }
        log.debug("Stats recalculated for province {}", provinceId);
    }

    private void recalculateFrequency(UUID provinceId, LocalDate refDate, String periodType) {
        int days = periodTypeToDays(periodType);
        LocalDate fromDate = days == -1 ? LocalDate.of(2000, 1, 1) : refDate.minusDays(days);

        // Tính tần suất từng số 00-99
        String sql = """
            SELECT
                number,
                COUNT(*) AS frequency,
                COUNT(*) * 100.0 / NULLIF(:total_days, 0) AS frequency_pct,
                MAX(draw_date) AS last_appeared,
                :ref_date - MAX(draw_date) AS days_since_last,
                AVG(gap) AS avg_cycle,
                MAX(gap) AS max_gap,
                MIN(gap) AS min_gap
            FROM (
                SELECT
                    number,
                    draw_date,
                    draw_date - LAG(draw_date) OVER (
                        PARTITION BY number ORDER BY draw_date
                    ) AS gap
                FROM lottery_loto_detail
                WHERE province_id = :province_id
                AND draw_date BETWEEN :from_date AND :ref_date
            ) sub
            GROUP BY number
            """;

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql,
            Map.of(
                "province_id", provinceId,
                "from_date",   fromDate,
                "ref_date",    refDate,
                "total_days",  days == -1 ? 9999 : days
            )
        );

        // Xử lý số không xuất hiện trong period (frequency = 0)
        Set<String> appearedNumbers = rows.stream()
            .map(r -> (String) r.get("number"))
            .collect(Collectors.toSet());

        List<LotteryFrequencyStats> statsList = new ArrayList<>();

        // Các số đã xuất hiện
        for (Map<String, Object> row : rows) {
            String number = (String) row.get("number");
            int frequency = ((Number) row.get("frequency")).intValue();
            int daysSinceLast = row.get("days_since_last") != null ?
                ((Number) row.get("days_since_last")).intValue() : 9999;
            double avgCycle = row.get("avg_cycle") != null ?
                ((Number) row.get("avg_cycle")).doubleValue() : 0;

            String trend = calculateTrend(frequency, days, daysSinceLast, avgCycle);

            statsList.add(LotteryFrequencyStats.builder()
                .provinceId(provinceId)
                .number(number)
                .periodType(periodType)
                .frequency(frequency)
                .frequencyPct(row.get("frequency_pct") != null ?
                    ((Number) row.get("frequency_pct")).doubleValue() : 0)
                .lastAppeared(row.get("last_appeared") != null ?
                    ((java.sql.Date) row.get("last_appeared")).toLocalDate() : null)
                .daysSinceLast(daysSinceLast)
                .avgCycle(avgCycle)
                .maxGap(row.get("max_gap") != null ?
                    ((Number) row.get("max_gap")).intValue() : 0)
                .minGap(row.get("min_gap") != null ?
                    ((Number) row.get("min_gap")).intValue() : 0)
                .trend(trend)
                .updatedAt(Instant.now())
                .build());
        }

        // Các số không xuất hiện (gan hoàn toàn)
        for (int i = 0; i <= 99; i++) {
            String num = String.format("%02d", i);
            if (!appearedNumbers.contains(num)) {
                statsList.add(LotteryFrequencyStats.builder()
                    .provinceId(provinceId)
                    .number(num)
                    .periodType(periodType)
                    .frequency(0)
                    .frequencyPct(0.0)
                    .lastAppeared(null)
                    .daysSinceLast(9999)
                    .trend("frozen")
                    .updatedAt(Instant.now())
                    .build());
            }
        }

        // Bulk upsert
        frequencyRepo.upsertAll(statsList);
    }

    private String calculateTrend(int frequency, int periodDays, int daysSinceLast, double avgCycle) {
        double expectedFreq = periodDays == -1 ? 30 : (double) periodDays / 3.5;
        double ratio = frequency / expectedFreq;

        if (daysSinceLast > avgCycle * 2 && avgCycle > 0) return "frozen";
        if (daysSinceLast > avgCycle * 1.5 && avgCycle > 0) return "cold";
        if (ratio >= 1.5) return "hot";
        if (ratio >= 1.1) return "warm";
        return "neutral";
    }

    private int periodTypeToDays(String periodType) {
        return switch (periodType) {
            case "10"  -> 10;
            case "20"  -> 20;
            case "30"  -> 30;
            case "60"  -> 60;
            case "90"  -> 90;
            case "180" -> 180;
            case "365" -> 365;
            case "all" -> -1;
            default    -> 30;
        };
    }

    private void refreshMaterializedViews() {
        jdbcTemplate.execute("REFRESH MATERIALIZED VIEW CONCURRENTLY mv_today_results");
        jdbcTemplate.execute("REFRESH MATERIALIZED VIEW CONCURRENTLY mv_top_gan");
    }
}
```
## 7. API DESIGN
### 7.1 API Structure
```

Base URLs:
  Public API:       https://api.saas-platform.vn/v1
  Tenant Admin API: https://{tenant}.saas-platform.vn/api/admin/v1
  Super Admin API:  https://admin.saas-platform.vn/api/v1
  WebSocket:        wss://ws.saas-platform.vn

Headers bắt buộc:
  Authorization: Bearer {jwt_token}
  X-Tenant-ID:   {tenant_id}          (auto-inject từ domain)
  Content-Type:  application/json
  Accept-Language: vi-VN
```
### 7.2 API Endpoints chi tiết
```yaml

# ============================================================
# PUBLIC API (Không cần auth - Frontend gọi)
# ============================================================

# --- Lottery Results ---
GET  /v1/lottery/results/today
     ?region=north|central|south
     Response: { results: [...], fetchedAt, isLive }

GET  /v1/lottery/results/{date}
     # date format: yyyy-MM-dd
     ?region=north|central|south
     ?provinceCode=mb|tphcm|danang...
     Response: { results: [...] }

GET  /v1/lottery/results/province/{provinceCode}
     ?page=1&size=30
     Response: { items: [...], totalPages, totalItems }

GET  /v1/lottery/provinces
     ?region=north|central|south
     Response: { provinces: [...] }

GET  /v1/lottery/schedule
     ?week=current|next
     Response: { schedule: {Mon: [...], Tue: [...], ...} }

# --- Statistics ---
GET  /v1/lottery/stats/frequency
     ?provinceCode=mb
     &period=30                # 10|20|30|60|90|180|365|all
     &type=number|head|tail
     Response: { stats: [...], updatedAt }

GET  /v1/lottery/stats/gan
     ?provinceCode=mb
     &limit=20
     Response: { topGan: [...] }

GET  /v1/lottery/stats/special-prize
     ?provinceCode=mb
     &period=30
     Response: { headStats: [...], lastTwoStats: [...] }

# --- Predictions ---
GET  /v1/lottery/predictions
     ?provinceCode=mb
     &date=2024-01-15
     &type=bach_thu|xien_2|xien_3
     &page=1&size=10
     Response: { items: [...], totalPages }

GET  /v1/lottery/predictions/{id}
     Response: { prediction, author, result }

# --- Check ticket ---
POST /v1/lottery/check-ticket
     Body: { provinceCode, drawDate, ticketNumber }
     Response: { isWinner, prizeName, prizeValue, matchedNumber }

# --- CMS Public ---
GET  /v1/cms/articles
     ?category=tin-tuc&tag=soi-cau
     &page=1&size=10
     Response: { items: [...], totalPages }

GET  /v1/cms/articles/{slug}
     Response: { article, relatedArticles }

GET  /v1/cms/categories
     Response: { categories: [...] }

GET  /v1/cms/pages/{slug}
     Response: { page }

# --- SEO ---
GET  /v1/seo/sitemap.xml
     Response: XML sitemap

GET  /v1/seo/robots.txt
     Response: robots.txt content

# ============================================================
# TENANT ADMIN API (Cần JWT auth)
# ============================================================

# --- Auth ---
POST /admin/v1/auth/login
     Body: { email, password }
     Response: { accessToken, refreshToken, user }

POST /admin/v1/auth/refresh
     Body: { refreshToken }
     Response: { accessToken }

POST /admin/v1/auth/logout

GET  /admin/v1/auth/me
     Response: { user, tenant, plan }

# --- Dashboard ---
GET  /admin/v1/dashboard/stats
     Response: {
       pageViews: { today, week, month },
       topPages: [...],
       recentArticles: [...],
       systemStatus: {...}
     }

# --- Articles ---
GET    /admin/v1/articles?status=published&page=1&size=20
POST   /admin/v1/articles
PUT    /admin/v1/articles/{id}
DELETE /admin/v1/articles/{id}
POST   /admin/v1/articles/{id}/publish
POST   /admin/v1/articles/{id}/unpublish
POST   /admin/v1/articles/bulk-delete
       Body: { ids: [...] }

# --- Predictions (Lottery) ---
GET    /admin/v1/lottery/predictions?date=2024-01-15&province=mb
POST   /admin/v1/lottery/predictions
PUT    /admin/v1/lottery/predictions/{id}
DELETE /admin/v1/lottery/predictions/{id}
POST   /admin/v1/lottery/predictions/verify-results
       # Trigger manual check kết quả

# --- Settings ---
GET  /admin/v1/settings/general
PUT  /admin/v1/settings/general
     Body: { name, tagline, logo, favicon, ... }

GET  /admin/v1/settings/seo
PUT  /admin/v1/settings/seo

GET  /admin/v1/settings/lottery
PUT  /admin/v1/settings/lottery

GET  /admin/v1/settings/ads
PUT  /admin/v1/settings/ads

# --- Domains ---
GET    /admin/v1/domains
POST   /admin/v1/domains
       Body: { domain }
DELETE /admin/v1/domains/{id}
POST   /admin/v1/domains/{id}/verify
POST   /admin/v1/domains/{id}/set-primary

# --- Media ---
GET    /admin/v1/media?folder=/&type=image&page=1
POST   /admin/v1/media/upload
       Body: multipart/form-data
DELETE /admin/v1/media/{id}
POST   /admin/v1/media/bulk-delete

# --- Analytics ---
GET /admin/v1/analytics/overview
    ?period=7d|30d|90d
    Response: { pageViews, visitors, topPages, devices, sources }

# --- Users ---
GET    /admin/v1/users
POST   /admin/v1/users
PUT    /admin/v1/users/{id}
DELETE /admin/v1/users/{id}

# --- Billing ---
GET  /admin/v1/billing/subscription
GET  /admin/v1/billing/invoices
GET  /admin/v1/billing/invoices/{id}/download
POST /admin/v1/billing/upgrade
     Body: { planId, billingCycle }

# ============================================================
# SUPER ADMIN API
# ============================================================

GET    /superadmin/v1/tenants?status=active&page=1&size=20
GET    /superadmin/v1/tenants/{id}
POST   /superadmin/v1/tenants
PUT    /superadmin/v1/tenants/{id}
POST   /superadmin/v1/tenants/{id}/suspend
POST   /superadmin/v1/tenants/{id}/activate
POST   /superadmin/v1/tenants/{id}/impersonate

GET    /superadmin/v1/plans
POST   /superadmin/v1/plans
PUT    /superadmin/v1/plans/{id}

GET    /superadmin/v1/revenue/mrr
GET    /superadmin/v1/revenue/overview?period=12m

GET    /superadmin/v1/jobs/executions?status=failed&page=1
POST   /superadmin/v1/jobs/{jobName}/trigger

GET    /superadmin/v1/system/health
GET    /superadmin/v1/system/logs?level=error&page=1
```
### 7.3 WebSocket - Live Lottery Results
```java

// WebSocket Config
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS();
    }
}

// Live Result Publisher
@Service
@RequiredArgsConstructor
public class LiveResultPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    // Subscribe: /topic/lottery.results.{tenantId}.{regionCode}
    @EventListener
    public void onResultFetched(ResultsFetchedEvent event) {
        String topic = String.format(
            "/topic/lottery.results.%s", event.getRegionCode()
        );

        LiveResultMessage message = LiveResultMessage.builder()
            .regionCode(event.getRegionCode())
            .date(event.getDate().toString())
            .results(event.getResults())
            .fetchedAt(Instant.now().toString())
            .build();

        messagingTemplate.convertAndSend(topic, message);
        log.info("📡 Published live results to {}", topic);
    }

    // Heartbeat để giữ connection
    @Scheduled(fixedRate = 30_000)
    public void heartbeat() {
        messagingTemplate.convertAndSend("/topic/heartbeat",
            Map.of("timestamp", Instant.now().toString()));
    }
}
```
# PHẦN V: FRONTEND ARCHITECTURE
## 8. NEXT.JS 14 MULTI-TENANT FRONTEND
### 8.1 Project Structure
```

web/
├── app/
│   ├── (public)/                       ← Public tenant website
│   │   ├── layout.tsx                  ← Root layout (load tenant config)
│   │   ├── page.tsx                    ← Homepage
│   │   ├── ket-qua/
│   │   │   ├── page.tsx                ← KQXS hôm nay
│   │   │   ├── [date]/
│   │   │   │   └── page.tsx            ← KQXS theo ngày
│   │   │   └── [province]/
│   │   │       └── page.tsx            ← KQXS theo đài
│   │   ├── thong-ke/
│   │   │   ├── tan-suat/
│   │   │   │   └── page.tsx
│   │   │   └── lo-gan/
│   │   │       └── page.tsx
│   │   ├── soi-cau/
│   │   │   └── page.tsx
│   │   ├── tra-cuu/
│   │   │   └── page.tsx
│   │   ├── lich-xo-so/
│   │   │   └── page.tsx
│   │   ├── tin-tuc/
│   │   │   ├── page.tsx
│   │   │   ├── [category]/
│   │   │   │   └── page.tsx
│   │   │   └── [slug]/
│   │   │       └── page.tsx
│   │   └── [slug]/                     ← Static pages
│   │       └── page.tsx
│   │
│   └── admin/                          ← Tenant Admin Panel
│       ├── layout.tsx
│       ├── page.tsx                    ← Dashboard
│       ├── bai-viet/
│       ├── nhan-dinh/
│       ├── quang-cao/
│       ├── media/
│       ├── cai-dat/
│       ├── domain/
│       ├── billing/
│       └── analytics/
│
├── components/
│   ├── lottery/
│   │   ├── ResultTable.tsx             ← Bảng KQXS
│   │   ├── ResultTableNorth.tsx
│   │   ├── ResultTableSouth.tsx
│   │   ├── LiveCountdown.tsx           ← Đếm ngược
│   │   ├── FrequencyChart.tsx          ← Biểu đồ tần suất
│   │   ├── FrequencyGrid.tsx           ← Lưới 10x10
│   │   ├── GanList.tsx                 ← Danh sách lô gan
│   │   ├── PredictionCard.tsx          ← Card nhận định
│   │   ├── ScheduleCalendar.tsx        ← Lịch xổ số
│   │   ├── TicketChecker.tsx           ← Tra cứu vé
│   │   └── LiveResult.tsx              ← Real-time result
│   ├── cms/
│   │   ├── ArticleCard.tsx
│   │   ├── ArticleList.tsx
│   │   └── RichTextRenderer.tsx
│   ├── layout/
│   │   ├── Header.tsx
│   │   ├── Footer.tsx
│   │   ├── Sidebar.tsx
│   │   └── MobileNav.tsx
│   ├── admin/
│   │   ├── RichTextEditor.tsx          ← Tiptap editor
│   │   ├── MediaPicker.tsx
│   │   ├── DataTable.tsx
│   │   └── StatCard.tsx
│   └── shared/
│       ├── SEOHead.tsx
│       ├── AdSlot.tsx
│       ├── Breadcrumb.tsx
│       └── Pagination.tsx
│
├── lib/
│   ├── tenant.ts                       ← Tenant context
│   ├── api.ts                          ← API client
│   ├── lottery-utils.ts               ← Utility functions
│   └── seo.ts                          ← SEO helpers
│
├── hooks/
│   ├── useLotteryResults.ts
│   ├── useFrequencyStats.ts
│   └── useLiveResults.ts               ← WebSocket hook
│
└── middleware.ts                       ← Tenant resolution
```
### 8.2 Tenant Resolution Middleware
```typescript

// middleware.ts
import { NextRequest, NextResponse } from 'next/server'

const PLATFORM_DOMAIN = process.env.NEXT_PUBLIC_PLATFORM_DOMAIN || 'saas-platform.vn'

export async function middleware(request: NextRequest) {
  const hostname = request.headers.get('host') || ''
  const { pathname } = request.nextUrl

  // Skip static files và API routes
  if (
    pathname.startsWith('/_next') ||
    pathname.startsWith('/api') ||
    pathname.includes('.')
  ) {
    return NextResponse.next()
  }

  // Resolve tenant slug
  let tenantSlug: string | null = null

  if (hostname.endsWith(`.${PLATFORM_DOMAIN}`)) {
    // Subdomain: phuongnghi.saas-platform.vn
    tenantSlug = hostname
      .replace(`.${PLATFORM_DOMAIN}`, '')
      .replace('www.', '')
  } else if (hostname !== PLATFORM_DOMAIN && hostname !== `www.${PLATFORM_DOMAIN}`) {
    // Custom domain: xosophuongnghi.com.vn
    // Lookup từ API (có cache)
    tenantSlug = await resolveTenantFromCustomDomain(hostname)
  }

  if (!tenantSlug) {
    // Platform homepage
    return NextResponse.next()
  }

  // Inject tenant info vào headers để Server Components đọc
  const requestHeaders = new Headers(request.headers)
  requestHeaders.set('x-tenant-slug', tenantSlug)
  requestHeaders.set('x-hostname', hostname)

  return NextResponse.next({
    request: { headers: requestHeaders }
  })
}

// Cache custom domain resolution (edge cache)
const domainCache = new Map<string, { slug: string; cachedAt: number }>()
const CACHE_TTL = 5 * 60 * 1000 // 5 minutes

async function resolveTenantFromCustomDomain(hostname: string): Promise<string | null> {
  const cached = domainCache.get(hostname)
  if (cached && Date.now() - cached.cachedAt < CACHE_TTL) {
    return cached.slug
  }

  try {
    const res = await fetch(
      `${process.env.API_BASE_URL}/v1/platform/resolve-domain?domain=${hostname}`,
      { next: { revalidate: 300 } } // Next.js cache 5 phút
    )

    if (!res.ok) return null

    const data = await res.json()
    if (data.slug) {
      domainCache.set(hostname, { slug: data.slug, cachedAt: Date.now() })
      return data.slug
    }
  } catch (e) {
    console.error('Domain resolution failed:', e)
  }

  return null
}

export const config = {
  matcher: [
    '/((?!_next/static|_next/image|favicon.ico|robots.txt|sitemap.xml).*)'
  ]
}
```
### 8.3 Tenant Context Provider

```typescript

// lib/tenant.ts
export interface TenantConfig {
  id: string
  slug: string
  name: string
  vertical: 'lottery' | 'realestate' | 'ecommerce' | 'restaurant'
  theme: string
  settings: {
    logoUrl: string | null
    faviconUrl: string | null
    tagline: string | null
    primaryColor: string
    secondaryColor: string
    accentColor: string
    fontHeading: string
    fontBody: string
    phone: string | null
    email: string | null
    address: string | null
    socialLinks: Record<string, string>
    seoTitleTemplate: string
    seoDefaultTitle: string | null
    seoDefaultDesc: string | null
    seoOgImage: string | null
    googleAnalyticsId: string | null
    headerScripts: string | null
    footerScripts: string | null
    customCss: string | null
    timezone: string
    locale: string
  }
  plan: {
    slug: string
    features: Record<string, unknown>
  }
  primaryDomain: string | null
}

// Server-side: fetch tenant config
export async function getTenantConfig(slug: string): Promise<TenantConfig | null> {
  try {
    const res = await fetch(
      `${process.env.API_BASE_URL}/v1/platform/tenant/${slug}/config`,
      {
        next: { revalidate: 60, tags: [`tenant-${slug}`] } // ISR 60 giây
      }
    )

    if (!res.ok) return null
    return res.json()
  } catch {
    return null
  }
}

// app/(public)/layout.tsx
import { headers } from 'next/headers'
import { getTenantConfig } from '@/lib/tenant'
import { notFound } from 'next/navigation'

export default async function TenantLayout({
  children
}: {
  children: React.ReactNode
}) {
  const headersList = headers()
  const tenantSlug = headersList.get('x-tenant-slug')

  if (!tenantSlug) notFound()

  const tenant = await getTenantConfig(tenantSlug)
  if (!tenant) notFound()

  return (
    <html lang="vi">
      <head>
        {/* Dynamic favicon */}
        {tenant.settings.faviconUrl && (
          <link rel="icon" href={tenant.settings.faviconUrl} />
        )}

        {/* Google Fonts */}
        <link
          href={`https://fonts.googleapis.com/css2?family=${tenant.settings.fontHeading}:wght@400;600;700&family=${tenant.settings.fontBody}:wght@400;500&display=swap`}
          rel="stylesheet"
        />

        {/* Dynamic CSS variables */}
        <style>{`
          :root {
            --color-primary:   ${tenant.settings.primaryColor};
            --color-secondary: ${tenant.settings.secondaryColor};
            --color-accent:    ${tenant.settings.accentColor};
            --font-heading:    '${tenant.settings.fontHeading}', sans-serif;
            --font-body:       '${tenant.settings.fontBody}', sans-serif;
          }
        `}</style>

        {/* Custom CSS */}
        {tenant.settings.customCss && (
          <style>{tenant.settings.customCss}</style>
        )}

        {/* Header scripts (GA, GTM, etc.) */}
        {tenant.settings.headerScripts && (
          <div dangerouslySetInnerHTML={{ __html: tenant.settings.headerScripts }} />
        )}
      </head>

      <body>
        <TenantProvider tenant={tenant}>
          <Header tenant={tenant} />
          <main>{children}</main>
          <Footer tenant={tenant} />
        </TenantProvider>

        {/* Footer scripts */}
        {tenant.settings.footerScripts && (
          <div dangerouslySetInnerHTML={{ __html: tenant.settings.footerScripts }} />
        )}
      </body>
    </html>
  )
}
```
### 8.4 KQXS Page (Server Component + ISR)
```typescript

// app/(public)/ket-qua/page.tsx
import { Metadata } from 'next'
import { headers } from 'next/headers'
import { getTenantConfig } from '@/lib/tenant'
import { getLotteryResults } from '@/lib/api'
import { ResultTabs } from '@/components/lottery/ResultTabs'
import { LiveCountdown } from '@/components/lottery/LiveCountdown'
import { AdSlot } from '@/components/shared/AdSlot'
import { formatSeoTitle } from '@/lib/seo'
import { format } from 'date-fns'
import { vi } from 'date-fns/locale'

interface Props {
  searchParams: { date?: string; region?: string }
}

// Dynamic metadata
export async function generateMetadata({ searchParams }: Props): Promise<Metadata> {
  const headersList = headers()
  const tenantSlug = headersList.get('x-tenant-slug')!
  const tenant = await getTenantConfig(tenantSlug)

  const dateStr = searchParams.date || format(new Date(), 'dd/MM/yyyy')
  const title = formatSeoTitle(
    tenant?.settings.seoTitleTemplate || 'KQXS {date} - Kết quả xổ số hôm nay',
    { date: dateStr, siteName: tenant?.name || '' }
  )

  return {
    title,
    description: tenant?.settings.seoDefaultDesc ||
      `Kết quả xổ số ngày ${dateStr}. KQXS Miền Bắc, Miền Trung, Miền Nam nhanh và chính xác nhất.`,
    openGraph: {
      title,
      images: tenant?.settings.seoOgImage ? [tenant.settings.seoOgImage] : [],
    },
    alternates: {
      canonical: `/ket-qua${searchParams.date ? `?date=${searchParams.date}` : ''}`
    }
  }
}

export default async function KetQuaPage({ searchParams }: Props) {
  const headersList = headers()
  const tenantSlug = headersList.get('x-tenant-slug')!

  const date = searchParams.date || format(new Date(), 'yyyy-MM-dd')
  const region = searchParams.region || 'all'

  // Fetch data server-side
  const results = await getLotteryResults({ date, region })

  const today = format(new Date(), 'dd/MM/yyyy', { locale: vi })
  const isToday = !searchParams.date

  return (
    <div className="container mx-auto px-4 py-6">
      {/* Breadcrumb */}
      <nav className="text-sm text-gray-500 mb-4">
        <span>Trang chủ</span> {'>'} <span>Kết quả xổ số</span>
        {!isToday && <><span> {'>'} </span><span>{today}</span></>}
      </nav>

      {/* Page title */}
      <h1 className="text-2xl font-bold text-gray-800 mb-2">
        Kết Quả Xổ Số Hôm Nay - {today}
      </h1>

      {/* Ad slot - header */}
      <AdSlot position="content_top" tenantSlug={tenantSlug} />

      {/* Live countdown (chỉ hiện khi là hôm nay) */}
      {isToday && <LiveCountdown />}

      {/* Date navigation */}
      <DateNavigator currentDate={date} />

      {/* Results tabs */}
      <ResultTabs
        results={results}
        date={date}
        isLive={isToday}
      />

      {/* Ad slot - after results */}
      <AdSlot position="content_middle" tenantSlug={tenantSlug} />

      {/* Quick stats */}
      <QuickStatsSection date={date} />

      {/* SEO content */}
      <SeoContent date={date} />
    </div>
  )
}

// Revalidate mỗi 2 phút
export const revalidate = 120
```
### 8.5 Real-time Live Result Component
```typescript

// components/lottery/LiveResult.tsx
'use client'

import { useEffect, useState, useCallback } from 'react'
import { Client, IMessage } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

interface LiveResultProps {
  regionCode: string
  date: string
  initialResults: LotteryResult[]
}

export function LiveResult({ regionCode, date, initialResults }: LiveResultProps) {
  const [results, setResults] = useState(initialResults)
  const [isConnected, setIsConnected] = useState(false)
  const [isLive, setIsLive] = useState(false)
  const [lastUpdate, setLastUpdate] = useState<Date | null>(null)

  const checkIsLive = useCallback(() => {
    const now = new Date()
    const hours = now.getHours()
    const minutes = now.getMinutes()
    const totalMinutes = hours * 60 + minutes

    // Live window: 30 phút trước đến 30 phút sau giờ quay
    const liveWindows: Record<string, [number, number]> = {
      north:   [17 * 60 + 40, 18 * 60 + 40], // 17:40 - 18:40
      central: [16 * 60 + 45, 17 * 60 + 45], // 16:45 - 17:45
      south:   [15 * 60 + 45, 16 * 60 + 45], // 15:45 - 16:45
    }

    const window = liveWindows[regionCode]
    if (!window) return false

    const [start, end] = window
    return totalMinutes >= start && totalMinutes <= end
  }, [regionCode])

  useEffect(() => {
    const live = checkIsLive()
    setIsLive(live)

    if (!live) return

    // Connect WebSocket chỉ khi đang trong giờ live
    const client = new Client({
      webSocketFactory: () => new SockJS(
        `${process.env.NEXT_PUBLIC_WS_URL}/ws`
      ),
      onConnect: () => {
        setIsConnected(true)
        console.log('🔌 WebSocket connected')

        client.subscribe(
          `/topic/lottery.results.${regionCode}`,
          (message: IMessage) => {
            const data = JSON.parse(message.body)
            setResults(data.results)
            setLastUpdate(new Date())
          }
        )
      },
      onDisconnect: () => {
        setIsConnected(false)
        console.log('🔌 WebSocket disconnected')
      },
      reconnectDelay: 5000,
    })

    client.activate()

    return () => {
      client.deactivate()
    }
  }, [regionCode, checkIsLive])

  return (
    <div className="relative">
      {/* Live indicator */}
      {isLive && (
        <div className="flex items-center gap-2 mb-3">
          <span className="relative flex h-3 w-3">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75" />
            <span className="relative inline-flex rounded-full h-3 w-3 bg-red-500" />
          </span>
          <span className="text-red-500 font-semibold text-sm">
            ĐANG PHÁT TRỰC TIẾP
          </span>
          {isConnected && (
            <span className="text-green-500 text-xs">● Kết nối ổn định</span>
          )}
          {lastUpdate && (
            <span className="text-gray-400 text-xs ml-auto">
              Cập nhật: {lastUpdate.toLocaleTimeString('vi-VN')}
            </span>
          )}
        </div>
      )}

      {/* Results table */}
      <ResultTableAnimated results={results} isLive={isLive} />
    </div>
  )
}

// Animated table khi số mới xuất hiện
function ResultTableAnimated({
  results,
  isLive
}: {
  results: LotteryResult[]
  isLive: boolean
}) {
  const [newNumbers, setNewNumbers] = useState<Set<string>>(new Set())

  useEffect(() => {
    if (!isLive || !results.length) return

    // Mark numbers as new (flash animation 3 giây rồi tắt)
    const allNumbers = extractAllNumbers(results[results.length - 1])
    setNewNumbers(new Set(allNumbers))

    const timer = setTimeout(() => setNewNumbers(new Set()), 3000)
    return () => clearTimeout(timer)
  }, [results, isLive])

  return (
    <div className="overflow-x-auto">
      <table className="w-full border-collapse lottery-table">
        <tbody>
          {results.map((result) => (
            <ResultRow
              key={result.provinceCode}
              result={result}
              newNumbers={newNumbers}
            />
          ))}
        </tbody>
      </table>
    </div>
  )
}
```
### 8.6 Frequency Statistics Component
```typescript

// components/lottery/FrequencyGrid.tsx
'use client'

import { useMemo, useState } from 'react'
import { FrequencyStats } from '@/types/lottery'

interface FrequencyGridProps {
  stats: FrequencyStats[]
  period: string
  onPeriodChange: (period: string) => void
}

const PERIODS = [
  { label: '10 kỳ',  value: '10' },
  { label: '20 kỳ',  value: '20' },
  { label: '30 kỳ',  value: '30' },
  { label: '60 kỳ',  value: '60' },
  { label: '90 kỳ',  value: '90' },
  { label: '180 kỳ', value: '180' },
]

export function FrequencyGrid({ stats, period, onPeriodChange }: FrequencyGridProps) {
  const [viewMode, setViewMode] = useState<'frequency' | 'gan' | 'heatmap'>('frequency')

  const statsMap = useMemo(() => {
    return stats.reduce((acc, s) => {
      acc[s.number] = s
      return acc
    }, {} as Record<string, FrequencyStats>)
  }, [stats])

  const maxFreq = useMemo(() => Math.max(...stats.map(s => s.frequency)), [stats])
  const maxGan  = useMemo(() => Math.max(...stats.map(s => s.daysSinceLast)), [stats])

  const getCellColor = (number: string): string => {
    const stat = statsMap[number]
    if (!stat) return 'bg-gray-100'

    if (viewMode === 'frequency') {
      const ratio = stat.frequency / maxFreq
      if (ratio >= 0.8) return 'bg-red-500 text-white'
      if (ratio >= 0.6) return 'bg-orange-400 text-white'
      if (ratio >= 0.4) return 'bg-yellow-300'
      if (ratio >= 0.2) return 'bg-blue-200'
      return 'bg-gray-100 text-gray-400'
    }

    if (viewMode === 'gan') {
      const trend = stat.trend
      if (trend === 'frozen') return 'bg-purple-600 text-white'
      if (trend === 'cold')   return 'bg-blue-500 text-white'
      if (trend === 'hot')    return 'bg-red-500 text-white'
      if (trend === 'warm')   return 'bg-orange-400 text-white'
      return 'bg-gray-100'
    }

    return 'bg-gray-100'
  }

  return (
    <div className="bg-white rounded-xl shadow-sm border p-4">
      {/* Controls */}
      <div className="flex flex-wrap gap-3 mb-4">
        {/* Period selector */}
        <div className="flex gap-1 bg-gray-100 rounded-lg p-1">
          {PERIODS.map(p => (
            <button
              key={p.value}
              onClick={() => onPeriodChange(p.value)}
              className={`px-3 py-1 rounded-md text-sm font-medium transition-colors ${
                period === p.value
                  ? 'bg-white shadow text-primary-600 font-semibold'
                  : 'text-gray-600 hover:text-gray-800'
              }`}
            >
              {p.label}
            </button>
          ))}
        </div>

        {/* View mode */}
        <div className="flex gap-1 bg-gray-100 rounded-lg p-1">
          {[
            { value: 'frequency', label: 'Tần suất' },
            { value: 'gan',       label: 'Lô gan'   },
            { value: 'heatmap',   label: 'Heatmap'  },
          ].map(mode => (
            <button
              key={mode.value}
              onClick={() => setViewMode(mode.value as typeof viewMode)}
              className={`px-3 py-1 rounded-md text-sm transition-colors ${
                viewMode === mode.value
                  ? 'bg-white shadow font-semibold'
                  : 'text-gray-600'
              }`}
            >
              {mode.label}
            </button>
          ))}
        </div>
      </div>

      {/* Legend */}
      <div className="flex flex-wrap gap-3 mb-4 text-xs">
        {viewMode === 'frequency' && (
          <>
            <span className="flex items-center gap-1">
              <span className="w-3 h-3 rounded bg-red-500 inline-block" />
              Ra nhiều
            </span>
            <span className="flex items-center gap-1">
              <span className="w-3 h-3 rounded bg-orange-400 inline-block" />
              Khá nhiều
            </span>
            <span className="flex items-center gap-1">
              <span className="w-3 h-3 rounded bg-blue-200 inline-block" />
              Ít
            </span>
            <span className="flex items-center gap-1">
              <span className="w-3 h-3 rounded bg-gray-100 border inline-block" />
              Rất ít
            </span>
          </>
        )}
        {viewMode === 'gan' && (
          <>
            <span className="flex items-center gap-1">
              <span className="w-3 h-3 rounded bg-purple-600 inline-block" />
              Siêu gan
            </span>
            <span className="flex items-center gap-1">
              <span className="w-3 h-3 rounded bg-blue-500 inline-block" />
              Đang gan
            </span>
            <span className="flex items-center gap-1">
              <span className="w-3 h-3 rounded bg-red-500 inline-block" />
              Hot
            </span>
          </>
        )}
      </div>

      {/* 10x10 Grid */}
      <div className="grid grid-cols-10 gap-1">
        {Array.from({ length: 100 }, (_, i) => {
          const number = String(i).padStart(2, '0')
          const stat = statsMap[number]
          return (
            <div
              key={number}
              className={`
                relative aspect-square flex flex-col items-center justify-center
                rounded-lg cursor-pointer text-xs font-bold
                transition-all duration-200 hover:scale-110 hover:shadow-md
                ${getCellColor(number)}
              `}
              title={stat
                ? `Số ${number}: Ra ${stat.frequency} lần | Gan ${stat.daysSinceLast} ngày`
                : `Số ${number}`
              }
            >
              <span>{number}</span>
              {stat && (
                <span className="text-[9px] font-normal opacity-80">
                  {viewMode === 'frequency' ? stat.frequency : stat.daysSinceLast}
                </span>
              )}
            </div>
          )
        })}
      </div>

      {/* Top 10 table */}
      <div className="mt-6 grid grid-cols-2 gap-4">
        <TopNumbersTable
          title="🔥 Ra nhiều nhất"
          numbers={stats.sort((a, b) => b.frequency - a.frequency).slice(0, 10)}
          valueKey="frequency"
          valueLabel="Lần"
        />
        <TopNumbersTable
          title="❄️ Gan lâu nhất"
          numbers={stats.sort((a, b) => b.daysSinceLast - a.daysSinceLast).slice(0, 10)}
          valueKey="daysSinceLast"
          valueLabel="Ngày"
        />
      </div>
    </div>
  )
}

function TopNumbersTable({ title, numbers, valueKey, valueLabel }: {
  title: string
  numbers: FrequencyStats[]
  valueKey: keyof FrequencyStats
  valueLabel: string
}) {
  return (
    <div>
      <h4 className="font-semibold text-sm mb-2">{title}</h4>
      <div className="space-y-1">
        {numbers.map((stat, idx) => (
          <div key={stat.number} className="flex items-center gap-2 text-sm">
            <span className="w-5 text-gray-400 text-xs">{idx + 1}</span>
            <span className="font-bold text-red-500 w-8">{stat.number}</span>
            <div className="flex-1 bg-gray-100 rounded-full h-2">
              <div
                className="bg-red-400 h-2 rounded-full"
                style={{
                  width: `${(Number(stat[valueKey]) / Number(numbers[0][valueKey])) * 100}%`
                }}
              />
            </div>
            <span className="text-gray-600 text-xs w-16 text-right">
              {String(stat[valueKey])} {valueLabel}
            </span>
          </div>
        ))}
      </div>
    </div>
  )
}
```
# PHẦN VI: INFRASTRUCTURE & DEVOPS
## 9. INFRASTRUCTURE ARCHITECTURE
### 9.1 Tổng quan hạ tầng
```

┌─────────────────────────────────────────────────────────────┐
│                      CLOUDFLARE                             │
│  DNS + CDN + WAF + DDoS Protection + SSL                    │
│  ├── Wildcard: *.saas-platform.vn                           │
│  ├── Custom domains: CNAME → saas-platform.vn               │
│  └── Page Rules: Cache static, bypass API                   │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                   LOAD BALANCER (Nginx)                     │
│  ├── Rate limiting: 100 req/s per IP                        │
│  ├── SSL termination                                        │
│  └── Upstream routing                                       │
└───────────┬────────────────────────┬────────────────────────┘
            │                        │
┌───────────▼──────────┐  ┌──────────▼───────────┐
│   Next.js Frontend   │  │  Spring Boot API      │
│   (2-4 instances)    │  │  (2-4 instances)      │
│   Port: 3000         │  │  Port: 8080           │
│   Node.js 20         │  │  Java 21 / JVM        │
└───────────┬──────────┘  └──────────┬────────────┘
            │                        │
            └────────────┬───────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    DATA LAYER                               │
│                                                             │
│  ┌──────────────────┐  ┌──────────────────┐                │
│  │   PostgreSQL 15   │  │   Redis 7.2       │               │
│  │   Primary + Read  │  │   Cache + Pub/Sub │               │
│  │   Replica        │  │   + Sessions      │               │
│  └──────────────────┘  └──────────────────┘                │
│                                                             │
│  ┌──────────────────┐  ┌──────────────────┐                │
│  │  Cloudflare R2   │  │   Elasticsearch   │               │
│  │  File Storage    │  │   (Phase 2)       │               │
│  └──────────────────┘  └──────────────────┘                │
└─────────────────────────────────────────────────────────────┘
```
### 9.2 Docker Compose (Development)
```yaml

# docker-compose.yml
version: '3.9'

services:
  # PostgreSQL
  postgres:
    image: postgres:15-alpine
    container_name: saas_postgres
    environment:
      POSTGRES_DB:       saas_platform
      POSTGRES_USER:     saas_user
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./scripts/init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U saas_user -d saas_platform"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Redis
  redis:
    image: redis:7.2-alpine
    container_name: saas_redis
    command: redis-server --requirepass ${REDIS_PASSWORD} --maxmemory 512mb --maxmemory-policy allkeys-lru
    volumes:
      - redis_data:/data
    ports:
      - "6379:6379"
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 10s
      timeout: 5s
      retries: 5

  # Spring Boot API
  api:
    build:
      context: ./platform-api
      dockerfile: Dockerfile
    container_name: saas_api
    environment:
      SPRING_DATASOURCE_URL:      jdbc:postgresql://postgres:5432/saas_platform
      SPRING_DATASOURCE_USERNAME: saas_user
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
      SPRING_REDIS_HOST:          redis
      SPRING_REDIS_PASSWORD:      ${REDIS_PASSWORD}
      JWT_SECRET:                 ${JWT_SECRET}
      PLATFORM_DOMAIN:            saas-platform.vn
      STORAGE_R2_ACCESS_KEY:      ${R2_ACCESS_KEY}
      STORAGE_R2_SECRET_KEY:      ${R2_SECRET_KEY}
      STORAGE_R2_BUCKET:          ${R2_BUCKET}
      STORAGE_R2_ENDPOINT:        ${R2_ENDPOINT}
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy
      redis:
        condition: service_healthy
    deploy:
      resources:
        limits:
          memory: 1G
          cpus: '1.0'

  # Next.js Frontend
  web:
    build:
      context: ./web
      dockerfile: Dockerfile
    container_name: saas_web
    environment:
      NEXT_PUBLIC_API_URL:       http://api:8080
      NEXT_PUBLIC_WS_URL:        http://api:8080
      NEXT_PUBLIC_PLATFORM_DOMAIN: saas-platform.vn
      API_BASE_URL:              http://api:8080
    ports:
      - "3000:3000"
    depends_on:
      - api

  # Nginx Reverse Proxy
  nginx:
    image: nginx:alpine
    container_name: saas_nginx
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf:ro
      - ./nginx/conf.d:/etc/nginx/conf.d:ro
      - certbot_certs:/etc/letsencrypt:ro
    ports:
      - "80:80"
      - "443:443"
    depends_on:
      - api
      - web

volumes:
  postgres_data:
  redis_data:
  certbot_certs:
```
### 9.3 Nginx Configuration
```nginx

# nginx/conf.d/saas-platform.conf

# Rate limiting zones
limit_req_zone $binary_remote_addr zone=api_limit:10m rate=60r/m;
limit_req_zone $binary_remote_addr zone=web_limit:10m rate=200r/m;
limit_req_zone $binary_remote_addr zone=upload_limit:10m rate=10r/m;

# Cache zones
proxy_cache_path /var/cache/nginx/api levels=1:2
                 keys_zone=api_cache:10m max_size=100m
                 inactive=60m use_temp_path=off;

proxy_cache_path /var/cache/nginx/static levels=1:2
                 keys_zone=static_cache:10m max_size=500m
                 inactive=7d use_temp_path=off;

# Upstream servers
upstream api_servers {
    least_conn;
    server api:8080 weight=1 max_fails=3 fail_timeout=30s;
    keepalive 32;
}

upstream web_servers {
    least_conn;
    server web:3000 weight=1 max_fails=3 fail_timeout=30s;
    keepalive 16;
}

# HTTP → HTTPS redirect
server {
    listen 80;
    server_name _;
    return 301 https://$host$request_uri;
}

# Main HTTPS server (wildcard subdomain + custom domains)
server {
    listen 443 ssl http2;
    server_name *.saas-platform.vn saas-platform.vn;

    # SSL - Wildcard cert
    ssl_certificate     /etc/letsencrypt/live/saas-platform.vn/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/saas-platform.vn/privkey.pem;
    ssl_protocols       TLSv1.2 TLSv1.3;
    ssl_ciphers         ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512;
    ssl_session_cache   shared:SSL:10m;
    ssl_session_timeout 10m;

    # Security headers
    add_header X-Frame-Options           "SAMEORIGIN"   always;
    add_header X-XSS-Protection          "1; mode=block" always;
    add_header X-Content-Type-Options    "nosniff"      always;
    add_header Referrer-Policy           "strict-origin-when-cross-origin" always;
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;

    # Gzip compression
    gzip on;
    gzip_vary on;
    gzip_min_length 1024;
    gzip_types text/plain text/css text/xml text/javascript
               application/json application/javascript application/xml+rss
               image/svg+xml;

    # Client upload limit
    client_max_body_size 20M;

    # API routes
    location /api/ {
        limit_req zone=api_limit burst=20 nodelay;

        proxy_pass         http://api_servers;
        proxy_http_version 1.1;
        proxy_set_header   Connection        "";
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-For   $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto $scheme;
        proxy_set_header   X-Forwarded-Host  $host;

        proxy_connect_timeout 10s;
        proxy_send_timeout    30s;
        proxy_read_timeout    30s;

        # Cache GET requests
        proxy_cache          api_cache;
        proxy_cache_valid    200 2m;
        proxy_cache_valid    404 1m;
        proxy_cache_methods  GET HEAD;
        proxy_cache_key      "$host$request_uri";
        proxy_cache_bypass   $http_authorization $http_pragma;
        proxy_no_cache       $http_authorization;
        add_header           X-Cache-Status $upstream_cache_status;
    }

    # WebSocket
    location /ws {
        proxy_pass         http://api_servers;
        proxy_http_version 1.1;
        proxy_set_header   Upgrade    $http_upgrade;
        proxy_set_header   Connection "upgrade";
        proxy_set_header   Host       $host;
        proxy_read_timeout 86400s;  # 24h để giữ WS connection
    }

    # Media upload (rate limited chặt hơn)
    location /api/admin/v1/media/upload {
        limit_req zone=upload_limit burst=5 nodelay;
        proxy_pass http://api_servers;
        proxy_set_header Host $host;
        client_max_body_size 20M;
    }

    # Next.js static files (aggressive cache)
    location /_next/static/ {
        proxy_pass        http://web_servers;
        proxy_cache       static_cache;
        proxy_cache_valid 200 7d;
        add_header        Cache-Control "public, max-age=604800, immutable";
    }

    # Next.js image optimization
    location /_next/image {
        proxy_pass http://web_servers;
        proxy_cache       static_cache;
        proxy_cache_valid 200 1d;
    }

    # Frontend (Next.js)
    location / {
        limit_req zone=web_limit burst=50 nodelay;

        proxy_pass         http://web_servers;
        proxy_http_version 1.1;
        proxy_set_header   Connection        "";
        proxy_set_header   Host              $host;
        proxy_set_header   X-Real-IP         $remote_addr;
        proxy_set_header   X-Forwarded-For   $proxy_add_x_forwarded_for;
        proxy_set_header   X-Forwarded-Proto $scheme;
        proxy_set_header   X-Forwarded-Host  $host;

        proxy_connect_timeout 10s;
        proxy_read_timeout    30s;
    }
}

# Custom domains (dynamic SSL - Cloudflare SSL for SaaS hoặc Let's Encrypt)
server {
    listen 443 ssl http2;
    server_name ~^(?<custom_domain>.+)$;

    # Dynamic SSL cert lookup
    ssl_certificate_by_lua_block {
        -- OpenResty Lua: load cert dynamically từ Redis/DB
        local ssl = require "ngx.ssl"
        local host = ngx.var.custom_domain
        local cert_data = get_cert_from_cache(host)
        ssl.set_cert(cert_data.cert)
        ssl.set_priv_key(cert_data.key)
    }

    # Same proxy rules as above
    location / {
        proxy_pass       http://web_servers;
        proxy_set_header Host              $host;
        proxy_set_header X-Forwarded-Host  $host;
        proxy_set_header X-Real-IP         $remote_addr;
    }

    location /api/ {
        proxy_pass       http://api_servers;
        proxy_set_header Host             $host;
        proxy_set_header X-Forwarded-Host $host;
    }
}
```
### 9.4 Spring Boot Dockerfile (Java 21 Optimized)
``` dockerfile

# Dockerfile - Multi-stage build
# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper và POM trước (cache dependencies)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Copy source và build
COPY src ./src
RUN ./mvnw package -DskipTests -B

# Extract layers cho Docker layer caching
RUN java -Djarmode=layertools -jar target/*.jar extract

# Stage 2: Runtime (JRE only - nhỏ hơn JDK)
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Security: chạy với non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy layers theo thứ tự ít thay đổi → hay thay đổi
COPY --from=builder /app/dependencies/          ./
COPY --from=builder /app/spring-boot-loader/    ./
COPY --from=builder /app/snapshot-dependencies/ ./
COPY --from=builder /app/application/           ./

# Java 21 JVM tuning
ENV JAVA_OPTS="\
  -server \
  -XX:+UseZGC \
  -XX:+ZGenerational \
  -Xms512m \
  -Xmx1g \
  -XX:MaxMetaspaceSize=256m \
  -XX:+UseContainerSupport \
  -XX:ActiveProcessorCount=2 \
  -Djava.security.egd=file:/dev/./urandom \
  -Dspring.backgroundpreinitializer.ignore=true"

# Virtual Threads (Java 21 - GA)
ENV SPRING_THREADS_VIRTUAL_ENABLED=true

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]
```
### 9.5 Redis Caching Strategy
```java

// CacheConfig.java
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();

        // Tenant config: cache 5 phút (invalidate khi settings thay đổi)
        cacheConfigs.put("tenant-config",
            defaultConfig().entryTtl(Duration.ofMinutes(5)));

        // Domain resolution: cache 5 phút
        cacheConfigs.put("tenant-resolution",
            defaultConfig().entryTtl(Duration.ofMinutes(5)));

        // KQXS hôm nay: cache 2 phút (cập nhật thường xuyên khi đang quay)
        cacheConfigs.put("lottery-today",
            defaultConfig().entryTtl(Duration.ofMinutes(2)));

        // KQXS ngày cũ: cache 1 ngày (không thay đổi)
        cacheConfigs.put("lottery-history",
            defaultConfig().entryTtl(Duration.ofDays(1)));

        // Frequency stats: cache 30 phút (tính lại 1 lần/ngày)
        cacheConfigs.put("lottery-frequency",
            defaultConfig().entryTtl(Duration.ofMinutes(30)));

        // Articles: cache 10 phút
        cacheConfigs.put("articles",
            defaultConfig().entryTtl(Duration.ofMinutes(10)));

        // Plans: cache 1 giờ (ít thay đổi)
        cacheConfigs.put("plans",
            defaultConfig().entryTtl(Duration.ofHours(1)));

        // Sitemap: cache 1 giờ
        cacheConfigs.put("sitemap",
            defaultConfig().entryTtl(Duration.ofHours(1)));

        return RedisCacheManager.builder(factory)
            .cacheDefaults(defaultConfig().entryTtl(Duration.ofMinutes(10)))
            .withInitialCacheConfigurations(cacheConfigs)
            .transactionAware()
            .build();
    }

    private RedisCacheConfiguration defaultConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer()
                )
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer()
                )
            )
            .disableCachingNullValues()
            .prefixCacheNameWith("saas:");
    }
}

// Cache usage examples
@Service
@RequiredArgsConstructor
public class LotteryResultService {

    private final LotteryResultRepository resultRepo;
    private final RedisTemplate<String, Object> redisTemplate;

    @Cacheable(
        value    = "lottery-today",
        key      = "#region + ':' + T(java.time.LocalDate).now()",
        unless   = "#result == null"
    )
    public List<LotteryResultDTO> getTodayResults(String region) {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        return resultRepo.findByRegionAndDate(region, today)
            .stream()
            .map(this::toDTO)
            .toList();
    }

    @Cacheable(
        value  = "lottery-history",
        key    = "#provinceCode + ':' + #date",
        unless = "#result == null"
    )
    public LotteryResultDTO getResultByProvinceAndDate(String provinceCode, LocalDate date) {
        return resultRepo.findByProvinceCodeAndDrawDate(provinceCode, date)
            .map(this::toDTO)
            .orElse(null);
    }

    // Evict cache khi có kết quả mới
    @CacheEvict(value = "lottery-today", allEntries = true)
    @CachePut(
        value = "lottery-history",
        key   = "#result.provinceCode + ':' + #result.drawDate"
    )
    @Transactional
    public LotteryResultDTO saveResult(LotteryResultDTO result) {
        // Save to DB
        LotteryResult entity = toEntity(result);
        resultRepo.save(entity);
        return result;
    }

    // Manual cache invalidation
    public void invalidateTenantCache(String tenantSlug) {
        Set<String> keys = redisTemplate.keys("saas:tenant-config*" + tenantSlug + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
```
# PHẦN VII: SEO ENGINE
## 10. SEO ENGINE CHI TIẾT
### 10.1 Auto SEO Page Generation
```java

// SeoPageGeneratorService.java
@Service
@RequiredArgsConstructor
@Slf4j
public class SeoPageGeneratorService {

    private final LotteryProvinceRepository provinceRepo;
    private final LotteryResultRepository resultRepo;
    private final TenantRepository tenantRepo;
    private final SeoPageRepository seoPageRepo;

    // Chạy mỗi đêm lúc 22:00 - generate SEO pages cho ngày hôm sau
    @Scheduled(cron = "0 0 22 * * *", zone = "Asia/Ho_Chi_Minh")
    public void generateDailySeoPages() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Tenant> activeTenants = tenantRepo.findAllActiveWithLotteryVertical();

        log.info("Generating SEO pages for {} tenants on {}", activeTenants.size(), tomorrow);

        activeTenants.forEach(tenant ->
            generateForTenant(tenant, tomorrow)
        );
    }

    public void generateForTenant(Tenant tenant, LocalDate date) {
        LotteryTenantSettings settings = tenant.getLotterySettings();
        if (settings == null || !settings.isAutoSeoPages()) return;

        List<LotteryProvince> provinces = getEnabledProvinces(tenant);

        provinces.forEach(province -> {
            String slug = buildSlug(province, date);
            String title = interpolate(settings.getSeoTitleTemplate(), Map.of(
                "province_name", province.getName(),
                "province_short", province.getShortName(),
                "draw_date", formatDate(date),
                "site_name", tenant.getName()
            ));
            String description = interpolate(settings.getSeoDescTemplate(), Map.of(
                "province_name", province.getName(),
                "draw_date", formatDate(date),
                "site_name", tenant.getName()
            ));

            SeoPage page = SeoPage.builder()
                .tenantId(tenant.getId())
                .pageType("lottery_result")
                .slug(slug)
                .title(title)
                .description(description)
                .date(date)
                .provinceId(province.getId())
                .schemaJson(buildResultSchema(province, date, tenant))
                .build();

            seoPageRepo.upsert(page);
        });
    }

    private String buildResultSchema(LotteryProvince province, LocalDate date, Tenant tenant) {
        // JSON-LD Schema markup
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("@context", "https://schema.org");
        schema.put("@type", "Article");
        schema.put("headline",
            "Kết quả xổ số " + province.getName() + " ngày " + formatDate(date));
        schema.put("datePublished", date.toString());
        schema.put("dateModified",  Instant.now().toString());
        schema.put("publisher", Map.of(
            "@type", "Organization",
            "name",  tenant.getName(),
            "logo",  Map.of(
                "@type", "ImageObject",
                "url",   tenant.getSettings().getLogoUrl()
            )
        ));

        // BreadcrumbList
        Map<String, Object> breadcrumb = new LinkedHashMap<>();
        breadcrumb.put("@context", "https://schema.org");
        breadcrumb.put("@type", "BreadcrumbList");
        breadcrumb.put("itemListElement", List.of(
            Map.of("@type","ListItem","position",1,"name","Trang chủ","item","/"),
            Map.of("@type","ListItem","position",2,"name","Kết quả xổ số","item","/ket-qua"),
            Map.of("@type","ListItem","position",3,"name",province.getName()),
            Map.of("@type","ListItem","position",4,"name",formatDate(date))
        ));

        return toJson(List.of(schema, breadcrumb));
    }

    private String interpolate(String template, Map<String, String> vars) {
        String result = template;
        for (Map.Entry<String, String> entry : vars.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
```
### 10.2 Sitemap Generator
```java

// SitemapService.java
@Service
@RequiredArgsConstructor
public class SitemapService {

    private final LotteryResultRepository resultRepo;
    private final ArticleRepository articleRepo;
    private final LotteryProvinceRepository provinceRepo;
    private final TenantService tenantService;

    @Cacheable(value = "sitemap", key = "#tenantId")
    public String generateSitemap(UUID tenantId) {
        TenantConfig tenant = tenantService.getConfig(tenantId);
        String baseUrl = "https://" + tenant.getPrimaryDomain();
        LocalDate today = LocalDate.now();

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

        // Homepage
        appendUrl(xml, baseUrl + "/", today, "daily", "1.0");

        // KQXS hôm nay
        appendUrl(xml, baseUrl + "/ket-qua", today, "always", "0.9");

        // KQXS 90 ngày gần nhất theo đài
        List<LotteryProvince> provinces = provinceRepo.findAllActive();
        provinces.forEach(province -> {
            // Province page
            appendUrl(xml,
                baseUrl + "/ket-qua/" + province.getSlug(),
                today, "daily", "0.8");

            // Daily results (90 ngày)
            resultRepo.findDrawDatesByProvince(province.getId(), today.minusDays(90), today)
                .forEach(date ->
                    appendUrl(xml,
                        baseUrl + "/ket-qua/" + province.getSlug() + "/" +
                            date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        date, "monthly", "0.7")
                );
        });

        // Thống kê pages
        provinces.forEach(province -> {
            appendUrl(xml, baseUrl + "/thong-ke/tan-suat/" + province.getSlug(),
                today, "daily", "0.7");
            appendUrl(xml, baseUrl + "/thong-ke/lo-gan/" + province.getSlug(),
                today, "daily", "0.7");
        });

        // Blog articles
        articleRepo.findPublishedByTenant(tenantId)
            .forEach(article ->
                appendUrl(xml,
                    baseUrl + "/tin-tuc/" + article.getSlug(),
                    article.getPublishedAt().atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate(),
                    "monthly", "0.6")
            );

        xml.append("</urlset>");
        return xml.toString();
    }

    private void appendUrl(StringBuilder xml, String loc, LocalDate lastmod,
                           String changefreq, String priority) {
        xml.append("  <url>\n");
        xml.append("    <loc>").append(escapeXml(loc)).append("</loc>\n");
        xml.append("    <lastmod>").append(lastmod).append("</lastmod>\n");
        xml.append("    <changefreq>").append(changefreq).append("</changefreq>\n");
        xml.append("    <priority>").append(priority).append("</priority>\n");
        xml.append("  </url>\n");
    }

    private String escapeXml(String s) {
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
```
# PHẦN VIII: BILLING & PAYMENTS
## 11. BILLING SYSTEM
### 11.1 Subscription Flow
```

Tenant đăng ký plan:

[Chọn Plan] → [Nhập thông tin] → [Chọn payment method]
     ↓
[Tạo Invoice pending]
     ↓
[Redirect sang Payment Gateway]
     ↓
[Gateway callback / webhook]
     ↓
┌─────────────┬─────────────────┐
│  SUCCESS    │    FAILED       │
│             │                 │
│ Activate    │ Mark invoice    │
│ subscription│ failed          │
│ Send welcome│ Send retry email│
│ email       │ Retry 3 lần     │
└─────────────┴─────────────────┘

Auto-renewal flow:
[3 ngày trước hết hạn] → [Gửi email nhắc]
[Ngày hết hạn] → [Auto charge]
     ↓
[Thành công] → [Gia hạn subscription]
[Thất bại] → [Grace period 3 ngày]
     ↓
[Sau grace period] → [Suspend tenant]
[7 ngày suspended] → [Cancel & archive]
11.2 Payment Integration (MoMo + PayOS)
```
```java

// PaymentService.java
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final MomoPaymentGateway momoGateway;
    private final PayOsGateway payOsGateway;
    private final InvoiceRepository invoiceRepo;
    private final PaymentRepository paymentRepo;
    private final SubscriptionService subscriptionService;
    private final NotificationService notificationService;

    public PaymentInitResponse initiatePayment(UUID invoiceId, String method) {
        Invoice invoice = invoiceRepo.findById(invoiceId)
            .orElseThrow(() -> new NotFoundException("Invoice not found"));

        Payment payment = Payment.builder()
            .id(UUID.randomUUID())
            .invoiceId(invoiceId)
            .tenantId(invoice.getTenantId())
            .amount(invoice.getTotal())
            .currency(invoice.getCurrency())
            .status(PaymentStatus.PENDING)
            .paymentMethod(method)
            .build();

        paymentRepo.save(payment);

        return switch (method) {
            case "momo"   -> momoGateway.createPayment(payment, invoice);
            case "payos"  -> payOsGateway.createPayment(payment, invoice);
            case "manual" -> handleManualPayment(payment, invoice);
            default       -> throw new InvalidPaymentMethodException(method);
        };
    }

    @Transactional
    public void handleWebhook(String method, Map<String, String> payload) {
        PaymentResult result = switch (method) {
            case "momo"  -> momoGateway.parseWebhook(payload);
            case "payos" -> payOsGateway.parseWebhook(payload);
            default      -> throw new InvalidPaymentMethodException(method);
        };

        Payment payment = paymentRepo.findByGatewayTransactionId(result.getTransactionId())
            .orElseThrow(() -> new NotFoundException("Payment not found"));

        if (result.isSuccess()) {
            completePayment(payment);
        } else {
            failPayment(payment, result.getFailureReason());
        }
    }

    @Transactional
    private void completePayment(Payment payment) {
        // Update payment
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setCompletedAt(Instant.now());
        paymentRepo.save(payment);

        // Update invoice
        Invoice invoice = invoiceRepo.findById(payment.getInvoiceId()).orElseThrow();
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setAmountPaid(payment.getAmount());
        invoice.setPaidAt(Instant.now());
        invoiceRepo.save(invoice);

        // Activate/Renew subscription
        subscriptionService.activateForInvoice(invoice);

        // Send success email
        notificationService.sendPaymentSuccess(payment.getTenantId(), invoice);

        log.info("✅ Payment completed: tenantId={}, amount={} VND",
            payment.getTenantId(), payment.getAmount());
    }

    @Transactional
    private void failPayment(Payment payment, String reason) {
        payment.setStatus(PaymentStatus.FAILED);
        payment.setFailedAt(Instant.now());
        payment.setFailureReason(reason);
        paymentRepo.save(payment);

        // Send failure notification
        notificationService.sendPaymentFailed(payment.getTenantId(), reason);

        log.warn("❌ Payment failed: tenantId={}, reason={}", payment.getTenantId(), reason);
    }
}

// MoMo Gateway Implementation
@Component
@RequiredArgsConstructor
public class MomoPaymentGateway {

    @Value("${payment.momo.partner-code}")
    private String partnerCode;

    @Value("${payment.momo.access-key}")
    private String accessKey;

    @Value("${payment.momo.secret-key}")
    private String secretKey;

    @Value("${payment.momo.endpoint}")
    private String endpoint;

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PaymentInitResponse createPayment(Payment payment, Invoice invoice) {
        String orderId    = payment.getId().toString();
        String requestId  = UUID.randomUUID().toString();
        String orderInfo  = "Thanh toan " + invoice.getInvoiceNumber();
        String returnUrl  = "https://saas-platform.vn/billing/payment/return";
        String notifyUrl  = "https://api.saas-platform.vn/v1/webhooks/momo";
        String amount     = String.valueOf(invoice.getTotal().longValue());

        String rawSignature = String.format(
            "accessKey=%s&amount=%s&extraData=&ipnUrl=%s&orderId=%s" +
            "&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=payWithATM",
            accessKey, amount, notifyUrl, orderId,
            orderInfo, partnerCode, returnUrl, requestId
        );

        String signature = hmacSHA256(rawSignature, secretKey);

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("partnerCode",   partnerCode);
        requestBody.put("accessKey",     accessKey);
        requestBody.put("requestId",     requestId);
        requestBody.put("amount",        amount);
        requestBody.put("orderId",       orderId);
        requestBody.put("orderInfo",     orderInfo);
        requestBody.put("redirectUrl",   returnUrl);
        requestBody.put("ipnUrl",        notifyUrl);
        requestBody.put("extraData",     "");
        requestBody.put("requestType",   "payWithATM");
        requestBody.put("signature",     signature);
        requestBody.put("lang",          "vi");

        try {
            RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                .url(endpoint + "/v2/gateway/api/create")
                .post(body)
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                Map<String, Object> result = objectMapper.readValue(
                    response.body().string(), Map.class
                );

                int resultCode = (int) result.get("resultCode");
                if (resultCode == 0) {
                    return PaymentInitResponse.builder()
                        .paymentUrl((String) result.get("payUrl"))
                        .transactionId(requestId)
                        .build();
                } else {
                    throw new PaymentGatewayException(
                        "MoMo error: " + result.get("message")
                    );
                }
            }
        } catch (Exception e) {
            throw new PaymentGatewayException("MoMo payment creation failed", e);
        }
    }

    public PaymentResult parseWebhook(Map<String, String> payload) {
        // Verify signature
        String receivedSig = payload.get("signature");
        String rawData = buildWebhookRawData(payload);
        String expectedSig = hmacSHA256(rawData, secretKey);

        if (!expectedSig.equals(receivedSig)) {
            throw new InvalidSignatureException("MoMo webhook signature invalid");
        }

        int resultCode = Integer.parseInt(payload.get("resultCode"));
        return PaymentResult.builder()
            .transactionId(payload.get("orderId"))
            .isSuccess(resultCode == 0)
            .failureReason(resultCode != 0 ? payload.get("message") : null)
            .gatewayTransactionId(payload.get("transId"))
            .amount(new BigDecimal(payload.get("amount")))
            .build();
    }

    private String hmacSHA256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA256 failed", e);
        }
    }
}
```
# PHẦN IX: PRICING & BUSINESS MODEL
## 12. PRICING MODEL
### 12.1 Bảng giá đề xuất (Vertical Xổ Số)
```

┌─────────────────┬──────────────┬──────────────┬──────────────┬──────────────┐
│   Tính năng     │   STARTER    │     PRO      │   BUSINESS   │  ENTERPRISE  │
│                 │  99,000đ/th  │  299,000đ/th │  599,000đ/th │ Liên hệ      │
├─────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Subdomain       │      ✅      │      ✅      │      ✅      │      ✅      │
│ Custom domain   │      ❌      │      ✅      │      ✅      │      ✅      │
│ SSL tự động     │      ✅      │      ✅      │      ✅      │      ✅      │
├─────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ KQXS tất cả đài │      ✅      │      ✅      │      ✅      │      ✅      │
│ Live result     │      ✅      │      ✅      │      ✅      │      ✅      │
│ Thống kê cơ bản │      ✅      │      ✅      │      ✅      │      ✅      │
│ Thống kê nâng cao│     ❌      │      ✅      │      ✅      │      ✅      │
│ Soi cầu system  │      ❌      │      ✅      │      ✅      │      ✅      │
│ Soi cầu VIP     │      ❌      │      ❌      │      ✅      │      ✅      │
│ Tra cứu vé số   │      ✅      │      ✅      │      ✅      │      ✅      │
├─────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Bài viết        │     20 bài   │  Không giới  │  Không giới  │  Không giới  │
│                 │              │     hạn      │     hạn      │     hạn      │
│ Admin users     │      1       │      3       │      10      │  Không giới  │
│                 │              │              │              │     hạn      │
│ Storage         │    500 MB    │    2 GB      │    10 GB     │  Không giới  │
│                 │              │              │              │     hạn      │
├─────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Ad slots        │      2       │      5       │  Không giới  │  Không giới  │
│                 │              │              │     hạn      │     hạn      │
│ Google AdSense  │      ✅      │      ✅      │      ✅      │      ✅      │
│ Custom banner   │      ❌      │      ✅      │      ✅      │      ✅      │
├─────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ SEO tự động     │   Cơ bản     │   Nâng cao   │   Nâng cao   │  Tùy chỉnh  │
│ Sitemap         │      ✅      │      ✅      │      ✅      │      ✅      │
│ Analytics       │   Cơ bản     │   Đầy đủ     │   Đầy đủ     │  Tùy chỉnh  │
│ GA Integration  │      ✅      │      ✅      │      ✅      │      ✅      │
├─────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Remove branding │      ❌      │      ❌      │      ✅      │      ✅      │
│ Custom CSS      │      ❌      │      ✅      │      ✅      │      ✅      │
│ API Access      │      ❌      │      ❌      │      ✅      │      ✅      │
│ White label     │      ❌      │      ❌      │      ❌      │      ✅      │
├─────────────────┼──────────────┼──────────────┼──────────────┼──────────────┤
│ Support         │    Email     │  Email 24h   │  Priority    │  Dedicated   │
│ Onboarding      │    Self      │    Self      │   Guided     │   Full       │
└─────────────────┴──────────────┴──────────────┴──────────────┴──────────────┘

Yearly discount: Giảm 20% khi đăng ký năm
Trial: 14 ngày miễn phí, không cần thẻ
```
### 12.2 Revenue Projection
```

Giả định sau 12 tháng:

Starter  (99k/th):  200 tenants × 99,000  =  19,800,000đ/th
Pro      (299k/th): 100 tenants × 299,000 =  29,900,000đ/th
Business (599k/th):  30 tenants × 599,000 =  17,970,000đ/th
Enterprise:          10 tenants × 2,000,000 = 20,000,000đ/th
                                            ─────────────────
Total MRR:                                   87,670,000đ/th
ARR:                                        ~1,052,040,000đ/năm

Chi phí vận hành ước tính/tháng:
├── Server (2 VPS × 500k):       1,000,000đ
├── Database (managed):          2,000,000đ
├── Redis (managed):               500,000đ
├── Cloudflare (Pro):              500,000đ
├── KQXS API provider:           3,000,000đ
├── Email service (Resend):        500,000đ
├── Storage (R2):                  300,000đ
├── Monitoring:                    200,000đ
└── Total COGS:                  8,000,000đ/th

Gross Margin: ~91%
```
# PHẦN X: ROADMAP TRIỂN KHAI
## 13. DEVELOPMENT ROADMAP
Phase 1 — Foundation (Tuần 1-4)
```

Sprint 1 (Tuần 1-2): Core Infrastructure
├── [ ] Setup project structure (monorepo)
├── [ ] Docker Compose development environment
├── [ ] PostgreSQL schema + Flyway migrations (Core tables)
├── [ ] Spring Boot base project
│   ├── [ ] TenantResolutionFilter
│   ├── [ ] TenantContextHolder
│   ├── [ ] JWT Authentication
│   ├── [ ] Base Repository pattern
│   └── [ ] Global exception handler
├── [ ] Next.js base project
│   ├── [ ] Middleware tenant resolution
│   ├── [ ] TenantProvider context
│   └── [ ] Base layout components
└── [ ] CI/CD Pipeline (GitHub Actions)

Sprint 2 (Tuần 3-4): Platform Core
├── [ ] Super Admin: Tenant CRUD
├── [ ] Super Admin: Plan management
├── [ ] Tenant registration flow
├── [ ] Tenant Admin: Login/Auth
├── [ ] Tenant Admin: Settings (General, SEO)
├── [ ] Domain management (subdomain auto, custom domain verify)
├── [ ] Media upload (R2/S3)
└── [ ] Email system (welcome, notifications)
Phase 2 — Lottery Vertical (Tuần 5-10)


Sprint 3 (Tuần 5-6): KQXS Core
├── [ ] Lottery DB schema (provinces, results, loto_detail)
├── [ ] Seed data: 63 tỉnh/đài đầy đủ
├── [ ] Scraper service (Miền Bắc, Nam, Trung)
├── [ ] Quartz scheduler (fetch jobs)
├── [ ] KQXS API endpoints
├── [ ] Frontend: ResultTable components
│   ├── [ ] ResultTableNorth
│   ├── [ ] ResultTableSouth
│   └── [ ] ResultTableCentral
├── [ ] Frontend: KQXS hôm nay page
└── [ ] Frontend: KQXS theo ngày page

Sprint 4 (Tuần 7-8): Statistics & Live
├── [ ] LotteryStatsService (frequency calculation)
├── [ ] Materialized views setup
├── [ ] Stats API endpoints
├── [ ] Frontend: FrequencyGrid (10×10 heatmap)
├── [ ] Frontend: GanList component
├── [ ] Frontend: Head-tail stats
├── [ ] WebSocket setup (Spring + STOMP)
├── [ ] Frontend: LiveResult component
├── [ ] Countdown timer component
└── [ ] Schedule/Calendar page

Sprint 5 (Tuần 9-10): Predictions & CMS
├── [ ] Prediction CRUD (Admin)
├── [ ] Auto-verify predictions after KQXS
├── [ ] Author stats calculation
├── [ ] Frontend: PredictionCard component
├── [ ] Frontend: Soi cầu page
├── [ ] CMS: Article editor (TipTap)
├── [ ] CMS: Category management
├── [ ] Frontend: Blog listing + detail pages
├── [ ] Ticket checker feature
└── [ ] Ad management (AdSense + custom banner)
Phase 3 — SEO & Billing (Tuần 11-14)

Sprint 6 (Tuần 11-12): SEO Engine + Analytics
├── [ ] Auto SEO page generator
├── [ ] Sitemap dynamic generation
├── [ ] Schema markup (JSON-LD)
├── [ ] robots.txt dynamic
├── [ ] Page view tracking (lightweight)
├── [ ] Analytics daily aggregation job
├── [ ] Frontend: Analytics dashboard
├── [ ] Print-friendly stylesheet
└── [ ] Social share buttons (Facebook, Zalo)

Sprint 7 (Tuần 13-14): Billing System
├── [ ] Subscription management
├── [ ] Invoice generation
├── [ ] MoMo payment integration
├── [ ] PayOS payment integration
├── [ ] Bank transfer (manual confirm)
├── [ ] Auto-renewal job
├── [ ] Grace period & suspension logic
├── [ ] Billing dashboard (tenant)
├── [ ] Revenue dashboard (super admin)
└── [ ] Coupon/discount system
Phase 4 — Polish & Launch (Tuần 15-16)

Sprint 8 (Tuần 15-16): Production Ready
├── [ ] Performance optimization
│   ├── [ ] Database query optimization (EXPLAIN ANALYZE)
│   ├── [ ] Redis caching fine-tuning
│   ├── [ ] Next.js bundle optimization
│   └── [ ] Image optimization pipeline
├── [ ] Security audit
│   ├── [ ] SQL injection prevention
│   ├── [ ] XSS protection
│   ├── [ ] Rate limiting tuning
│   └── [ ] OWASP checklist
├── [ ] Load testing (k6 / JMeter)
│   ├── [ ] Target: 1000 concurrent users
│   └── [ ] Target: < 200ms p95 response time
├── [ ] Monitoring setup
│   ├── [ ] Sentry (error tracking)
│   ├── [ ] Grafana + Prometheus (metrics)
│   └── [ ] Uptime Robot (availability)
├── [ ] Documentation
│   ├── [ ] API docs (Swagger/OpenAPI)
│   ├── [ ] Tenant onboarding guide
│   └── [ ] Admin user manual
└── [ ] Soft launch (10 beta tenants)
Phase 5 — Multi-Vertical Expansion (Tháng 5+)

Tháng 5-6: Real Estate Vertical
├── [ ] BDS listing CRUD
├── [ ] Advanced search & filter
├── [ ] Map integration (Google Maps)
├── [ ] Image gallery
├── [ ] Contact form / Lead capture
└── [ ] BDS-specific SEO pages

Tháng 7-8: E-Commerce Vertical
├── [ ] Product catalog
├── [ ] Shopping cart
├── [ ] Order management
├── [ ] Payment integration (COD, banking)
├── [ ] Inventory management
└── [ ] Shipping integration (GHN, GHTK)

Tháng 9-10: Restaurant Vertical
├── [ ] Menu management
├── [ ] Online reservation
├── [ ] QR menu
├── [ ] Table management
└── [ ] Order tracking

Tháng 11-12: Platform Marketplace
├── [ ] Vertical marketplace (tenant chọn vertical)
├── [ ] Theme marketplace
├── [ ] Plugin system
└── [ ] API for third-party integrations
```
# PHẦN XI: RỦI RO & GIẢI PHÁP
## 14. RISK ANALYSIS & MITIGATION
### 14.1 Rủi ro kỹ thuật
```

┌──────────────────────────────┬────────────┬────────────────────────────────────┐
│ Rủi ro                       │ Mức độ     │ Giải pháp                          │
├──────────────────────────────┼────────────┼────────────────────────────────────┤
│ KQXS source bị block/down    │ HIGH       │ - Multi-source: 3+ scraper targets │
│                               │            │ - Fallback API provider dự phòng   │
│                               │            │ - Manual entry khi khẩn cấp        │
│                               │            │ - Alert ngay khi fetch fail 2 lần  │
├──────────────────────────────┼────────────┼────────────────────────────────────┤
│ Data leak giữa tenants       │ CRITICAL   │ - RLS PostgreSQL bắt buộc           │
│                               │            │ - Audit tất cả query có tenant_id  │
│                               │            │ - Integration tests kiểm tra       │
│                               │            │   cross-tenant isolation           │
│                               │            │ - Penetration testing trước launch │
├──────────────────────────────┼────────────┼────────────────────────────────────┤
│ DB performance khi scale     │ MEDIUM     │ - Index strategy từ đầu            │
│                               │            │ - Partition lottery_results by year│
│                               │            │ - Read replica cho stats queries   │
│                               │            │ - Materialized views cache stats   │
│                               │            │ - PgBouncer connection pooling     │
├──────────────────────────────┼────────────┼────────────────────────────────────┤
│ Downtime ảnh hưởng tenants   │ HIGH       │ - Blue/Green deployment            │
│                               │            │ - Health checks & auto-restart     │
│                               │            │ - DB backup mỗi 6 giờ             │
│                               │            │ - SLA 99.9% uptime cam kết         │
├──────────────────────────────┼────────────┼────────────────────────────────────┤
│ SSL cert expire custom domain│ MEDIUM     │ - Auto-renew 30 ngày trước expire  │
│                               │            │ - Alert 14 ngày trước              │
│                               │            │ - Cloudflare SSL for SaaS (paid)   │
├──────────────────────────────┼────────────┼────────────────────────────────────┤
│ "Noisy neighbor" problem     │ MEDIUM     │ - Rate limiting per tenant          │
│ (1 tenant consume quá nhiều) │            │ - CPU/Memory limits per container  │
│                               │            │ - Alert khi tenant > 80% quota     │
│                               │            │ - Auto-upgrade prompt              │
└──────────────────────────────┴────────────┴────────────────────────────────────┘
```
### 14.2 Rủi ro kinh doanh
```

┌──────────────────────────────┬────────────┬────────────────────────────────────┐
│ Rủi ro                       │ Mức độ     │ Giải pháp                          │
├──────────────────────────────┼────────────┼────────────────────────────────────┤
│ Pháp lý: Xổ số online VN     │ HIGH       │ - Chỉ cung cấp THÔNG TIN, không    │
│                               │            │   liên quan đến cá cược/đặt cược  │
│                               │            │ - Terms of Service rõ ràng         │
│                               │            │ - Không lưu thông tin người dùng   │
│                               │            │   cuối của tenant                  │
│                               │            │ - Tham khảo luật sư trước launch   │
├──────────────────────────────┼────────────┼────────────────────────────────────┤
│ Cạnh tranh: Tenant tự làm    │ LOW        │ - Giá trị: tốc độ, auto-update,    │
│ WordPress                     │            │   không cần dev                     │
│                               │            │ - Time-to-market < 1 giờ           │
│                               │            │ - SEO auto-generation = USP        │
├──────────────────────────────┼────────────┼────────────────────────────────────┤
│ Churn rate cao                │ MEDIUM     │ - Onboarding tốt (video guides)    │
│                               │            │ - Customer success program         │
│                               │            │ - Feature releases thường xuyên    │
│                               │            │ - Exit survey khi cancel           │
├──────────────────────────────┼────────────┼────────────────────────────────────┤
│ Copyright dữ liệu KQXS       │ MEDIUM     │ - KQXS là thông tin công khai      │
│                               │            │ - Scrape từ nhiều nguồn            │
│                               │            │ - Không copy nguyên si nội dung    │
└──────────────────────────────┴────────────┴────────────────────────────────────┘
```
# PHẦN XII: CHECKLIST TRƯỚC KHI CODE
## 15. PRE-DEVELOPMENT CHECKLIST
### 15.1 Architecture Decisions Cần Confirm
```

DATABASE:
[ ] Confirm: Shared DB + tenant_id (Phase 1)
[ ] Confirm: PostgreSQL RLS bật từ đầu
[ ] Confirm: Flyway cho DB migrations
[ ] Confirm: Partition strategy cho lottery_results & page_views
[ ] Confirm: Materialized view refresh schedule

BACKEND:
[ ] Confirm: Java 21 + Spring Boot 3.2
[ ] Confirm: Virtual Threads enabled (spring.threads.virtual.enabled=true)
[ ] Confirm: JPA + Hibernate (không dùng MyBatis)
[ ] Confirm: Quartz cho scheduled jobs
[ ] Confirm: Caffeine (L1) + Redis (L2) cache strategy
[ ] Confirm: JWT secret rotation strategy
[ ] Confirm: API versioning (/v1, /v2...)

FRONTEND:
[ ] Confirm: Next.js 14 App Router
[ ] Confirm: TypeScript strict mode
[ ] Confirm: Tailwind CSS + shadcn/ui
[ ] Confirm: ISR (Incremental Static Regeneration) strategy
[ ] Confirm: SWR hoặc React Query cho client fetching

INFRASTRUCTURE:
[ ] Confirm: Hosting provider (Railway/Render/DigitalOcean/AWS)
[ ] Confirm: Managed PostgreSQL hay self-hosted
[ ] Confirm: Managed Redis hay self-hosted
[ ] Confirm: File storage: Cloudflare R2 (cheapest) hay AWS S3
[ ] Confirm: Cloudflare cho DNS/CDN
[ ] Confirm: Email provider (Resend recommended)
[ ] Confirm: Custom domain SSL strategy

LOTTERY DATA:
[ ] Confirm: Scraping targets (minhngoc.net.vn, xosomienbac.vn)
[ ] Confirm: API provider backup (nếu có ngân sách)
[ ] Confirm: Fetch schedule theo múi giờ VN
[ ] Confirm: Retry strategy khi fetch thất bại
```
### 15.2 Naming Conventions
```

DATABASE:
- Tables:          snake_case, plural       (lottery_results, tenant_users)
- Columns:         snake_case               (draw_date, province_id)
- Primary keys:    id (UUID)
- Foreign keys:    {table_singular}_id      (tenant_id, province_id)
- Timestamps:      created_at, updated_at, deleted_at
- Booleans:        is_{name}                (is_active, is_featured)
- Enums:           stored as VARCHAR        (không dùng PG ENUM type)
- Indexes:         idx_{table}_{columns}    (idx_articles_tenant_id)

JAVA:
- Packages:        com.platform.{module}
- Classes:         PascalCase
- Methods:         camelCase
- Constants:       UPPER_SNAKE_CASE
- DTOs:            {Name}Request / {Name}Response / {Name}DTO
- Entities:        {Name} (không suffix)
- Services:        {Name}Service
- Repositories:    {Name}Repository
- Controllers:     {Name}Controller
- Jobs:            {Name}Job

API:
- Endpoints:       kebab-case              (/lottery-results, /tenant-users)
- Query params:    camelCase               (?provinceCode=mb&startDate=...)
- Response:        camelCase JSON
- Error codes:     UPPER_SNAKE             (TENANT_NOT_FOUND, PLAN_LIMIT_EXCEEDED)

TYPESCRIPT/REACT:
- Components:      PascalCase              (ResultTable, FrequencyGrid)
- Hooks:           use{Name}               (useLotteryResults, useTenant)
- Utils:           camelCase files         (lottery-utils.ts, seo.ts)
- Types/Interfaces: PascalCase            (TenantConfig, LotteryResult)
- CSS classes:     Tailwind utility-first
```
### 15.3 Environment Variables Template
```bash

# .env.example

# ============ DATABASE ============
DATABASE_URL=jdbc:postgresql://localhost:5432/saas_platform
DATABASE_USERNAME=saas_user
DATABASE_PASSWORD=your_secure_password
DATABASE_POOL_SIZE=20

# ============ REDIS ============
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=your_redis_password
REDIS_DATABASE=0

# ============ JWT ============
JWT_SECRET=your_256_bit_secret_key_here
JWT_EXPIRY_MINUTES=60
JWT_REFRESH_EXPIRY_DAYS=30

# ============ PLATFORM ============
PLATFORM_DOMAIN=saas-platform.vn
PLATFORM_API_URL=https://api.saas-platform.vn
PLATFORM_ADMIN_URL=https://admin.saas-platform.vn
PLATFORM_SUPER_ADMIN_EMAIL=admin@saas-platform.vn
PLATFORM_SUPER_ADMIN_PASSWORD=initial_password_change_immediately

# ============ STORAGE (Cloudflare R2) ============
R2_ACCESS_KEY=your_r2_access_key
R2_SECRET_KEY=your_r2_secret_key
R2_BUCKET=saas-platform-media
R2_ENDPOINT=https://your-account-id.r2.cloudflarestorage.com
R2_PUBLIC_URL=https://media.saas-platform.vn

# ============ EMAIL (Resend) ============
RESEND_API_KEY=re_your_api_key
EMAIL_FROM=noreply@saas-platform.vn
EMAIL_FROM_NAME=SaaS Platform

# ============ PAYMENTS ============
MOMO_PARTNER_CODE=your_partner_code
MOMO_ACCESS_KEY=your_access_key
MOMO_SECRET_KEY=your_secret_key
MOMO_ENDPOINT=https://payment.momo.vn

PAYOS_CLIENT_ID=your_client_id
PAYOS_API_KEY=your_api_key
PAYOS_CHECKSUM_KEY=your_checksum_key

# ============ LOTTERY DATA ============
LOTTERY_FETCH_ENABLED=true
LOTTERY_API_PROVIDER_URL=https://api.lottery-provider.vn
LOTTERY_API_KEY=your_api_key
LOTTERY_SCRAPER_ENABLED=true
LOTTERY_SCRAPER_USER_AGENT=Mozilla/5.0 (compatible; SaaSBot/1.0)

# ============ MONITORING ============
SENTRY_DSN=https://your_sentry_dsn
SENTRY_ENVIRONMENT=production
SENTRY_TRACES_SAMPLE_RATE=0.1

# ============ NEXT.JS (Frontend) ============
NEXT_PUBLIC_API_URL=https://api.saas-platform.vn
NEXT_PUBLIC_WS_URL=wss://api.saas-platform.vn
NEXT_PUBLIC_PLATFORM_DOMAIN=saas-platform.vn
NEXT_PUBLIC_PLATFORM_NAME=SaaS Platform
API_BASE_URL=http://api:8080  # Internal Docker network

```
### 15.4 Git Workflow & Branch Strategy
```

main                    ← Production (protected, deploy tự động)
├── develop             ← Staging (merge từ feature branches)
│   ├── feature/lottery-scraper
│   ├── feature/billing-momo
│   ├── feature/tenant-admin-dashboard
│   └── fix/kqxs-display-bug
└── hotfix/critical-bug ← Hotfix trực tiếp vào main

Commit convention (Conventional Commits):
feat(lottery): add frequency stats calculation
fix(auth): resolve JWT token expiry bug
docs(api): update KQXS endpoint documentation
chore(deps): upgrade Spring Boot to 3.2.1
test(scraper): add unit tests for Minhngoc parser
refactor(tenant): extract domain resolution to service
perf(db): add index on lottery_results draw_date

PR Rules:
- Minimum 1 reviewer approval
- All CI checks pass
- No merge conflicts
- Test coverage > 70%
16. QUICK START GUIDE
16.1 Khởi động Development Environment
bash

# 1. Clone repository
git clone https://github.com/your-org/saas-platform.git
cd saas-platform

# 2. Copy environment files
cp .env.example .env
# Edit .env với các giá trị thực

# 3. Start infrastructure
docker-compose up -d postgres redis

# 4. Run DB migrations (Flyway tự chạy khi start Spring Boot)
cd platform-api
./mvnw spring-boot:run -Dspring.profiles.active=dev

# 5. Start Next.js frontend
cd ../web
npm install
npm run dev

# 6. Seed initial data
curl -X POST http://localhost:8080/api/v1/platform/seed \
  -H "X-Platform-Admin-Key: your_seed_key"

# 7. Access points:
# Frontend:    http://localhost:3000
# API:         http://localhost:8080
# API Docs:    http://localhost:8080/swagger-ui.html
# Super Admin: http://admin.localhost:3000
16.2 First Tenant Setup Flow
bash

# 1. Tạo Super Admin account
POST http://localhost:8080/superadmin/v1/setup
{
  "email": "admin@platform.vn",
  "password": "secure_password",
  "setupKey": "INITIAL_SETUP_KEY"
}

# 2. Tạo Plans
POST http://localhost:8080/superadmin/v1/plans
Authorization: Bearer {super_admin_token}
{
  "name": "Starter",
  "slug": "starter",
  "priceMonthly": 99000,
  "priceYearly": 950000,
  "features": {
    "custom_domain": false,
    "lottery_predictions": false,
    "analytics": "basic"
  }
}

# 3. Tenant đăng ký
POST http://localhost:8080/v1/platform/register
{
  "tenantName": "Xổ Số Phương Nghi",
  "slug": "phuongnghi",
  "email": "owner@phuongnghi.vn",
  "password": "tenant_password",
  "planSlug": "starter",
  "vertical": "lottery"
}

# 4. Tenant truy cập website
# http://phuongnghi.localhost:3000  (development)
# https://phuongnghi.saas-platform.vn (production)

```
## 17. TỔNG KẾT KIẾN TRÚC
```

┌─────────────────────────────────────────────────────────────────────┐
│                    PLATFORM ARCHITECTURE SUMMARY                    │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  TENANTS ──→ Custom Domain / Subdomain                             │
│                       │                                             │
│              Cloudflare (DNS + CDN + SSL)                          │
│                       │                                             │
│              Nginx (Reverse Proxy + Rate Limit)                    │
│                       │                                             │
│        ┌──────────────┴───────────────┐                            │
│        │                              │                            │
│   Next.js 14                   Spring Boot 3.2                     │
│   (Frontend SSR/ISR)           (Java 21 + Virtual Threads)         │
│   - Tenant layout              - Multi-tenant API                  │
│   - Lottery pages              - JWT Auth                          │
│   - Admin panel                - Quartz Jobs                       │
│   - SEO optimization           - WebSocket (STOMP)                 │
│                                - Scraper/Fetcher                   │
│        │                              │                            │
│        └──────────────┬───────────────┘                            │
│                       │                                             │
│        ┌──────────────┴───────────────┐                            │
│        │                              │                            │
│   PostgreSQL 15                   Redis 7.2                        │
│   - Shared DB + RLS               - L2 Cache                       │
│   - Partitioned tables            - Sessions                       │
│   - Materialized views            - Pub/Sub (Live)                 │
│   - Full-text search              - Job queues                     │
│        │                                                            │
│   Cloudflare R2                                                     │
│   - Media storage                                                   │
│   - Tenant isolation by prefix                                      │
│                                                                     │
├─────────────────────────────────────────────────────────────────────┤
│  VERTICALS:  Lottery ✅  │  Real Estate 🔜  │  E-Commerce 🔜       │
├─────────────────────────────────────────────────────────────────────┤
│  PLANS:      Starter │ Pro │ Business │ Enterprise                  │
├─────────────────────────────────────────────────────────────────────┤
│  PAYMENTS:   MoMo │ PayOS │ Bank Transfer │ Stripe (future)        │
└─────────────────────────────────────────────────────────────────────┘
```
# PHẦN XIII: APPENDIX - CODE TEMPLATES
## 18. SPRING BOOT BASE CODE TEMPLATES
### 18.1 Base Entity
```java

// BaseEntity.java - Tất cả entities kế thừa
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
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

// TenantBaseEntity.java - Entities thuộc về tenant
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TenantBaseEntity extends BaseEntity {

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    // Auto-inject tenantId trước khi persist
    @PrePersist
    protected void onPrePersist() {
        if (this.tenantId == null) {
            this.tenantId = UUID.fromString(TenantContextHolder.getTenantId());
        }
    }
}
```
### 18.2 Standard API Response
```java

// ApiResponse.java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String  message;
    private T       data;
    private String  errorCode;
    private Map<String, String> errors;  // Validation errors
    private long    timestamp = Instant.now().toEpochMilli();

    // Factory methods
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

    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return ApiResponse.<T>builder()
            .success(false)
            .errorCode(errorCode)
            .message(message)
            .build();
    }

    public static <T> ApiResponse<T> validationError(Map<String, String> errors) {
        return ApiResponse.<T>builder()
            .success(false)
            .errorCode("VALIDATION_ERROR")
            .message("Dữ liệu không hợp lệ")
            .errors(errors)
            .build();
    }
}

// PageResponse.java - Cho paginated results
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> items;
    private int     page;
    private int     size;
    private long    totalItems;
    private int     totalPages;
    private boolean hasNext;
    private boolean hasPrev;

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
### 18.3 Global Exception Handler
```java

// GlobalExceptionHandler.java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TenantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleTenantNotFound(TenantNotFoundException ex) {
        return ApiResponse.error("TENANT_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleNotFound(ResourceNotFoundException ex) {
        return ApiResponse.error("RESOURCE_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleAccessDenied(AccessDeniedException ex) {
        return ApiResponse.error("ACCESS_DENIED", "Bạn không có quyền thực hiện hành động này");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResponse<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                e -> e.getDefaultMessage() != null ? e.getDefaultMessage() : "Invalid",
                (existing, replacement) -> existing  // Keep first error per field
            ));
        return ApiResponse.validationError(errors);
    }

    @ExceptionHandler(PlanLimitExceededException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ApiResponse<?> handlePlanLimit(PlanLimitExceededException ex) {
        return ApiResponse.error("PLAN_LIMIT_EXCEEDED", ex.getMessage());
    }

    @ExceptionHandler(SubscriptionExpiredException.class)
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ApiResponse<?> handleSubscriptionExpired(SubscriptionExpiredException ex) {
        return ApiResponse.error("SUBSCRIPTION_EXPIRED",
            "Gói dịch vụ đã hết hạn. Vui lòng gia hạn để tiếp tục sử dụng.");
    }

    @ExceptionHandler(DuplicateSlugException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<?> handleDuplicateSlug(DuplicateSlugException ex) {
        return ApiResponse.error("DUPLICATE_SLUG", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception on {} {}: {}",
            request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        return ApiResponse.error("INTERNAL_ERROR",
            "Có lỗi xảy ra. Vui lòng thử lại sau.");
    }
}
```
### 18.4 Plan Feature Guard (AOP)
```java

// RequireFeature.java - Custom annotation
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireFeature {
    String value();                 // Feature key: "custom_domain", "lottery_vip"
    String message() default "";   // Custom error message
}

// RequirePlan.java
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePlan {
    String[] value();               // Plan slugs: {"pro", "business", "enterprise"}
    String message() default "";
}

// FeatureGuardAspect.java
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class FeatureGuardAspect {

    private final TenantService tenantService;

    @Before("@annotation(requireFeature)")
    public void checkFeature(JoinPoint jp, RequireFeature requireFeature) {
        String tenantId = TenantContextHolder.getTenantId();
        TenantConfig tenant = tenantService.getConfig(UUID.fromString(tenantId));

        Object featureValue = tenant.getPlan().getFeatures().get(requireFeature.value());
        boolean hasFeature = featureValue != null &&
            (Boolean.TRUE.equals(featureValue) || !"false".equals(featureValue.toString()));

        if (!hasFeature) {
            String msg = requireFeature.message().isEmpty()
                ? "Tính năng này không có trong gói dịch vụ của bạn. Vui lòng nâng cấp."
                : requireFeature.message();
            throw new PlanLimitExceededException(msg);
        }
    }

    @Before("@annotation(requirePlan)")
    public void checkPlan(JoinPoint jp, RequirePlan requirePlan) {
        String tenantId = TenantContextHolder.getTenantId();
        TenantConfig tenant = tenantService.getConfig(UUID.fromString(tenantId));

        boolean hasPlan = Arrays.asList(requirePlan.value())
            .contains(tenant.getPlan().getSlug());

        if (!hasPlan) {
            throw new PlanLimitExceededException(
                "Tính năng này yêu cầu gói " +
                String.join(" hoặc ", requirePlan.value()) + " trở lên."
            );
        }
    }
}

// Usage example
@RestController
@RequestMapping("/admin/v1/lottery/predictions")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;

    @GetMapping
    public ApiResponse<PageResponse<PredictionDTO>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(predictionService.list(page, size));
    }

    @PostMapping
    @RequireFeature("lottery_predictions")          // Kiểm tra feature flag
    public ApiResponse<PredictionDTO> create(@Valid @RequestBody CreatePredictionRequest req) {
        return ApiResponse.ok(predictionService.create(req), "Tạo nhận định thành công");
    }

    @PostMapping("/vip")
    @RequirePlan({"business", "enterprise"})        // Kiểm tra plan
    public ApiResponse<PredictionDTO> createVip(@Valid @RequestBody CreatePredictionRequest req) {
        return ApiResponse.ok(predictionService.createVip(req), "Tạo nhận định VIP thành công");
    }
}
```
### 18.5 Flyway Migration Example
```sql


-- db/migration/V1__create_core_tables.sql
-- Core platform tables (Phase 1)

CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";     -- For LIKE query optimization
CREATE EXTENSION IF NOT EXISTS "unaccent";    -- For Vietnamese text search

-- Plans
CREATE TABLE plans (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(100) NOT NULL,
    slug            VARCHAR(50) UNIQUE NOT NULL,
    description     TEXT,
    price_monthly   DECIMAL(12,2) NOT NULL DEFAULT 0,
    price_yearly    DECIMAL(12,2),
    currency        CHAR(3) DEFAULT 'VND',
    trial_days      INT DEFAULT 14,
    max_articles    INT DEFAULT 50,
    max_media_mb    INT DEFAULT 500,
    max_admin_users INT DEFAULT 1,
    features        JSONB DEFAULT '{}',
    allowed_verticals TEXT[] DEFAULT '{lottery}',
    is_active       BOOLEAN DEFAULT TRUE,
    is_public       BOOLEAN DEFAULT TRUE,
    display_order   INT DEFAULT 0,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

-- Tenants
CREATE TABLE tenants (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    slug            VARCHAR(100) UNIQUE NOT NULL,
    name            VARCHAR(255) NOT NULL,
    plan_id         UUID REFERENCES plans(id),
    status          VARCHAR(20) DEFAULT 'trialing'
                    CHECK (status IN ('trialing','active','past_due','suspended','cancelled')),
    active_vertical VARCHAR(50) DEFAULT 'lottery',
    active_theme    VARCHAR(100) DEFAULT 'lottery-default',
    trial_ends_at   TIMESTAMPTZ,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE INDEX idx_tenants_slug   ON tenants(slug);
CREATE INDEX idx_tenants_status ON tenants(status) WHERE deleted_at IS NULL;

-- Enable Row Level Security
ALTER TABLE tenants ENABLE ROW LEVEL SECURITY;

-- (Các tables khác...)

-- V2__create_lottery_tables.sql
-- V3__create_billing_tables.sql
-- V4__seed_plans.sql
-- V5__seed_lottery_provinces.sql
```
## 19. FRONTEND CODE TEMPLATES
### 19.1 API Client (TypeScript)
```typescript


// lib/api-client.ts
const BASE_URL = process.env.NEXT_PUBLIC_API_URL || ''

interface RequestOptions extends RequestInit {
  params?: Record<string, string | number | boolean | undefined>
}

class ApiClient {
  private baseUrl: string
  private defaultHeaders: Record<string, string>

  constructor(baseUrl: string) {
    this.baseUrl = baseUrl
    this.defaultHeaders = {
      'Content-Type': 'application/json',
    }
  }

  private buildUrl(path: string, params?: RequestOptions['params']): string {
    const url = new URL(this.baseUrl + path)
    if (params) {
      Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          url.searchParams.set(key, String(value))
        }
      })
    }
    return url.toString()
  }

  private getAuthHeaders(): Record<string, string> {
    if (typeof window === 'undefined') return {}
    const token = localStorage.getItem('admin_token')
    return token ? { Authorization: `Bearer ${token}` } : {}
  }

  async get<T>(path: string, options?: RequestOptions): Promise<T> {
    const url = this.buildUrl(path, options?.params)
    const res = await fetch(url, {
      ...options,
      method: 'GET',
      headers: {
        ...this.defaultHeaders,
        ...this.getAuthHeaders(),
        ...options?.headers,
      },
      next: options?.next,
    })
    return this.handleResponse<T>(res)
  }

  async post<T>(path: string, body?: unknown, options?: RequestOptions): Promise<T> {
    const res = await fetch(this.buildUrl(path), {
      ...options,
      method: 'POST',
      headers: {
        ...this.defaultHeaders,
        ...this.getAuthHeaders(),
        ...options?.headers,
      },
      body: body ? JSON.stringify(body) : undefined,
    })
    return this.handleResponse<T>(res)
  }

  async put<T>(path: string, body?: unknown, options?: RequestOptions): Promise<T> {
    const res = await fetch(this.buildUrl(path), {
      ...options,
      method: 'PUT',
      headers: {
        ...this.defaultHeaders,
        ...this.getAuthHeaders(),
        ...options?.headers,
      },
      body: body ? JSON.stringify(body) : undefined,
    })
    return this.handleResponse<T>(res)
  }

  async delete<T>(path: string, options?: RequestOptions): Promise<T> {
    const res = await fetch(this.buildUrl(path), {
      ...options,
      method: 'DELETE',
      headers: {
        ...this.defaultHeaders,
        ...this.getAuthHeaders(),
        ...options?.headers,
      },
    })
    return this.handleResponse<T>(res)
  }

  private async handleResponse<T>(res: Response): Promise<T> {
    const data = await res.json()

    if (!res.ok || !data.success) {
      // Handle auth errors
      if (res.status === 401 && typeof window !== 'undefined') {
        localStorage.removeItem('admin_token')
        window.location.href = '/admin/login'
      }

      // Throw error với errorCode để UI handle
      const error = new ApiError(
        data.message || 'Có lỗi xảy ra',
        data.errorCode || 'UNKNOWN_ERROR',
        res.status,
        data.errors
      )
      throw error
    }

    return data.data as T
  }
}

export class ApiError extends Error {
  constructor(
    message: string,
    public readonly errorCode: string,
    public readonly statusCode: number,
    public readonly fieldErrors?: Record<string, string>
  ) {
    super(message)
    this.name = 'ApiError'
  }
}

export const apiClient = new ApiClient(BASE_URL)

// Typed API functions
export const lotteryApi = {
  getTodayResults: (region?: string) =>
    apiClient.get<LotteryResultsResponse>('/v1/lottery/results/today', {
      params: { region },
      next: { revalidate: 120, tags: ['lottery-today'] }
    }),

  getResultsByDate: (date: string, region?: string) =>
    apiClient.get<LotteryResultsResponse>(`/v1/lottery/results/${date}`, {
      params: { region },
      next: { revalidate: 3600, tags: [`lottery-${date}`] }
    }),

  getFrequencyStats: (provinceCode: string, period: string) =>
    apiClient.get<FrequencyStatsResponse>('/v1/lottery/stats/frequency', {
      params: { provinceCode, period },
      next: { revalidate: 1800, tags: [`stats-freq-${provinceCode}`] }
    }),

  getGanStats: (provinceCode: string, limit?: number) =>
    apiClient.get<GanStatsResponse>('/v1/lottery/stats/gan', {
      params: { provinceCode, limit },
      next: { revalidate: 1800, tags: [`stats-gan-${provinceCode}`] }
    }),

  getPredictions: (provinceCode: string, date: string) =>
    apiClient.get<PredictionsResponse>('/v1/lottery/predictions', {
      params: { provinceCode, date },
      next: { revalidate: 300 }
    }),

  checkTicket: (data: CheckTicketRequest) =>
    apiClient.post<CheckTicketResponse>('/v1/lottery/check-ticket', data),
}

export const adminApi = {
  // Articles
  getArticles: (params: ArticleListParams) =>
    apiClient.get<PageResponse<ArticleDTO>>('/admin/v1/articles', { params }),

  createArticle: (data: CreateArticleRequest) =>
    apiClient.post<ArticleDTO>('/admin/v1/articles', data),

  updateArticle: (id: string, data: UpdateArticleRequest) =>
    apiClient.put<ArticleDTO>(`/admin/v1/articles/${id}`, data),

  deleteArticle: (id: string) =>
    apiClient.delete(`/admin/v1/articles/${id}`),

  publishArticle: (id: string) =>
    apiClient.post(`/admin/v1/articles/${id}/publish`),

  // Settings
  getGeneralSettings: () =>
    apiClient.get<GeneralSettings>('/admin/v1/settings/general'),

  updateGeneralSettings: (data: Partial<GeneralSettings>) =>
    apiClient.put<GeneralSettings>('/admin/v1/settings/general', data),

  getLotterySettings: () =>
    apiClient.get<LotterySettings>('/admin/v1/settings/lottery'),

  updateLotterySettings: (data: Partial<LotterySettings>) =>
    apiClient.put<LotterySettings>('/admin/v1/settings/lottery', data),

  // Media
  uploadMedia: async (file: File, folder?: string): Promise<MediaDTO> => {
    const formData = new FormData()
    formData.append('file', file)
    if (folder) formData.append('folder', folder)

    const token = localStorage.getItem('admin_token')
    const res = await fetch(`${BASE_URL}/admin/v1/media/upload`, {
      method: 'POST',
      headers: token ? { Authorization: `Bearer ${token}` } : {},
      body: formData,
    })
    const data = await res.json()
    if (!data.success) throw new ApiError(data.message, data.errorCode, res.status)
    return data.data
  },

  // Analytics
  getAnalyticsOverview: (period: string) =>
    apiClient.get<AnalyticsOverview>('/admin/v1/analytics/overview', {
      params: { period }
    }),
}
```
### 19.2 Custom Hooks
```typescript


// hooks/useLotteryResults.ts
import { useState, useEffect, useCallback } from 'react'
import { lotteryApi } from '@/lib/api-client'
import type { LotteryResult } from '@/types/lottery'

export function useLotteryResults(date?: string, region?: string) {
  const [results, setResults]   = useState<LotteryResult[]>([])
  const [loading, setLoading]   = useState(true)
  const [error, setError]       = useState<string | null>(null)
  const [lastFetch, setLastFetch] = useState<Date | null>(null)

  const fetch = useCallback(async () => {
    try {
      setLoading(true)
      setError(null)

      const res = date
        ? await lotteryApi.getResultsByDate(date, region)
        : await lotteryApi.getTodayResults(region)

      setResults(res.results || [])
      setLastFetch(new Date())
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Có lỗi xảy ra')
    } finally {
      setLoading(false)
    }
  }, [date, region])

  useEffect(() => {
    fetch()
  }, [fetch])

  // Auto-refresh mỗi 2 phút nếu là hôm nay
  useEffect(() => {
    if (date) return // Không refresh khi xem ngày cụ thể

    const interval = setInterval(fetch, 2 * 60 * 1000)
    return () => clearInterval(interval)
  }, [date, fetch])

  return { results, loading, error, lastFetch, refetch: fetch }
}

// hooks/useFrequencyStats.ts
import useSWR from 'swr'
import { lotteryApi } from '@/lib/api-client'

export function useFrequencyStats(provinceCode: string, period: string = '30') {
  const { data, error, isLoading, mutate } = useSWR(
    ['frequency-stats', provinceCode, period],
    () => lotteryApi.getFrequencyStats(provinceCode, period),
    {
      revalidateOnFocus: false,
      revalidateIfStale: true,
      dedupingInterval: 5 * 60 * 1000, // 5 phút
    }
  )

  return {
    stats:    data?.stats || [],
    loading:  isLoading,
    error:    error?.message,
    refresh:  mutate,
  }
}

// hooks/useAdminAuth.ts
import { useState, useEffect, useCallback } from 'react'
import { useRouter } from 'next/navigation'
import { adminApi } from '@/lib/api-client'
import type { AdminUser } from '@/types/admin'

export function useAdminAuth() {
  const [user, setUser]       = useState<AdminUser | null>(null)
  const [loading, setLoading] = useState(true)
  const router = useRouter()

  const checkAuth = useCallback(async () => {
    const token = localStorage.getItem('admin_token')
    if (!token) {
      setLoading(false)
      return
    }

    try {
      const res = await adminApi.getMe()
      setUser(res.user)
    } catch {
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_refresh_token')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    checkAuth()
  }, [checkAuth])

  const login = async (email: string, password: string) => {
    const res = await adminApi.login(email, password)
    localStorage.setItem('admin_token', res.accessToken)
    localStorage.setItem('admin_refresh_token', res.refreshToken)
    setUser(res.user)
    router.push('/admin')
  }

  const logout = async () => {
    try {
      await adminApi.logout()
    } finally {
      localStorage.removeItem('admin_token')
      localStorage.removeItem('admin_refresh_token')
      setUser(null)
      router.push('/admin/login')
    }
  }

  return { user, loading, login, logout, isAuthenticated: !!user }
}
```
### 19.3 Type Definitions
```typescript


// types/lottery.ts
export interface LotteryRegion {
  code:      'north' | 'central' | 'south'
  name:      string
  drawTime:  string
}

export interface LotteryProvince {
  id:          string
  regionCode:  string
  regionName:  string
  code:        string
  name:        string
  shortName:   string
  slug:        string
  drawDays:    number[]
  resultType:  'north' | 'central' | 'south'
}

export interface LotteryResult {
  id:             string
  provinceId:     string
  provinceCode:   string
  provinceName:   string
  provinceSlug:   string
  regionCode:     string
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
  provinceId:     string
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
  resultNumbers:  string[] | null
  viewCount:      number
  createdAt:      string
}

// types/tenant.ts
export interface TenantConfig {
  id:             string
  slug:           string
  name:           string
  vertical:       'lottery' | 'realestate' | 'ecommerce' | 'restaurant'
  theme:          string
  status:         string
  settings:       TenantSettings
  plan:           TenantPlan
  primaryDomain:  string | null
}

export interface TenantSettings {
  logoUrl:            string | null
  faviconUrl:         string | null
  tagline:            string | null
  description:        string | null
  primaryColor:       string
  secondaryColor:     string
  accentColor:        string
  fontHeading:        string
  fontBody:           string
  phone:              string | null
  email:              string | null
  address:            string | null
  socialLinks:        Record<string, string>
  seoTitleTemplate:   string
  seoDefaultTitle:    string | null
  seoDefaultDesc:     string | null
  seoOgImage:         string | null
  googleAnalyticsId:  string | null
  headerScripts:      string | null
  footerScripts:      string | null
  customCss:          string | null
  timezone:           string
  locale:             string
}

export interface TenantPlan {
  slug:     string
  name:     string
  features: Record<string, unknown>
}

// types/api.ts
export interface ApiResponse<T> {
  success:   boolean
  message:   string | null
  data:      T
  errorCode: string | null
  errors:    Record<string, string> | null
  timestamp: number
}

export interface PageResponse<T> {
  items:      T[]
  page:       number
  size:       number
  totalItems: number
  totalPages: number
  hasNext:    boolean
  hasPrev:    boolean
}
```
## 20. TESTING STRATEGY
### 20.1 Backend Tests
```java

// LotteryResultServiceTest.java
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LotteryResultServiceTest {

    @Autowired private LotteryResult

tiep di
Avatar for Assistant
```java

    @Autowired private LotteryResultService resultService;
    @Autowired private LotteryProvinceRepository provinceRepo;
    @Autowired private LotteryResultRepository resultRepo;

    @MockBean private LotteryScraperService scraperService;
    @MockBean private LotteryApiService apiService;

    private LotteryProvince mbProvince;

    @BeforeEach
    void setUp() {
        mbProvince = provinceRepo.findByCode("mb")
            .orElseThrow(() -> new RuntimeException("Test data not found"));
    }

    @Test
    @DisplayName("Lưu kết quả xổ số Miền Bắc thành công")
    void saveNorthResult_Success() {
        // Given
        LocalDate date = LocalDate.of(2024, 1, 15);
        LotteryResultData mockData = LotteryResultData.builder()
            .specialPrize("12345")
            .firstPrizes(new String[]{"67890"})
            .secondPrizes(new String[]{"11111", "22222"})
            .thirdPrizes(new String[]{"33333","44444","55555","66666","77777","88888"})
            .fourthPrizes(new String[]{"1234","5678","9012","3456"})
            .fifthPrizes(new String[]{"111","222","333","444","555","666"})
            .sixthPrizes(new String[]{"11","22","33"})
            .seventhPrizes(new String[]{"12","34","56","78"})
            .source("scraper_test")
            .build();

        when(apiService.fetchResult(any(), any()))
            .thenThrow(new RuntimeException("API unavailable"));
        when(scraperService.scrape("mb", date))
            .thenReturn(mockData);

        // When
        resultService.fetchProvinceResult(mbProvince, date);

        // Then
        Optional<LotteryResult> saved = resultRepo
            .findByProvinceIdAndDrawDate(mbProvince.getId(), date);

        assertThat(saved).isPresent();
        assertThat(saved.get().getSpecialPrize()).isEqualTo("12345");
        assertThat(saved.get().getLotoNumbers()).contains("45", "90"); // 2 số cuối
        assertThat(saved.get().getSource()).isEqualTo("scraper_test");
    }

    @Test
    @DisplayName("Extract loto numbers đúng từ kết quả")
    void extractLotoNumbers_CorrectExtraction() {
        // Given
        LotteryResultData data = LotteryResultData.builder()
            .specialPrize("12345")  // → 45
            .firstPrizes(new String[]{"67890"})  // → 90
            .secondPrizes(new String[]{"11123", "22256"})  // → 23, 56
            .thirdPrizes(new String[]{"33312","44478","55590","66601","77712","88823"})
            .fourthPrizes(new String[]{"1234","5678","9012","3456"})
            .fifthPrizes(new String[]{"111","222","333","444","555","666"})
            .sixthPrizes(new String[]{"11","22","33"})
            .seventhPrizes(new String[]{"12","34","56","78"})
            .build();

        // When
        String[] loto = resultService.extractLotoNumbers(data);

        // Then
        assertThat(loto).contains("45", "90", "23", "56");
        assertThat(loto).allMatch(n -> n.length() == 2);
        assertThat(loto).doesNotHaveDuplicates();
    }

    @Test
    @DisplayName("Retry khi fetch thất bại, thành công ở lần thứ 2")
    void fetchWithRetry_SuccessOnSecondAttempt() {
        // Given
        LocalDate date = LocalDate.of(2024, 1, 15);
        LotteryResultData mockData = createMockResultData();

        when(apiService.fetchResult(any(), any()))
            .thenThrow(new RuntimeException("First attempt fails"))
            .thenReturn(mockData);

        // When
        resultService.fetchProvinceResult(mbProvince, date);

        // Then
        verify(apiService, times(2)).fetchResult(any(), any());
        assertThat(resultRepo.findByProvinceIdAndDrawDate(mbProvince.getId(), date))
            .isPresent();
    }

    @Test
    @DisplayName("Tất cả retry thất bại → lưu FetchLog với status FAILED")
    void fetchWithRetry_AllAttemptsFail_SavesFailedLog() {
        // Given
        LocalDate date = LocalDate.of(2024, 1, 15);

        when(apiService.fetchResult(any(), any()))
            .thenThrow(new RuntimeException("API down"));
        when(scraperService.scrape(any(), any()))
            .thenThrow(new ScraperException("Scraper blocked"));

        // When
        resultService.fetchProvinceResult(mbProvince, date);

        // Then
        assertThat(resultRepo.findByProvinceIdAndDrawDate(mbProvince.getId(), date))
            .isEmpty();

        Optional<FetchLog> log = fetchLogRepo
            .findByProvinceIdAndDrawDate(mbProvince.getId(), date);
        assertThat(log).isPresent();
        assertThat(log.get().getStatus()).isEqualTo(FetchStatus.FAILED);
        assertThat(log.get().getAttemptCount()).isEqualTo(3);
    }

    private LotteryResultData createMockResultData() {
        return LotteryResultData.builder()
            .specialPrize("12345")
            .firstPrizes(new String[]{"67890"})
            .secondPrizes(new String[]{"11111", "22222"})
            .thirdPrizes(new String[]{"33333","44444","55555","66666","77777","88888"})
            .fourthPrizes(new String[]{"1234","5678","9012","3456"})
            .fifthPrizes(new String[]{"111","222","333","444","555","666"})
            .sixthPrizes(new String[]{"11","22","33"})
            .seventhPrizes(new String[]{"12","34","56","78"})
            .source("api_test")
            .build();
    }
}

// TenantIsolationTest.java - Quan trọng nhất: test data isolation
@SpringBootTest
@ActiveProfiles("test")
class TenantIsolationTest {

    @Autowired private ArticleRepository articleRepo;
    @Autowired private TenantContextHolder tenantContextHolder;

    private UUID tenantAId;
    private UUID tenantBId;

    @BeforeEach
    void setUp() {
        tenantAId = UUID.randomUUID();
        tenantBId = UUID.randomUUID();

        // Tạo articles cho cả 2 tenants
        articleRepo.save(Article.builder()
            .tenantId(tenantAId)
            .title("Article of Tenant A")
            .slug("article-tenant-a")
            .status(ArticleStatus.PUBLISHED)
            .build());

        articleRepo.save(Article.builder()
            .tenantId(tenantBId)
            .title("Article of Tenant B")
            .slug("article-tenant-b")
            .status(ArticleStatus.PUBLISHED)
            .build());
    }

    @Test
    @DisplayName("Tenant A chỉ thấy articles của mình, không thấy Tenant B")
    void tenantA_CannotSee_TenantB_Articles() {
        // Set context = Tenant A
        TenantContextHolder.set(new TenantContext(
            tenantAId.toString(), "tenant-a", "starter", "lottery", Map.of()
        ));

        List<Article> articles = articleRepo.findByTenantId(tenantAId);

        assertThat(articles).hasSize(1);
        assertThat(articles.get(0).getTitle()).isEqualTo("Article of Tenant A");
        assertThat(articles).noneMatch(a -> a.getTenantId().equals(tenantBId));

        TenantContextHolder.clear();
    }

    @Test
    @DisplayName("Không thể truy cập article của tenant khác dù biết ID")
    void cannotAccess_AnotherTenants_Article_ById() {
        // Lấy ID của article thuộc Tenant B
        UUID tenantBArticleId = articleRepo
            .findAll().stream()
            .filter(a -> a.getTenantId().equals(tenantBId))
            .findFirst()
            .map(Article::getId)
            .orElseThrow();

        // Set context = Tenant A
        TenantContextHolder.set(new TenantContext(
            tenantAId.toString(), "tenant-a", "starter", "lottery", Map.of()
        ));

        // Tenant A cố tình query bằng ID của Tenant B
        Optional<Article> result = articleRepo
            .findByIdAndTenantId(tenantBArticleId, tenantAId);

        assertThat(result).isEmpty(); // Phải không tìm thấy

        TenantContextHolder.clear();
    }
}

// LotteryStatsServiceTest.java
@SpringBootTest
@ActiveProfiles("test")
class LotteryStatsServiceTest {

    @Autowired private LotteryStatsService statsService;
    @Autowired private LotteryLotoDetailRepository lotoDetailRepo;
    @Autowired private LotteryFrequencyStatsRepository frequencyRepo;

    @Test
    @DisplayName("Tính tần suất đúng cho số xuất hiện nhiều lần")
    void calculateFrequency_CorrectForHighFrequencyNumber() {
        // Given: Seed 30 ngày data, số "23" xuất hiện 15 lần
        UUID provinceId = seedTestData("mb", 30, "23", 15);

        // When
        statsService.recalculateForProvince(provinceId, LocalDate.now());

        // Then
        Optional<LotteryFrequencyStats> stats = frequencyRepo
            .findByProvinceIdAndNumberAndPeriodType(provinceId, "23", "30");

        assertThat(stats).isPresent();
        assertThat(stats.get().getFrequency()).isEqualTo(15);
        assertThat(stats.get().getFrequencyPct()).isBetween(45.0, 55.0); // ~50%
        assertThat(stats.get().getTrend()).isIn("hot", "warm");
    }

    @Test
    @DisplayName("Số không xuất hiện trong 30 ngày → trend = frozen")
    void calculateFrequency_FrozenTrend_WhenNumberNotAppeared() {
        // Given: 30 ngày không có số "99"
        UUID provinceId = seedTestDataWithout("mb", 30, "99");

        // When
        statsService.recalculateForProvince(provinceId, LocalDate.now());

        // Then
        Optional<LotteryFrequencyStats> stats = frequencyRepo
            .findByProvinceIdAndNumberAndPeriodType(provinceId, "99", "30");

        assertThat(stats).isPresent();
        assertThat(stats.get().getFrequency()).isZero();
        assertThat(stats.get().getTrend()).isEqualTo("frozen");
        assertThat(stats.get().getDaysSinceLast()).isGreaterThan(30);
    }
}
```
### 20.2 Frontend Tests
```typescript


// __tests__/components/FrequencyGrid.test.tsx
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { FrequencyGrid } from '@/components/lottery/FrequencyGrid'
import type { FrequencyStats } from '@/types/lottery'

// Mock data factory
const createMockStats = (overrides?: Partial<FrequencyStats>[]): FrequencyStats[] => {
  return Array.from({ length: 100 }, (_, i) => ({
    number:        String(i).padStart(2, '0'),
    frequency:     Math.floor(Math.random() * 20),
    frequencyPct:  Math.random() * 20,
    lastAppeared:  '2024-01-10',
    daysSinceLast: Math.floor(Math.random() * 30),
    avgCycle:      3.5,
    maxGap:        15,
    minGap:        1,
    trend:         'neutral' as const,
    ...overrides?.[i],
  }))
}

describe('FrequencyGrid', () => {

  it('renders all 100 numbers in the grid', () => {
    const stats = createMockStats()
    render(
      <FrequencyGrid
        stats={stats}
        period="30"
        onPeriodChange={jest.fn()}
      />
    )

    // Kiểm tra tất cả 100 số từ 00-99
    expect(screen.getByText('00')).toBeInTheDocument()
    expect(screen.getByText('50')).toBeInTheDocument()
    expect(screen.getByText('99')).toBeInTheDocument()
  })

  it('calls onPeriodChange when period button clicked', () => {
    const mockOnPeriodChange = jest.fn()
    const stats = createMockStats()

    render(
      <FrequencyGrid
        stats={stats}
        period="30"
        onPeriodChange={mockOnPeriodChange}
      />
    )

    fireEvent.click(screen.getByText('60 kỳ'))
    expect(mockOnPeriodChange).toHaveBeenCalledWith('60')
  })

  it('shows frozen trend cells with purple color', () => {
    const stats = createMockStats()
    stats[23] = { ...stats[23], number: '23', trend: 'frozen', daysSinceLast: 99 }

    const { container } = render(
      <FrequencyGrid
        stats={stats}
        period="30"
        onPeriodChange={jest.fn()}
      />
    )

    const frozenCell = container.querySelector('[title*="Số 23"]')
    expect(frozenCell).toHaveClass('bg-purple-600')
  })

  it('switches view mode correctly', async () => {
    const stats = createMockStats()
    render(
      <FrequencyGrid
        stats={stats}
        period="30"
        onPeriodChange={jest.fn()}
      />
    )

    // Default: frequency mode
    expect(screen.getByText('Tần suất')).toHaveClass('font-semibold')

    // Switch to gan mode
    fireEvent.click(screen.getByText('Lô gan'))
    await waitFor(() => {
      expect(screen.getByText('Lô gan')).toHaveClass('font-semibold')
    })
  })
})

// __tests__/lib/lottery-utils.test.ts
import {
  extractLotoNumbers,
  formatLotteryDate,
  checkTicketWinner,
  calculateDaysSinceLastAppeared,
} from '@/lib/lottery-utils'

describe('lottery-utils', () => {

  describe('extractLotoNumbers', () => {
    it('extracts last 2 digits from all prizes', () => {
      const result = {
        specialPrize: '12345',
        firstPrizes:  ['67890'],
        secondPrizes: ['11123', '22256'],
        thirdPrizes:  ['33312','44478','55590','66601','77712','88823'],
        fourthPrizes: ['1234','5678','9012','3456'],
        fifthPrizes:  ['111','222','333','444','555','666'],
        sixthPrizes:  ['11','22','33'],
        seventhPrizes:['12','34','56','78'],
        eighthPrizes: [],
      }

      const loto = extractLotoNumbers(result)

      expect(loto).toContain('45') // từ 12345
      expect(loto).toContain('90') // từ 67890
      expect(loto).toContain('23') // từ 11123
      expect(loto).toContain('56') // từ 22256
      expect(loto.every(n => n.length === 2)).toBe(true)
    })

    it('removes duplicates', () => {
      const result = {
        specialPrize: '12345',
        firstPrizes:  ['67845'], // '45' xuất hiện lại
        secondPrizes: [],
        thirdPrizes: [],
        fourthPrizes: [],
        fifthPrizes: [],
        sixthPrizes: [],
        seventhPrizes: [],
        eighthPrizes: [],
      }

      const loto = extractLotoNumbers(result)
      const count45 = loto.filter(n => n === '45').length
      expect(count45).toBe(1)
    })
  })

  describe('checkTicketWinner', () => {
    const mockResult = {
      specialPrize: '123456',
      firstPrizes:  ['78901'],
      secondPrizes: ['23456'],
      thirdPrizes:  ['34567','45678','56789','67890','78901','89012'],
      fourthPrizes: ['9012','0123','1234','2345'],
      fifthPrizes:  ['345','456','567','678','789','890'],
      sixthPrizes:  ['12','23','34'],
      seventhPrizes:['5','6','7','8'],
      eighthPrizes: ['90'],
    }

    it('detects special prize winner correctly', () => {
      const result = checkTicketWinner('123456', mockResult, 'south')
      expect(result.isWinner).toBe(true)
      expect(result.prizeName).toBe('Giải Đặc Biệt')
    })

    it('returns not winner for non-matching ticket', () => {
      const result = checkTicketWinner('999999', mockResult, 'south')
      expect(result.isWinner).toBe(false)
      expect(result.prizeName).toBeNull()
    })

    it('detects eighth prize (2 digit match) for south', () => {
      const result = checkTicketWinner('999990', mockResult, 'south')
      expect(result.isWinner).toBe(true)
      expect(result.prizeName).toBe('Giải Tám')
    })
  })

  describe('formatLotteryDate', () => {
    it('formats date in Vietnamese style', () => {
      expect(formatLotteryDate('2024-01-15')).toBe('15/01/2024')
      expect(formatLotteryDate('2024-12-31')).toBe('31/12/2024')
    })

    it('handles today correctly', () => {
      const today = new Date()
      const formatted = formatLotteryDate('today')
      expect(formatted).toMatch(/\d{2}\/\d{2}\/\d{4}/)
    })
  })
})

// __tests__/middleware.test.ts
import { middleware } from '@/middleware'
import { NextRequest } from 'next/server'

describe('Tenant Resolution Middleware', () => {

  const createRequest = (hostname: string, path: string = '/') => {
    return new NextRequest(`https://${hostname}${path}`, {
      headers: { host: hostname }
    })
  }

  it('extracts tenant slug from subdomain', async () => {
    const req = createRequest('phuongnghi.saas-platform.vn')
    const res = await middleware(req)

    expect(res.headers.get('x-tenant-slug')).toBe('phuongnghi')
  })

  it('skips _next static files', async () => {
    const req = createRequest('phuongnghi.saas-platform.vn', '/_next/static/chunk.js')
    const res = await middleware(req)

    // Should pass through without modification
    expect(res.headers.get('x-tenant-slug')).toBeNull()
  })

  it('handles www subdomain correctly', async () => {
    const req = createRequest('www.phuongnghi.saas-platform.vn')
    const res = await middleware(req)

    expect(res.headers.get('x-tenant-slug')).toBe('phuongnghi')
  })
})
```
## 21. MONITORING & OBSERVABILITY
### 21.1 Application Metrics (Spring Boot Actuator + Prometheus)
```java

// MetricsConfig.java
@Configuration
public class MetricsConfig {

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(
            @Value("${spring.application.name}") String appName) {
        return registry -> registry.config()
            .commonTags("application", appName, "environment",
                System.getProperty("spring.profiles.active", "dev"));
    }
}

// LotteryMetricsService.java
@Service
@RequiredArgsConstructor
public class LotteryMetricsService {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter fetchSuccessCounter;
    private final Counter fetchFailureCounter;

    // Timers
    private final Timer fetchDurationTimer;
    private final Timer statsCalcTimer;

    // Gauges
    private final AtomicInteger pendingFetches = new AtomicInteger(0);

    @PostConstruct
    void initMetrics() {
        Counter.builder("lottery.fetch.success")
            .description("Số lần fetch KQXS thành công")
            .tag("source", "all")
            .register(meterRegistry);

        Counter.builder("lottery.fetch.failure")
            .description("Số lần fetch KQXS thất bại")
            .register(meterRegistry);

        Timer.builder("lottery.fetch.duration")
            .description("Thời gian fetch KQXS")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry);

        Gauge.builder("lottery.fetch.pending", pendingFetches, AtomicInteger::get)
            .description("Số fetch đang chờ xử lý")
            .register(meterRegistry);

        Timer.builder("lottery.stats.calculation.duration")
            .description("Thời gian tính thống kê")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(meterRegistry);
    }

    public void recordFetchSuccess(String provinceCode, long durationMs) {
        meterRegistry.counter("lottery.fetch.success",
            "province", provinceCode).increment();
        meterRegistry.timer("lottery.fetch.duration",
            "province", provinceCode).record(durationMs, TimeUnit.MILLISECONDS);
    }

    public void recordFetchFailure(String provinceCode, String reason) {
        meterRegistry.counter("lottery.fetch.failure",
            "province", provinceCode,
            "reason",   reason).increment();
    }

    public void recordTenantPageView(String tenantSlug, String page) {
        meterRegistry.counter("tenant.page.views",
            "tenant", tenantSlug,
            "page",   page).increment();
    }
}
```
### 21.2 Grafana Dashboard Config (JSON)
```json

{
  "dashboard": {
    "title": "SaaS Platform - Overview",
    "panels": [
      {
        "title": "Active Tenants",
        "type": "stat",
        "targets": [{
          "expr": "count(tenant_status{status='active'})",
          "legendFormat": "Active Tenants"
        }]
      },
      {
        "title": "KQXS Fetch Success Rate (1h)",
        "type": "gauge",
        "targets": [{
          "expr": "rate(lottery_fetch_success_total[1h]) / (rate(lottery_fetch_success_total[1h]) + rate(lottery_fetch_failure_total[1h])) * 100"
        }],
        "fieldConfig": {
          "min": 0, "max": 100,
          "thresholds": {
            "steps": [
              {"value": 0,  "color": "red"},
              {"value": 80, "color": "yellow"},
              {"value": 95, "color": "green"}
            ]
          }
        }
      },
      {
        "title": "API Response Time P95 (ms)",
        "type": "timeseries",
        "targets": [{
          "expr": "histogram_quantile(0.95, rate(http_server_requests_seconds_bucket[5m])) * 1000",
          "legendFormat": "P95"
        }]
      },
      {
        "title": "Page Views / minute",
        "type": "timeseries",
        "targets": [{
          "expr": "rate(tenant_page_views_total[1m]) * 60",
          "legendFormat": "{{tenant}}"
        }]
      },
      {
        "title": "DB Connection Pool",
        "type": "timeseries",
        "targets": [
          {
            "expr": "hikaricp_connections_active",
            "legendFormat": "Active"
          },
          {
            "expr": "hikaricp_connections_idle",
            "legendFormat": "Idle"
          },
          {
            "expr": "hikaricp_connections_pending",
            "legendFormat": "Pending"
          }
        ]
      },
      {
        "title": "JVM Memory Usage",
        "type": "timeseries",
        "targets": [{
          "expr": "jvm_memory_used_bytes{area='heap'} / 1024 / 1024",
          "legendFormat": "Heap Used (MB)"
        }]
      }
    ]
  }
}
```
### 21.3 Alerting Rules
```yaml

# alerting-rules.yml
groups:
  - name: saas-platform-critical
    rules:

      # KQXS fetch thất bại quá nhiều
      - alert: LotteryFetchHighFailureRate
        expr: |
          rate(lottery_fetch_failure_total[30m]) /
          (rate(lottery_fetch_success_total[30m]) + rate(lottery_fetch_failure_total[30m]))
          > 0.3
        for: 5m
        labels:
          severity: critical
        annotations:
          summary: "KQXS fetch failure rate cao: {{ $value | humanizePercentage }}"
          description: "Tỷ lệ fetch thất bại > 30% trong 30 phút qua. Kiểm tra scraper/API."

      # API response time chậm
      - alert: HighApiResponseTime
        expr: |
          histogram_quantile(0.95,
            rate(http_server_requests_seconds_bucket[5m])
          ) > 2
        for: 3m
        labels:
          severity: warning
        annotations:
          summary: "API P95 response time > 2 giây"
          description: "Response time P95 = {{ $value }}s. Kiểm tra DB queries, cache."

      # DB connections gần hết
      - alert: DatabaseConnectionPoolExhausted
        expr: |
          hikaricp_connections_pending > 5
        for: 2m
        labels:
          severity: critical
        annotations:
          summary: "DB connection pool sắp hết"
          description: "{{ $value }} connections đang chờ. Tăng pool size hoặc optimize queries."

      # Subscription sắp hết hạn (cần thanh toán)
      - alert: HighPastDueSubscriptions
        expr: |
          count(subscription_status{status='past_due'}) > 10
        for: 1h
        labels:
          severity: warning
        annotations:
          summary: "{{ $value }} subscriptions đang past_due"
          description: "Nhiều tenant chưa thanh toán. Kiểm tra payment gateway."

      # Disk space
      - alert: HighDiskUsage
        expr: |
          (node_filesystem_size_bytes - node_filesystem_free_bytes)
          / node_filesystem_size_bytes > 0.85
        for: 5m
        labels:
          severity: warning
        annotations:
          summary: "Disk usage > 85%: {{ $value | humanizePercentage }}"

  - name: saas-platform-info
    rules:

      # Tenant mới đăng ký (thông báo tốt :D)
      - alert: NewTenantRegistered
        expr: increase(tenant_registrations_total[1h]) > 0
        labels:
          severity: info
        annotations:
          summary: "{{ $value }} tenant mới đăng ký trong 1 giờ qua"
```         
## 22. SECURITY CHECKLIST
```markdown

## Security Implementation Checklist

### Authentication & Authorization
- [ ] JWT với RS256 (asymmetric) thay vì HS256
- [ ] Access token ngắn hạn (15-60 phút)
- [ ] Refresh token rotation (mỗi lần dùng tạo token mới)
- [ ] Refresh token stored in HttpOnly cookie (không localStorage)
- [ ] Logout invalidates refresh token trong DB
- [ ] Rate limit login endpoint: 5 lần/15 phút per IP
- [ ] Account lockout sau 10 lần login sai
- [ ] MFA support (TOTP) cho admin accounts
- [ ] Password requirements: min 8 chars, có uppercase, số, ký tự đặc biệt

### Multi-tenant Security
- [ ] PostgreSQL RLS enabled trên tất cả tenant tables
- [ ] Tenant ID validation trong mọi Controller method
- [ ] Unit test: cross-tenant data access bị từ chối
- [ ] Integration test: tenant isolation end-to-end
- [ ] SQL injection prevention: dùng PreparedStatement/JPA, KHÔNG string concat
- [ ] Audit log mọi thao tác nhạy cảm (delete, billing, settings)

### API Security
- [ ] HTTPS only (HSTS header)
- [ ] CORS: chỉ allow known origins
- [ ] Rate limiting: global + per-tenant + per-endpoint
- [ ] Input validation: tất cả request body (Bean Validation @NotNull, @Size, @Pattern)
- [ ] File upload: validate MIME type, size limit, scan virus (nếu có budget)
- [ ] API versioning để dễ deprecate vulnerable versions
- [ ] Remove stack traces từ error responses (production)
- [ ] Sensitive data masking trong logs (email, phone, API keys)

### Infrastructure Security
- [ ] Secrets trong environment variables, KHÔNG hard-code
- [ ] Secrets management: HashiCorp Vault hoặc AWS Secrets Manager
- [ ] Docker: chạy với non-root user
- [ ] Network: DB và Redis không expose ra public internet
- [ ] Firewall: chỉ mở port 80, 443 ra ngoài
- [ ] Regular dependency updates (Dependabot)
- [ ] Container image scanning (Trivy/Snyk)
- [ ] DB backups: automated, encrypted, tested monthly
- [ ] Penetration testing trước go-live

### Payment Security
- [ ] PCI DSS compliance: KHÔNG l

tiep di
Avatar for Assistant
markdown

- [ ] PCI DSS compliance: KHÔNG lưu card number, CVV
- [ ] Payment webhook signature verification (HMAC)
- [ ] Idempotency keys cho payment requests
- [ ] Double-check payment amount server-side (không tin client)
- [ ] Payment logs audit trail đầy đủ
- [ ] Refund only through official gateway API

### Content Security
- [ ] XSS prevention: sanitize HTML content (jsoup/DOMPurify)
- [ ] CSP header (Content Security Policy)
- [ ] Custom CSS sandbox: chỉ allow CSS properties, không allow JS
- [ ] Script injection: whitelist allowed domains cho header/footer scripts
- [ ] Image upload: resize & re-encode (strip EXIF data)
- [ ] File upload: rename file (không dùng original filename)
```
# PHẦN XIV: LAUNCH CHECKLIST
## 23. GO-LIVE CHECKLIST
### 23.1 Infrastructure Ready
```markdown

## Infrastructure Checklist

### DNS & Domain
- [ ] Platform domain trỏ đúng về server
- [ ] Wildcard DNS: *.saas-platform.vn → server IP
- [ ] Cloudflare proxy enabled
- [ ] TTL set thấp (300s) trong giai đoạn launch, tăng sau

### SSL/TLS
- [ ] Wildcard SSL cert cho *.saas-platform.vn
- [ ] Auto-renewal configured (cert expiry monitoring)
- [ ] SSL Labs test: Grade A hoặc A+
- [ ] HSTS preload ready

### Server
- [ ] Production server đủ specs (min: 4 vCPU, 8GB RAM)
- [ ] Swap space configured (4GB)
- [ ] Disk: SSD, min 100GB, monitoring alert 80%
- [ ] Firewall rules: chỉ 22 (SSH), 80, 443 mở ra ngoài
- [ ] SSH key-based auth only (password disabled)
- [ ] Fail2ban configured
- [ ] Automatic security updates enabled
- [ ] NTP synchronized

### Database
- [ ] PostgreSQL tuned (shared_buffers, work_mem, etc.)
- [ ] Connection pooling: PgBouncer configured
- [ ] Backup: automated daily, retain 30 ngày
- [ ] Backup restore tested
- [ ] Read replica configured (nếu cần)
- [ ] Monitoring: slow query log enabled (> 1s)

### Application
- [ ] Environment variables tất cả đã set (production values)
- [ ] Database migrations chạy thành công
- [ ] Seed data: plans, provinces, regions, system configs
- [ ] Super admin account tạo xong, password đổi ngay
- [ ] Health check endpoint trả về 200
- [ ] Sentry configured và nhận được test error
- [ ] Log aggregation configured (ELK / Loki)

### Performance
- [ ] Load test: 500 concurrent users → p95 < 500ms
- [ ] DB query: không có query > 1s trong load test
- [ ] Redis cache hit rate > 80%
- [ ] CDN caching static assets
- [ ] Next.js build optimized (bundle analysis done)
- [ ] Images: WebP format, lazy loading
```
### 23.2 Business Ready
```markdown

## Business Checklist

### Legal & Compliance
- [ ] Terms of Service (Điều khoản dịch vụ) - đã có luật sư review
- [ ] Privacy Policy (Chính sách bảo mật)
- [ ] Cookie Policy
- [ ] PDPA compliance (Luật bảo vệ dữ liệu cá nhân VN)
- [ ] Refund Policy rõ ràng
- [ ] Disclaimer về tính chất thông tin xổ số (không phải cá cược)

### Payment
- [ ] MoMo merchant account approved
- [ ] PayOS account active
- [ ] Test payment flow end-to-end (sandbox → production)
- [ ] Webhook URL đã configure tại payment gateway dashboard
- [ ] Invoice PDF template đẹp, đủ thông tin pháp lý
- [ ] VAT/tax handling (nếu cần)

### Content
- [ ] Landing page (saas-platform.vn) hoàn chỉnh
- [ ] Pricing page rõ ràng
- [ ] FAQ page đầy đủ câu hỏi thường gặp
- [ ] Onboarding guide (video/text) cho tenant mới
- [ ] Blog: 5+ bài viết SEO ban đầu
- [ ] Support email/chat configured

### Operations
- [ ] Support workflow: ticket system (Freshdesk/Zendesk)
- [ ] On-call rotation (ai trực khi có incident)
- [ ] Incident response playbook viết sẵn
- [ ] Status page (statuspage.io) configured
- [ ] Customer communication template (maintenance, incidents)
- [ ] Churn prevention: trigger email khi tenant inactive 7 ngày
- [ ] Renewal reminder: email 7 ngày, 3 ngày, 1 ngày trước hết hạn
```
### 23.3 First 30 Days Post-Launch Plan
```markdown

## Post-Launch Execution Plan

### Tuần 1: Stabilization
├── Monitor metrics liên tục (Grafana dashboards)
├── Fix bugs khẩn cấp ngay lập tức
├── Onboard 5-10 beta tenants (free/discounted)
├── Thu thập feedback chi tiết từng tenant
└── Daily standup: review errors, performance, feedback

### Tuần 2: Quick Wins
├── Fix top 10 bugs/UX issues từ beta feedback
├── Performance optimization dựa trên real traffic data
├── SEO: submit sitemap lên Google Search Console
├── Soft launch marketing (Facebook, Zalo groups về xổ số)
└── Bắt đầu content marketing (blog về xổ số, SEO)

### Tuần 3-4: Growth
├── Launch chính thức → marketing campaign
├── Affiliate/referral program cho early adopters
├── Monitor conversion: free trial → paid
├── A/B test pricing page
├── Collect NPS score từ tenants đầu tiên
└── Plan Sprint cho tính năng được request nhiều nhất

### KPIs cần đạt sau 30 ngày:
├── Tenants đăng ký: 50+
├── Paying tenants: 15+
├── MRR: 2,000,000đ+
├── Uptime: 99.5%+
├── Avg response time: < 300ms
├── NPS score: > 30
└── Churn rate: < 10%
```
## 24. DOCUMENT REVISION HISTORY
```markdown

| Version | Date       | Author  | Changes                                    |
|---------|------------|---------|---------------------------------------------|
| 1.0     | 2024-01-15 | Initial | First complete draft                        |
| 1.1     | TBD        | -       | Add Real Estate vertical details           |
| 1.2     | TBD        | -       | Add E-Commerce vertical details            |
| 2.0     | TBD        | -       | Post Phase 1 retrospective updates         |
```
## 25. GLOSSARY
```markdown

| Thuật ngữ          | Giải thích                                                    |
|--------------------|---------------------------------------------------------------|
| Tenant             | Một khách hàng sử dụng platform (mỗi tenant = 1 website)     |
| Vertical           | Ngành nghề cụ thể (Xổ số, BDS, Ecommerce...)                 |
| MRR                | Monthly Recurring Revenue - Doanh thu định kỳ hàng tháng     |
| ARR                | Annual Recurring Revenue - Doanh thu định kỳ hàng năm        |
| Churn              | Tỷ lệ khách hàng hủy dịch vụ                                 |
| MRR                | Monthly Recurring Revenue                                     |
| RLS                | Row Level Security - Bảo mật cấp hàng trong PostgreSQL        |
| ISR                | Incremental Static Regeneration - Tái tạo trang theo yêu cầu |
| SSR                | Server-Side Rendering - Render phía server                    |
| KQXS               | Kết quả xổ số                                                 |
| Loto               | 2 số cuối của mỗi giải xổ số                                 |
| Lô gan             | Số lâu chưa xuất hiện trong KQXS                             |
| Bạch thủ           | Dự đoán 1 số duy nhất sẽ về                                  |
| Xiên               | Cặp 2-3-4 số cùng về trong một ngày                          |
| 3 càng             | 3 số cuối của giải đặc biệt                                  |
| Cầu                | Phương pháp dự đoán số dựa trên pattern lịch sử              |
| Subdomain          | Tên miền con: phuongnghi.saas-platform.vn                    |
| Custom Domain      | Tên miền riêng của tenant: xosophuongnghi.com.vn             |
| Virtual Threads    | Java 21 feature - lightweight threads hiệu năng cao           |
| Materialized View  | View được cache kết quả query để tăng tốc đọc               |
| PgBouncer          | Connection pooler cho PostgreSQL                              |
| WAF                | Web Application Firewall - tường lửa ứng dụng web            |
| HMAC               | Hash-based Message Authentication Code - xác thực webhook     |
| TOTP               | Time-based One-Time Password - MFA method                    |
| Blue/Green Deploy  | Chiến lược deploy zero-downtime                               |
| Grace Period       | Thời gian gia hạn sau khi hết hạn trước khi khóa dịch vụ    |
```
📌 END OF DOCUMENT
```

Document Stats:
├── Tổng sections:     25
├── Tables:            15+
├── Code blocks:       50+
├── SQL schemas:       30+ tables
├── Java code:         2000+ lines
├── TypeScript code:   1000+ lines
├── Estimated read:    3-4 giờ
└── Version:           1.0 Complete
```
Lưu ý quan trọng: Tài liệu này là blueprint tổng thể. Trong quá trình triển khai,
một số quyết định kỹ thuật có thể thay đổi dựa trên thực tế.
Luôn update document khi có thay đổi kiến trúc quan trọng.
Next step: Review document → Confirm architecture decisions (Section 15.1)
→ Setup project → Sprint 1 kickoff.