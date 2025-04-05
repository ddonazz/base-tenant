# Spring Boot Multitenancy Application

This is a Java Spring Boot application with built-in security (user and role management) that is being extended to support multitenancy.

**Key Features:**

* **Secure Application:** Includes pre-configured security for user authentication and authorization based on roles.
* **Centralized User Management:** All users are managed within a single database.
* **Database-per-Tenant Architecture:** Each tenant will have its own dedicated database.
* **Tenant Context Management:** Implements logic to manage and switch between tenant-specific data sources at runtime.

**Current Development:**

The application is currently under development to integrate multitenancy features. This includes:

* Implementation of tenant identification mechanisms (e.g., subdomain, request header).
* Dynamic data source configuration based on the current tenant.
* Management of tenant-specific database schemas.

**Further Documentation:**

More detailed documentation regarding the security setup and multitenancy implementation will be added as development progresses.
