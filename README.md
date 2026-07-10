# Expense Tracker (Microservices)

A Spring Boot microservices-based backend for personal expense tracking with JWT authentication, API gateway routing, and service discovery.

## Services

- **eureka-service** (`:8761`)  
  Service registry (Netflix Eureka).
- **api-gateway** (`:8080`)  
  Single entry point, JWT validation, request routing.
- **profile-service** (`:8081`)  
  User registration, login, and current-profile retrieval.
- **expense-service** (`:8082`)  
  Category, transaction, income, and balance management.

## Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Cloud 2025.0.3 (Eureka + Gateway + LoadBalancer)
- Spring Security + JWT (`jjwt`)
- Spring Data JPA + PostgreSQL
- springdoc OpenAPI / Swagger UI
- Maven Wrapper (`./mvnw`)

## Architecture Overview

Clients call **api-gateway**. Gateway validates JWT and routes:

- `/profile/**` → `profile-service`
- `/expense/**` → `expense-service`

Gateway forwards authenticated identity as `X-User-Email`, which downstream services use for user scoping.

## Prerequisites

- JDK 21
- PostgreSQL (for profile-service and expense-service)
- Bash/terminal

## Environment Variables

Create a `.env` file in each service directory that needs secrets (or export env vars globally).

### `profile-service`

Required:

- `DB_URL` (e.g. `jdbc:postgresql://localhost:5432/profile_db`)
- `DB_USER`
- `DB_PASS`
- `JWT_SECRET`

### `expense-service`

Required:

- `EXPENSE_DB_URL` (e.g. `jdbc:postgresql://localhost:5432/expense_db`)
- `EXPENSE_DB_USER`
- `EXPENSE_DB_PASS`
- `JWT_SECRET`

### `api-gateway`

Required:

- `JWT_SECRET` (must match the secret used by other services)

## Run Locally

Start services in this order:

1. `eureka-service`
2. `profile-service`
3. `expense-service`
4. `api-gateway`

From repository root:

```bash
cd /home/runner/work/expense-tracker/expense-tracker/eureka-service
./mvnw spring-boot:run
```

```bash
cd /home/runner/work/expense-tracker/expense-tracker/profile-service
./mvnw spring-boot:run
```

```bash
cd /home/runner/work/expense-tracker/expense-tracker/expense-service
./mvnw spring-boot:run
```

```bash
cd /home/runner/work/expense-tracker/expense-tracker/api-gateway
./mvnw spring-boot:run
```

## API Documentation (Swagger)

- Profile service: `http://localhost:8081/swagger-ui.html`
- Expense service: `http://localhost:8082/swagger-ui.html`

You can also access APIs through the gateway at `http://localhost:8080`.

## Authentication Flow

1. Register user: `POST /profile/register`
2. Login: `POST /profile/login`
3. Login response includes:
   - JWT in response body (`token`)
   - `jwt` httpOnly cookie
4. For protected endpoints, pass either:
   - an `Authorization` header with a valid JWT bearer token
   - or rely on the `jwt` cookie

## Core API Endpoints

### Profile Service (`/profile`)

Public:

- `POST /profile/register`
- `POST /profile/login`
- `GET /profile/test`

Protected:

- `GET /profile/me`

### Expense Service (`/expense`)

Public:

- `GET /expense/test`

Protected:

#### Categories
- `POST /expense/categories`
- `GET /expense/categories`
- `GET /expense/categories/{id}`
- `PUT /expense/categories/{id}`
- `DELETE /expense/categories/{id}`

#### Transactions
- `POST /expense/transactions`
- `GET /expense/transactions?categoryId={id}&type={INCOME|EXPENSE}`
- `GET /expense/transactions/{id}`
- `PUT /expense/transactions/{id}`
- `DELETE /expense/transactions/{id}`

#### Income & Balance
- `GET /expense/income`
- `PUT /expense/income`
- `POST /expense/income/add`
- `GET /expense/balance`

## Business Rules

- Category names must be unique per user.
- Category deletion is blocked if transactions exist for that category.
- Transaction `type` must match selected category `type`.
- Income is stored as a single record per user.
- Balance is computed as: `income - total_expense_transactions`.

## Example Requests

### Register

```bash
curl -X POST http://localhost:8080/profile/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "P@ssw0rd",
    "profileImageUrl": "https://example.com/avatar.png"
  }'
```

### Login

```bash
curl -i -X POST http://localhost:8080/profile/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "P@ssw0rd"
  }'
```

### Create Category (with auth token)

```bash
curl -X POST http://localhost:8080/expense/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Groceries",
    "type": "EXPENSE",
    "icon": "shopping-cart",
    "color": "#22C55E"
  }'
```

### Add Transaction

```bash
curl -X POST http://localhost:8080/expense/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "categoryId": 1,
    "type": "EXPENSE",
    "amount": 49.99,
    "description": "Weekly groceries",
    "date": "2026-07-10"
  }'
```

## Build & Test

Each service is built and tested independently.

```bash
cd /home/runner/work/expense-tracker/expense-tracker/<service-name>
./mvnw clean test
```

Available service names:

- `eureka-service`
- `api-gateway`
- `profile-service`
- `expense-service`

## Notes

- JPA `ddl-auto=update` is enabled in profile and expense services.
- Profile activation fields exist in the model, but activation endpoints are not currently exposed in controllers.
- Swagger/OpenAPI is enabled in profile and expense services.
