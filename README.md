# Spring Boot REST API with PostgreSQL

![Build](https://github.com/OKaluzny/spring-boot-rest-api-postgresql/actions/workflows/ci.yml/badge.svg)

REST API CRUD application built with Spring Boot 4 and PostgreSQL.

## Tech Stack

- Java 17
- Spring Boot 4.0.2
- Spring Data JPA
- Spring Security (Basic Auth)
- PostgreSQL
- Lombok
- Docker

## Requirements

- JDK 17+
- Maven 3.8+
- PostgreSQL 14+ (or Docker)

## Quick Start

### 1. Start PostgreSQL

```bash
docker-compose up -d postgres
```

### 2. Run Application

```bash
mvn spring-boot:run
```

Application starts at `http://localhost:8080`

### 3. Default Credentials

- **API Auth:** `user:user`
- **Database:** `postgres:postgres`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/books` | Create book |
| GET | `/api/books` | Get all books |
| GET | `/api/books/{id}` | Get book by ID |
| GET | `/api/books?name={name}` | Search by name |
| PUT | `/api/books/{id}` | Update book |
| DELETE | `/api/books/{id}` | Delete book |
| DELETE | `/api/books` | Delete all books |

### Example Request

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic dXNlcjp1c2Vy" \
  -d '{
    "name": "Java Programming",
    "description": "Learn Java",
    "tags": ["java", "programming"]
  }'
```

### Example Response

```json
{
  "id": 1,
  "name": "Java Programming",
  "description": "Learn Java",
  "tags": ["java", "programming"]
}
```

## Configuration

Environment variables:

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_HOST` | localhost | Database host |
| `DB_PORT` | 5432 | Database port |
| `DB_NAME` | book_db | Database name |
| `DB_USERNAME` | postgres | Database user |
| `DB_PASSWORD` | postgres | Database password |
| `SECURITY_USER` | user | API username |
| `SECURITY_PASSWORD` | user | API password |
| `SERVER_PORT` | 8080 | Application port |

## Docker

### Run full stack

```bash
docker-compose --profile full up -d
```

### Run only PostgreSQL

```bash
docker-compose up -d postgres
```

### Build image only

```bash
docker build -t book-api .
```

## Actuator Endpoints

- Health: `GET /actuator/health`
- Info: `GET /actuator/info`
- Metrics: `GET /actuator/metrics`

## Project Structure

```
src/main/java/com/kaluzny/
├── Application.java              # Entry point
├── domain/
│   ├── Book.java                 # JPA Entity
│   └── BookRepository.java       # Spring Data Repository
├── exception/
│   ├── BookNotFoundException.java
│   └── GlobalExceptionHandler.java
└── web/
    └── BookRestController.java   # REST Controller
```

## Testing

```bash
# Run tests (requires PostgreSQL)
mvn test

# Skip tests
mvn package -DskipTests
```

## License

MIT
