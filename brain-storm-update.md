🏗️ PLATFORM REDESIGN - MULTI-VERTICAL, MULTI-TEMPLATE, RBAC
Bán Website Đa Ngành | Template System | Phân Quyền Chi Tiết
# PHẦN I: TỔNG QUAN KIẾN TRÚC MỚI
## 1. BIG PICTURE
```

PLATFORM OWNER (Bạn)
        │
        │ Quản lý qua Super Admin Panel
        ▼
┌───────────────────────────────────────────────────────┐
│                   PLATFORM                            │
│                                                       │
│  Verticals:  Lottery | BDS | Spa | Restaurant | ...  │
│                                                       │
│  Templates per Vertical:                              │
│  ├── Lottery:     template-A | template-B | template-C│
│  ├── BDS:         template-A | template-B             │
│  └── Restaurant:  template-A | template-B             │
└───────────────────────────────────────────────────────┘
        │
        │ Mỗi khách hàng = 1 Website
        ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│  Client A    │  │  Client B    │  │  Client C    │
│  Lottery     │  │  Lottery     │  │  BDS         │
│  Template-A  │  │  Template-B  │  │  Template-A  │
│  xoso-a.com  │  │  xoso-b.com  │  │  bds-c.com   │
└──────────────┘  └──────────────┘  └──────────────┘

ROLES trong hệ thống:
├── super_admin   → Bạn (toàn quyền platform)
├── admin         → Nhân viên của bạn (giúp setup, support)
├── website_owner → Chủ website (đại lý vé số, chủ BDS...)
├── editor        → Nhân viên của chủ website (đăng bài)
└── viewer        → Xem báo cáo, không edit (kế toán...)
```
# PHẦN II: TEMPLATE SYSTEM DESIGN
## 2. TEMPLATE ARCHITECTURE
```

Template = Vertical + Theme + Layout Config

Ví dụ Lottery Vertical có 3 templates:

template: lottery-classic
├── Layout: Header trên, Sidebar phải, Footer dưới
├── Color scheme: Đỏ - Vàng (truyền thống)
├── Features visible: KQXS, Thống kê, Soi cầu, Blog
└── Widget order: KQXS → Lô gan → Soi cầu → Tin tức

template: lottery-modern  
├── Layout: Mega menu, No sidebar, Card-based
├── Color scheme: Xanh navy - Trắng (hiện đại)
├── Features visible: KQXS, Thống kê, Live result nổi bật
└── Widget order: Live result hero → KQXS → Stats

template: lottery-minimal
├── Layout: Tối giản, focus KQXS
├── Color scheme: Trắng - Xám
├── Features visible: Chỉ KQXS + Blog
└── Widget order: KQXS full width → Blog

Switch template:
→ Chỉ đổi template_id trong tenants table
→ Content (bài viết, KQXS) giữ nguyên
→ Website reload với giao diện mới ngay lập tức
```
# PHẦN III: DATABASE DESIGN HOÀN CHỈNH
## 3. SCHEMA CHI TIẾT
### 3.1 Template & Vertical Tables
```sql

-- ============================================================
-- VERTICALS (Các ngành nghề)
-- ============================================================
CREATE TABLE verticals (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code            VARCHAR(50) UNIQUE NOT NULL,
    -- 'lottery', 'realestate', 'spa', 'restaurant', 'hotel'

    name            VARCHAR(255) NOT NULL,
    -- 'Xổ Số', 'Bất Động Sản', 'Spa & Beauty'

    description     TEXT,
    icon            VARCHAR(100),               -- Icon class hoặc emoji
    is_active       BOOLEAN DEFAULT TRUE,
    display_order   INT DEFAULT 0,

    -- Config schema của vertical này
    -- Dùng để validate settings của từng website
    settings_schema JSONB DEFAULT '{}',

    created_at      TIMESTAMPTZ DEFAULT NOW()
);

-- Seed data
INSERT INTO verticals (code, name, display_order) VALUES
('lottery',    'Xổ Số',          1),
('realestate', 'Bất Động Sản',   2),
('spa',        'Spa & Beauty',   3),
('restaurant', 'Nhà Hàng',       4),
('hotel',      'Khách Sạn',      5),
('ecommerce',  'Thương Mại Điện Tử', 6);

-- ============================================================
-- TEMPLATES (Giao diện mẫu cho từng vertical)
-- ============================================================
CREATE TABLE templates (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vertical_id     UUID NOT NULL REFERENCES verticals(id),

    code            VARCHAR(100) UNIQUE NOT NULL,
    -- 'lottery-classic', 'lottery-modern', 'bds-elegant'

    name            VARCHAR(255) NOT NULL,
    -- 'Classic Đỏ Vàng', 'Modern Navy', 'Elegant BDS'

    description     TEXT,
    preview_image   VARCHAR(1000),          -- Ảnh preview template
    preview_url     VARCHAR(1000),          -- Demo URL

    -- Layout configuration
    layout_config   JSONB DEFAULT '{}',
    -- {
    --   "layout_type": "sidebar-right",    // sidebar-right|sidebar-left|no-sidebar|full-width
    --   "header_style": "sticky",          // sticky|static|transparent
    --   "footer_style": "simple",          // simple|detailed|minimal
    --   "color_scheme": "red-yellow",
    --   "font_heading": "Be Vietnam Pro",
    --   "font_body": "Inter",
    --   "border_radius": "rounded",        // sharp|rounded|pill
    --   "widget_order": ["kqxs","lo_gan","soi_cau","blog"],
    --   "features_visible": {
    --     "live_result": true,
    --     "frequency_stats": true,
    --     "predictions": true,
    --     "ticket_checker": true,
    --     "schedule": true,
    --     "print_button": true
    --   },
    --   "sidebar_widgets": ["lo_gan","soi_cau","lich_xo_so"],
    --   "homepage_sections": ["hero","kqxs","stats","predictions","blog"]
    -- }

    -- Default colors (có thể override per-website)
    default_colors  JSONB DEFAULT '{}',
    -- {
    --   "primary": "#E53E3E",
    --   "secondary": "#2D3748",
    --   "accent": "#ECC94B",
    --   "background": "#FFFFFF",
    --   "text": "#1A202C"
    -- }

    -- Default SEO template
    default_seo     JSONB DEFAULT '{}',

    is_active       BOOLEAN DEFAULT TRUE,
    is_premium      BOOLEAN DEFAULT FALSE,  -- Template cao cấp (tính phí thêm)
    display_order   INT DEFAULT 0,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_templates_vertical ON templates(vertical_id);
CREATE INDEX idx_templates_active   ON templates(is_active);

-- Seed templates
INSERT INTO templates (vertical_id, code, name, display_order) VALUES
-- Lottery templates
((SELECT id FROM verticals WHERE code='lottery'),
 'lottery-classic', 'Classic - Đỏ Vàng', 1),
((SELECT id FROM verticals WHERE code='lottery'),
 'lottery-modern', 'Modern - Navy Blue', 2),
((SELECT id FROM verticals WHERE code='lottery'),
 'lottery-minimal', 'Minimal - Tối Giản', 3),
-- BDS templates
((SELECT id FROM verticals WHERE code='realestate'),
 'bds-professional', 'Professional', 1),
((SELECT id FROM verticals WHERE code='realestate'),
 'bds-luxury', 'Luxury', 2),
-- Spa templates
((SELECT id FROM verticals WHERE code='spa'),
 'spa-elegant', 'Elegant', 1),
((SELECT id FROM verticals WHERE code='spa'),
 'spa-minimal', 'Minimal', 2);
```
###3.2 RBAC - Roles & Permissions
```sql

-- ============================================================
-- ROLES (Vai trò trong hệ thống)
-- ============================================================
CREATE TABLE roles (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code            VARCHAR(50) UNIQUE NOT NULL,
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    level           SMALLINT NOT NULL,
    -- Level thứ bậc:
    -- 100 = super_admin  (cao nhất)
    -- 80  = admin        (nhân viên platform)
    -- 50  = website_owner (chủ website)
    -- 30  = editor       (nhân viên chủ website)
    -- 10  = viewer       (chỉ xem)

    is_system       BOOLEAN DEFAULT TRUE,   -- Role hệ thống, không xóa được
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

INSERT INTO roles (code, name, level, description) VALUES
('super_admin',   'Super Admin',    100, 'Toàn quyền platform'),
('admin',         'Admin',          80,  'Nhân viên platform, hỗ trợ setup và support'),
('website_owner', 'Website Owner',  50,  'Chủ website, toàn quyền trên website của mình'),
('editor',        'Editor',         30,  'Nhân viên chủ website, quản lý nội dung'),
('viewer',        'Viewer',         10,  'Chỉ xem báo cáo và thống kê');

-- ============================================================
-- PERMISSIONS (Quyền hạn chi tiết)
-- ============================================================
CREATE TABLE permissions (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code        VARCHAR(100) UNIQUE NOT NULL,
    name        VARCHAR(255) NOT NULL,
    group_name  VARCHAR(50) NOT NULL,
    -- Group: 'platform', 'tenant', 'content', 'lottery',
    --        'analytics', 'billing', 'settings'
    description TEXT
);

-- Platform-level permissions (Super Admin & Admin)
INSERT INTO permissions (code, name, group_name) VALUES
-- Platform management
('platform.tenants.view',      'Xem danh sách websites',        'platform'),
('platform.tenants.create',    'Tạo website mới',               'platform'),
('platform.tenants.edit',      'Chỉnh sửa thông tin website',   'platform'),
('platform.tenants.delete',    'Xóa website',                   'platform'),
('platform.tenants.suspend',   'Tạm ngưng website',             'platform'),
('platform.tenants.impersonate','Đăng nhập vào website',        'platform'),

-- Client management
('platform.clients.view',      'Xem danh sách khách hàng',      'platform'),
('platform.clients.create',    'Thêm khách hàng mới',           'platform'),
('platform.clients.edit',      'Chỉnh sửa thông tin KH',        'platform'),
('platform.clients.delete',    'Xóa khách hàng',                'platform'),

-- Template management
('platform.templates.view',    'Xem templates',                 'platform'),
('platform.templates.create',  'Tạo template mới',              'platform'),
('platform.templates.edit',    'Chỉnh sửa template',            'platform'),
('platform.templates.delete',  'Xóa template',                  'platform'),

-- Payment management
('platform.payments.view',     'Xem lịch sử thanh toán',        'platform'),
('platform.payments.create',   'Ghi nhận thanh toán',           'platform'),
('platform.payments.edit',     'Chỉnh sửa ghi nhận TT',         'platform'),

-- System
('platform.admins.view',       'Xem danh sách admin',           'platform'),
('platform.admins.create',     'Tạo admin mới',                 'platform'),
('platform.admins.edit',       'Chỉnh sửa admin',               'platform'),
('platform.system.config',     'Cấu hình hệ thống',             'platform'),
('platform.system.logs',       'Xem system logs',               'platform'),

-- Tenant-level permissions (Website Owner & Editor)
-- Content
('content.articles.view',      'Xem bài viết',                  'content'),
('content.articles.create',    'Tạo bài viết',                  'content'),
('content.articles.edit',      'Chỉnh sửa bài viết',            'content'),
('content.articles.delete',    'Xóa bài viết',                  'content'),
('content.articles.publish',   'Xuất bản bài viết',             'content'),

('content.pages.view',         'Xem trang tĩnh',                'content'),
('content.pages.edit',         'Chỉnh sửa trang tĩnh',          'content'),

('content.media.view',         'Xem thư viện ảnh',              'content'),
('content.media.upload',       'Upload ảnh',                    'content'),
('content.media.delete',       'Xóa ảnh',                       'content'),

('content.categories.view',    'Xem danh mục',                  'content'),
('content.categories.manage',  'Quản lý danh mục',              'content'),

-- Lottery specific
('lottery.predictions.view',   'Xem nhận định soi cầu',         'lottery'),
('lottery.predictions.create', 'Tạo nhận định soi cầu',         'lottery'),
('lottery.predictions.edit',   'Chỉnh sửa nhận định',           'lottery'),
('lottery.predictions.delete', 'Xóa nhận định',                 'lottery'),

('lottery.ads.view',           'Xem quảng cáo',                 'lottery'),
('lottery.ads.manage',         'Quản lý quảng cáo',             'lottery'),

-- Analytics
('analytics.view',             'Xem báo cáo thống kê',          'analytics'),
('analytics.export',           'Xuất báo cáo',                  'analytics'),

-- Settings
('settings.general.view',      'Xem cài đặt chung',             'settings'),
('settings.general.edit',      'Chỉnh sửa cài đặt chung',       'settings'),
('settings.domain.manage',     'Quản lý tên miền',              'settings'),
('settings.template.switch',   'Đổi giao diện template',        'settings'),
('settings.users.view',        'Xem danh sách users',           'settings'),
('settings.users.manage',      'Quản lý users',                 'settings'),
('settings.seo.edit',          'Chỉnh sửa SEO',                 'settings'),
('settings.ads.manage',        'Quản lý quảng cáo',             'settings');

-- ============================================================
-- ROLE_PERMISSIONS (Gán quyền cho từng role)
-- ============================================================
CREATE TABLE role_permissions (
    role_id         UUID REFERENCES roles(id) ON DELETE CASCADE,
    permission_id   UUID REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- Super Admin: Tất cả quyền (insert programmatically)
-- Admin: Platform permissions + view tenant
-- Website Owner: Tất cả tenant permissions
-- Editor: Content + lottery.predictions
-- Viewer: analytics.view only

-- Gán quyền cho website_owner
INSERT INTO role_permissions (role_id, permission_id)
SELECT
    (SELECT id FROM roles WHERE code = 'website_owner'),
    id
FROM permissions
WHERE code IN (
    'content.articles.view', 'content.articles.create',
    'content.articles.edit', 'content.articles.delete',
    'content.articles.publish',
    'content.pages.view', 'content.pages.edit',
    'content.media.view', 'content.media.upload', 'content.media.delete',
    'content.categories.view', 'content.categories.manage',
    'lottery.predictions.view', 'lottery.predictions.create',
    'lottery.predictions.edit', 'lottery.predictions.delete',
    'lottery.ads.view', 'lottery.ads.manage',
    'analytics.view', 'analytics.export',
    'settings.general.view', 'settings.general.edit',
    'settings.domain.manage', 'settings.template.switch',
    'settings.users.view', 'settings.users.manage',
    'settings.seo.edit', 'settings.ads.manage'
);

-- Gán quyền cho editor
INSERT INTO role_permissions (role_id, permission_id)
SELECT
    (SELECT id FROM roles WHERE code = 'editor'),
    id
FROM permissions
WHERE code IN (
    'content.articles.view', 'content.articles.create',
    'content.articles.edit', 'content.articles.publish',
    'content.media.view', 'content.media.upload',
    'content.categories.view',
    'lottery.predictions.view', 'lottery.predictions.create',
    'lottery.predictions.edit',
    'analytics.view'
);

-- Gán quyền cho viewer
INSERT INTO role_permissions (role_id, permission_id)
SELECT
    (SELECT id FROM roles WHERE code = 'viewer'),
    id
FROM permissions
WHERE code IN ('analytics.view', 'content.articles.view');

-- ============================================================
-- PLATFORM USERS (Super Admin & Admin của bạn)
-- ============================================================
CREATE TABLE platform_users (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role_id         UUID NOT NULL REFERENCES roles(id),

    email           VARCHAR(255) UNIQUE NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    full_name       VARCHAR(255) NOT NULL,
    phone           VARCHAR(20),
    avatar_url      VARCHAR(1000),

    -- Custom permissions (override role permissions)
    extra_permissions   UUID[] DEFAULT '{}',    -- Thêm quyền ngoài role
    revoked_permissions UUID[] DEFAULT '{}',    -- Rút quyền khỏi role

    -- Restrict scope (Admin chỉ quản lý 1 số tenants)
    allowed_tenant_ids  UUID[] DEFAULT '{}',
    -- Empty = được phép manage tất cả tenants
    -- Non-empty = chỉ manage tenants trong list

    is_active       BOOLEAN DEFAULT TRUE,
    last_login_at   TIMESTAMPTZ,
    last_login_ip   INET,
    created_by      UUID REFERENCES platform_users(id),

    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_platform_users_role   ON platform_users(role_id);
CREATE INDEX idx_platform_users_email  ON platform_users(email);

-- ============================================================
-- CLIENTS (Khách hàng mua website)
-- ============================================================
CREATE TABLE clients (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Thông tin cá nhân
    full_name       VARCHAR(255) NOT NULL,
    business_name   VARCHAR(255),
    phone           VARCHAR(20) UNIQUE NOT NULL,
    email           VARCHAR(255),
    address         TEXT,
    province        VARCHAR(100),

    -- Quản lý bởi admin nào
    managed_by      UUID REFERENCES platform_users(id),

    -- Trạng thái
    status          VARCHAR(20) DEFAULT 'active'
                    CHECK (status IN ('lead','active','suspended','churned')),

    -- Nguồn khách hàng
    referral_source VARCHAR(100),
    referred_by     UUID REFERENCES clients(id),

    -- Notes nội bộ
    internal_notes  TEXT,
    tags            TEXT[] DEFAULT '{}',        -- ['vip', 'potential', 'problem']

    created_by      UUID REFERENCES platform_users(id),
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_clients_phone      ON clients(phone);
CREATE INDEX idx_clients_status     ON clients(status);
CREATE INDEX idx_clients_managed_by ON clients(managed_by);

-- ============================================================
-- TENANTS (Mỗi website = 1 tenant)
-- ============================================================
CREATE TABLE tenants (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Quan hệ
    client_id       UUID NOT NULL REFERENCES clients(id),
    vertical_id     UUID NOT NULL REFERENCES verticals(id),
    template_id     UUID NOT NULL REFERENCES templates(id),

    -- Identity
    slug            VARCHAR(100) UNIQUE NOT NULL,
    name            VARCHAR(255) NOT NULL,

    -- Template override (màu sắc, font riêng theo ý khách)
    -- Nếu null → dùng default_colors của template
    color_overrides JSONB DEFAULT NULL,
    -- {
    --   "primary": "#FF0000",
    --   "secondary": "#000000"
    -- }

    -- Feature overrides (bật/tắt tính năng cụ thể)
    feature_overrides JSONB DEFAULT '{}',
    -- {
    --   "show_predictions": false,  -- Tắt soi cầu cho site này
    --   "show_live_result": true
    -- }

    -- Status
    status          VARCHAR(20) DEFAULT 'building'
                    CHECK (status IN (
                        'building',     -- Đang setup
                        'active',       -- Đang hoạt động
                        'suspended',    -- Tạm ngưng
                        'cancelled'     -- Đã hủy
                    )),

    -- Template switch history (audit)
    template_history JSONB DEFAULT '[]',
    -- [{"template_id": "...", "template_code": "lottery-classic",
    --   "switched_at": "2024-01-15", "switched_by": "admin_id"}]

    -- Setup info
    setup_completed_at  TIMESTAMPTZ,
    setup_notes         TEXT,

    created_by      UUID REFERENCES platform_users(id),
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE INDEX idx_tenants_client_id   ON tenants(client_id);
CREATE INDEX idx_tenants_vertical_id ON tenants(vertical_id);
CREATE INDEX idx_tenants_template_id ON tenants(template_id);
CREATE INDEX idx_tenants_status      ON tenants(status);

-- ============================================================
-- TENANT_USERS (Users của mỗi website)
-- website_owner, editor, viewer của từng site
-- ============================================================
CREATE TABLE tenant_users (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    role_id         UUID NOT NULL REFERENCES roles(id),

    -- Credentials
    email           VARCHAR(255) NOT NULL,
    password_hash   VARCHAR(255) NOT NULL,
    full_name       VARCHAR(255) NOT NULL,
    phone           VARCHAR(20),
    avatar_url      VARCHAR(1000),

    -- Custom permissions (override role)
    extra_permissions   UUID[] DEFAULT '{}',
    revoked_permissions UUID[] DEFAULT '{}',

    -- Auth
    last_login_at   TIMESTAMPTZ,
    last_login_ip   INET,
    reset_token     VARCHAR(255),
    reset_expires_at TIMESTAMPTZ,

    is_active       BOOLEAN DEFAULT TRUE,
    created_by      UUID,
    -- UUID của platform_user hoặc tenant_user tạo account này

    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),

    UNIQUE(tenant_id, email)
);

CREATE INDEX idx_tenant_users_tenant   ON tenant_users(tenant_id);
CREATE INDEX idx_tenant_users_role     ON tenant_users(role_id);
CREATE INDEX idx_tenant_users_email    ON tenant_users(email);

-- ============================================================
-- DOMAINS
-- ============================================================
CREATE TABLE domains (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,

    domain          VARCHAR(255) UNIQUE NOT NULL,
    type            VARCHAR(20) DEFAULT 'subdomain'
                    CHECK (type IN ('subdomain','custom')),
    is_primary      BOOLEAN DEFAULT TRUE,

    -- SSL
    ssl_status      VARCHAR(20) DEFAULT 'pending'
                    CHECK (ssl_status IN ('pending','active','failed','expired')),
    ssl_expires_at  TIMESTAMPTZ,

    -- Verification
    verified        BOOLEAN DEFAULT FALSE,
    verify_token    VARCHAR(255),
    verified_at     TIMESTAMPTZ,

    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_domains_tenant ON domains(tenant_id);
CREATE INDEX idx_domains_domain ON domains(domain);

-- ============================================================
-- TENANT_SETTINGS (Config website)
-- ============================================================
CREATE TABLE tenant_settings (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID UNIQUE NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,

    -- Branding
    logo_url        VARCHAR(1000),
    favicon_url     VARCHAR(1000),
    tagline         VARCHAR(500),
    description     TEXT,

    -- Contact
    phone           VARCHAR(50),
    email           VARCHAR(255),
    address         TEXT,
    social_links    JSONB DEFAULT '{}',

    -- SEO
    seo_title_template  VARCHAR(255) DEFAULT '{page_title} - {site_name}',
    seo_default_title   VARCHAR(255),
    seo_default_desc    VARCHAR(500),
    seo_og_image        VARCHAR(1000),

    -- Tracking
    google_analytics_id VARCHAR(50),
    facebook_pixel_id   VARCHAR(50),
    header_scripts      TEXT,
    footer_scripts      TEXT,
    custom_css          TEXT,

    -- Localization
    timezone    VARCHAR(50) DEFAULT 'Asia/Ho_Chi_Minh',
    locale      VARCHAR(10) DEFAULT 'vi-VN',

    updated_at  TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================
-- SESSIONS (Cho cả platform_users và tenant_users)
-- ============================================================
CREATE TABLE sessions (
    id              VARCHAR(255) PRIMARY KEY,

    -- Polymorphic: ai đang login
    user_type       VARCHAR(20) NOT NULL
                    CHECK (user_type IN ('platform_user','tenant_user')),
    user_id         UUID NOT NULL,
    tenant_id       UUID REFERENCES tenants(id),
    -- NULL nếu là platform_user

    -- Cache role + permissions (tránh query DB mỗi request)
    role_code       VARCHAR(50) NOT NULL,
    permissions     TEXT[] DEFAULT '{}',
    -- Cached list of permission codes

    ip_address      INET,
    user_agent      TEXT,
    expires_at      TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    last_active_at  TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_sessions_user    ON sessions(user_type, user_id);
CREATE INDEX idx_sessions_expires ON sessions(expires_at);

-- ============================================================
-- HOSTING_STATUS (Theo dõi hạn hosting)
-- ============================================================
CREATE TABLE hosting_status (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id       UUID UNIQUE NOT NULL REFERENCES tenants(id),
    client_id       UUID NOT NULL REFERENCES clients(id),

    status          VARCHAR(20) DEFAULT 'active'
                    CHECK (status IN ('active','suspended','cancelled')),

    -- Package info
    package_name    VARCHAR(100),
    monthly_fee     DECIMAL(12,2),

    -- Timeline
    activated_at    DATE NOT NULL,
    paid_until      DATE NOT NULL,
    next_billing    DATE,

    -- Suspension
    grace_period_days   SMALLINT DEFAULT 5,
    reminder_sent_at    TIMESTAMPTZ,
    reminder_count      SMALLINT DEFAULT 0,
    suspended_at        TIMESTAMPTZ,
    suspension_reason   TEXT,

    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_hosting_paid_until ON hosting_status(paid_until);
CREATE INDEX idx_hosting_status     ON hosting_status(status);

-- ============================================================
-- PAYMENT_RECORDS (Sổ thu tiền thủ công)
-- ============================================================
CREATE TABLE payment_records (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id       UUID NOT NULL REFERENCES clients(id),
    tenant_id       UUID NOT NULL REFERENCES tenants(id),

    payment_type    VARCHAR(30) NOT NULL
                    CHECK (payment_type IN (
                        'setup_fee','monthly_hosting',
                        'renewal','upgrade','other'
                    )),

    amount          DECIMAL(12,2) NOT NULL,
    method          VARCHAR(20)
                    CHECK (method IN ('cash','bank_transfer','momo','zalo_pay')),

    period_month    SMALLINT,
    period_year     SMALLINT,

    paid_at         DATE NOT NULL,
    note            TEXT,
    transaction_ref VARCHAR(255),

    confirmed       BOOLEAN DEFAULT FALSE,
    confirmed_at    TIMESTAMPTZ,
    confirmed_by    UUID REFERENCES platform_users(id),

    created_by      UUID REFERENCES platform_users(id),
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_payments_client  ON payment_records(client_id);
CREATE INDEX idx_payments_tenant  ON payment_records(tenant_id);
CREATE INDEX idx_payments_period  ON payment_records(period_year, period_month);

-- ============================================================
-- SETUP_REQUESTS
-- ============================================================
CREATE TABLE setup_requests (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id       UUID REFERENCES clients(id),

    vertical_id     UUID REFERENCES verticals(id),
    template_id     UUID REFERENCES templates(id),

    -- Yêu cầu
    business_name   VARCHAR(255) NOT NULL,
    desired_slug    VARCHAR(100),
    primary_color   VARCHAR(7),
    logo_url        VARCHAR(1000),
    special_requests TEXT,

    -- Processing
    status          VARCHAR(20) DEFAULT 'pending'
                    CHECK (status IN ('pending','in_progress','completed','cancelled')),
    assigned_to     UUID REFERENCES platform_users(id),
    tenant_id       UUID REFERENCES tenants(id),
    completed_at    TIMESTAMPTZ,
    notes           TEXT,

    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================
-- AUDIT_LOGS (Ghi lại mọi action quan trọng)
-- ============================================================
CREATE TABLE audit_logs (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Ai thực hiện
    actor_type      VARCHAR(20) NOT NULL
                    CHECK (actor_type IN ('platform_user','tenant_user','system')),
    actor_id        UUID,
    actor_name      VARCHAR(255),           -- Cache tên để không cần join

    -- Trên website nào
    tenant_id       UUID REFERENCES tenants(id) ON DELETE SET NULL,

    -- Action gì
    action          VARCHAR(100) NOT NULL,
    -- 'tenant.created', 'template.switched', 'user.created'
    -- 'article.published', 'payment.recorded', 'domain.added'

    resource_type   VARCHAR(50),
    resource_id     UUID,
    resource_name   VARCHAR(255),           -- Cache tên resource

    -- Chi tiết thay đổi
    changes         JSONB,
    -- {
    --   "before": {"template_id": "old-id", "template_code": "lottery-classic"},
    --   "after":  {"template_id": "new-id", "template_code": "lottery-modern"}
    -- }

    -- Context
    ip_address      INET,
    user_agent      TEXT,
    metadata        JSONB DEFAULT '{}',

    created_at      TIMESTAMPTZ DEFAULT NOW()
)
PARTITION BY RANGE (created_at);

-- Partition theo tháng
CREATE TABLE audit_logs_2024_01 PARTITION OF audit_logs
    FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');
CREATE TABLE audit_logs_2024_02 PARTITION OF audit_logs
    FOR VALUES FROM ('2024-02-01') TO ('2024-03-01');
-- (Dùng pg_partman để auto-create partitions)

CREATE INDEX idx_audit_tenant    ON audit_logs(tenant_id, created_at DESC);
CREATE INDEX idx_audit_actor     ON audit_logs(actor_type, actor_id);
CREATE INDEX idx_audit_action    ON audit_logs(action);

-- ============================================================
-- NOTIFICATIONS (Thông báo nội bộ cho platform admins)
-- ============================================================
CREATE TABLE notifications (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- Ai nhận
    recipient_type  VARCHAR(20)
                    CHECK (recipient_type IN ('platform_user','tenant_user')),
    recipient_id    UUID NOT NULL,

    -- Nội dung
    type            VARCHAR(50) NOT NULL,
    -- 'hosting_expiring', 'fetch_failed', 'new_setup_request'
    -- 'payment_received', 'domain_verified'

    title           VARCHAR(255) NOT NULL,
    body            TEXT,
    data            JSONB DEFAULT '{}',     -- Extra data để render link, etc.

    -- Delivery
    channel         VARCHAR(20) DEFAULT 'in_app'
                    CHECK (channel IN ('in_app','telegram','email','sms')),

    is_read         BOOLEAN DEFAULT FALSE,
    read_at         TIMESTAMPTZ,
    sent_at         TIMESTAMPTZ,

    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_notif_recipient ON notifications(recipient_type, recipient_id, is_read);
CREATE INDEX idx_notif_created   ON notifications(created_at DESC);

-- ============================================================
-- JOB_EXECUTIONS (Monitor background jobs)
-- ============================================================
CREATE TABLE job_executions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_name        VARCHAR(100) NOT NULL,
    tenant_id       UUID REFERENCES tenants(id),

    status          VARCHAR(20) DEFAULT 'running'
                    CHECK (status IN ('running','completed','failed','skipped')),

    payload         JSONB DEFAULT '{}',
    result          JSONB DEFAULT '{}',
    error_message   TEXT,

    started_at      TIMESTAMPTZ DEFAULT NOW(),
    completed_at    TIMESTAMPTZ,
    duration_ms     INT,
    retry_count     SMALLINT DEFAULT 0,

    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_jobs_name   ON job_executions(job_name, created_at DESC);
CREATE INDEX idx_jobs_status ON job_executions(status);

-- ============================================================
-- SYSTEM_CONFIGS
-- ============================================================
CREATE TABLE system_configs (
    key         VARCHAR(255) PRIMARY KEY,
    value       TEXT NOT NULL,
    value_type  VARCHAR(20) DEFAULT 'string'
                CHECK (value_type IN ('string','number','boolean','json')),
    description TEXT,
    is_sensitive BOOLEAN DEFAULT FALSE,
    updated_at  TIMESTAMPTZ DEFAULT NOW()
);

INSERT INTO system_configs (key, value, value_type, description) VALUES
('platform.name',               'WebsiteHub',         'string',  'Tên platform'),
('platform.domain',             'websitehub.vn',      'string',  'Domain platform'),
('platform.support_phone',      '0909xxxxxx',         'string',  'SĐT hỗ trợ'),
('platform.telegram_bot_token', 'xxx',                'string',  'Telegram bot nhận alert'),
('platform.telegram_chat_id',   'xxx',                'string',  'Chat ID nhận alert'),
('lottery.fetch_retry_max',     '3',                  'number',  'Max retry fetch KQXS'),
('lottery.fetch_retry_delay',   '300',                'number',  'Giây chờ giữa retry'),
('lottery.cache_ttl_seconds',   '120',                'number',  'Cache KQXS (giây)'),
('hosting.grace_period_days',   '5',                  'number',  'Ngày gia hạn trước khi suspend'),
('hosting.reminder_days_before','7',                  'number',  'Nhắc trước khi hết hạn bao nhiêu ngày');
```
### 3.3 Lottery + CMS Tables (Giữ nguyên từ design trước)
```sql

-- ============================================================
-- CMS TABLES (Giữ nguyên, không thay đổi)
-- ============================================================
-- categories, articles, pages, menus, media
-- (Xem schema đã thiết kế ở các phần trước)

-- ============================================================
-- LOTTERY TABLES (Giữ nguyên, không thay đổi)
-- ============================================================
-- lottery_regions, lottery_provinces
-- lottery_results, lottery_loto_detail
-- lottery_frequency_stats, lottery_head_tail_stats
-- lottery_special_prize_stats, lottery_predictions
-- lottery_author_stats, lottery_tenant_settings
-- lottery_fetch_logs

-- ============================================================
-- ADS & ANALYTICS (Giữ nguyên)
-- ============================================================
-- ad_zones, ads
-- page_views (partitioned), analytics_daily

-- ============================================================
-- VIEWS - Hữu ích cho queries thường dùng
-- ============================================================

-- View: Thông tin đầy đủ của tenant
CREATE VIEW v_tenant_detail AS
SELECT
    t.id,
    t.slug,
    t.name,
    t.status,
    t.color_overrides,
    t.feature_overrides,
    t.template_history,
    t.created_at,

    -- Client info
    c.id            AS client_id,
    c.full_name     AS client_name,
    c.phone         AS client_phone,
    c.business_name AS client_business,

    -- Vertical info
    v.code          AS vertical_code,
    v.name          AS vertical_name,

    -- Template info
    tpl.id          AS template_id,
    tpl.code        AS template_code,
    tpl.name        AS template_name,
    tpl.layout_config,
    tpl.default_colors,

    -- Effective colors (override nếu có, fallback về default)
    COALESCE(t.color_overrides, tpl.default_colors) AS effective_colors,

    -- Settings
    ts.logo_url,
    ts.favicon_url,
    ts.tagline,
    ts.google_analytics_id,
    ts.custom_css,
    ts.timezone,
    ts.locale,

    -- Primary domain
    d.domain        AS primary_domain,

    -- Hosting status
    hs.status       AS hosting_status,
    hs.paid_until,
    hs.next_billing,
    hs.monthly_fee

FROM tenants t
LEFT JOIN clients c          ON c.id = t.client_id
LEFT JOIN verticals v        ON v.id = t.vertical_id
LEFT JOIN templates tpl      ON tpl.id = t.template_id
LEFT JOIN tenant_settings ts ON ts.tenant_id = t.id
LEFT JOIN domains d          ON d.tenant_id = t.id AND d.is_primary = TRUE
LEFT JOIN hosting_status hs  ON hs.tenant_id = t.id
WHERE t.deleted_at IS NULL;

-- View: Websites sắp hết hạn (cần thu tiền)
CREATE VIEW v_expiring_websites AS
SELECT
    hs.tenant_id,
    t.name          AS website_name,
    t.slug,
    c.full_name     AS client_name,
    c.phone         AS client_phone,
    hs.paid_until,
    hs.next_billing,
    hs.monthly_fee,
    hs.status       AS hosting_status,
    (hs.paid_until - CURRENT_DATE) AS days_remaining,
    d.domain        AS primary_domain
FROM hosting_status hs
JOIN tenants t  ON t.id = hs.tenant_id
JOIN clients c  ON c.id = hs.client_id
LEFT JOIN domains d ON d.tenant_id = t.id AND d.is_primary = TRUE
WHERE hs.status = 'active'
AND hs.paid_until <= CURRENT_DATE + INTERVAL '30 days'
ORDER BY hs.paid_until ASC;

-- View: Revenue summary
CREATE VIEW v_revenue_summary AS
SELECT
    DATE_TRUNC('month', paid_at) AS month,
    COUNT(*)                      AS transaction_count,
    SUM(amount)                   AS total_revenue,
    SUM(CASE WHEN payment_type = 'setup_fee'       THEN amount ELSE 0 END) AS setup_revenue,
    SUM(CASE WHEN payment_type = 'monthly_hosting' THEN amount ELSE 0 END) AS hosting_revenue,
    COUNT(DISTINCT client_id)     AS paying_clients
FROM payment_records
WHERE confirmed = TRUE
GROUP BY DATE_TRUNC('month', paid_at)
ORDER BY month DESC;

-- View: User permissions đầy đủ (platform user)
CREATE VIEW v_platform_user_permissions AS
SELECT
    pu.id           AS user_id,
    pu.email,
    pu.full_name,
    r.code          AS role_code,
    r.level         AS role_level,
    -- Permissions từ role
    ARRAY_AGG(DISTINCT p.code)
        FILTER (WHERE p.id NOT IN (
            SELECT UNNEST(pu.revoked_permissions)
        )) AS effective_permissions
FROM platform_users pu
JOIN roles r ON r.id = pu.role_id
JOIN role_permissions rp ON rp.role_id = r.id
JOIN permissions p ON p.id = rp.permission_id
WHERE pu.is_active = TRUE
GROUP BY pu.id, pu.email, pu.full_name, r.code, r.level;
```
# PHẦN IV: FINAL TABLE LIST
## 4. DANH SÁCH ĐẦY ĐỦ CÁC BẢNG
```

═══════════════════════════════════════════════════════════
TỔNG: 38 BẢNG + 4 VIEWS
═══════════════════════════════════════════════════════════

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GROUP 1: PLATFORM CORE (10 bảng)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
01. verticals          → Ngành nghề (lottery, bds, spa...)
02. templates          → Template/giao diện của từng vertical
03. roles              → Vai trò (super_admin, admin, owner, editor, viewer)
04. permissions        → Quyền hạn chi tiết (~35 permissions)
05. role_permissions   → Mapping role ↔ permission
06. platform_users     → Super admin + Admin của bạn
07. clients            → Khách hàng mua website
08. tenants            → Mỗi website = 1 tenant
09. tenant_settings    → Cài đặt website (logo, SEO, tracking...)
10. domains            → Domain mapping (subdomain + custom)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GROUP 2: USER & AUTH (3 bảng)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
11. tenant_users       → Users của mỗi website (owner, editor, viewer)
12. sessions           → Sessions cho cả platform & tenant users
13. notifications      → Thông báo nội bộ

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GROUP 3: BUSINESS MANAGEMENT (3 bảng)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
14. hosting_status     → Theo dõi hạn hosting của mỗi website
15. payment_records    → Sổ thu tiền thủ công
16. setup_requests     → Quản lý yêu cầu tạo website mới

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GROUP 4: CMS (5 bảng)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
17. categories         → Danh mục bài viết
18. articles           → Bài viết / tin tức / nhận định
19. pages              → Trang tĩnh (Giới thiệu, Liên hệ)
20. menus              → Navigation menu builder
21. media              → Thư viện ảnh

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GROUP 5: LOTTERY VERTICAL (10 bảng)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
22. lottery_regions          → Miền Bắc / Trung / Nam
23. lottery_provinces        → 63 tỉnh/đài xổ số
24. lottery_results          → Kết quả xổ số hàng ngày
25. lottery_loto_detail      → Chi tiết loto (00-99) extracted
26. lottery_frequency_stats  → Thống kê tần suất (cached)
27. lottery_head_tail_stats  → Thống kê đầu/đuôi
28. lottery_special_prize_stats → Thống kê giải ĐB
29. lottery_predictions      → Soi cầu / nhận định
30. lottery_author_stats     → Tỷ lệ thắng của tác giả
31. lottery_tenant_settings  → Cấu hình lottery per-website
32. lottery_fetch_logs       → Log fetch KQXS

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GROUP 6: ADS (2 bảng)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
33. ad_zones           → Vị trí quảng cáo
34. ads                → Nội dung quảng cáo

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GROUP 7: ANALYTICS (2 bảng - partitioned)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
35. page_views         → Lượt xem trang (partitioned by month)
36. analytics_daily    → Tổng hợp analytics theo ngày

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
GROUP 8: SYSTEM (2 bảng)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
37. audit_logs         → Log mọi action (partitioned by month)
38. system_configs     → Cấu hình hệ thống
39. job_executions     → Monitor background jobs

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
VIEWS (4 views)
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
V1. v_tenant_detail           → Tenant đầy đủ info (join nhiều bảng)
V2. v_expiring_websites       → Websites sắp hết hạn
V3. v_revenue_summary         → Tổng hợp doanh thu theo tháng
V4. v_platform_user_permissions → Permissions hiệu lực của platform user
```
# PHẦN V: JAVA IMPLEMENTATION
## 5. RBAC IMPLEMENTATION
### 5.1 Permission System
```java

// PermissionConstants.java
public final class PermissionConstants {

    // Platform permissions
    public static final String PLATFORM_TENANTS_VIEW     = "platform.tenants.view";
    public static final String PLATFORM_TENANTS_CREATE   = "platform.tenants.create";
    public static final String PLATFORM_TENANTS_EDIT     = "platform.tenants.edit";
    public static final String PLATFORM_TENANTS_DELETE   = "platform.tenants.delete";
    public static final String PLATFORM_TENANTS_SUSPEND  = "platform.tenants.suspend";
    public static final String PLATFORM_TENANTS_IMPERSONATE = "platform.tenants.impersonate";

    public static final String PLATFORM_CLIENTS_VIEW    = "platform.clients.view";
    public static final String PLATFORM_CLIENTS_CREATE  = "platform.clients.create";
    public static final String PLATFORM_CLIENTS_EDIT    = "platform.clients.edit";

    public static final String PLATFORM_TEMPLATES_VIEW  = "platform.templates.view";
    public static final String PLATFORM_TEMPLATES_EDIT  = "platform.templates.edit";

    public static final String PLATFORM_PAYMENTS_VIEW   = "platform.payments.view";
    public static final String PLATFORM_PAYMENTS_CREATE = "platform.payments.create";

    public static final String PLATFORM_SYSTEM_CONFIG   = "platform.system.config";
    public static final String PLATFORM_SYSTEM_LOGS     = "platform.system.logs";

    // Content permissions
    public static final String CONTENT_ARTICLES_VIEW    = "content.articles.view";
    public static final String CONTENT_ARTICLES_CREATE  = "content.articles.create";
    public static final String CONTENT_ARTICLES_EDIT    = "content.articles.edit";
    public static final String CONTENT_ARTICLES_DELETE  = "content.articles.delete";
    public static final String CONTENT_ARTICLES_PUBLISH = "content.articles.publish";

    public static final String CONTENT_MEDIA_VIEW       = "content.media.view";
    public static final String CONTENT_MEDIA_UPLOAD     = "content.media.upload";
    public static final String CONTENT_MEDIA_DELETE     = "content.media.delete";

    // Lottery permissions
    public static final String LOTTERY_PREDICTIONS_VIEW   = "lottery.predictions.view";
    public static final String LOTTERY_PREDICTIONS_CREATE = "lottery.predictions.create";
    public static final String LOTTERY_PREDICTIONS_EDIT   = "lottery.predictions.edit";
    public static final String LOTTERY_PREDICTIONS_DELETE = "lottery.predictions.delete";

    // Analytics
    public static final String ANALYTICS_VIEW   = "analytics.view";
    public static final String ANALYTICS_EXPORT = "analytics.export";

    // Settings
    public static final String SETTINGS_GENERAL_EDIT   = "settings.general.edit";
    public static final String SETTINGS_DOMAIN_MANAGE  = "settings.domain.manage";
    public static final String SETTINGS_TEMPLATE_SWITCH = "settings.template.switch";
    public static final String SETTINGS_USERS_MANAGE   = "settings.users.manage";
    public static final String SETTINGS_SEO_EDIT       = "settings.seo.edit";

    private PermissionConstants() {}
}

// UserPrincipal.java - Security context
@Data
@Builder
public class UserPrincipal {
    private UUID    userId;
    private String  userType;       // 'platform_user' | 'tenant_user'
    private String  email;
    private String  fullName;
    private String  roleCode;
    private int     roleLevel;
    private UUID    tenantId;       // null nếu là platform_user
    private Set<String> permissions; // Cached permission codes

    // Helper methods
    public boolean isPlatformUser() {
        return "platform_user".equals(userType);
    }

    public boolean isSuperAdmin() {
        return "super_admin".equals(roleCode);
    }

    public boolean isAdmin() {
        return "admin".equals(roleCode) || isSuperAdmin();
    }

    public boolean hasPermission(String permissionCode) {
        if (isSuperAdmin()) return true; // Super admin có tất cả
        return permissions.contains(permissionCode);
    }

    public boolean hasAnyPermission(String... permissionCodes) {
        if (isSuperAdmin()) return true;
        for (String code : permissionCodes) {
            if (permissions.contains(code)) return true;
        }
        return false;
    }

    public boolean hasAllPermissions(String... permissionCodes) {
        if (isSuperAdmin()) return true;
        for (String code : permissionCodes) {
            if (!permissions.contains(code)) return false;
        }
        return true;
    }

    public boolean canManageTenant(UUID targetTenantId) {
        if (isSuperAdmin()) return true;
        if (!"platform_user".equals(userType)) return false;
        // Admin chỉ manage tenants trong allowed list
        // Nếu allowed_tenant_ids empty = manage all
        return true; // Simplified - check DB for real restriction
    }
}

// @RequirePermission annotation
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {
    String[] value();                       // Permission codes
    boolean requireAll() default false;     // true = AND, false = OR
    String message() default "";
}

// @RequireRole annotation
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    String[] value();                       // Role codes
    int minLevel() default 0;              // Hoặc check theo level
}

// PermissionAspect.java
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PermissionAspect {

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint pjp,
                                  RequirePermission requirePermission) throws Throwable {

        UserPrincipal user = SecurityContextHolder.getUser();

        boolean hasAccess;
        if (requirePermission.requireAll()) {
            hasAccess = user.hasAllPermissions(requirePermission.value());
        } else {
            hasAccess = user.hasAnyPermission(requirePermission.value());
        }

        if (!hasAccess) {
            String msg = requirePermission.message().isEmpty()
                ? "Bạn không có quyền thực hiện hành động này"
                : requirePermission.message();
            throw new AccessDeniedException(msg);
        }

        return pjp.proceed();
    }

    @Around("@annotation(requireRole)")
    public Object checkRole(ProceedingJoinPoint pjp,
                            RequireRole requireRole) throws Throwable {

        UserPrincipal user = SecurityContextHolder.getUser();

        boolean hasRole = Arrays.asList(requireRole.value()).contains(user.getRoleCode())
                       || user.getRoleLevel() >= requireRole.minLevel();

        if (!hasRole) {
            throw new AccessDeniedException("Bạn không có vai trò phù hợp");
        }

        return pjp.proceed();
    }
}
```
### 5.2 Template Switch Service
```java

// TemplateSwitchService.java
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TemplateSwitchService {

    private final TenantRepository tenantRepository;
    private final TemplateRepository templateRepository;
    private final AuditLogService auditLogService;
    private final TenantCacheService cacheService;
    private final NotificationService notificationService;

    public TenantDTO switchTemplate(UUID tenantId, UUID newTemplateId) {
        UserPrincipal actor = SecurityContextHolder.getUser();

        // 1. Validate tenant tồn tại và actor có quyền
        Tenant tenant = tenantRepository.findById(tenantId)
            .orElseThrow(() -> new NotFoundException("Website không tồn tại"));

        // 2. Validate template tồn tại và thuộc cùng vertical
        Template newTemplate = templateRepository.findById(newTemplateId)
            .orElseThrow(() -> new NotFoundException("Template không tồn tại"));

        if (!newTemplate.getVerticalId().equals(tenant.getVerticalId())) {
            throw new BusinessException(
                "Template không thuộc ngành nghề của website này"
            );
        }

        // 3. Lưu lịch sử switch
        Template oldTemplate = templateRepository.findById(tenant.getTemplateId())
            .orElseThrow();

        List<Map<String, Object>> history = tenant.getTemplateHistory() != null
            ? new ArrayList<>(tenant.getTemplateHistory())
            : new ArrayList<>();

        history.add(Map.of(
            "template_id",   oldTemplate.getId().toString(),
            "template_code", oldTemplate.getCode(),
            "template_name", oldTemplate.getName(),
            "switched_at",   Instant.now().toString(),
            "switched_by",   actor.getUserId().toString(),
            "switched_by_name", actor.getFullName(),
            "reason",        "Manual switch"
        ));

        // Chỉ giữ 10 lịch sử gần nhất
        if (history.size() > 10) {
            history = history.subList(history.size() - 10, history.size());
        }

        // 4. Cập nhật template
        tenant.setTemplateId(newTemplateId);
        tenant.setTemplateHistory(history);
        tenantRepository.save(tenant);

        // 5. Invalidate cache (quan trọng - frontend sẽ load template mới)
        cacheService.invalidateTenantCache(tenant.getSlug());

        // 6. Audit log
        auditLogService.log(AuditLog.builder()
            .actorType(actor.getUserType())
            .actorId(actor.getUserId())
            .actorName(actor.getFullName())
            .tenantId(tenantId)
            .action("template.switched")
            .resourceType("tenant")
            .resourceId(tenantId)
            .resourceName(tenant.getName())
            .changes(Map.of(
                "before", Map.of(
                    "template_id",   oldTemplate.getId(),
                    "template_code", oldTemplate.getCode()
                ),
                "after", Map.of(
                    "template_id",   newTemplate.getId(),
                    "template_code", newTemplate.getCode()
                )
            ))
            .build()
        );

        // 7. Notify website owner
        notificationService.notifyTenantOwner(tenantId,
            Notification.builder()
                .type("template.switched")
                .title("Giao diện website đã thay đổi")
                .body("Website " + tenant.getName() + " đã được đổi sang giao diện " +
                      newTemplate.getName())
                .build()
        );

        log.info("Template switched: tenant={}, from={}, to={}, by={}",
            tenant.getSlug(), oldTemplate.getCode(),
            newTemplate.getCode(), actor.getEmail());

        return toDTO(tenant, newTemplate);
    }

    // Preview template (không lưu, chỉ return config)
    public TemplatePreviewDTO previewTemplate(UUID tenantId, UUID templateId) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow();
        Template template = templateRepository.findById(templateId).orElseThrow();

        // Merge: template default + tenant overrides
        Map<String, Object> effectiveColors = mergeColors(
            template.getDefaultColors(),
            tenant.getColorOverrides()
        );

        Map<String, Object> effectiveFeatures = mergeFeatures(
            template.getLayoutConfig(),
            tenant.getFeatureOverrides()
        );

        return TemplatePreviewDTO.builder()
            .templateId(templateId)
            .templateCode(template.getCode())
            .templateName(template.getName())
            .layoutConfig(template.getLayoutConfig())
            .effectiveColors(effectiveColors)
            .effectiveFeatures(effectiveFeatures)
            .previewUrl(template.getPreviewUrl())
            .build();
    }

    private Map<String, Object> mergeColors(
            Map<String, Object> templateColors,
            Map<String, Object> tenantOverrides) {

        if (tenantOverrides == null || tenantOverrides.isEmpty()) {
            return templateColors;
        }

        Map<String, Object> merged = new HashMap<>(templateColors);
        merged.putAll(tenantOverrides);
        return merged;
    }

    private Map<String, Object> mergeFeatures(
            Map<String, Object> layoutConfig,
            Map<String, Object> featureOverrides) {

        if (featureOverrides == null || featureOverrides.isEmpty()) {
            return layoutConfig;
        }

       Map<String, Object> merged = new HashMap<>(layoutConfig);
        // Override features_visible
        if (layoutConfig.containsKey("features_visible")) {
            Map<String, Object> defaultFeatures =
                new HashMap<>((Map<String, Object>) layoutConfig.get("features_visible"));
            defaultFeatures.putAll(featureOverrides);
            merged.put("features_visible", defaultFeatures);
        }
        return merged;
    }
}

// ============================================================
// TenantSetupService.java - Tạo website mới cho khách
// ============================================================
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TenantSetupService {

    private final TenantRepository tenantRepository;
    private final TenantSettingsRepository settingsRepository;
    private final TenantUserRepository tenantUserRepository;
    private final DomainRepository domainRepository;
    private final HostingStatusRepository hostingStatusRepository;
    private final LotteryTenantSettingsRepository lotterySettingsRepository;
    private final SetupRequestRepository setupRequestRepository;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Tạo website mới cho khách hàng trong < 5 phút
     * Được gọi từ Super Admin Panel
     */
    public TenantSetupResult quickSetup(QuickSetupRequest request) {
        UserPrincipal actor = SecurityContextHolder.getUser();

        log.info("Starting quick setup for client={}, vertical={}, template={}",
            request.getClientId(), request.getVerticalCode(), request.getTemplateCode());

        // Step 1: Validate
        validateSetupRequest(request);

        // Step 2: Tạo Tenant
        Tenant tenant = createTenant(request, actor);

        // Step 3: Tạo Tenant Settings
        createTenantSettings(tenant, request);

        // Step 4: Tạo Domain (subdomain mặc định)
        Domain domain = createDefaultDomain(tenant, request.getSlug());

        // Step 5: Tạo Owner Account cho khách
        TenantUser ownerAccount = createOwnerAccount(tenant, request);

        // Step 6: Setup Vertical-specific config
        setupVerticalConfig(tenant, request);

        // Step 7: Tạo Hosting Status
        HostingStatus hostingStatus = createHostingStatus(tenant, request);

        // Step 8: Update Setup Request nếu có
        if (request.getSetupRequestId() != null) {
            completeSetupRequest(request.getSetupRequestId(), tenant.getId());
        }

        // Step 9: Audit log
        auditLogService.log(AuditLog.builder()
            .actorType(actor.getUserType())
            .actorId(actor.getUserId())
            .actorName(actor.getFullName())
            .tenantId(tenant.getId())
            .action("tenant.created")
            .resourceType("tenant")
            .resourceId(tenant.getId())
            .resourceName(tenant.getName())
            .changes(Map.of(
                "slug",         request.getSlug(),
                "vertical",     request.getVerticalCode(),
                "template",     request.getTemplateCode(),
                "client_phone", request.getClientPhone()
            ))
            .build()
        );

        // Step 10: Notify via Telegram
        notificationService.notifyAdmins(
            "🎉 Website mới đã tạo xong!\n" +
            "Tên: " + tenant.getName() + "\n" +
            "Domain: " + domain.getDomain() + "\n" +
            "Khách: " + request.getClientName() + "\n" +
            "SĐT: " + request.getClientPhone()
        );

        log.info("✅ Quick setup completed: slug={}, domain={}",
            tenant.getSlug(), domain.getDomain());

        return TenantSetupResult.builder()
            .tenantId(tenant.getId())
            .slug(tenant.getSlug())
            .domain(domain.getDomain())
            .adminEmail(ownerAccount.getEmail())
            .adminPassword(request.getInitialPassword()) // Trả về để gửi cho khách
            .adminUrl("https://" + domain.getDomain() + "/admin")
            .build();
    }

    private void validateSetupRequest(QuickSetupRequest request) {
        // Check slug chưa tồn tại
        if (tenantRepository.existsBySlug(request.getSlug())) {
            throw new BusinessException("Slug '" + request.getSlug() + "' đã được sử dụng");
        }

        // Check domain chưa tồn tại
        String fullDomain = request.getSlug() + "." +
            systemConfigService.get("platform.domain");
        if (domainRepository.existsByDomain(fullDomain)) {
            throw new BusinessException("Domain đã tồn tại");
        }
    }

    private Tenant createTenant(QuickSetupRequest request, UserPrincipal actor) {
        UUID verticalId = verticalRepository.findByCode(request.getVerticalCode())
            .orElseThrow(() -> new NotFoundException("Vertical không tồn tại"))
            .getId();

        UUID templateId = templateRepository.findByCode(request.getTemplateCode())
            .orElseThrow(() -> new NotFoundException("Template không tồn tại"))
            .getId();

        Tenant tenant = Tenant.builder()
            .id(UUID.randomUUID())
            .clientId(request.getClientId())
            .verticalId(verticalId)
            .templateId(templateId)
            .slug(request.getSlug())
            .name(request.getBusinessName())
            .colorOverrides(request.getColorOverrides())
            .featureOverrides(Map.of())
            .status(TenantStatus.ACTIVE)
            .setupCompletedAt(Instant.now())
            .createdBy(actor.getUserId())
            .build();

        return tenantRepository.save(tenant);
    }

    private TenantSettings createTenantSettings(Tenant tenant, QuickSetupRequest request) {
        TenantSettings settings = TenantSettings.builder()
            .tenantId(tenant.getId())
            .logoUrl(request.getLogoUrl())
            .tagline(request.getTagline())
            .phone(request.getContactPhone())
            .email(request.getContactEmail())
            .address(request.getAddress())
            .seoDefaultTitle(request.getBusinessName() +
                " - Kết Quả Xổ Số Hôm Nay")
            .seoDefaultDesc("Xem kết quả xổ số " + request.getBusinessName() +
                " nhanh nhất, chính xác nhất")
            .timezone("Asia/Ho_Chi_Minh")
            .locale("vi-VN")
            .build();

        return settingsRepository.save(settings);
    }

    private Domain createDefaultDomain(Tenant tenant, String slug) {
        String platformDomain = systemConfigService.get("platform.domain");
        String fullDomain = slug + "." + platformDomain;

        Domain domain = Domain.builder()
            .tenantId(tenant.getId())
            .domain(fullDomain)
            .type(DomainType.SUBDOMAIN)
            .isPrimary(true)
            .verified(true)          // Subdomain tự động verified
            .sslStatus(SslStatus.ACTIVE)  // Wildcard SSL đã có sẵn
            .build();

        return domainRepository.save(domain);
    }

    private TenantUser createOwnerAccount(Tenant tenant, QuickSetupRequest request) {
        UUID ownerRoleId = roleRepository.findByCode("website_owner")
            .orElseThrow().getId();

        // Generate password nếu không cung cấp
        String password = request.getInitialPassword() != null
            ? request.getInitialPassword()
            : generatePassword();

        request.setInitialPassword(password); // Set lại để return

        TenantUser owner = TenantUser.builder()
            .tenantId(tenant.getId())
            .roleId(ownerRoleId)
            .email(request.getOwnerEmail())
            .passwordHash(passwordEncoder.encode(password))
            .fullName(request.getClientName())
            .phone(request.getClientPhone())
            .isActive(true)
            .build();

        return tenantUserRepository.save(owner);
    }

    private void setupVerticalConfig(Tenant tenant, QuickSetupRequest request) {
        switch (request.getVerticalCode()) {
            case "lottery" -> setupLotteryConfig(tenant, request);
            case "realestate" -> setupRealEstateConfig(tenant, request);
            // More verticals...
        }
    }

    private void setupLotteryConfig(Tenant tenant, QuickSetupRequest request) {
        LotteryTenantSettings lotterySettings = LotteryTenantSettings.builder()
            .tenantId(tenant.getId())
            .defaultRegion(request.getDefaultRegion() != null
                ? request.getDefaultRegion() : "south")
            .showPredictions(true)
            .showStatistics(true)
            .showLiveResult(true)
            .showPrintButton(true)
            .showShareButtons(true)
            .autoSeoPages(true)
            .seoTitleTemplate(
                "KQXS {province_name} {draw_date} - " + request.getBusinessName()
            )
            .build();

        lotterySettingsRepository.save(lotterySettings);
    }

    private HostingStatus createHostingStatus(
            Tenant tenant, QuickSetupRequest request) {

        LocalDate today = LocalDate.now();
        LocalDate paidUntil = request.getPaidMonths() != null
            ? today.plusMonths(request.getPaidMonths())
            : today.plusMonths(1);

        HostingStatus status = HostingStatus.builder()
            .tenantId(tenant.getId())
            .clientId(request.getClientId())
            .status(HostingStatusEnum.ACTIVE)
            .packageName(request.getPackageName())
            .monthlyFee(request.getMonthlyFee())
            .activatedAt(today)
            .paidUntil(paidUntil)
            .nextBilling(paidUntil.plusDays(-7)) // Nhắc trước 7 ngày
            .build();

        return hostingStatusRepository.save(status);
    }

    private String generatePassword() {
        // Generate password 8 ký tự dễ nhớ
        String chars = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
```
### 5.3 Permission-based Controllers
```java

// PlatformTenantController.java (Super Admin & Admin)
@RestController
@RequestMapping("/platform/v1/tenants")
@RequiredArgsConstructor
@Slf4j
public class PlatformTenantController {

    private final TenantSetupService setupService;
    private final TemplateSwitchService templateSwitchService;
    private final TenantRepository tenantRepository;
    private final TenantStatusService statusService;

    // Xem danh sách websites
    @GetMapping
    @RequirePermission(PLATFORM_TENANTS_VIEW)
    public ApiResponse<PageResponse<TenantListDTO>> list(
            @RequestParam(defaultValue = "1")   int page,
            @RequestParam(defaultValue = "20")  int size,
            @RequestParam(required = false)     String status,
            @RequestParam(required = false)     String verticalCode,
            @RequestParam(required = false)     String search) {

        UserPrincipal actor = SecurityContextHolder.getUser();
        return ApiResponse.ok(
            tenantRepository.findAllWithFilter(
                actor.isAdmin() ? null : actor.getAllowedTenantIds(),
                status, verticalCode, search,
                PageRequest.of(page - 1, size)
            )
        );
    }

    // Tạo website mới
    @PostMapping("/setup")
    @RequirePermission(PLATFORM_TENANTS_CREATE)
    public ApiResponse<TenantSetupResult> quickSetup(
            @Valid @RequestBody QuickSetupRequest request) {
        return ApiResponse.ok(
            setupService.quickSetup(request),
            "Website đã được tạo thành công"
        );
    }

    // Xem chi tiết website
    @GetMapping("/{id}")
    @RequirePermission(PLATFORM_TENANTS_VIEW)
    public ApiResponse<TenantDetailDTO> getDetail(@PathVariable UUID id) {
        return ApiResponse.ok(tenantRepository.findDetailById(id)
            .orElseThrow(() -> new NotFoundException("Website không tồn tại")));
    }

    // Đổi template
    @PutMapping("/{id}/template")
    @RequirePermission(PLATFORM_TENANTS_EDIT)
    public ApiResponse<TenantDTO> switchTemplate(
            @PathVariable UUID id,
            @Valid @RequestBody SwitchTemplateRequest request) {
        return ApiResponse.ok(
            templateSwitchService.switchTemplate(id, request.getTemplateId()),
            "Đã đổi giao diện thành công"
        );
    }

    // Preview template
    @GetMapping("/{id}/template/preview")
    @RequirePermission(PLATFORM_TENANTS_VIEW)
    public ApiResponse<TemplatePreviewDTO> previewTemplate(
            @PathVariable UUID id,
            @RequestParam UUID templateId) {
        return ApiResponse.ok(
            templateSwitchService.previewTemplate(id, templateId)
        );
    }

    // Suspend website
    @PostMapping("/{id}/suspend")
    @RequirePermission(PLATFORM_TENANTS_SUSPEND)
    public ApiResponse<Void> suspend(
            @PathVariable UUID id,
            @RequestBody SuspendRequest request) {
        statusService.suspend(id, request.getReason());
        return ApiResponse.ok(null, "Website đã tạm ngưng");
    }

    // Activate website
    @PostMapping("/{id}/activate")
    @RequirePermission(PLATFORM_TENANTS_SUSPEND)
    public ApiResponse<Void> activate(@PathVariable UUID id) {
        statusService.activate(id);
        return ApiResponse.ok(null, "Website đã kích hoạt");
    }

    // Impersonate (Login vào website)
    @PostMapping("/{id}/impersonate")
    @RequirePermission(PLATFORM_TENANTS_IMPERSONATE)
    public ApiResponse<ImpersonateResponse> impersonate(@PathVariable UUID id) {
        // Tạo một-time token để login vào admin panel của tenant
        String token = statusService.generateImpersonateToken(id);
        String adminUrl = tenantRepository.findAdminUrl(id);

        auditLogService.log("tenant.impersonated", id);

        return ApiResponse.ok(ImpersonateResponse.builder()
            .token(token)
            .adminUrl(adminUrl + "?impersonate=" + token)
            .expiresIn(300) // 5 phút
            .build()
        );
    }

    // Lịch sử đổi template
    @GetMapping("/{id}/template/history")
    @RequirePermission(PLATFORM_TENANTS_VIEW)
    public ApiResponse<List<TemplateHistoryDTO>> templateHistory(
            @PathVariable UUID id) {
        return ApiResponse.ok(
            templateSwitchService.getHistory(id)
        );
    }
}

// TenantAdminController.java (Website Owner & Editor)
@RestController
@RequestMapping("/admin/v1")
@RequiredArgsConstructor
public class TenantAdminController {

    private final ArticleService articleService;
    private final PredictionService predictionService;
    private final AnalyticsService analyticsService;
    private final TenantSettingsService settingsService;
    private final TenantUserService userService;

    // ── Dashboard ──────────────────────────────────────────
    @GetMapping("/dashboard")
    @RequirePermission(ANALYTICS_VIEW)
    public ApiResponse<DashboardDTO> dashboard() {
        UUID tenantId = TenantContextHolder.getTenantId();
        return ApiResponse.ok(analyticsService.getDashboard(tenantId));
    }

    // ── Articles ────────────────────────────────────────────
    @GetMapping("/articles")
    @RequirePermission(CONTENT_ARTICLES_VIEW)
    public ApiResponse<PageResponse<ArticleDTO>> listArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        UUID tenantId = TenantContextHolder.getTenantId();
        return ApiResponse.ok(articleService.list(tenantId, status, page, size));
    }

    @PostMapping("/articles")
    @RequirePermission(CONTENT_ARTICLES_CREATE)
    public ApiResponse<ArticleDTO> createArticle(
            @Valid @RequestBody CreateArticleRequest request) {
        UUID tenantId = TenantContextHolder.getTenantId();
        return ApiResponse.ok(
            articleService.create(tenantId, request),
            "Tạo bài viết thành công"
        );
    }

    @PutMapping("/articles/{id}")
    @RequirePermission(CONTENT_ARTICLES_EDIT)
    public ApiResponse<ArticleDTO> updateArticle(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateArticleRequest request) {
        UUID tenantId = TenantContextHolder.getTenantId();
        return ApiResponse.ok(articleService.update(tenantId, id, request));
    }

    @DeleteMapping("/articles/{id}")
    @RequirePermission(CONTENT_ARTICLES_DELETE)
    public ApiResponse<Void> deleteArticle(@PathVariable UUID id) {
        UUID tenantId = TenantContextHolder.getTenantId();
        articleService.delete(tenantId, id);
        return ApiResponse.ok(null, "Đã xóa bài viết");
    }

    @PostMapping("/articles/{id}/publish")
    @RequirePermission(CONTENT_ARTICLES_PUBLISH)
    public ApiResponse<ArticleDTO> publishArticle(@PathVariable UUID id) {
        UUID tenantId = TenantContextHolder.getTenantId();
        return ApiResponse.ok(
            articleService.publish(tenantId, id),
            "Đã xuất bản bài viết"
        );
    }

    // ── Lottery Predictions ─────────────────────────────────
    @GetMapping("/lottery/predictions")
    @RequirePermission(LOTTERY_PREDICTIONS_VIEW)
    public ApiResponse<PageResponse<PredictionDTO>> listPredictions(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String provinceCode,
            @RequestParam(defaultValue = "1") int page) {
        UUID tenantId = TenantContextHolder.getTenantId();
        return ApiResponse.ok(predictionService.list(tenantId, date, provinceCode, page));
    }

    @PostMapping("/lottery/predictions")
    @RequirePermission(LOTTERY_PREDICTIONS_CREATE)
    public ApiResponse<PredictionDTO> createPrediction(
            @Valid @RequestBody CreatePredictionRequest request) {
        UUID tenantId = TenantContextHolder.getTenantId();
        return ApiResponse.ok(
            predictionService.create(tenantId, request),
            "Tạo nhận định thành công"
        );
    }

    // ── Settings (chỉ Owner) ────────────────────────────────
    @GetMapping("/settings/general")
    @RequirePermission(SETTINGS_GENERAL_EDIT)
    public ApiResponse<TenantSettingsDTO> getSettings() {
        UUID tenantId = TenantContextHolder.getTenantId();
        return ApiResponse.ok(settingsService.getSettings(tenantId));
    }

    @PutMapping("/settings/general")
    @RequirePermission(SETTINGS_GENERAL_EDIT)
    public ApiResponse<TenantSettingsDTO> updateSettings(
            @Valid @RequestBody UpdateSettingsRequest request) {
        UUID tenantId = TenantContextHolder.getTenantId();
        return ApiResponse.ok(
            settingsService.update(tenantId, request),
            "Cập nhật cài đặt thành công"
        );
    }

    // Owner đổi template cho website của mình
    @PutMapping("/settings/template")
    @RequirePermission(SETTINGS_TEMPLATE_SWITCH)
    public ApiResponse<Void> switchTemplate(
            @Valid @RequestBody SwitchTemplateRequest request) {
        UUID tenantId = TenantContextHolder.getTenantId();
        templateSwitchService.switchTemplate(tenantId, request.getTemplateId());
        return ApiResponse.ok(null, "Đổi giao diện thành công");
    }

    // ── User Management (chỉ Owner) ─────────────────────────
    @GetMapping("/users")
    @RequirePermission(SETTINGS_USERS_MANAGE)
    public ApiResponse<List<TenantUserDTO>> listUsers() {
        UUID tenantId = TenantContextHolder.getTenantId();
        return ApiResponse.ok(userService.listByTenant(tenantId));
    }

    @PostMapping("/users")
    @RequirePermission(SETTINGS_USERS_MANAGE)
    public ApiResponse<TenantUserDTO> createUser(
            @Valid @RequestBody CreateTenantUserRequest request) {

        // Owner chỉ được tạo editor hoặc viewer, không được tạo owner khác
        UserPrincipal actor = SecurityContextHolder.getUser();
        if (!actor.isSuperAdmin() &&
            !actor.isAdmin() &&
            "website_owner".equals(request.getRoleCode())) {
            throw new AccessDeniedException("Không thể tạo thêm owner");
        }

        UUID tenantId = TenantContextHolder.getTenantId();
        return ApiResponse.ok(
            userService.create(tenantId, request),
            "Tạo tài khoản thành công"
        );
    }

    @DeleteMapping("/users/{userId}")
    @RequirePermission(SETTINGS_USERS_MANAGE)
    public ApiResponse<Void> deleteUser(@PathVariable UUID userId) {
        UUID tenantId = TenantContextHolder.getTenantId();

        // Không được tự xóa chính mình
        UserPrincipal actor = SecurityContextHolder.getUser();
        if (actor.getUserId().equals(userId)) {
            throw new BusinessException("Không thể xóa tài khoản đang đăng nhập");
        }

        userService.delete(tenantId, userId);
        return ApiResponse.ok(null, "Đã xóa tài khoản");
    }
}
```
# PHẦN VI: FRONTEND - TEMPLATE SWITCHING
## 6. NEXT.JS TEMPLATE SYSTEM
### 6.1 Dynamic Template Loader
```typescript

// lib/template-loader.ts
import { TenantConfig } from '@/types/tenant'

// Map template code → Next.js layout component
const TEMPLATE_MAP: Record<string, () => Promise<{ default: React.ComponentType<any> }>> = {
  // Lottery templates
  'lottery-classic': () => import('@/templates/lottery/classic/Layout'),
  'lottery-modern':  () => import('@/templates/lottery/modern/Layout'),
  'lottery-minimal': () => import('@/templates/lottery/minimal/Layout'),

  // BDS templates (future)
  'bds-professional': () => import('@/templates/realestate/professional/Layout'),
  'bds-luxury':       () => import('@/templates/realestate/luxury/Layout'),

  // Spa templates (future)
  'spa-elegant': () => import('@/templates/spa/elegant/Layout'),
  'spa-minimal': () => import('@/templates/spa/minimal/Layout'),
}

export async function loadTemplate(templateCode: string) {
  const loader = TEMPLATE_MAP[templateCode]
  if (!loader) {
    console.warn(`Template '${templateCode}' not found, using default`)
    return import('@/templates/lottery/classic/Layout')
  }
  return loader()
}

// Template config merger
export function resolveTemplateConfig(tenant: TenantConfig) {
  const { template, colorOverrides, featureOverrides } = tenant

  // Merge colors: template default + tenant overrides
  const effectiveColors = {
    ...template.defaultColors,
    ...(colorOverrides || {}),
  }

  // Merge features từ layout_config + feature_overrides
  const templateFeatures = template.layoutConfig?.features_visible || {}
  const effectiveFeatures = {
    ...templateFeatures,
    ...(featureOverrides || {}),
  }

  return {
    templateCode:     template.code,
    layoutType:       template.layoutConfig?.layout_type || 'sidebar-right',
    headerStyle:      template.layoutConfig?.header_style || 'sticky',
    widgetOrder:      template.layoutConfig?.widget_order || [],
    sidebarWidgets:   template.layoutConfig?.sidebar_widgets || [],
    homepageSections: template.layoutConfig?.homepage_sections || [],
    effectiveColors,
    effectiveFeatures,
  }
}

// ============================================================
// app/(public)/layout.tsx - Dynamic template loading
// ============================================================
import { headers } from 'next/headers'
import { notFound } from 'next/navigation'
import { getTenantConfig } from '@/lib/tenant'
import { loadTemplate, resolveTemplateConfig } from '@/lib/template-loader'

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

  // Check hosting status
  if (tenant.hostingStatus === 'suspended') {
    return <SuspendedPage tenant={tenant} />
  }

  // Load template dynamically
  const templateModule = await loadTemplate(tenant.template.code)
  const TemplateLayout = templateModule.default

  // Resolve effective config
  const templateConfig = resolveTemplateConfig(tenant)

  return (
    <html lang="vi">
      <head>
        {/* Dynamic CSS variables từ effective colors */}
        <style>{`
          :root {
            --color-primary:    ${templateConfig.effectiveColors.primary};
            --color-secondary:  ${templateConfig.effectiveColors.secondary};
            --color-accent:     ${templateConfig.effectiveColors.accent};
            --color-background: ${templateConfig.effectiveColors.background || '#ffffff'};
            --color-text:       ${templateConfig.effectiveColors.text || '#1a202c'};
          }
        `}</style>

        {/* Custom CSS của tenant */}
        {tenant.settings.customCss && (
          <style>{tenant.settings.customCss}</style>
        )}

        {/* Header scripts */}
        {tenant.settings.headerScripts && (
          <div dangerouslySetInnerHTML={{
            __html: tenant.settings.headerScripts
          }} />
        )}
      </head>

      <body>
        {/* Template layout wrap toàn bộ content */}
        <TemplateLayout
          tenant={tenant}
          config={templateConfig}
        >
          {children}
        </TemplateLayout>

        {/* Footer scripts */}
        {tenant.settings.footerScripts && (
          <div dangerouslySetInnerHTML={{
            __html: tenant.settings.footerScripts
          }} />
        )}
      </body>
    </html>
  )
}
```
### 6.2 Template Implementations
```typescript

// templates/lottery/classic/Layout.tsx
import { TenantConfig, TemplateConfig } from '@/types'
import { ClassicHeader } from './components/ClassicHeader'
import { ClassicSidebar } from './components/ClassicSidebar'
import { ClassicFooter } from './components/ClassicFooter'

interface Props {
  tenant: TenantConfig
  config: TemplateConfig
  children: React.ReactNode
}

export default function LotteryClassicLayout({ tenant, config, children }: Props) {
  return (
    <div className="min-h-screen bg-gray-50">
      <ClassicHeader tenant={tenant} config={config} />

      <div className="container mx-auto px-4 py-4">
        {config.layoutType === 'sidebar-right' ? (
          // Layout: Content trái, Sidebar phải
          <div className="flex gap-6">
            <main className="flex-1 min-w-0">{children}</main>
            <aside className="w-80 flex-shrink-0">
              <ClassicSidebar tenant={tenant} config={config} />
            </aside>
          </div>
        ) : config.layoutType === 'sidebar-left' ? (
          // Layout: Sidebar trái, Content phải
          <div className="flex gap-6">
            <aside className="w-72 flex-shrink-0">
              <ClassicSidebar tenant={tenant} config={config} />
            </aside>
            <main className="flex-1 min-w-0">{children}</main>
          </div>
        ) : (
          // Layout: No sidebar / Full width
          <main className="w-full">{children}</main>
        )}
      </div>

      <ClassicFooter tenant={tenant} />
    </div>
  )
}

// templates/lottery/modern/Layout.tsx
export default function LotteryModernLayout({ tenant, config, children }: Props) {
  return (
    <div className="min-h-screen bg-white">
      {/* Modern: Mega menu, hero section */}
      <ModernHeader tenant={tenant} config={config} />

      {/* Full-width sections, no sidebar */}
      <main className="w-full">
        {children}
      </main>

      <ModernFooter tenant={tenant} />
    </div>
  )
}

// templates/lottery/minimal/Layout.tsx
export default function LotteryMinimalLayout({ tenant, config, children }: Props) {
  return (
    <div className="min-h-screen bg-white font-light">
      {/* Minimal: Simple nav, clean design */}
      <MinimalHeader tenant={tenant} />
      <main className="max-w-4xl mx-auto px-4 py-8">
        {children}
      </main>
      <MinimalFooter tenant={tenant} />
    </div>
  )
}
```
### 6.3 Feature Gate Component
```typescript

// components/shared/FeatureGate.tsx
// Ẩn/hiện component dựa trên template config
'use client'

import { useTenant } from '@/hooks/useTenant'

interface FeatureGateProps {
  feature: string
  // 'show_predictions' | 'show_live_result' | 'show_statistics'
  // 'show_ticket_checker' | 'show_schedule' | 'show_print_button'
  fallback?: React.ReactNode
  children: React.ReactNode
}

export function FeatureGate({ feature, fallback = null, children }: FeatureGateProps) {
  const { templateConfig } = useTenant()
  const isEnabled = templateConfig?.effectiveFeatures?.[feature] ?? true

  if (!isEnabled) return <>{fallback}</>
  return <>{children}</>
}

// Usage example trong KQXS page:
// <FeatureGate feature="show_predictions">
//   <PredictionSection />
// </FeatureGate>
//
// <FeatureGate feature="show_live_result">
//   <LiveCountdown />
// </FeatureGate>

// ============================================================
// hooks/useTenant.ts
// ============================================================
'use client'

import { createContext, useContext } from 'react'
import type { TenantConfig, TemplateConfig } from '@/types'

interface TenantContextValue {
  tenant:         TenantConfig
  templateConfig: TemplateConfig
}

const TenantContext = createContext<TenantContextValue | null>(null)

export function TenantProvider({
  tenant,
  templateConfig,
  children
}: TenantContextValue & { children: React.ReactNode }) {
  return (
    <TenantContext.Provider value={{ tenant, templateConfig }}>
      {children}
    </TenantContext.Provider>
  )
}

export function useTenant(): TenantContextValue {
  const ctx = useContext(TenantContext)
  if (!ctx) throw new Error('useTenant must be used within TenantProvider')
  return ctx
}

// ============================================================
// components/shared/SuspendedPage.tsx
// ============================================================
export function SuspendedPage({ tenant }: { tenant: TenantConfig }) {
  return (
    <html lang="vi">
      <body>
        <div className="min-h-screen flex items-center justify-center bg-gray-100">
          <div className="text-center p-8 bg-white rounded-xl shadow max-w-md">
            <div className="text-6xl mb-4">🔒</div>
            <h1 className="text-2xl font-bold text-gray-800 mb-2">
              Website Tạm Ngưng
            </h1>
            <p className="text-gray-500 mb-6">
              Website <strong>{tenant.name}</strong> hiện đang tạm ngưng hoạt động.
              Vui lòng liên hệ quản trị viên để được hỗ trợ.
            </p>
            {tenant.settings.phone && (
              <a
                href={`tel:${tenant.settings.phone}`}
                className="btn-primary"
              >
                📞 {tenant.settings.phone}
              </a>
            )}
          </div>
        </div>
      </body>
    </html>
  )
}
```
# PHẦN VII: ADMIN PANEL FRONTEND
## 7. SUPER ADMIN PANEL
### 7.1 Dashboard & Client Management
```typescript

// app/platform-admin/dashboard/page.tsx
import { platformApi } from '@/lib/platform-api'

export default async function PlatformDashboard() {
  const [stats, expiring, recentSetups] = await Promise.all([
    platformApi.getDashboardStats(),
    platformApi.getExpiringWebsites({ days: 7 }),
    platformApi.getRecentSetups({ limit: 5 }),
  ])

  return (
    <div className="p-6 space-y-6">
      <h1 className="text-2xl font-bold">Dashboard</h1>

      {/* Stats overview */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <StatCard
          title="Tổng websites"
          value={stats.totalTenants}
          icon="🌐"
          color="blue"
        />
        <StatCard
          title="Đang hoạt động"
          value={stats.activeTenants}
          icon="✅"
          color="green"
        />
        <StatCard
          title="Tổng khách hàng"
          value={stats.totalClients}
          icon="👥"
          color="purple"
        />
        <StatCard
          title="Doanh thu tháng này"
          value={formatVND(stats.monthlyRevenue)}
          icon="💰"
          color="yellow"
        />
      </div>

      {/* Websites sắp hết hạn */}
      {expiring.length > 0 && (
        <div className="bg-orange-50 border border-orange-200 rounded-xl p-4">
          <h2 className="font-semibold text-orange-800 mb-3">
            ⚠️ {expiring.length} website sắp hết hạn hosting
          </h2>
          <div className="space-y-2">
            {expiring.map(site => (
              <ExpiringWebsiteRow key={site.tenantId} site={site} />
            ))}
          </div>
        </div>
      )}

      {/* Quick actions */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
        <QuickActionCard
          href="/platform-admin/tenants/new"
          icon="➕"
          label="Tạo website mới"
          color="blue"
        />
        <QuickActionCard
          href="/platform-admin/clients/new"
          icon="👤"
          label="Thêm khách hàng"
          color="green"
        />
        <QuickActionCard
          href="/platform-admin/payments/record"
          icon="💳"
          label="Ghi nhận thanh toán"
          color="purple"
        />
        <QuickActionCard
          href="/platform-admin/setup-requests"
          icon="📋"
          label="Yêu cầu setup"
          color="orange"
        />
      </div>

      {/* Recent setups */}
      <RecentSetupsTable data={recentSetups} />
    </div>
  )
}

// ============================================================
// app/platform-admin/tenants/[id]/page.tsx
// ============================================================
export default async function TenantDetailPage({
  params
}: { params: { id: string } }) {
  const tenant = await platformApi.getTenantDetail(params.id)
  const templates = await platformApi.getTemplatesByVertical(tenant.verticalCode)
  const paymentHistory = await platformApi.getPaymentHistory(params.id)

  return (
    <div className="p-6 space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold">{tenant.name}</h1>
          <p className="text-gray-500">{tenant.primaryDomain}</p>
        </div>
        <div className="flex gap-2">
          <a
            href={`https://${tenant.primaryDomain}`}
            target="_blank"
            className="btn-outline"
          >
            🌐 Xem website
          </a>
          <ImpersonateButton tenantId={tenant.id} />
          {tenant.status === 'active'
            ? <SuspendButton tenantId={tenant.id} />
            : <ActivateButton tenantId={tenant.id} />
          }
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left: Website info */}
        <div className="lg:col-span-2 space-y-4">

          {/* Template switcher */}
          <div className="bg-white rounded-xl border p-4">
            <h3 className="font-semibold mb-4">🎨 Giao diện Template</h3>
            <TemplateSwitcher
              currentTemplateId={tenant.templateId}
              templates={templates}
              tenantId={tenant.id}
              onSwitch={async (templateId) => {
                'use server'
                await platformApi.switchTemplate(tenant.id, templateId)
              }}
            />
          </div>

          {/* Color overrides */}
          <div className="bg-white rounded-xl border p-4">
            <h3 className="font-semibold mb-4">🎨 Màu sắc tùy chỉnh</h3>
            <ColorOverrideEditor
              currentColors={tenant.colorOverrides}
              defaultColors={tenant.template.defaultColors}
              tenantId={tenant.id}
            />
          </div>

          {/* Feature overrides */}
          <div className="bg-white rounded-xl border p-4">
            <h3 className="font-semibold mb-4">⚙️ Tính năng bật/tắt</h3>
            <FeatureToggleEditor
              features={tenant.effectiveFeatures}
              tenantId={tenant.id}
            />
          </div>

          {/* Template history */}
          <div className="bg-white rounded-xl border p-4">
            <h3 className="font-semibold mb-4">📜 Lịch sử đổi template</h3>
            <TemplateHistoryTable history={tenant.templateHistory} />
          </div>
        </div>

        {/* Right: Client + Hosting + Payments */}
        <div className="space-y-4">
          {/* Client info */}
          <div className="bg-white rounded-xl border p-4">
            <h3 className="font-semibold mb-3">👤 Thông tin khách hàng</h3>
            <ClientInfoCard client={tenant.client} />
          </div>

          {/* Hosting status */}
          <div className="bg-white rounded-xl border p-4">
            <h3 className="font-semibold mb-3">🖥️ Hosting</h3>
            <HostingStatusCard
              hosting={tenant.hostingStatus}
              tenantId={tenant.id}
            />
            <RecordPaymentButton
              tenantId={tenant.id}
              clientId={tenant.client.id}
            />
          </div>

          {/* Payment history */}
          <div className="bg-white rounded-xl border p-4">
            <h3 className="font-semibold mb-3">💰 Lịch sử thanh toán</h3>
            <PaymentHistoryList payments={paymentHistory} />
          </div>
        </div>
      </div>
    </div>
  )
}
```
### 7.2 Template Switcher Component
```typescript

