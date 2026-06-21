# 📝 Secure Multi-User Todo API

A production-ready, secure, and robust RESTful API built with **Spring Boot**. This project demonstrates enterprise-level backend patterns, including stateless JWT authentication, resource ownership, role-based access control (RBAC), and global exception handling.

---

## ✨ Features

* **🔐 Stateless JWT Authentication:** Fully secured endpoints using custom JSON Web Tokens and Spring Security Filter Chains.
* **👥 Multi-User Support:** Registration and login capabilities. Users are completely isolated from each other.
* **🛡️ Ownership-Based Authorization:** Users can only view, create, update, and delete their *own* Todos. Unauthorized access attempts are intercepted and return clean `403 Forbidden` responses.
* **👑 Role-Based Access Control (RBAC):** Dedicated endpoints protected by `@PreAuthorize` that only users with the `ADMIN` role can access.
* **✅ Data Validation:** DTOs are strictly validated using `jakarta.validation` constraints to ensure data integrity.
* **📄 Pagination & Sorting:** The API utilizes Spring Data JPA `Pageable` to efficiently return chunks of data and metadata, preventing memory overload on large datasets.
* **🚨 Global Exception Handling:** Custom exceptions (e.g., `OwnershipException`, `EmailExistsException`) are caught globally by a `@RestControllerAdvice` to guarantee clean and predictable JSON error responses.

---

## 🛠️ Technology Stack

* **Java 21**
* **Spring Boot 3.x**
* **Spring Security** (Stateless configuration)
* **Spring Data JPA / Hibernate**
* **PostgreSQL**
* **JJWT** (Java JWT implementation)
* **Maven**

---

## 🚀 Getting Started

### Prerequisites
* JDK 21 or higher installed
* PostgreSQL installed and running locally
* Maven installed (or use the provided wrapper)

### Database Setup
1. Open pgAdmin or your PostgreSQL CLI.
2. Create a new database named `tododb`:
   ```sql
   CREATE DATABASE tododb;
   ```

### Configuration
1. Open `src/main/resources/application.properties`.
2. Ensure the database credentials match your local PostgreSQL setup:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/tododb
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   ```

### Running the Application
Run the following command in the root directory of the project:
```bash
./mvnw spring-boot:run
```
*(Hibernate will automatically create all necessary tables in your database upon startup).*

---

## 📚 API Documentation

### 👤 Authentication (`/api/auth`)

| Method | Endpoint | Description | Requires Auth |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new user | ❌ |
| `POST` | `/api/auth/login` | Login and receive a JWT | ❌ |

### 📝 Todos (`/api/todo`)

*(All Todo endpoints require a valid JWT passed in the `Authorization: Bearer <token>` header).*

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/todo` | Get all Todos belonging to the authenticated user (Supports Pagination: `?page=0&size=10&sortBy=id`) |
| `POST` | `/api/todo` | Create a new Todo |
| `PUT` | `/api/todo/{id}` | Update the title or status of a specific Todo |
| `DELETE` | `/api/todo/{id}` | Delete a specific Todo |

### 👑 Admin (`/api/admin`)

| Method | Endpoint | Description | Requires Auth | Role |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/admin/users` | View all registered users | ✅ | `ADMIN` |

---

## 👨‍💻 Architecture Highlights

### Security Context Lifecycle
When a request is made to a protected endpoint:
1. The request enters the `JwtAuthenticationFilter`.
2. The Bearer token is extracted and cryptographically verified.
3. The user's email is extracted from the token's subject.
4. `CustomUserDetailsService` loads the user and their assigned roles from the database.
5. A `UsernamePasswordAuthenticationToken` is created and stored in the `SecurityContextHolder`.
6. The request safely proceeds to the Controller layer, completely authenticated.

### Global Exception Handling
This project avoids exposing raw stack traces or default Spring Boot error pages. All business logic exceptions are routed through the `GlobalExceptionHandler`.

Example 403 Response:
```json
{
  "message": "Access Denied : You do not own this todo"
}
```
