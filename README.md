# Expense Tracker

A microservices-based expense tracking REST API built with **Java 21**, **Spring Boot 3.5**, and **Spring Cloud 2025**.

## Architecture

```
                    +-----------+
                    |   Client   |
                    +-----+-----+
                          |
                     (HTTP :8080)
                          |
              +-----------+-----------+
              |      API Gateway      |
              |  Spring Cloud Gateway  |
              +-----------+-----------+
                    |           |
               (Eureka)    (Eureka)
              +------+      +------+
              |             |
      +-------v--------+   +-------v--------+
      | Profile Service |  | Expense Service |
      | port 8081       |  | port 8082       |
      +-------+--------+  +-------+--------+
              |                    |
      +-------v--------+   +-------v--------+
      | PostgreSQL DB   |   | PostgreSQL DB   |
      | profile_db      |   | expense_db      |
      +----------------+   +----------------+

              +------------------+
              |  Eureka Server   |
              | (eureka-service) |
              | port 8761        |
              +------------------+
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| **api-gateway** | 8080 | Single entry point — routes requests, handles JWT auth, injects user identity |
| **eureka-service** | 8761 | Netflix Eureka service registry for discovery |
| **profile-service** | 8081 | User registration, login, and profile management |
| **expense-service** | 8082 | Categories, transactions, income, and balance calculations |

## Tech Stack

- **Java 21** + **Spring Boot 3.5.16** + **Spring Cloud 2025.0.3**
- **Spring Cloud Gateway** (reactive) — API gateway
- **Netflix Eureka** — service discovery
- **Spring Data JPA** — ORM
- **PostgreSQL** — database (separate DB per service)
- **Spring Security** — authentication & authorization
- **JWT (jjwt 0.11.5)** — token-based auth (Bearer header + httpOnly cookie)
- **SpringDoc OpenAPI 2.6.0** — Swagger UI
- **Lombok** — boilerplate reduction
- **Maven** — build tool

## Features

- **User Management** — register, login (JWT), view profile
- **Category Management** — create, list, update, delete categories (typed as INCOME/EXPENSE)
- **Transaction Management** — CRUD with category linking, type validation, and filtering
- **Income Tracking** — set, update, and add to monthly income (one record per user)
- **Balance Dashboard** — total income, total expenses, and net balance

## Prerequisites

- Java 21 JDK
- PostgreSQL (two databases)
- Maven (or use the included `mvnw` wrappers)

## Database Setup

Create two PostgreSQL databases:

```sql
CREATE DATABASE expense_tracker_profile_service;
CREATE DATABASE expense_tracker_expense_service;
```

Tables are auto-created by Hibernate (`ddl-auto=update`).

## Configuration

Each service has a `.env` file. The **JWT secret must be identical** across all services.

| File | Key Variables |
|------|--------------|
| `profile-service/.env` | `DB_URL`, `DB_USER`, `DB_PASS`, `JWT_SECRET` |
| `expense-service/.env` | `EXPENSE_DB_URL`, `EXPENSE_DB_USER`, `EXPENSE_DB_PASS`, `JWT_SECRET` |
| `api-gateway/.env` | `JWT_SECRET` |

## Running

Start services **in this order**:

```bash
# 1. Service Registry
cd eureka-service
./mvnw spring-boot:run

# 2. Profile Service
cd profile-service
./mvnw spring-boot:run

# 3. Expense Service
cd expense-service
./mvnw spring-boot:run

# 4. API Gateway
cd api-gateway
./mvnw spring-boot:run
```

All four services can also be launched from VS Code via the provided `.vscode/launch.json` configurations.

## API Endpoints

### Profile Service (via Gateway: `/profile/**`)

| Method | Path | Description |
|--------|------|-------------|
| POST | `/profile/register` | Register a new user |
| POST | `/profile/login` | Authenticate and receive JWT |
| GET | `/profile/me` | Get current user profile (authenticated) |
| GET | `/profile/test` | Health check |

### Expense Service (via Gateway: `/expense/**`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/expense/test` | Health check |
| POST | `/expense/categories` | Create a category |
| GET | `/expense/categories` | List all categories |
| GET | `/expense/categories/{id}` | Get a category |
| PUT | `/expense/categories/{id}` | Update a category |
| DELETE | `/expense/categories/{id}` | Delete a category |
| POST | `/expense/transactions` | Create a transaction |
| GET | `/expense/transactions` | List transactions (`?categoryId=`, `?type=`) |
| GET | `/expense/transactions/{id}` | Get a transaction |
| PUT | `/expense/transactions/{id}` | Update a transaction |
| DELETE | `/expense/transactions/{id}` | Delete a transaction |
| GET | `/expense/income` | Get income amount |
| PUT | `/expense/income` | Set income amount |
| POST | `/expense/income/add` | Add to existing income |
| GET | `/expense/balance` | Get balance summary |

### Public Endpoints (no auth required)

- `GET /status`, `GET /health`
- `POST /profile/register`, `POST /profile/login`, `GET /profile/test`
- Swagger UI and OpenAPI docs

## Authentication Flow

1. Client logs in via `POST /profile/login` — receives JWT in response body + httpOnly cookie
2. Subsequent requests include the JWT via `Authorization: Bearer <token>` header or the `jwt` cookie
3. API Gateway validates the JWT, extracts the user's email, and injects `X-User-Email` header
4. Downstream services trust the gateway header or validate the JWT directly

## API Documentation

Swagger UI is available at `/swagger-ui.html` for each service (also accessible through the gateway).

## Project Structure

```
expense-tracker/
├── api-gateway/          # Spring Cloud Gateway
│   └── src/main/java/com/micro/api_gateway/
│       ├── config/       # GatewayRouteConfig, SecurityConfig, JWT config, EmailHeaderFilter
│       └── service/      # JwtService
├── eureka-service/       # Netflix Eureka Server
│   └── src/main/java/com/micro/eureka_service/
├── profile-service/      # User & Auth service
│   └── src/main/java/com/micro/profile_service/
│       ├── config/       # SecurityConfig, JwtAuthFilter, OpenApiConfig
│       ├── controller/   # ProfileController
│       ├── DTO/          # ProfileDTO, AuthDTO
│       ├── model/        # ProfileEntity
│       ├── repository/   # ProfileRepository
│       └── service/      # ProfileService, JwtService, AppUserDetailService
├── expense-service/      # Expense tracking service
│   └── src/main/java/com/micro/expense_service/
│       ├── config/       # SecurityConfig, JwtAuthFilter, OpenApiConfig
│       ├── controller/   # ExpenseController, DashBoardController
│       ├── DTO/          # Transaction/Category/Income/Balance DTOs
│       ├── model/        # Transaction, Income, Category entities
│       ├── repository/   # Transaction/Income/Category repositories
│       └── service/      # TransactionService, IncomeService, CategoryService, JwtService
├── .vscode/              # VS Code launch configs for all services
└── codeCount.py          # Line count utility
```
