POST   /api/v1/auth/login
POST   /api/v1/auth/logout
POST   /api/v1/auth/refresh

GET    /api/v1/users
POST   /api/v1/users
GET    /api/v1/users/{id}
PUT    /api/v1/users/{id}
DELETE /api/v1/users/{id}

GET    /api/v1/roles
POST   /api/v1/roles

GET    /api/v1/permissions


user
role
permission
user_role
role_permission
user_session
refresh_token
login_history


docs/
└── 01-business-architecture/
    └── identity/
        ├── README.md
        ├── 01_Business_Specification.md
        ├── 02_Aggregates.md
        ├── 03_Domain_Events.md
        ├── 04_State_Machine.md
        ├── 05_API_Contract.md
        ├── 06_ERD.md
        ├── 07_Business_Rules.md
        └── 08_Review_Checklist.md