// components/platform-admin/TemplateSwitcher.tsx
'use client'

import { useState } from 'react'
import Image from 'next/image'
import { platformApi } from '@/lib/platform-api'
import type { Template } from '@/types'

interface Props {
  currentTemplateId: string
  templates:         Template[]
  tenantId:          string
  onSwitch:          (templateId: string) => Promise<void>
}

export function TemplateSwitcher({
  currentTemplateId,
  templates,
  tenantId,
}: Props) {
  const [selected, setSelected]       = useState(currentTemplateId)
  const [previewing, setPreviewing]   = useState<string | null>(null)
  const [switching, setSwitching]     = useState(false)
  const [showConfirm, setShowConfirm] = useState(false)

  const handleSwitch = async () => {
    if (selected === currentTemplateId) return
    setSwitching(true)
    try {
      await platformApi.switchTemplate(tenantId, selected)
      setShowConfirm(false)
      // Reload để thấy thay đổi
      window.location.reload()
    } catch (error) {
      alert('Lỗi khi đổi template: ' + error)
    } finally {
      setSwitching(false)
    }
  }

  return (
    <div className="space-y-4">
      {/* Template grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
        {templates.map(template => (
          <div
            key={template.id}
            onClick={() => setSelected(template.id)}
            className={`
              relative rounded-xl border-2 cursor-pointer
              transition-all duration-200 overflow-hidden
              ${selected === template.id
                ? 'border-blue-500 shadow-lg shadow-blue-100'
                : 'border-gray-200 hover:border-gray-300'
              }
              ${currentTemplateId === template.id
                ? 'ring-2 ring-green-400 ring-offset-1'
                : ''
              }
            `}
          >
            {/* Current badge */}
            {currentTemplateId === template.id && (
              <div className="absolute top-2 left-2 z-10
                              bg-green-500 text-white text-xs
                              px-2 py-0.5 rounded-full">
                Đang dùng
              </div>
            )}

            {/* Selected badge */}
            {selected === template.id &&
             selected !== currentTemplateId && (
              <div className="absolute top-2 right-2 z-10
                              bg-blue-500 text-white text-xs
                              px-2 py-0.5 rounded-full">
                Đã chọn
              </div>
            )}

            {/* Preview image */}
            <div className="aspect-video bg-gray-100 relative">
              {template.previewImage ? (
                <Image
                  src={template.previewImage}
                  alt={template.name}
                  fill
                  className="object-cover"
                />
              ) : (
                <div className="absolute inset-0 flex items-center
                                justify-center text-gray-400">
                  <span className="text-4xl">🖼️</span>
                </div>
              )}
            </div>

            {/* Template info */}
            <div className="p-3">
              <h4 className="font-medium text-sm">{template.name}</h4>
              <p className="text-xs text-gray-500 mt-0.5">
                {template.description}
              </p>

              {/* Preview button */}
              {template.previewUrl && (
                <a
                  href={template.previewUrl}
                  target="_blank"
                  onClick={e => e.stopPropagation()}
                  className="text-xs text-blue-600 hover:underline mt-1 block"
                >
                  👁️ Xem demo
                </a>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Switch button */}
      {selected !== currentTemplateId && (
        <div className="flex items-center gap-3 p-3
                        bg-blue-50 rounded-lg border border-blue-200">
          <span className="text-sm text-blue-700 flex-1">
            Đổi sang: <strong>
              {templates.find(t => t.id === selected)?.name}
            </strong>
          </span>
          <button
            onClick={() => setShowConfirm(true)}
            className="btn-primary text-sm"
            disabled={switching}
          >
            {switching ? 'Đang đổi...' : 'Xác nhận đổi'}
          </button>
        </div>
      )}

      {/* Confirm dialog */}
      {showConfirm && (
        <ConfirmDialog
          title="Xác nhận đổi giao diện"
          message={`Website sẽ đổi sang template "${templates.find(t => t.id === selected)?.name}". 
                    Nội dung (bài viết, KQXS) sẽ không bị ảnh hưởng.`}
          onConfirm={handleSwitch}
          onCancel={() => setShowConfirm(false)}
          loading={switching}
        />
      )}
    </div>
  )
}

// ============================================================
// components/platform-admin/ColorOverrideEditor.tsx
// ============================================================
'use client'

import { useState } from 'react'
import { platformApi } from '@/lib/platform-api'

interface ColorConfig {
  primary:    string
  secondary:  string
  accent:     string
  background: string
  text:       string
}

interface Props {
  currentColors: Partial<ColorConfig> | null
  defaultColors: ColorConfig
  tenantId:      string
}

export function ColorOverrideEditor({
  currentColors,
  defaultColors,
  tenantId,
}: Props) {
  const [colors, setColors] = useState<Partial<ColorConfig>>(
    currentColors || {}
  )
  const [saving, setSaving] = useState(false)

  const colorFields = [
    { key: 'primary',    label: 'Màu chính',    desc: 'Header, buttons' },
    { key: 'secondary',  label: 'Màu phụ',      desc: 'Footer, sidebar' },
    { key: 'accent',     label: 'Màu nhấn',     desc: 'Highlight, badges' },
    { key: 'background', label: 'Nền trang',     desc: 'Page background' },
    { key: 'text',       label: 'Màu chữ',      desc: 'Body text' },
  ] as const

  const handleColorChange = (key: keyof ColorConfig, value: string) => {
    setColors(prev => ({ ...prev, [key]: value }))
  }

  const handleReset = (key: keyof ColorConfig) => {
    setColors(prev => {
      const next = { ...prev }
      delete next[key]
      return next
    })
  }

  const handleSave = async () => {
    setSaving(true)
    try {
      await platformApi.updateColorOverrides(tenantId, colors)
      alert('✅ Đã lưu màu sắc')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="space-y-3">
      {colorFields.map(({ key, label, desc }) => {
        const effectiveColor = colors[key] || defaultColors[key]
        const isOverridden = !!colors[key]

        return (
          <div key={key}
               className="flex items-center gap-3">
            {/* Color picker */}
            <input
              type="color"
              value={effectiveColor}
              onChange={e => handleColorChange(key, e.target.value)}
              className="w-10 h-10 rounded border cursor-pointer"
            />

            {/* Label */}
            <div className="flex-1">
              <div className="flex items-center gap-2">
                <span className="text-sm font-medium">{label}</span>
                {isOverridden && (
                  <span className="text-xs bg-blue-100 text-blue-600
                                   px-1.5 py-0.5 rounded">
                    Tùy chỉnh
                  </span>
                )}
              </div>
              <span className="text-xs text-gray-400">{desc}</span>
            </div>

            {/* Current value */}
            <span className="text-xs text-gray-500 font-mono">
              {effectiveColor}
            </span>

            {/* Reset button */}
            {isOverridden && (
              <button
                onClick={() => handleReset(key)}
                className="text-xs text-red-500 hover:text-red-700"
                title="Reset về mặc định"
              >
                ↩️ Reset
              </button>
            )}
          </div>
        )
      })}

      <button
        onClick={handleSave}
        disabled={saving}
        className="btn-primary w-full mt-4"
      >
        {saving ? 'Đang lưu...' : '💾 Lưu màu sắc'}
      </button>
    </div>
  )
}

// ============================================================
// components/platform-admin/FeatureToggleEditor.tsx
// ============================================================
'use client'

import { useState } from 'react'
import { platformApi } from '@/lib/platform-api'

interface Features {
  show_predictions:    boolean
  show_statistics:     boolean
  show_live_result:    boolean
  show_ticket_checker: boolean
  show_schedule:       boolean
  show_print_button:   boolean
  show_share_buttons:  boolean
}

interface Props {
  features: Features
  tenantId: string
}

export function FeatureToggleEditor({ features, tenantId }: Props) {
  const [values, setValues]   = useState<Features>(features)
  const [saving, setSaving]   = useState(false)

  const featureList = [
    { key: 'show_live_result',    label: 'Kết quả trực tiếp',  desc: 'Hiển thị live countdown và real-time' },
    { key: 'show_statistics',     label: 'Thống kê tần suất',  desc: 'Bảng 10x10, lô gan, đầu đuôi' },
    { key: 'show_predictions',    label: 'Soi cầu nhận định',  desc: 'Trang soi cầu và dự đoán' },
    { key: 'show_ticket_checker', label: 'Tra cứu vé số',      desc: 'Kiểm tra vé trúng thưởng' },
    { key: 'show_schedule',       label: 'Lịch xổ số',         desc: 'Lịch quay thưởng theo tuần' },
    { key: 'show_print_button',   label: 'Nút in kết quả',     desc: 'In KQXS dạng bảng' },
    { key: 'show_share_buttons',  label: 'Nút chia sẻ',        desc: 'Chia sẻ lên Facebook, Zalo' },
  ] as const

  const handleSave = async () => {
    setSaving(true)
    try {
      await platformApi.updateFeatureOverrides(tenantId, values)
      alert('✅ Đã cập nhật tính năng')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="space-y-2">
      {featureList.map(({ key, label, desc }) => (
        <div key={key}
             className="flex items-center justify-between
                        py-2 border-b border-gray-100 last:border-0">
          <div>
            <p className="text-sm font-medium">{label}</p>
            <p className="text-xs text-gray-400">{desc}</p>
          </div>
          <label className="relative inline-flex items-center cursor-pointer">
            <input
              type="checkbox"
              checked={values[key]}
              onChange={e => setValues(prev => ({
                ...prev, [key]: e.target.checked
              }))}
              className="sr-only peer"
            />
            <div className="w-11 h-6 bg-gray-200 rounded-full peer
                            peer-checked:bg-blue-600
                            peer-checked:after:translate-x-full
                            after:content-[''] after:absolute after:top-0.5
                            after:left-[2px] after:bg-white after:rounded-full
                            after:h-5 after:w-5 after:transition-all" />
          </label>
        </div>
      ))}

      <button
        onClick={handleSave}
        disabled={saving}
        className="btn-primary w-full mt-4"
      >
        {saving ? 'Đang lưu...' : '💾 Lưu cài đặt'}
      </button>
    </div>
  )
}
```
# PHẦN VIII: BACKGROUND JOBS
## 8. HOSTING EXPIRY CHECKER
```java

// HostingExpiryJob.java
@Component
@RequiredArgsConstructor
@Slf4j
public class HostingExpiryJob {

    private final HostingStatusRepository hostingRepo;
    private final TenantRepository tenantRepository;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final SystemConfigService configService;

    // Chạy mỗi ngày lúc 9:00 sáng
    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Ho_Chi_Minh")
    public void checkExpiringHosting() {
        log.info("🔍 Checking hosting expiry...");

        int reminderDays = configService.getInt("hosting.reminder_days_before", 7);
        int gracePeriod  = configService.getInt("hosting.grace_period_days", 5);
        LocalDate today  = LocalDate.now();

        // 1. Gửi reminder cho sắp hết hạn
        sendReminders(today, reminderDays);

        // 2. Suspend các website đã quá grace period
        suspendOverdueWebsites(today, gracePeriod);

        // 3. Notify admin qua Telegram về tổng quan
        sendDailySummary(today, reminderDays);
    }

    private void sendReminders(LocalDate today, int reminderDays) {
        LocalDate reminderDate = today.plusDays(reminderDays);

        List<HostingStatus> expiring = hostingRepo
            .findByStatusAndPaidUntilBetweenAndReminderSentAtIsNull(
                HostingStatusEnum.ACTIVE,
                today,
                reminderDate
            );

        expiring.forEach(hs -> {
            long daysLeft = today.until(hs.getPaidUntil(), ChronoUnit.DAYS);

            // Notify admin
            notificationService.notifyAdmins(
                String.format(
                    "⚠️ Website sắp hết hạn!\n" +
                    "Website: %s\n" +
                    "Khách hàng: %s\n" +
                    "SĐT: %s\n" +
                    "Hết hạn: %s (còn %d ngày)\n" +
                    "Phí: %s đ/tháng",
                    hs.getTenant().getName(),
                    hs.getClient().getFullName(),
                    hs.getClient().getPhone(),
                    hs.getPaidUntil().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    daysLeft,
                    formatMoney(hs.getMonthlyFee())
                )
            );

            // Update reminder sent
            hs.setReminderSentAt(Instant.now());
            hs.setReminderCount(hs.getReminderCount() + 1);
            hostingRepo.save(hs);

            log.info("Reminder sent for tenant={}, daysLeft={}",
                hs.getTenant().getSlug(), daysLeft);
        });
    }

    private void suspendOverdueWebsites(LocalDate today, int gracePeriod) {
        // Website quá hạn grace period
        LocalDate suspendDate = today.minusDays(gracePeriod);

        List<HostingStatus> overdue = hostingRepo
            .findByStatusAndPaidUntilBefore(
                HostingStatusEnum.ACTIVE,
                suspendDate
            );

        overdue.forEach(hs -> {
            // Suspend tenant
            Tenant tenant = hs.getTenant();
            tenant.setStatus(TenantStatus.SUSPENDED);
            tenantRepository.save(tenant);

            // Update hosting status
            hs.setStatus(HostingStatusEnum.SUSPENDED);
            hs.setSuspendedAt(Instant.now());
            hs.setSuspensionReason(
                "Auto-suspended: quá hạn " + gracePeriod + " ngày"
            );
            hostingRepo.save(hs);

            // Notify admin
            notificationService.notifyAdmins(
                String.format(
                    "🚫 Auto-suspend website!\n" +
                    "Website: %s\n" +
                    "Khách: %s - %s\n" +
                    "Hết hạn từ: %s",
                    tenant.getName(),
                    hs.getClient().getFullName(),
                    hs.getClient().getPhone(),
                    hs.getPaidUntil().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
            );

            // Audit log
            auditLogService.log(AuditLog.builder()
                .actorType("system")
                .actorName("Auto-Suspend Job")
                .tenantId(tenant.getId())
                .action("tenant.auto_suspended")
                .resourceType("tenant")
                .resourceId(tenant.getId())
                .resourceName(tenant.getName())
                .changes(Map.of(
                    "reason", "Quá hạn hosting " + gracePeriod + " ngày",
                    "paid_until", hs.getPaidUntil().toString()
                ))
                .build()
            );

            log.warn("🚫 Auto-suspended: tenant={}, paidUntil={}",
                tenant.getSlug(), hs.getPaidUntil());
        });
    }

    private void sendDailySummary(LocalDate today, int reminderDays) {
        long activeCount    = hostingRepo.countByStatus(HostingStatusEnum.ACTIVE);
        long expiringCount  = hostingRepo.countExpiringWithin(today, reminderDays);
        long suspendedCount = hostingRepo.countByStatus(HostingStatusEnum.SUSPENDED);

        if (expiringCount > 0 || suspendedCount > 0) {
            notificationService.notifyAdmins(
                String.format(
                    "📊 Báo cáo hosting hàng ngày\n" +
                    "Ngày: %s\n\n" +
                    "✅ Đang hoạt động: %d\n" +
                    "⚠️ Sắp hết hạn (%d ngày): %d\n" +
                    "🚫 Đã suspend: %d\n\n" +
                    "👉 Xem chi tiết: %s/platform-admin/hosting",
                    today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    activeCount,
                    reminderDays,
                    expiringCount,
                    suspendedCount,
                    configService.get("platform.admin_url")
                )
            );
        }
    }

    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "N/A";
        return String.format("%,.0f", amount);
    }
}

// ============================================================

// TelegramNotificationService.java
@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramNotificationService implements NotificationService {

    private final SystemConfigService configService;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    private static final String TELEGRAM_API = "https://api.telegram.org/bot%s/sendMessage";

    @Override
    public void notifyAdmins(String message) {
        String botToken = configService.get("platform.telegram_bot_token");
        String chatId   = configService.get("platform.telegram_chat_id");

        if (botToken == null || chatId == null) {
            log.warn("Telegram not configured, skipping notification");
            return;
        }

        sendTelegramMessage(botToken, chatId, message);
    }

    @Override
    public void notifyTenantOwner(UUID tenantId, Notification notification) {
        // Lưu in-app notification
        saveInAppNotification(tenantId, notification);
        // Có thể thêm SMS sau
    }

    @Async
    public void sendTelegramMessage(String botToken, String chatId, String text) {
        try {
            Map<String, String> body = Map.of(
                "chat_id",    chatId,
                "text",       text,
                "parse_mode", "HTML"
            );

            RequestBody requestBody = RequestBody.create(
                objectMapper.writeValueAsString(body),
                MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                .url(String.format(TELEGRAM_API, botToken))
                .post(requestBody)
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("Telegram send failed: {}", response.code());
                }
            }
        } catch (Exception e) {
            log.error("Failed to send Telegram notification: {}", e.getMessage());
        }
    }

    private void saveInAppNotification(UUID tenantId, Notification notification) {
        // Save to notifications table
        // tenantUserRepository.findOwnerByTenantId(tenantId)
        //     .ifPresent(owner -> notificationRepo.save(...));
    }
}
```
# PHẦN IX: API ROUTES HOÀN CHỈNH
## 9. API ENDPOINT MAP
```yaml

# ============================================================
# PLATFORM ADMIN API (Super Admin & Admin)
# Base: /platform/v1
# Auth: JWT với role super_admin hoặc admin
# ============================================================

# --- Dashboard ---
GET  /platform/v1/dashboard/stats
     Permission: platform.tenants.view
     Response: { totalTenants, activeTenants, totalClients,
                 monthlyRevenue, expiringCount }

GET  /platform/v1/dashboard/expiring
     ?days=7
     Permission: platform.tenants.view
     Response: [{ tenantId, name, clientName, phone, paidUntil, daysLeft }]

# --- Tenants (Websites) ---
GET    /platform/v1/tenants
       ?status=active&vertical=lottery&search=phuong&page=1&size=20
       Permission: platform.tenants.view

POST   /platform/v1/tenants/setup
       Permission: platform.tenants.create
       Body: {
         clientId, verticalCode, templateCode, slug,
         businessName, ownerEmail, initialPassword,
         logoUrl, tagline, contactPhone, contactEmail,
         defaultRegion, packageName, monthlyFee,
         paidMonths, colorOverrides
       }

GET    /platform/v1/tenants/{id}
       Permission: platform.tenants.view

PUT    /platform/v1/tenants/{id}/template
       Permission: platform.tenants.edit
       Body: { templateId }

GET    /platform/v1/tenants/{id}/template/preview
       ?templateId=xxx
       Permission: platform.tenants.view

GET    /platform/v1/tenants/{id}/template/history
       Permission: platform.tenants.view

PUT    /platform/v1/tenants/{id}/colors
       Permission: platform.tenants.edit
       Body: { primary, secondary, accent, background, text }

PUT    /platform/v1/tenants/{id}/features
       Permission: platform.tenants.edit
       Body: { show_predictions, show_statistics, ... }

POST   /platform/v1/tenants/{id}/suspend
       Permission: platform.tenants.suspend
       Body: { reason }

POST   /platform/v1/tenants/{id}/activate
       Permission: platform.tenants.suspend

POST   /platform/v1/tenants/{id}/impersonate
       Permission: platform.tenants.impersonate
       Response: { token, adminUrl, expiresIn }

DELETE /platform/v1/tenants/{id}
       Permission: platform.tenants.delete

# --- Templates ---
GET    /platform/v1/templates
       ?verticalCode=lottery&isActive=true
       Permission: platform.templates.view

GET    /platform/v1/templates/{id}
       Permission: platform.templates.view

POST   /platform/v1/templates
       Permission: platform.templates.create
       Body: { verticalId, code, name, layoutConfig,
               defaultColors, previewImage }

PUT    /platform/v1/templates/{id}
       Permission: platform.templates.edit

DELETE /platform/v1/templates/{id}
       Permission: platform.templates.delete

# --- Verticals ---
GET    /platform/v1/verticals
       Permission: platform.templates.view

# --- Clients ---
GET    /platform/v1/clients
       ?status=active&province=HCM&search=nguyen&page=1
       Permission: platform.clients.view

POST   /platform/v1/clients
       Permission: platform.clients.create
       Body: { fullName, businessName, phone, email,
               address, province, referralSource }

GET    /platform/v1/clients/{id}
       Permission: platform.clients.view

GET    /platform/v1/clients/{id}/websites
       Permission: platform.clients.view
       Response: [tenants của client này]

PUT    /platform/v1/clients/{id}
       Permission: platform.clients.edit

# --- Payments ---
GET    /platform/v1/payments
       ?clientId=xxx&tenantId=xxx&month=1&year=2024&page=1
       Permission: platform.payments.view

POST   /platform/v1/payments
       Permission: platform.payments.create
       Body: {
         clientId, tenantId, paymentType,
         amount, method, paidAt,
         periodMonth, periodYear,
         note, transactionRef
       }

PUT    /platform/v1/payments/{id}/confirm
       Permission: platform.payments.create
       # Xác nhận đã nhận tiền → cập nhật hosting_status.paid_until

PUT    /platform/v1/payments/{id}
       Permission: platform.payments.edit

GET    /platform/v1/payments/summary
       ?year=2024
       Permission: platform.payments.view
       Response: monthly revenue summary

# --- Hosting Status ---
GET    /platform/v1/hosting
       ?status=active&expiringSoon=true
       Permission: platform.tenants.view

PUT    /platform/v1/hosting/{tenantId}/extend
       Permission: platform.payments.create
       Body: { months, newPaidUntil }
       # Gia hạn hosting sau khi confirm payment

# --- Setup Requests ---
GET    /platform/v1/setup-requests
       ?status=pending
       Permission: platform.tenants.view

POST   /platform/v1/setup-requests
       # Không cần auth - khách hàng gửi yêu cầu
       Body: { clientName, clientPhone, businessName,
               verticalCode, message }

PUT    /platform/v1/setup-requests/{id}/assign
       Permission: platform.tenants.create
       Body: { assignedTo }

PUT    /platform/v1/setup-requests/{id}/complete
       Permission: platform.tenants.create
       Body: { tenantId }

# --- Platform Users (Admin accounts) ---
GET    /platform/v1/admins
       Permission: platform.admins.view

POST   /platform/v1/admins
       Permission: platform.admins.create
       Body: { email, password, fullName, roleCode,
               allowedTenantIds }

PUT    /platform/v1/admins/{id}
       Permission: platform.admins.edit

DELETE /platform/v1/admins/{id}
       Permission: platform.admins.edit
       # Chỉ super_admin được xóa admin khác

# --- System ---
GET    /platform/v1/system/configs
       Permission: platform.system.config

PUT    /platform/v1/system/configs/{key}
       Permission: platform.system.config
       Body: { value }

GET    /platform/v1/system/logs
       ?level=error&page=1
       Permission: platform.system.logs

GET    /platform/v1/system/jobs
       ?status=failed&jobName=lottery.fetch
       Permission: platform.system.logs

POST   /platform/v1/system/jobs/{jobName}/trigger
       Permission: platform.system.config
       # Manual trigger job

GET    /platform/v1/system/health
       # No auth required


# ============================================================
# TENANT ADMIN API (Website Owner, Editor, Viewer)
# Base: /admin/v1
# Auth: JWT + Tenant context từ domain
# ============================================================

# --- Auth ---
POST   /admin/v1/auth/login
       Body: { email, password }
       Response: { accessToken, user, tenant }

POST   /admin/v1/auth/logout

GET    /admin/v1/auth/me
       Response: { user, tenant, permissions }

POST   /admin/v1/auth/forgot-password
       Body: { phone }  # Dùng SĐT thay vì email

POST   /admin/v1/auth/reset-password
       Body: { token, newPassword }

POST   /admin/v1/auth/change-password
       Body: { currentPassword, newPassword }

# --- Dashboard ---
GET    /admin/v1/dashboard
       Permission: analytics.view
       Response: { pageViews, topPages, recentArticles,
                   lotteryStatus, hostingInfo }

# --- Articles ---
GET    /admin/v1/articles
       ?status=published&category=tin-tuc&page=1&size=20
       Permission: content.articles.view

POST   /admin/v1/articles
       Permission: content.articles.create

GET    /admin/v1/articles/{id}
       Permission: content.articles.view

PUT    /admin/v1/articles/{id}
       Permission: content.articles.edit

DELETE /admin/v1/articles/{id}
       Permission: content.articles.delete

POST   /admin/v1/articles/{id}/publish
       Permission: content.articles.publish

POST   /admin/v1/articles/{id}/unpublish
       Permission: content.articles.publish

# --- Categories ---
GET    /admin/v1/categories
       Permission: content.categories.view

POST   /admin/v1/categories
       Permission: content.categories.manage

PUT    /admin/v1/categories/{id}
       Permission: content.categories.manage

DELETE /admin/v1/categories/{id}
       Permission: content.categories.manage

# --- Media ---
GET    /admin/v1/media
       ?folder=/&type=image&page=1
       Permission: content.media.view

POST   /admin/v1/media/upload
       Permission: content.media.upload
       Body: multipart/form-data

DELETE /admin/v1/media/{id}
       Permission: content.media.delete

# --- Pages ---
GET    /admin/v1/pages
       Permission: content.pages.view

PUT    /admin/v1/pages/{id}
       Permission: content.pages.edit

# --- Lottery Predictions ---
GET    /admin/v1/lottery/predictions
       ?date=2024-01-15&provinceCode=mb&page=1
       Permission: lottery.predictions.view

POST   /admin/v1/lottery/predictions
       Permission: lottery.predictions.create
       Body: { provinceId, drawDate, predictionType,
               numbers, confidence, methodName, analysis }

PUT    /admin/v1/lottery/predictions/{id}
       Permission: lottery.predictions.edit

DELETE /admin/v1/lottery/predictions/{id}
       Permission: lottery.predictions.delete

# --- Ads ---
GET    /admin/v1/ads/zones
       Permission: lottery.ads.view

GET    /admin/v1/ads
       ?zoneId=xxx
       Permission: lottery.ads.view

POST   /admin/v1/ads
       Permission: lottery.ads.manage
       Body: { zoneId, type, adsenseCode OR imageUrl + clickUrl }

PUT    /admin/v1/ads/{id}
       Permission: lottery.ads.manage

DELETE /admin/v1/ads/{id}
       Permission: lottery.ads.manage

# --- Analytics ---
GET    /admin/v1/analytics/overview
       ?period=7d|30d|90d
       Permission: analytics.view

GET    /admin/v1/analytics/pages
       ?period=30d&limit=20
       Permission: analytics.view

GET    /admin/v1/analytics/export
       ?period=30d&format=csv
       Permission: analytics.export

# --- Settings (Owner only) ---
GET    /admin/v1/settings/general
       Permission: settings.general.edit

PUT    /admin/v1/settings/general
       Permission: settings.general.edit

GET    /admin/v1/settings/seo
       Permission: settings.seo.edit

PUT    /admin/v1/settings/seo
       Permission: settings.seo.edit

GET    /admin/v1/settings/lottery
       Permission: settings.general.view

PUT    /admin/v1/settings/lottery
       Permission: settings.general.edit

GET    /admin/v1/settings/template
       Permission: settings.template.switch
       Response: { currentTemplate, availableTemplates }

PUT    /admin/v1/settings/template
       Permission: settings.template.switch
       Body: { templateId }

# --- Domains ---
GET    /admin/v1/domains
       Permission: settings.domain.manage

POST   /admin/v1/domains
       Permission: settings.domain.manage
       Body: { domain }

POST   /admin/v1/domains/{id}/verify
       Permission: settings.domain.manage

DELETE /admin/v1/domains/{id}
       Permission: settings.domain.manage

# --- Users (Owner only) ---
GET    /admin/v1/users
       Permission: settings.users.manage

POST   /admin/v1/users
       Permission: settings.users.manage
       Body: { email, password, fullName, phone, roleCode }
       # roleCode chỉ được là 'editor' hoặc 'viewer'

PUT    /admin/v1/users/{id}
       Permission: settings.users.manage

DELETE /admin/v1/users/{id}
       Permission: settings.users.manage

PUT    /admin/v1/users/{id}/reset-password
       Permission: settings.users.manage
       Body: { newPassword }

# --- Hosting Info (Read only) ---
GET    /admin/v1/billing/hosting
       Permission: settings.general.view
       Response: { status, paidUntil, monthlyFee,
                   nextBilling, daysRemaining }


# ============================================================
# PUBLIC API (Không cần auth)
# Base: /v1
# Context: Tenant từ domain
# ============================================================

# --- Lottery Results ---
GET  /v1/lottery/results/today
     ?region=north|central|south

GET  /v1/lottery/results/{date}
     ?region=all&provinceCode=mb

GET  /v1/lottery/results/province/{slug}
     ?page=1&size=30

GET  /v1/lottery/provinces
     ?region=north|central|south

GET  /v1/lottery/schedule
     ?week=current|next

# --- Statistics ---
GET  /v1/lottery/stats/frequency
     ?provinceCode=mb&period=30

GET  /v1/lottery/stats/gan
     ?provinceCode=mb&limit=20

GET  /v1/lottery/stats/head-tail
     ?provinceCode=mb&period=30

GET  /v1/lottery/stats/special-prize
     ?provinceCode=mb&period=30

# --- Predictions ---
GET  /v1/lottery/predictions
     ?provinceCode=mb&date=2024-01-15&page=1

GET  /v1/lottery/predictions/{id}

# --- Ticket Checker ---
POST /v1/lottery/check-ticket
     Body: { provinceCode, drawDate, ticketNumber }

# --- CMS ---
GET  /v1/cms/articles
     ?category=tin-tuc&tag=soi-cau&page=1&size=10

GET  /v1/cms/articles/{slug}

GET  /v1/cms/categories

GET  /v1/cms/pages/{slug}

# --- SEO ---
GET  /v1/seo/sitemap.xml
GET  /v1/seo/robots.txt

# --- Platform (Domain resolution) ---
GET  /v1/platform/resolve-domain
     ?domain=xosophuongnghi.com.vn
     Response: { slug, tenantId }

GET  /v1/platform/tenant/{slug}/config
     Response: TenantConfig (public info only)
```
# PHẦN X: TỔNG KẾT CUỐI
## 10. SUMMARY - MỌI THỨ ĐÃ THAY ĐỔI
```

┌─────────────────────────────────────────────────────────────┐
│                    FINAL SUMMARY                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  DATABASE: 39 bảng + 4 views                               │
│  ├── Platform Core:    10 bảng                             │
│  │   (verticals, templates, roles, permissions,            │
│  │    role_permissions, platform_users, clients,           │
│  │    tenants, tenant_settings, domains)                   │
│  ├── User & Auth:       3 bảng                             │
│  │   (tenant_users, sessions, notifications)               │
│  ├── Business Mgmt:     3 bảng                             │
│  │   (hosting_status, payment_records, setup_requests)     │
│  ├── CMS:               5 bảng                             │
│  ├── Lottery:          11 bảng                             │
│  ├── Ads:               2 bảng                             │
│  ├── Analytics:         2 bảng                             │
│  └── System:            3 bảng                             │
│                                                             │
│  ROLES: 5 roles                                            │
│  super_admin → admin → website_owner → editor → viewer     │
│                                                             │
│  PERMISSIONS: ~35 permissions theo group                   │
│  platform.* | content.* | lottery.* | analytics.* | settings.*│
│                                                             │
│  TEMPLATES: Mỗi vertical có 2-3 templates                  │
│  Switch template = 1 API call + cache invalidate           │
│                                                             │
│  APIs: ~80 endpoints                                        │
│  /platform/v1/* → Platform admin                           │
│  /admin/v1/*    → Tenant admin panel                        │
│  /v1/*          → Public website                           │
│                                                             │
│  KEY FEATURES:                                              │
│  ✅ Template switch instant (không cần redeploy)            │
│  ✅ Color override per-website                              │
│  ✅ Feature toggle per-website                              │
│  ✅ RBAC granular permissions                               │
│  ✅ Hosting expiry auto-suspend                             │
│  ✅ Telegram notification cho admin                         │
│  ✅ Audit log mọi action quan trọng                         │
│  ✅ Multi-vertical ready (thêm vertical = thêm templates)   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

🔄 FULL FLOW ANALYSIS & TEMPLATE CREATION GUIDE
# PHẦN I: ĐÁNH GIÁ CORE ĐÃ ĐÁP ỨNG CHƯA
## 1. CHECKLIST ĐÁNH GIÁ
```

✅ ĐÃ ĐÁP ỨNG:

Infrastructure:
├── ✅ Multi-tenant domain routing (subdomain + custom domain)
├── ✅ SSL wildcard tự động
├── ✅ Tenant context isolation
├── ✅ Redis caching per-tenant
└── ✅ Background jobs (KQXS fetch, stats, hosting check)

Database:
├── ✅ verticals + templates tables
├── ✅ tenants với template_id, color_overrides, feature_overrides
├── ✅ RBAC (roles, permissions, role_permissions)
├── ✅ clients, hosting_status, payment_records
└── ✅ Lottery full schema (results, stats, predictions)

Backend:
├── ✅ TenantResolutionFilter
├── ✅ TemplateSwitchService
├── ✅ TenantSetupService (quickSetup)
├── ✅ HostingExpiryJob
├── ✅ LotteryResultFetchJob + Scraper
└── ✅ Permission AOP guards

Frontend:
├── ✅ Dynamic template loader
├── ✅ FeatureGate component
├── ✅ Template switcher UI
└── ✅ Color override editor

⚠️  CÒN THIẾU / CHƯA RÕ:

├── ⚠️  Chưa có: Template file structure cụ thể
│         → Bạn chưa biết cần tạo file gì khi làm template mới
│
├── ⚠️  Chưa có: Full flow từ A→Z khi khách đăng ký
│         → Bạn chưa biết step-by-step cần làm gì
│
├── ⚠️  Chưa có: Tenant config API response cụ thể
│         → Frontend cần biết format để render đúng template
│
└── ⚠️  Chưa có: DNS setup guide cho custom domain
          → Cần hướng dẫn khách trỏ domain
```
# PHẦN II: FULL FLOW - KHÁCH HÀNG MỚI
## 2. FLOW TỔNG QUAN
```

KHÁCH HÀNG LIÊN HỆ
        │
        ▼
[BƯỚC 1] BẠN THU THẬP THÔNG TIN
        │
        ▼
[BƯỚC 2] BẠN TẠO CLIENT + TENANT trên Super Admin Panel
        │ (< 5 phút)
        ▼
[BƯỚC 3] HỆ THỐNG TỰ ĐỘNG SETUP
        │ - Tạo subdomain
        │ - Copy template
        │ - Tạo admin account
        │ - Khởi tạo KQXS settings
        ▼
[BƯỚC 4] BẠN CUSTOMIZE (nếu cần)
        │ - Upload logo
        │ - Chỉnh màu sắc
        │ - Cấu hình tính năng
        ▼
[BƯỚC 5] TEST & BÀN GIAO
        │ - Kiểm tra website
        │ - Gửi thông tin cho khách
        ▼
[BƯỚC 6] KHÁCH TỰ QUẢN LÝ
          - Đăng bài viết
          - Viết soi cầu
          - Xem analytics
```
## 3. CHI TIẾT TỪNG BƯỚC

BƯỚC 1: Thu thập thông tin từ khách
```

Thông tin cần thu thập:

THÔNG TIN KHÁCH HÀNG:
├── Họ tên chủ đại lý:      Nguyễn Văn A
├── Tên đại lý/website:     Xổ Số Phương Nghi
├── Số điện thoại:          0901234567
├── Email (nếu có):         phongnhi@gmail.com
├── Địa chỉ:                123 Nguyễn Trãi, Q1, HCM
└── Tỉnh/thành:             TP. Hồ Chí Minh

THÔNG TIN WEBSITE:
├── Tên hiển thị:           Xổ Số Phương Nghi
├── Slug muốn dùng:         xoso-phuongnghi
│   → URL sẽ là: xoso-phuongnghi.websitehub.vn
├── Domain riêng (nếu có):  xosophuongnghi.com.vn
├── Ngành:                  Xổ Số (Lottery)
├── Template chọn:          lottery-classic (Đỏ Vàng)
├── Màu chủ đạo:            Đỏ (#E53E3E) - hoặc theo ý khách
├── Miền mặc định:          Miền Nam
└── Logo:                   File logo (nếu có)

GÓI DỊCH VỤ:
├── Gói:                    Nâng cao
├── Setup fee:              3,500,000đ
├── Phí hosting:            350,000đ/tháng
└── Số tháng thanh toán:    3 tháng (1,050,000đ)
```
BƯỚC 2: Tạo trên Super Admin Panel
```

2A. Vào Super Admin Panel
    URL: https://admin.websitehub.vn
    Login: super_admin account của bạn

2B. Tạo Client (Khách hàng)
    Menu: Khách hàng → Thêm mới
    ┌─────────────────────────────────────┐
    │ Họ tên:      Nguyễn Văn A           │
    │ Tên đại lý:  Xổ Số Phương Nghi     │
    │ SĐT:         0901234567             │
    │ Email:       phongnhi@gmail.com     │
    │ Tỉnh:        TP. Hồ Chí Minh       │
    │ Nguồn KH:    Zalo                   │
    │ [Lưu khách hàng]                    │
    └─────────────────────────────────────┘

2C. Tạo Website (Tenant) cho khách
    Menu: Websites → Tạo website mới
    ┌─────────────────────────────────────┐
    │ Khách hàng:  Nguyễn Văn A (chọn)   │
    │ Tên website: Xổ Số Phương Nghi     │
    │ Slug:        xoso-phuongnghi        │
    │ Ngành:       Xổ Số                  │
    │ Template:    lottery-classic        │
    │ Màu chính:   #E53E3E               │
    │ Miền mặc định: Miền Nam            │
    │ Logo:        [Upload file]          │
    │                                     │
    │ --- Admin account ---               │
    │ Email admin: phongnhi@gmail.com     │
    │ Mật khẩu:   [Auto generate]        │
    │                                     │
    │ --- Gói dịch vụ ---                │
    │ Gói:         Nâng cao              │
    │ Phí/tháng:   350,000đ              │
    │ Số tháng:    3                      │
    │ [🚀 Tạo website]                   │
    └─────────────────────────────────────┘
```
BƯỚC 3: Hệ thống tự động chạy (QuickSetupService)
```

Sau khi click "Tạo website", backend thực hiện tuần tự:

Step 3.1: Validate
  ✓ Slug 'xoso-phuongnghi' chưa tồn tại
  ✓ Template 'lottery-classic' thuộc vertical lottery
  ✓ Client tồn tại

Step 3.2: INSERT tenants
  id:           uuid-tenant-001
  client_id:    uuid-client-001
  vertical_id:  uuid-lottery
  template_id:  uuid-lottery-classic
  slug:         xoso-phuongnghi
  name:         Xổ Số Phương Nghi
  status:       active
  color_overrides: {"primary": "#E53E3E"}

Step 3.3: INSERT tenant_settings
  logo_url:     https://r2.../logo.png
  seo_default_title: "Xổ Số Phương Nghi - KQXS Hôm Nay"
  timezone:     Asia/Ho_Chi_Minh

Step 3.4: INSERT domains
  domain:   xoso-phuongnghi.websitehub.vn
  type:     subdomain
  verified: true  ← Subdomain tự động verified
  ssl_status: active  ← Wildcard SSL đã có sẵn

Step 3.5: INSERT tenant_users (owner account)
  email:         phongnhi@gmail.com
  password_hash: bcrypt(auto-generated)
  role:          website_owner
  full_name:     Nguyễn Văn A

Step 3.6: INSERT lottery_tenant_settings
  default_region:   south
  show_predictions: true
  show_statistics:  true
  auto_seo_pages:   true

Step 3.7: INSERT hosting_status
  status:      active
  activated_at: 2024-01-15
  paid_until:   2024-04-15  ← 3 tháng
  monthly_fee:  350,000

Step 3.8: INSERT payment_records
  payment_type: setup_fee
  amount:       3,500,000đ
  status:       pending  ← Chờ confirm khi nhận tiền thật

Step 3.9: Gửi Telegram notify cho bạn
  "🎉 Website mới tạo xong!
   Tên: Xổ Số Phương Nghi
   URL: xoso-phuongnghi.websitehub.vn
   Admin: phongnhi@gmail.com / Abc12345
   Khách: 0901234567"

⏱️  Tổng thời gian: ~3-5 giây
```
BƯỚC 4: Customize (Nếu cần thêm)
```

4A. Upload logo (nếu chưa upload lúc tạo)
    Admin Panel → Settings → Upload logo, favicon

4B. Chỉnh màu sắc chi tiết
    Admin Panel → Websites → [website] → Màu sắc
    Chỉnh từng màu: primary, secondary, accent

4C. Bật/tắt tính năng
    Admin Panel → Websites → [website] → Tính năng
    Toggle: Soi cầu ON/OFF, Thống kê ON/OFF...

4D. Cài đặt SEO
    Admin Panel → Websites → [website] → SEO
    Meta title template, description mặc định

4E. Thêm custom domain (nếu khách có domain riêng)
    → Xem BƯỚC 4E chi tiết bên dưới

⏱️  Tổng thời gian customize: 5-15 phút
```
BƯỚC 4E: Setup Custom Domain (xosophuongnghi.com.vn)
```

Nếu khách muốn dùng domain riêng:

[BẠN LÀM - trên Admin Panel]:
1. Vào: Websites → xoso-phuongnghi → Domains → Thêm domain
2. Nhập: xosophuongnghi.com.vn
3. Hệ thống generate verify token: _verify.xosophuongnghi.com.vn
4. Hiển thị hướng dẫn cho khách:

[KHÁCH LÀM - trên domain registrar (tenten.vn, pa.vn...)]:
Bước 1: Đăng nhập vào nơi mua domain
Bước 2: Vào DNS Management
Bước 3: Thêm record sau:
  Loại: CNAME
  Host: www
  Value: xoso-phuongnghi.websitehub.vn
  TTL: 3600

  Loại: CNAME
  Host: @  (hoặc để trống)
  Value: xoso-phuongnghi.websitehub.vn
  TTL: 3600

  Loại: TXT  (để verify ownership)
  Host: _verify
  Value: verify-token-abc123
  TTL: 3600
```
Bước 4: Báo lại bạn "đã xong"
```
[BẠN LÀM]:
5. Click "Verify domain" trên Admin Panel
6. Hệ thống check TXT record → Nếu đúng → verified = true
7. SSL tự động provision qua Let's Encrypt (~1-2 phút)
8. Domain active!

⏱️  Tổng thời gian: 5-30 phút
    (Tùy DNS propagation, thường 5-15 phút)
```    
BƯỚC 5: Test & Bàn giao
```

[BẠN TEST]:
□ Mở: https://xoso-phuongnghi.websitehub.vn
□ Kiểm tra logo hiển thị đúng
□ Kiểm tra màu sắc đúng
□ Kiểm tra KQXS hôm nay hiển thị
□ Kiểm tra mobile responsive
□ Kiểm tra trang Admin: /admin login được
□ Kiểm tra tốc độ load < 3 giây

[GỬI CHO KHÁCH qua Zalo/SMS]:
"Anh/Chị ơi, website đã sẵn sàng ạ! 🎉

🌐 Website: https://xoso-phuongnghi.websitehub.vn
🔑 Đăng nhập quản trị:
   Link: https://xoso-phuongnghi.websitehub.vn/admin
   Email: phongnhi@gmail.com
   Mật khẩu: Abc12345

📱 Hướng dẫn sử dụng: [link video/PDF]
📞 Hỗ trợ: 0909xxxxxx (Zalo/Call)

Anh/Chị nhớ đổi mật khẩu sau khi đăng nhập ạ!"

⏱️  Tổng thời gian: 5-10 phút
```
BƯỚC 6: Khách tự quản lý
```

KHÁCH TRUY CẬP ADMIN PANEL:
URL: https://xoso-phuongnghi.websitehub.vn/admin
Hoặc: https://xosophuongnghi.com.vn/admin (nếu có custom domain)

MENU ADMIN PANEL (Website Owner thấy):
├── 📊 Dashboard
│   ├── Lượt xem hôm nay: 1,234
│   ├── Bài viết: 5 bài
│   └── Hosting: Còn 87 ngày
│
├── ✍️  Bài viết
│   ├── Danh sách bài viết
│   ├── Tạo bài mới
│   └── Danh mục
│
├── 🔮 Soi cầu (Lottery)
│   ├── Danh sách nhận định
│   └── Tạo nhận định mới
│
├── 🖼️  Thư viện ảnh
│
├── 📈 Thống kê
│
├── ⚙️  Cài đặt
│   ├── Thông tin chung
│   ├── SEO
│   ├── Tên miền
│   ├── Giao diện (template)
│   └── Tài khoản

KHÁCH KHÔNG THẤY:
├── ❌ Billing/Payment (bạn quản lý)
├── ❌ Server settings
├── ❌ Database
└── ❌ Other tenants
```
## 4. TIMELINE TỔNG KẾT
```

┌─────────────────────────────────────────────────────────┐
│          TIMELINE: KHÁCH MỚI → WEBSITE LIVE             │
├──────────────────────────────┬──────────────────────────┤
│ Bước                         │ Thời gian                │
├──────────────────────────────┼──────────────────────────┤
│ 1. Thu thập thông tin        │ 5-10 phút (qua Zalo)     │
│ 2. Tạo Client + Tenant       │ 3-5 phút                 │
│ 3. Hệ thống auto setup       │ 3-5 giây 🚀              │
│ 4. Customize (logo, màu)     │ 5-15 phút                │
│ 4E. Custom domain (nếu có)   │ 5-30 phút                │
│ 5. Test + Bàn giao           │ 5-10 phút                │
├──────────────────────────────┼──────────────────────────┤
│ TỔNG (không custom domain)   │ 20-40 phút ✅            │
│ TỔNG (có custom domain)      │ 30-70 phút ✅            │
└──────────────────────────────┴──────────────────────────┘
```
# PHẦN III: TẠO TEMPLATE MỚI
## 5. TẠO TEMPLATE CÓ DỄ KHÔNG?
```

Câu trả lời thật: TƯƠNG ĐỐI DỄ nếu setup đúng từ đầu

Độ khó tùy vào:
├── Template cùng vertical (ví dụ: lottery-modern từ lottery-classic)
│   → DỄ: Copy structure, đổi CSS/layout
│   → Thời gian: 1-3 ngày
│
├── Template vertical mới (ví dụ: bds-professional)
│   → TRUNG BÌNH: Cần thêm pages mới cho vertical đó
│   → Thời gian: 3-7 ngày
│
└── Vertical hoàn toàn mới (ví dụ: hotel)
    → KHÓ HƠN: Cần thêm DB schema + API + Template
    → Thời gian: 2-4 tuần
```
## 6. CẤU TRÚC THƯ MỤC TEMPLATE
```

web/
└── templates/
    │
    ├── lottery/                        ← Vertical Lottery
    │   ├── _shared/                    ← Dùng chung mọi lottery template
    │   │   ├── components/
    │   │   │   ├── ResultTableNorth.tsx
    │   │   │   ├── ResultTableSouth.tsx
    │   │   │   ├── ResultTableCentral.tsx
    │   │   │   ├── FrequencyGrid.tsx
    │   │   │   ├── GanList.tsx
    │   │   │   ├── PredictionCard.tsx
    │   │   │   ├── LiveCountdown.tsx
    │   │   │   ├── TicketChecker.tsx
    │   │   │   └── ScheduleCalendar.tsx
    │   │   ├── hooks/
    │   │   │   ├── useLotteryResults.ts
    │   │   │   ├── useFrequencyStats.ts
    │   │   │   └── useLiveResult.ts
    │   │   └── utils/
    │   │       └── lottery-utils.ts
    │   │
    │   ├── classic/                    ← Template 1: Classic Đỏ Vàng
    │   │   ├── Layout.tsx              ← ⭐ Entry point (bắt buộc)
    │   │   ├── components/
    │   │   │   ├── Header.tsx
    │   │   │   ├── Footer.tsx
    │   │   │   ├── Sidebar.tsx
    │   │   │   └── MobileNav.tsx
    │   │   ├── pages/
    │   │   │   ├── HomePage.tsx
    │   │   │   ├── KQXSPage.tsx
    │   │   │   ├── ThongKePage.tsx
    │   │   │   ├── SoiCauPage.tsx
    │   │   │   ├── BlogPage.tsx
    │   │   │   └── BlogDetailPage.tsx
    │   │   └── styles/
    │   │       └── classic.css         ← CSS riêng của template
    │   │
    │   ├── modern/                     ← Template 2: Modern Navy
    │   │   ├── Layout.tsx              ← ⭐ Entry point
    │   │   ├── components/
    │   │   │   ├── Header.tsx          ← Header khác classic
    │   │   │   ├── Footer.tsx
    │   │   │   └── MegaMenu.tsx        ← Component riêng
    │   │   ├── pages/
    │   │   │   ├── HomePage.tsx        ← Layout khác classic
    │   │   │   ├── KQXSPage.tsx
    │   │   │   └── ...
    │   │   └── styles/
    │   │       └── modern.css
    │   │
    │   └── minimal/                    ← Template 3: Minimal
    │       ├── Layout.tsx
    │       ├── components/
    │       └── pages/
    │
    ├── realestate/                     ← Vertical BDS (future)
    │   ├── _shared/
    │   │   └── components/
    │   │       ├── ListingCard.tsx
    │   │       ├── ListingFilter.tsx
    │   │       ├── MapView.tsx
    │   │       └── ContactForm.tsx
    │   ├── professional/
    │   │   ├── Layout.tsx
    │   │   └── pages/
    │   └── luxury/
    │       ├── Layout.tsx
    │       └── pages/
    │
    └── spa/                            ← Vertical Spa (future)
        ├── _shared/
        ├── elegant/
        └── minimal/
```
## 7. TẠO TEMPLATE MỚI CHO LOTTERY (Ví dụ: lottery-modern)
BƯỚC 1: Tạo files cấu trúc
```bash

# Terminal commands
cd web/templates/lottery
mkdir -p modern/components modern/pages modern/styles

# Tạo các files cần thiết
touch modern/Layout.tsx
touch modern/components/Header.tsx
touch modern/components/Footer.tsx
touch modern/pages/HomePage.tsx
touch modern/pages/KQXSPage.tsx
touch modern/pages/ThongKePage.tsx
touch modern/pages/SoiCauPage.tsx
touch modern/pages/BlogPage.tsx
touch modern/pages/BlogDetailPage.tsx
touch modern/styles/modern.css
```
BƯỚC 2: Implement Layout.tsx (Entry point bắt buộc)
```typescript

// templates/lottery/modern/Layout.tsx
// ⭐ File này PHẢI export default một React component
// ⭐ Nhận đúng 3 props: tenant, config, children

import type { TenantConfig, TemplateConfig } from '@/types'
import { ModernHeader } from './components/Header'
import { ModernFooter } from './components/Footer'
import { TenantProvider } from '@/hooks/useTenant'
import './styles/modern.css'

interface LayoutProps {
  tenant:   TenantConfig
  config:   TemplateConfig
  children: React.ReactNode
}

export default function LotteryModernLayout({
  tenant,
  config,
  children
}: LayoutProps) {
  return (
    <TenantProvider tenant={tenant} templateConfig={config}>
      <div className="lottery-modern min-h-screen bg-slate-900">

        {/* Header full-width */}
        <ModernHeader tenant={tenant} config={config} />

        {/* No sidebar - full width content */}
        <main className="w-full">
          {children}
        </main>

        <ModernFooter tenant={tenant} />
      </div>
    </TenantProvider>
  )
}
```
BƯỚC 3: Implement Pages
```typescript

