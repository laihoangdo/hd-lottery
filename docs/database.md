user

id
username
email
phone
password_hash
status
last_login_at
created_at
updated_at

tenant

id
code
name
domain
status
...


membership

id
tenant_id
user_id
status
joined_at


role 
<!-- PLATFORM_ADMIN

TENANT_ADMIN

DEALER_MANAGER

STAFF

VIEWER -->

id
code
name
description


permission
<!-- LOTTERY_RESULT

VIEW

LOTTERY_RESULT_VIEW -->

id
resource
action
code



role_permission

role_id
permission_id


role_assignment

membership_id
role_id


refresh_token

id
user_id
token
expired_at
revoked


login_history

id
user_id
tenant_id
ip
device
browser
login_at
status


User

↓

Membership

↓

RoleAssignment

↓

Role

↓

Permission



User nhập email + password
          │
          ▼
Identity xác thực
          │
          ▼
Lấy danh sách Membership
          │
          ▼
Nếu chỉ có 1 Tenant
          │
          ▼
Đăng nhập trực tiếp
          │
          ▼
JWT chứa tenant_id


Login
     │
     ▼
Membership List
     │
     ▼
Chọn Tenant
     │
     ▼
JWT


payload
{
  "sub": "user-id",
  "tenantId": "tenant-id",
  "membershipId": "membership-id",
  "roles": [
    "TENANT_ADMIN"
  ],
  "permissions": [
    "LOTTERY_RESULT_VIEW",
    "LOTTERY_RESULT_EDIT"
  ]
}



user case map

                    Identity Domain

                 Authentication
                 ├── Login
                 ├── Logout
                 ├── Refresh Token
                 ├── Switch Tenant
                 └── Validate JWT

                 User Management
                 ├── Create User
                 ├── Update User
                 ├── Lock User
                 ├── Unlock User
                 ├── Reset Password
                 └── Change Password

                 Membership
                 ├── Invite User
                 ├── Join Tenant
                 ├── Remove Membership
                 └── Switch Active Tenant

                 Authorization
                 ├── Create Role
                 ├── Update Role
                 ├── Assign Role
                 ├── Revoke Role
                 ├── Create Permission
                 └── Check Permission

                 Session
                 ├── List Sessions
                 ├── Revoke Session
                 └── Logout All Devices



UC-001 Login
                 1 User nhập username/password

↓

2 Identity xác thực mật khẩu

↓

3 Tìm Membership

↓

4 Nếu nhiều Membership

↓

5 Hiển thị Tenant Selection

↓

6 User chọn Tenant

↓

7 Sinh JWT

↓

8 Ghi Login History

↓

9 Publish UserLoggedIn Event

↓

10 Login Success


UC-002 Logout
User

↓

Logout

↓

Revoke Refresh Token

↓

Delete Session

↓

Publish Logout Event


UC-003 Refresh Token
Business Rules

Refresh Token còn hạn.
Chưa bị revoke.

UC-004 Switch Tenant ⭐⭐⭐


Current JWT

↓

Membership List

↓

Select Tenant

↓

Generate New JWT

↓

Done


UC-005 Create User

Create User

↓

Generate Temporary Password

↓

Create Membership

↓

Assign Default Role

↓

Send Invitation

↓

Done

UC-006 Invite Existing User

Search User

↓

Create Membership

↓

Assign Role

↓

Done

UC-007 Remove Membership
User

↓

Tenant B

↓

Remove

UC-008 Assign Role

Membership

↓

Assign Role

↓

Publish RoleAssigned

UC-009 Change Password
Old Password

↓

Verify

↓

Hash New Password

↓

Save

↓

Logout All Devices

↓

Done


UC-010 Lock User
Lock User

↓

Terminate Sessions

↓

Revoke Refresh Tokens

↓

Publish UserLocked


UC-011 List Active Sessions
Chrome Windows

Chrome Android

Safari iPhone

UC-012 Logout All Devices
Delete Sessions

↓

Revoke Refresh Tokens

↓

Done

| Use Case      | Platform Admin | Tenant Admin | Dealer Staff |
| ------------- | -------------- | ------------ | ------------ |
| Login         | ✓              | ✓            | ✓            |
| Logout        | ✓              | ✓            | ✓            |
| Create User   | ✓              | ✓            | ✗            |
| Lock User     | ✓              | Tenant Only  | ✗            |
| Create Role   | ✓              | ✗            | ✗            |
| Assign Role   | ✓              | ✓            | ✗            |
| Switch Tenant | ✓              | ✓            | ✗            |
| Refresh Token | ✓              | ✓            | ✓            |


Use Case	Lý do
UC-013 Impersonate User	Platform Admin có thể đăng nhập thay Tenant Admin để hỗ trợ xử lý sự cố (mọi thao tác phải được audit).
UC-014 API Key Authentication	Chuẩn bị cho việc tích hợp hệ thống bên ngoài hoặc Mobile App.
UC-015 Service-to-Service Authentication	Khi sau này tách Microservice, các service sẽ xác thực với nhau mà không dùng tài khoản người dùng.


sequenceDiagram
    autonumber

    actor User
    participant Identity as Identity Service
    participant UserRepo as User Repository
    participant Membership as Membership Service
    participant Session as Session Service
    participant Token as JWT Service
    participant Audit as Audit Service

    User->>Identity: Login(username,password)

    Identity->>UserRepo: Find User

    UserRepo-->>Identity: User

    Identity->>Identity: Verify Password

    alt Password Invalid

        Identity->>Audit: Log Failed Login

        Identity-->>User: Login Failed

    else Password Valid

        Identity->>Membership: Find Memberships

        Membership-->>Identity: Membership List

        alt Only One Membership

            Identity->>Session: Create Session

            Session-->>Identity: Session

            Identity->>Token: Generate JWT

            Token-->>Identity: JWT

            Identity->>Audit: Log Success

            Identity-->>User: Login Success

        else Multiple Membership

            Identity-->>User: Membership Selection

        end

    end




    SWITCH TENANT Không Login lại.

Không nhập Password.

    sequenceDiagram

actor User

participant Identity

participant Membership

participant JWT

User->>Identity: Switch Tenant

Identity->>Membership: Validate Membership

Membership-->>Identity: Valid

Identity->>JWT: Generate New Token

JWT-->>Identity: JWT

Identity-->>User: New JWT