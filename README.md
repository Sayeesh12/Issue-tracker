# Issue Tracker System (Spring Boot)

## Overview

A backend Issue Tracking System inspired by tools like GitHub Issues and Jira. It enables teams to create, assign, and manage issues across projects with a well-defined lifecycle and secure access control.

This project is designed with **clean architecture principles**, **production-grade practices**, and **scalable design patterns** suitable for real-world applications.

---

## Features

### Core Features

* User authentication using JWT
* Project creation and management
* Issue creation, assignment, and tracking
* Issue lifecycle management:

  * `OPEN → IN_PROGRESS → DONE`
* Commenting on issues

### Advanced Features

* Pagination and filtering of issues
* DTO-based request/response handling
* Global exception handling
* Input validation using annotations
* Ownership and access control rules

### (In Progress)

* Role-Based Access Control (RBAC)
* Activity logs / audit tracking
* Strict workflow transition validation

---

## Tech Stack

* **Backend:** Spring Boot
* **Security:** Spring Security + JWT
* **Database:** PostgreSQL (configurable)
* **ORM:** Spring Data JPA (Hibernate)
* **Build Tool:** Maven
* **Validation:** Jakarta Validation

---

## Project Structure

```
com.example.issuetracker
│
├── controller        # REST APIs
├── service
│   ├── impl          # Business logic implementations
│   └── auth          # Authentication services
├── repository        # Data access layer (JPA)
├── entity            # JPA entities (User, Project, Issue, Comment)
├── dto               # Request & Response objects
├── security          # JWT, filters, config
├── exception         # Global exception handling
├── enums             # Status, roles, issue types
```

---

## Entity Relationships

* **User**

  * Can create projects
  * Can be assigned issues

* **Project**

  * Has multiple members
  * Contains multiple issues

* **Issue**

  * Belongs to a project
  * Has:

    * Creator
    * Assignee
    * Status
    * Type

* **Comment**

  * Linked to an issue
  * Created by a user

---

## API Highlights

### Authentication

* `POST /auth/register`
* `POST /auth/login`

### Projects

* `POST /projects`
* `GET /projects`
* `GET /projects/{id}`

### Issues

* `POST /issues`
* `GET /issues`
* `PUT /issues/{id}/status`
* `PUT /issues/{id}/assign`

### Comments

* `POST /comments`
* `GET /comments/{issueId}`

---

## Security Flow

1. User logs in → JWT token is generated
2. Token is sent in headers for protected APIs
3. JWT filter validates token on each request
4. Security context is populated with authenticated user

---

## Key Design Decisions

* **Layered Architecture** → separation of concerns
* **DTO Pattern** → prevents entity exposure
* **Global Exception Handling** → consistent error responses
* **Lazy Loading** → optimized database interactions
* **Pagination** → scalable data retrieval

---

## How to Run

1. Clone the repository
2. Configure database in `application.properties`
3. Run the application:

   ```
   mvn spring-boot:run
   ```
4. Access APIs via Postman / Swagger (if enabled)

---

## Future Enhancements

* Frontend integration (React)
* Email notifications
* File attachments for issues
* Advanced search & filters
* Microservices architecture (scaling)

---

## Author

**Sayeesh Mahale**
Information Science Engineering Student
Vidyavardhaka College of Engineering

---

## Notes

This project is built as a **backend system** demonstrating:

* real-world architecture
* security implementation
* scalable API design

---