// templates/lottery/modern/pages/HomePage.tsx
// Trang chủ của template modern - khác hoàn toàn classic

import { FeatureGate } from '@/components/shared/FeatureGate'
import { useTenant } from '@/hooks/useTenant'
// Import shared lottery components
import { ResultTableNorth } from '../_shared/components/ResultTableNorth'
import { ResultTableSouth } from '../_shared/components/ResultTableSouth'
import { LiveCountdown } from '../_shared/components/LiveCountdown'
import { FrequencyGrid } from '../_shared/components/FrequencyGrid'
import { PredictionCard } from '../_shared/components/PredictionCard'

interface HomePageProps {
  results:     LotteryResult[]
  predictions: LotteryPrediction[]
  articles:    Article[]
}

export function ModernHomePage({
  results,
  predictions,
  articles
}: HomePageProps) {
  const { templateConfig } = useTenant()
  const sections = templateConfig.homepageSections
  // ['hero', 'kqxs', 'stats', 'predictions', 'blog']

  return (
    <div className="modern-homepage">

      {/* Hero Section - đặc trưng của modern template */}
      {sections.includes('hero') && (
        <section className="hero-section bg-gradient-to-r
                            from-slate-900 to-blue-900
                            text-white py-12">
          <div className="container mx-auto px-4 text-center">
            <h1 className="text-4xl font-bold mb-4">
              Kết Quả Xổ Số Hôm Nay
            </h1>
            <FeatureGate feature="show_live_result">
              <LiveCountdown className="mt-4" />
            </FeatureGate>
          </div>
        </section>
      )}

      {/* KQXS Section */}
      {sections.includes('kqxs') && (
        <section className="kqxs-section container mx-auto
                            px-4 py-8">
          <ModernResultTabs results={results} />
        </section>
      )}

      {/* Stats Section */}
      {sections.includes('stats') && (
        <FeatureGate feature="show_statistics">
          <section className="stats-section bg-slate-800
                              py-8 mt-4">
            <div className="container mx-auto px-4">
              <h2 className="text-white text-xl font-bold mb-4">
                Thống Kê Lô Tô
              </h2>
              <FrequencyGrid />
            </div>
          </section>
        </FeatureGate>
      )}

      {/* Predictions Section */}
      {sections.includes('predictions') && (
        <FeatureGate feature="show_predictions">
          <section className="predictions-section container
                              mx-auto px-4 py-8">
            <h2 className="text-2xl font-bold mb-4">
              Soi Cầu Hôm Nay
            </h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {predictions.map(p => (
                <PredictionCard key={p.id} prediction={p} />
              ))}
            </div>
          </section>
        </FeatureGate>
      )}

      {/* Blog Section */}
      {sections.includes('blog') && (
        <section className="blog-section container
                            mx-auto px-4 py-8">
          <h2 className="text-2xl font-bold mb-4">Tin Tức</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {articles.map(a => (
              <ArticleCard key={a.id} article={a} />
            ))}
          </div>
        </section>
      )}
    </div>
  )
}
```
BƯỚC 4: Implement Header riêng
```typescript

// templates/lottery/modern/components/Header.tsx
// Header khác hoàn toàn classic - dùng mega menu

import Link from 'next/link'
import { useTenant } from '@/hooks/useTenant'
import type { TenantConfig, TemplateConfig } from '@/types'

interface HeaderProps {
  tenant: TenantConfig
  config: TemplateConfig
}

export function ModernHeader({ tenant, config }: HeaderProps) {
  return (
    <header className="bg-slate-900 border-b border-slate-700
                       sticky top-0 z-50">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between h-16">

          {/* Logo */}
          <Link href="/" className="flex items-center gap-3">
            {tenant.settings.logoUrl ? (
              <img
                src={tenant.settings.logoUrl}
                alt={tenant.name}
                className="h-10 w-auto"
              />
            ) : (
              <span className="text-white font-bold text-xl">
                {tenant.name}
              </span>
            )}
          </Link>

          {/* Modern: Horizontal nav (không có sidebar) */}
          <nav className="hidden md:flex items-center gap-6">
            <Link href="/"
                  className="text-slate-300 hover:text-white
                             text-sm font-medium">
              Trang chủ
            </Link>
            <Link href="/ket-qua"
                  className="text-slate-300 hover:text-white text-sm">
              Kết quả XS
            </Link>
            <Link href="/thong-ke"
                  className="text-slate-300 hover:text-white text-sm">
              Thống kê
            </Link>
            <Link href="/soi-cau"
                  className="text-slate-300 hover:text-white text-sm">
              Soi cầu
            </Link>
            <Link href="/tin-tuc"
                  className="text-slate-300 hover:text-white text-sm">
              Tin tức
            </Link>
          </nav>

          {/* Mobile menu button */}
          <button className="md:hidden text-white">
            ☰
          </button>
        </div>
      </div>
    </header>
  )
}
```
BƯỚC 5: CSS riêng của template
```css

/* templates/lottery/modern/styles/modern.css */

/* Override CSS variables cho modern theme */
.lottery-modern {
  --header-bg:     #0f172a;
  --card-bg:       #1e293b;
  --card-border:   #334155;
  --text-primary:  #f1f5f9;
  --text-secondary:#94a3b8;
}

/* Cards */
.lottery-modern .result-card {
  background:    var(--card-bg);
  border:        1px solid var(--card-border);
  border-radius: 12px;
  color:         var(--text-primary);
}

/* Special prize - nổi bật hơn classic */
.lottery-modern .special-prize {
  font-size:   2.5rem;
  font-weight: 900;
  color:       var(--color-accent);
  text-shadow: 0 0 20px rgba(var(--color-accent), 0.5);
}

/* Table headers */
.lottery-modern .prize-label {
  color:       var(--text-secondary);
  font-size:   0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.1em;
}

/* Tabs */
.lottery-modern .region-tab {
  background:    transparent;
  border:        1px solid var(--card-border);
  color:         var(--text-secondary);
  border-radius: 8px;
  padding:       6px 16px;
  transition:    all 0.2s;
}

.lottery-modern .region-tab.active {
  background: var(--color-primary);
  border-color: var(--color-primary);
  color:       white;
}
```
BƯỚC 6: Đăng ký template vào hệ thống
```typescript

// lib/template-loader.ts
// THÊM DÒNG NÀY VÀO MAP:

const TEMPLATE_MAP = {
  // Lottery templates
  'lottery-classic': () => import('@/templates/lottery/classic/Layout'),
  'lottery-modern':  () => import('@/templates/lottery/modern/Layout'),  // ← THÊM
  'lottery-minimal': () => import('@/templates/lottery/minimal/Layout'),
  // ...
}
```
BƯỚC 7: Thêm vào DB
```sql

-- Thêm template mới vào database
INSERT INTO templates (
    vertical_id,
    code,
    name,
    description,
    preview_image,
    layout_config,
    default_colors,
    display_order
) VALUES (
    (SELECT id FROM verticals WHERE code = 'lottery'),
    'lottery-modern',
    'Modern - Navy Blue',
    'Giao diện tối hiện đại, phù hợp website chuyên nghiệp',
    'https://r2.../preview-modern.png',
    '{
        "layout_type": "no-sidebar",
        "header_style": "sticky",
        "footer_style": "simple",
        "widget_order": ["hero","kqxs","stats","predictions","blog"],
        "homepage_sections": ["hero","kqxs","stats","predictions","blog"],
        "sidebar_widgets": [],
        "features_visible": {
            "show_live_result": true,
            "show_statistics": true,
            "show_predictions": true,
            "show_ticket_checker": true,
            "show_schedule": true,
            "show_print_button": false,
            "show_share_buttons": true
        }
    }',
    '{
        "primary":    "#3B82F6",
        "secondary":  "#0f172a",
        "accent":     "#F59E0B",
        "background": "#0f172a",
        "text":       "#f1f5f9"
    }',
    2
);
```
## 8. PAGES ROUTING - KẾT NỐI TEMPLATE VỚI NEXT.JS
```

Vấn đề: Next.js App Router dùng file-based routing
→ Không thể có nhiều pages cho cùng 1 URL

Giải pháp: 1 route file duy nhất
           → Load đúng page component theo template

app/(public)/
├── layout.tsx          ← Load Layout theo template_code
├── page.tsx            ← Load HomePage theo template_code
├── ket-qua/
│   └── page.tsx        ← Load KQXSPage theo template_code
├── thong-ke/
│   └── page.tsx
├── soi-cau/
│   └── page.tsx
├── tin-tuc/
│   ├── page.tsx
│   └── [slug]/
│       └── page.tsx
└── [slug]/             ← Static pages
    └── page.tsx
```
```typescript

// app/(public)/page.tsx
// Route file: Detect template → Load đúng HomePage component

import { headers } from 'next/headers'
import { getTenantConfig } from '@/lib/tenant'
import { notFound } from 'next/navigation'

// Page registry theo template code
const HOME_PAGE_REGISTRY: Record<string,
    () => Promise<{ HomePage: React.ComponentType<any> }>
> = {
  'lottery-classic': () => import(
      '@/templates/lottery/classic/pages/HomePage'
  ),
  'lottery-modern':  () => import(
      '@/templates/lottery/modern/pages/HomePage'
  ),
  'lottery-minimal': () => import(
      '@/templates/lottery/minimal/pages/HomePage'
  ),
}

export default async function PublicHomePage() {
  const headersList  = headers()
  const tenantSlug   = headersList.get('x-tenant-slug')
  if (!tenantSlug) notFound()

  const tenant = await getTenantConfig(tenantSlug)
  if (!tenant) notFound()

  // Fetch data cần thiết cho homepage
  const [results, predictions, articles] = await Promise.all([
    fetchTodayResults(tenant),
    fetchTodayPredictions(tenant),
    fetchLatestArticles(tenant),
  ])

  // Load đúng HomePage component theo template
  const templateCode   = tenant.template.code
  const pageLoader     = HOME_PAGE_REGISTRY[templateCode]

  if (!pageLoader) {
    // Fallback về classic nếu template không có homepage riêng
    const { HomePage } = await import(
        '@/templates/lottery/classic/pages/HomePage'
    )
    return <HomePage results={results}
                     predictions={predictions}
                     articles={articles} />
  }

  const { HomePage } = await pageLoader()
  return <HomePage results={results}
                   predictions={predictions}
                   articles={articles} />
}

// Helper: Fetch data với tenant context
async function fetchTodayResults(tenant: TenantConfig) {
  const res = await fetch(
    `${process.env.API_BASE_URL}/v1/lottery/results/today`,
    {
      headers: { 'x-tenant-slug': tenant.slug },
      next: { revalidate: 120, tags: [`lottery-today-${tenant.slug}`] }
    }
  )
  if (!res.ok) return []
  const data = await res.json()
  return data.data?.results || []
}

async function fetchTodayPredictions(tenant: TenantConfig) {
  const res = await fetch(
    `${process.env.API_BASE_URL}/v1/lottery/predictions?limit=6`,
    {
      headers: { 'x-tenant-slug': tenant.slug },
      next: { revalidate: 300 }
    }
  )
  if (!res.ok) return []
  const data = await res.json()
  return data.data?.items || []
}

async function fetchLatestArticles(tenant: TenantConfig) {
  const res = await fetch(
    `${process.env.API_BASE_URL}/v1/cms/articles?size=6`,
    {
      headers: { 'x-tenant-slug': tenant.slug },
      next: { revalidate: 600 }
    }
  )
  if (!res.ok) return []
  const data = await res.json()
  return data.data?.items || []
}

// ============================================================
// Tương tự cho KQXS page
// app/(public)/ket-qua/page.tsx
// ============================================================

const KQXS_PAGE_REGISTRY: Record<string,
    () => Promise<{ KQXSPage: React.ComponentType<any> }>
> = {
  'lottery-classic': () => import(
      '@/templates/lottery/classic/pages/KQXSPage'
  ),
  'lottery-modern':  () => import(
      '@/templates/lottery/modern/pages/KQXSPage'
  ),
  'lottery-minimal': () => import(
      '@/templates/lottery/minimal/pages/KQXSPage'
  ),
}

export default async function KQXSPage({
  searchParams
}: {
  searchParams: { date?: string; region?: string }
}) {
  const headersList = headers()
  const tenantSlug  = headersList.get('x-tenant-slug')!
  const tenant      = await getTenantConfig(tenantSlug)
  if (!tenant) notFound()

  const date    = searchParams.date || 'today'
  const region  = searchParams.region || 'all'
  const results = await fetchResultsByDate(tenant, date, region)

  // Load đúng KQXSPage component
  const loader = KQXS_PAGE_REGISTRY[tenant.template.code]
  const { KQXSPage } = loader
    ? await loader()
    : await import('@/templates/lottery/classic/pages/KQXSPage')

  return <KQXSPage results={results} date={date} region={region} />
}

// ============================================================
// PageRegistry helper - DRY hơn
// lib/page-registry.ts
// ============================================================

type PageName = 'HomePage' | 'KQXSPage' | 'ThongKePage'
              | 'SoiCauPage' | 'BlogPage' | 'BlogDetailPage'

const PAGE_REGISTRY: Record<string, Record<PageName, () => Promise<any>>> = {
  'lottery-classic': {
    HomePage:       () => import('@/templates/lottery/classic/pages/HomePage'),
    KQXSPage:       () => import('@/templates/lottery/classic/pages/KQXSPage'),
    ThongKePage:    () => import('@/templates/lottery/classic/pages/ThongKePage'),
    SoiCauPage:     () => import('@/templates/lottery/classic/pages/SoiCauPage'),
    BlogPage:       () => import('@/templates/lottery/classic/pages/BlogPage'),
    BlogDetailPage: () => import('@/templates/lottery/classic/pages/BlogDetailPage'),
  },
  'lottery-modern': {
    HomePage:       () => import('@/templates/lottery/modern/pages/HomePage'),
    KQXSPage:       () => import('@/templates/lottery/modern/pages/KQXSPage'),
    ThongKePage:    () => import('@/templates/lottery/modern/pages/ThongKePage'),
    SoiCauPage:     () => import('@/templates/lottery/classic/pages/SoiCauPage'),
    // ↑ Nếu modern không có SoiCauPage riêng → Dùng classic làm fallback
    BlogPage:       () => import('@/templates/lottery/classic/pages/BlogPage'),
    BlogDetailPage: () => import('@/templates/lottery/classic/pages/BlogDetailPage'),
  },
  'lottery-minimal': {
    HomePage:       () => import('@/templates/lottery/minimal/pages/HomePage'),
    KQXSPage:       () => import('@/templates/lottery/minimal/pages/KQXSPage'),
    ThongKePage:    () => import('@/templates/lottery/classic/pages/ThongKePage'),
    SoiCauPage:     () => import('@/templates/lottery/classic/pages/SoiCauPage'),
    BlogPage:       () => import('@/templates/lottery/classic/pages/BlogPage'),
    BlogDetailPage: () => import('@/templates/lottery/classic/pages/BlogDetailPage'),
  },
}

const FALLBACK_TEMPLATE = 'lottery-classic'

export async function loadPage(
    templateCode: string,
    pageName: PageName
): Promise<React.ComponentType<any>> {

  // Try exact template
  const registry = PAGE_REGISTRY[templateCode]
  if (registry?.[pageName]) {
    const module = await registry[pageName]()
    return module[pageName] || module.default
  }

  // Fallback về classic
  console.warn(`Page ${pageName} not found in template ${templateCode}, 
                using fallback`)
  const fallback = PAGE_REGISTRY[FALLBACK_TEMPLATE][pageName]
  const module   = await fallback()
  return module[pageName] || module.default
}
```
## 9. CHECKLIST TẠO TEMPLATE MỚI
```

Khi muốn tạo thêm template mới (ví dụ: lottery-vip):

BACKEND (30 phút):
□ INSERT vào bảng templates với đầy đủ layout_config
□ Cấu hình default_colors phù hợp
□ Upload preview_image lên R2

FRONTEND (1-3 ngày):
□ Tạo thư mục: templates/lottery/vip/
□ Tạo file: Layout.tsx (bắt buộc có đúng interface)
□ Tạo file: components/Header.tsx
□ Tạo file: components/Footer.tsx
□ Tạo file: pages/HomePage.tsx
□ Tạo file: pages/KQXSPage.tsx
□ Tạo file: styles/vip.css
□ Đăng ký vào TEMPLATE_MAP trong template-loader.ts
□ Đăng ký vào PAGE_REGISTRY trong page-registry.ts

TESTING (1 giờ):
□ Tạo test tenant với template mới trên local
□ Kiểm tra desktop layout
□ Kiểm tra mobile responsive
□ Kiểm tra feature gates hoạt động
□ Kiểm tra color overrides apply đúng
□ Kiểm tra switch template từ classic → vip và ngược lại

DEPLOY:
□ npm run build (kiểm tra không có lỗi)
□ Deploy lên staging
□ Test lại trên staging
□ Deploy production
□ Switch 1 tenant thử → kiểm tra live

TỔNG THỜI GIAN: 1-3 ngày cho template cùng vertical
10. PHÂN TÍCH: CÓ ĐÁP ỨNG ĐỦ KHÔNG?
Raw code

┌─────────────────────────────────────────────────────────────┐
│                    FINAL ASSESSMENT                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ✅ ĐÁNH GIÁ: ĐÁP ỨNG ĐỦ NHU CẦU                          │
│                                                             │
│  KHÁCH HÀNG MỚI:                                            │
│  ✅ Từ liên hệ → website live: 20-40 phút                   │
│  ✅ Không cần code mỗi lần có khách mới                     │
│  ✅ Khách tự quản lý qua Admin Panel                        │
│                                                             │
│  TEMPLATE SYSTEM:                                           │
│  ✅ Switch template: 1 click, không redeploy                │
│  ✅ Tạo template mới: 1-3 ngày/template                     │
│  ✅ Color override per-website                              │
│  ✅ Feature toggle per-website                              │
│  ✅ Templates dùng chung shared components                  │
│     (tránh duplicate code)                                  │
│                                                             │
│  SCALE:                                                     │
│  ✅ 10 khách → 100 khách: chỉ tăng server                   │
│  ✅ Thêm ngành mới: thêm vertical + templates               │
│  ✅ 1 codebase quản lý tất cả                               │
│                                                             │
│  NEXT STEPS ĐỀ XUẤT:                                        │
│  1. Build lottery-classic template hoàn chỉnh              │
│  2. Launch với 5-10 khách beta                              │
│  3. Build lottery-modern dựa trên feedback                  │
│  4. Sau 20+ khách → Build vertical thứ 2 (BDS/Spa)         │
│                                                             │
└─────────────────────────────────────────────────────────────┘